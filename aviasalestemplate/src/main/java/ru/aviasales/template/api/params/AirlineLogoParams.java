package ru.aviasales.template.api.params;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class AirlineLogoParams extends ApiParams {
	private String iata;
	private ImageView image;
	private ImageLoadingListener imageLoadingListener;
	private int width;
	private int height;

	public ImageLoadingListener getImageLoadingListener() {
		return imageLoadingListener;
	}

	public void setImageLoadingListener(ImageLoadingListener imageLoadingListener) {
		this.imageLoadingListener = imageLoadingListener;
	}

	public ImageView getImage() {
		return image;
	}

	public void setImage(ImageView mImage) {
		this.image = mImage;
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String mIata) {
		this.iata = mIata;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
