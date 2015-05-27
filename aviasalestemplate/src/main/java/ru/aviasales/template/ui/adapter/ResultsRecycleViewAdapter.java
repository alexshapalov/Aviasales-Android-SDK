package ru.aviasales.template.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.aviasales.core.search.object.TicketData;
import ru.aviasales.template.R;
import ru.aviasales.template.filters.manager.FiltersManager;
import ru.aviasales.template.ui.dialog.ResultsSortingDialog;
import ru.aviasales.template.ui.fragment.ResultsFragment;
import ru.aviasales.template.ui.view.ResultsItemView;


public class ResultsRecycleViewAdapter extends RecyclerView.Adapter<ResultsRecycleViewAdapter.ViewHolder> {

	private Context context;
	private List<TicketData> tickets = new ArrayList<TicketData>();
	private OnClickListener listener;

	public interface OnClickListener {
		void onClick(TicketData ticketData, int position);
	}

	public ResultsRecycleViewAdapter(Context context) {
		this.context = context.getApplicationContext();
		tickets = FiltersManager.getInstance().getFilteredTickets();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.result_item, parent, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		viewHolder.resultsItemView.setTicketData(getItem(position), context);
		viewHolder.resultsItemView.setAlternativePrice(getItem(position).getTotalWithFilters());
		viewHolder.resultsItemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (listener != null) {
					listener.onClick(getItem(position), position);
				}
			}
		});

	}

	@Override
	public int getItemCount() {
		if (tickets != null) {
			return tickets.size();
		} else {
			return 0;
		}
	}

	public TicketData getItem(int i) {
		return tickets.get(i);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ResultsItemView resultsItemView;

		public ViewHolder(View itemView) {
			super(itemView);
			resultsItemView = (ResultsItemView) itemView.findViewById(R.id.cv_results_item);
		}
	}

	private void sort() {
		switch (ResultsFragment.sortingType) {
			case ResultsSortingDialog.SORTING_BY_PRICE:
				Collections.sort(tickets, new Comparator<TicketData>() {
					@Override
					public int compare(TicketData lhs, TicketData rhs) {
						return lhs.getTotalWithFilters() - rhs.getTotalWithFilters();
					}
				});
				break;
			case ResultsSortingDialog.SORTING_BY_DEPARTURE:
				Collections.sort(tickets, new Comparator<TicketData>() {
					@Override
					public int compare(TicketData lhs, TicketData rhs) {
						return (int) (lhs.getDeparture() - rhs.getDeparture());
					}
				});
				break;
			case ResultsSortingDialog.SORTING_BY_ARRIVAL:
				Collections.sort(tickets, new Comparator<TicketData>() {
					@Override
					public int compare(TicketData lhs, TicketData rhs) {
						return (int) (lhs.getArrival() - rhs.getArrival());
					}
				});
				break;
			case ResultsSortingDialog.SORTING_BY_DEPARTURE_ON_RETURN:
				Collections.sort(tickets, new Comparator<TicketData>() {
					@Override
					public int compare(TicketData lhs, TicketData rhs) {
						return (int) (lhs.getReturnDeparture() - rhs.getReturnDeparture());
					}
				});
				break;
			case ResultsSortingDialog.SORTING_BY_ARRIVAL_ON_RETURN:
				Collections.sort(tickets, new Comparator<TicketData>() {
					@Override
					public int compare(TicketData lhs, TicketData rhs) {
						return (int) (lhs.getReturnArrival() - rhs.getReturnArrival());
					}
				});
				break;
			case ResultsSortingDialog.SORTING_BY_DURATION:
				Collections.sort(tickets, new Comparator<TicketData>() {
					@Override
					public int compare(TicketData lhs, TicketData rhs) {
						return lhs.getDurationInMinutes() - rhs.getDurationInMinutes();
					}
				});
				break;
			case ResultsSortingDialog.SORTING_BY_RATING:
				Collections.sort(tickets, new Comparator<TicketData>() {
					@Override
					public int compare(TicketData lhs, TicketData rhs) {
						double lhsRating = lhs.getRating(tickets.get(0));
						double rhsRating = rhs.getRating(tickets.get(0));
						if (lhsRating - rhsRating < 0) {
							return -1;
						} else if (lhsRating - rhsRating > 0) {
							return 1;
						} else {
							return 0;
						}
					}
				});
				break;
		}
	}

	public void setListener(OnClickListener listener) {
		this.listener = listener;
	}

	public void notifyDataSet() {
		if (tickets != null) {
			sort();
		}
		super.notifyDataSetChanged();
	}

	public void reloadFilteredTickets(List<TicketData> filteredTickets) {
		tickets = filteredTickets;
		notifyDataSet();
	}
}