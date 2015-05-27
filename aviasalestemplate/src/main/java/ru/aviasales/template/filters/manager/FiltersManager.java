package ru.aviasales.template.filters.manager;

import android.content.Context;
import android.os.Handler;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.aviasales.core.AviasalesSDK;
import ru.aviasales.core.search.object.SearchData;
import ru.aviasales.core.search.object.TicketData;
import ru.aviasales.template.filters.GeneralFilter;
import ru.aviasales.template.filters.PreInitializeFilters;
import ru.aviasales.template.ticket.TicketManager;

public class FiltersManager {
	private static volatile FiltersManager sInstance;

	public interface OnFilterResultListener {
		void onFilteringFinished(List<TicketData> filteredTicketsData);
	}

	private GeneralFilter mFilter;

	private ExecutorService pool;

	private Handler mHandler = new Handler();
	private OnFilterResultListener mOnFilterResultsListener;

	private List<TicketData> mFilteredTickets;

	public static FiltersManager getInstance() {
		if (sInstance == null) {
			synchronized (FiltersManager.class) {
				if (sInstance == null) {
					sInstance = new FiltersManager();
				}
			}
		}
		return sInstance;
	}


	public void filterSearchData(final SearchData searchData, OnFilterResultListener listener) {

		mOnFilterResultsListener = listener;

		createPool();

		pool.submit(new Runnable() {
			@Override
			public void run() {

				List<TicketData> filteredTickets = mFilter.applyFilters(searchData);
				mHandler.post(new EndRunnable(filteredTickets));

			}
		});

	}

	private void createPool() {
		if (pool == null) {
			pool = Executors.newCachedThreadPool();
		}
	}

	public void setOnFilterResultsListener(OnFilterResultListener onFilterResultsListener){
		this.mOnFilterResultsListener = onFilterResultsListener;
	}

	public List<TicketData> getFilteredTickets(){
		return mFilteredTickets;
	}

	public class EndRunnable implements Runnable {

		public EndRunnable(List<TicketData> filteredTickets) {
			mFilteredTickets = filteredTickets;
		}

		@Override
		public void run() {
			if (mOnFilterResultsListener != null) {
				mOnFilterResultsListener.onFilteringFinished(mFilteredTickets);
			}
		}
	}

	public GeneralFilter getFilters() {
		return mFilter;
	}

	public void initFilter(final SearchData searchData,final Context context) {

		createPool();

		mFilteredTickets = AviasalesSDK.getInstance().getSearchData().getTickets();
		pool.submit(new Runnable() {
			@Override
			public void run() {
				if (context == null) {
					return;
				}
				mFilter = new GeneralFilter(context);

				if (searchData.getTickets() != null) {

					PreInitializeFilters preInitializeFilters = new PreInitializeFilters(context, searchData);
					preInitializeFilters.setupFilters();
					mFilter. init(searchData, preInitializeFilters);

					List<TicketData> filteredTickets = mFilter.applyFilters(searchData);
					Collections.sort(filteredTickets, TicketManager.getInstance().getTicketComparator());

					mFilteredTickets = filteredTickets;
				}

			}
		});

	}

}
