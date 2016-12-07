package com.github.lzyzsd.jsbridge.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

public class MainActivity extends Activity {

	private final String TAG = "MainActivity";

	BridgeWebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        webView = (BridgeWebView) findViewById(R.id.webView);

		webView.setWebChromeClient(new WebChromeClient());

		webView.loadUrl("file:///android_asset/demo.html");

		webView.registerHandler("testObjcCallback", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				Log.i(TAG, "testObjcCallback called: " + data);
                function.onCallBack("Response from testObjcCallback");
			}

		});

        webView.callHandler("testJavascriptHandler", "{\"foo\":\"before ready\"}", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

            }
        });

		findViewById(R.id.button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.callHandler("testJavascriptHandler", "{\"greetingFromObjC\": \"Hi there, JS!\"}", new CallBackFunction() {

					@Override
					public void onCallBack(String data) {
						// TODO Auto-generated method stub
						Log.i(TAG, "testJavascriptHandler responded: " + data);
					}

				});
			}
		});

		findViewById(R.id.reload).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.loadUrl("file:///android_asset/demo.html");
			}
		});

		findViewById(R.id.disable).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.callHandler("_disableJavascriptAlertBoxSafetyTimeout", null, null);
			}
		});
	}
}
