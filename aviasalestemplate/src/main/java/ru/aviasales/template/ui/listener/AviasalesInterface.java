package ru.aviasales.template.ui.listener;

import ru.aviasales.core.search_airports.object.PlaceData;
import ru.aviasales.template.ui.model.SearchFormData;

public interface AviasalesInterface extends OnPlaceSelectedListener {

	void setOriginData(PlaceData placeData);
	void setDestinationData(PlaceData placeData);

	SearchFormData getSearchFormData();

	void saveState();

}
