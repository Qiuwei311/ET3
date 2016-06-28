package com.example.et3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends ActionBarActivity {
	private WebView mWebPage;
	private ProgressBar mProgressBar;

	private static String mUrl1 = "xx";

	private static final int OPERATION_FINISH = 3;
	private static final int UPDATE_PROGRESS = 4;

	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case OPERATION_FINISH:
				mUrl1 = null;
				mWebPage.destroy();
				System.exit(0);
				break;
			case UPDATE_PROGRESS:
				int progress = (Integer) msg.obj;
				mProgressBar.setProgress(progress);
				break;
			}
			return false;
		}
	});

	@SuppressLint("SetJavaScriptEnabled")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mProgressBar = (ProgressBar) findViewById(R.id.web_progressBar);
		mUrl1 = getIntent().getStringExtra("URL");
		Log.e("CQW", "url = " + mUrl1);

		mWebPage = (WebView) findViewById(R.id.webpage);
		mWebPage.setInitialScale(75);
		if (mUrl1 != null && !mUrl1.equals("")) {
			mWebPage.loadUrl(mUrl1);
		}
		WebSettings settings = mWebPage.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setBlockNetworkImage(true);
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mWebPage.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return false;
			}
		});

		mWebPage.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				Message msg = new Message();
				msg.what = UPDATE_PROGRESS;
				msg.obj = newProgress;
				mHandler.sendMessage(msg);

				if (newProgress == 100) {
					Log.e("CQW", "Web page loading done : " + newProgress);
					operationClick(OPERATION_FINISH, 2);
					return;
				}
			}
		});

	}

	private void operationClick(int action, int secends) {
		Message msg = Message.obtain();
		msg.what = action;
		mHandler.sendMessageDelayed(msg, secends * 1000);
	}
}
