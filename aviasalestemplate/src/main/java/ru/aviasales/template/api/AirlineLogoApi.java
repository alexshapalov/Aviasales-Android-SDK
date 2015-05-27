package ru.aviasales.template.api;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import ru.aviasales.template.api.params.AirlineLogoParams;
import ru.aviasales.template.utils.Defined;


public class AirlineLogoApi {

	private DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.resetViewBeforeLoading(true)
			.build();

	public void getAirlineLogo(final AirlineLogoParams params) {
		ImageLoader.getInstance().displayImage(
				getUrl(params.getIata(), params.getWidth(), params.getHeight()),
				params.getImage(),
				displayOptions,
				params.getImageLoadingListener());
	}

	public void loadAirlineLogo(final AirlineLogoParams params) {
		ImageLoader.getInstance().loadImage(getUrl(params.getIata(), params.getWidth(), params.getHeight()),
				params.getImageLoadingListener());
	}

	public static String getUrl(String iata, int logoWidth, int logoHeight) {
		return Defined.getAirlineLogoTemplateUrl()
				.replace("{Width}", String.valueOf(logoWidth))
				.replace("{Height}", String.valueOf(logoHeight))
				.replace("{IATA}", iata);
	}
}
