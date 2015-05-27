package ru.aviasales.template.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import ru.aviasales.core.AviasalesSDK;
import ru.aviasales.core.search_airports.object.PlaceData;
import ru.aviasales.template.R;
import ru.aviasales.template.ui.listener.AviasalesInterface;
import ru.aviasales.template.ui.model.SearchFormData;

public class AviasalesFragment extends Fragment implements AviasalesInterface {

	public final static String TAG = "aviasales_fragment";
	public final static String TAG_CHILD = "aviasales_child_fragment";

	public final static int CACHE_SIZE = 20 * 1024 * 1024;
	public final static int CACHE_FILE_COUNT = 100;
	public final static int MEMORY_CACHE_SIZE = 5 * 1024 * 1024;

	private FragmentManager mFragmentManager;

	private SearchFormData mSearchFormData;

	private View mView;

	public static Fragment newInstance() {
		return new AviasalesFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSearchFormData = new SearchFormData(getActivity());
		AviasalesSDK.getInstance().init(getActivity());
		initImageLoader(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.aviasales_fragment_layout, null);


		return mView;
	}

	public FragmentManager getAviasalesFragmentManager() {
		return mFragmentManager;
	}

	public void startFragment(BaseFragment fragment, boolean shouldAddToBackStack) {

		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_child_place, (Fragment) fragment, fragment.getClass().getSimpleName());
		if (shouldAddToBackStack) {
			fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
		}
		fragmentTransaction.commit();

	}

	public void popFragmentFromBackStack(){
		onBackPressed();
	}


	private void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.diskCacheSize(CACHE_SIZE)
				.diskCacheFileCount(CACHE_FILE_COUNT)
				.memoryCacheSize(MEMORY_CACHE_SIZE)
				.build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mFragmentManager = this.getChildFragmentManager();

		Fragment fragment;
		if((mFragmentManager.findFragmentByTag(TAG_CHILD)) == null) {
			fragment = SearchFromFragment.newInstance();
			mFragmentManager.beginTransaction().replace(R.id.fragment_child_place, fragment, TAG_CHILD).commit();
		}

	}

	public boolean onBackPressed() {
		if (mFragmentManager.getBackStackEntryCount() > 0) {
			mFragmentManager.popBackStack();
			return true;
		} else {
			return false;
		}
	}

	public void popBackStackInclusive(String tag){
		mFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	@Override
	public void onStop() {
		mSearchFormData.saveState();
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mView != null) {
			ViewGroup parentViewGroup = (ViewGroup) mView.getParent();
			if (parentViewGroup != null) {
				parentViewGroup.removeAllViews();
			}
		}
	}

	@Override
	public void onAirportSelected(PlaceData placeData, int type) {
		if (type == SelectAirportFragment.TYPE_ORIGIN) {
			setOriginData(placeData);
		} else {
			setDestinationData(placeData);
		}
	}

	@Override
	public void setOriginData(PlaceData placeData) {
		mSearchFormData.setOrigin(placeData);
	}

	@Override
	public void setDestinationData(PlaceData placeData) {
		mSearchFormData.setDestination(placeData);
	}

	@Override
	public SearchFormData getSearchFormData() {
		return mSearchFormData;
	}

	@Override
	public void saveState(){
		mSearchFormData.saveState();
	}
}
