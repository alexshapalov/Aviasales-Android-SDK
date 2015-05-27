package ru.aviasales.template.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

import ru.aviasales.template.R;
import ru.aviasales.template.ui.dialog.BrowserLoadingDialogFragment;
import ru.aviasales.template.utils.Utils;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class BrowserFragment extends BaseFragment {

	private static final String EXTRA_SHOULD_SHOW_DIALOG = "extra_should_show_dialog";

	public static final String PROPERTY_BUY_URL = "BUY_URL";
	public static final String PROPERTY_BUY_AGENCY = "BUY_AGENCY";

	private boolean needToDismissDialog = false;
	private WebView webView;

	private BrowserLoadingDialogFragment dialog;
	private String agency;
	private boolean loadingFinished = false;
	private MenuItem btnBack;
	private MenuItem btnForward;
	private ProgressBar progressbar;

	public static BrowserFragment newInstance() {
		BrowserFragment browserFragment = new BrowserFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean(EXTRA_SHOULD_SHOW_DIALOG, true);
		return browserFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		agency = Utils.getPreferences(getActivity()).getString(PROPERTY_BUY_AGENCY, null);

		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.browser_fragment, container, false);
		setupViews(layout);

		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		setTextToActionBar(String.format(getString(R.string.browser_title), agency));

		return layout;
	}

	private void setupViews(ViewGroup layout) {
		FrameLayout webViewPlaceHolder = (FrameLayout) layout.findViewById(R.id.webview_placeholder);
		progressbar = (ProgressBar) layout.findViewById(R.id.progressbar);
		progressbar.setAlpha(0);

		String url = Utils.getPreferences(getActivity()).getString(PROPERTY_BUY_URL, null);
		setupWebView(webViewPlaceHolder, url);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setupWebView(FrameLayout webViewPlaceHolder, String url) {
		if (webView == null) {

			webView = new WebView(getActivity());
			webView.setLayoutParams(getWebViewLayoutParams());

			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setUseWideViewPort(true);
			webView.getSettings().setLoadWithOverviewMode(true);
			webView.getSettings().setSupportZoom(true);
			webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			webView.getSettings().setBuiltInZoomControls(true);
			webView.getSettings().setDisplayZoomControls(false);
			webView.getSettings().setDomStorageEnabled(true);

			webView.loadUrl(url);

			webView.setWebViewClient(new AsWebViewClient());

			webView.setWebChromeClient(new WebChromeClient() {
				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					super.onProgressChanged(view, newProgress);

					if (progressbar.getAlpha() == 0) {
						progressbar.setVisibility(View.VISIBLE);
						progressbar.animate().alpha(1).setDuration(200).start();
					}
					progressbar.setProgress(newProgress);

					if (newProgress == 100) {
						progressbar.animate().alpha(0).setDuration(200).setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								progressbar.setVisibility(View.GONE);
							}
						}).start();
					}
				}
			});
		} else {
			if (webView.getParent() != null) {
				((ViewGroup) webView.getParent()).removeView(webView);
			}
		}

		webViewPlaceHolder.removeAllViews();
		webViewPlaceHolder.addView(webView);
	}

	private ViewGroup.LayoutParams getWebViewLayoutParams() {
		return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	}

	@Override
	public void onResume() {
		if (!loadingFinished) {
			showLoadingDialog();
		}

		if (needToDismissDialog) {
			if (dialog != null) {
				dialog.dismiss();
			}
		}

		super.onResume();
	}

	@Override
	public void onPause() {
		dismissDialogFragment();
		super.onPause();
	}

	private void showLoadingDialog() {
		if (dialog != null) {
			try {
				dialog.dismiss();
				dialog = null;
			} catch (Exception ignore) {
			}
		}
		if (dialog == null || !dialog.isAdded()) {
			FragmentManager fm = getActivity().getFragmentManager();
			dialog = new BrowserLoadingDialogFragment();
			dialog.setCancelable(false);
			dialog.setAgency(agency);
			dialog.show(fm, "browser_dialog");
		}

		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				if (dialog != null) {
					dismissDialogFragment();
				}
			}
		};
		new Timer().schedule(timerTask, 4000);
	}

	@Override
	public boolean onBackPressed() {
		dismissDialogFragment();
		if (webView != null) {
			webView.setVisibility(View.INVISIBLE);
			((ViewGroup) webView.getParent()).removeAllViews();
			webView.clearHistory();
			webView.clearCache(true);
			webView.destroy();
			webView = null;
		}
		return false;
	}

	@Override
	protected void resumeDialog(String removedDialogFragmentTag) {

	}


	private void dismissDialogFragment() {
		if (dialog != null) {
			try {
				dialog.dismiss();
				dialog = null;
			} catch (IllegalStateException e) {
				needToDismissDialog = true;
			}
		}
	}

	private class AsWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return false;
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed(); // Ignore SSL certificate errors
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);

			setBrowserNav();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

			loadingFinished = true;

			dismissDialogFragment();
			setBrowserNav();
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			super.onLoadResource(view, url);
		}
	}

	private void setBrowserNav() {
		if (webView != null && btnBack != null && btnForward != null) {
			btnBack.setEnabled(webView.canGoBack());
			btnForward.setEnabled(webView.canGoForward());
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (getActivity() != null) {
			inflater.inflate(R.menu.browser_menu, menu);
		}

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		btnBack = menu.findItem(R.id.back);
		btnForward = menu.findItem(R.id.forward);

		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if(id == R.id.back){
			webView.goBack();
			return true;
		} else if(id == R.id. forward){
			webView.goForward();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}

	}

}
