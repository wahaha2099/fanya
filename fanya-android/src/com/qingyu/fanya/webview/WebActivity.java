package com.qingyu.fanya.webview;

import org.json.JSONException;
import org.json.JSONObject;

import com.qingyu.fanya.QingyuSDK;
import com.qingyu.fanya.Utils;
import com.qingyu.fanya.WebDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;




public class WebActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	String url = "";
	
	public void setUrl(String u ){ 
		url = u ;
	}
	
	private Boolean loaded = Boolean.valueOf(false);
	private String callstr;
	protected WebView webview;
	protected JSObject jsobject;
	
	@SuppressLint({ "SetJavaScriptEnabled" })
	public void show(){
//		setCancelable(false);
		requestWindowFeature(1);
		
		jsobject = getJSObject();

		Window window = getWindow();
		window.setBackgroundDrawable(new ColorDrawable(0));

		this.webview = new WebView(this.getApplicationContext());
		WebSettings settings = this.webview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDefaultTextEncodingName("utf-8");
		settings.setAppCacheEnabled(false);
		settings.setAppCacheMaxSize(1L);
		settings.setCacheMode(2);
		settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

		this.webview.setBackgroundColor(0);
		this.webview.setVerticalScrollBarEnabled(false);
		this.webview.setHorizontalScrollBarEnabled(false);
		if (jsobject != null) {
			this.webview.addJavascriptInterface(jsobject, "android");
			this.jsobject = jsobject;
		}
		this.webview.setWebChromeClient(new WebChromeClient());
		this.webview.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				if (!WebActivity.this.loaded.booleanValue()) {
					WebActivity.this.loaded = Boolean.valueOf(true);
					if (WebActivity.this.callstr != null) {
						WebActivity.this.webview.loadUrl(WebActivity.this.callstr);
						WebActivity.this.callstr = null;
					}
				}
				super.onPageFinished(view, url);
			}
		});
		if(url.startsWith("http://") || url.startsWith("https://")){
			this.webview.loadUrl(url);
		}else{
			this.webview.loadUrl(Utils.localURL(url));
		}
		
		setContentView(this.webview);

		WindowManager.LayoutParams params = window.getAttributes();
		params.dimAmount = 0.2F;
		window.setAttributes(params);
	}
	
	public JSObject getJSObject() {
		return this.jsobject;
	}

	public void call(String method, JSONObject obj) {
		call(method, obj.toString());
	}

	public void call(String method, String arg) {
		if (!this.loaded.booleanValue()) {
			this.callstr = ("javascript:" + method + "('" + arg + "')");
		} else {
			this.webview.loadUrl("javascript:" + method + "('" + arg + "')");
		}
	}

	public void call(String method) {
		call(method, "");
	}

	public static class JSObject {
		
		@JavascriptInterface  
		public String toString() { return "android"; }  
		
		public static final int METHOD_INFO = 1001;
		public static final int METHOD_DEBUG = 1002;
		public static final int METHOD_ERROR = 1003;
		public static final int METHOD_WARN = 1004;
		public static final int METHOD_TOAST = 1005;
		public static final int METHOD_SHOWWAIT = 1006;
		public static final int METHOD_HIDEWAIT = 1007;
		protected Context context;
		protected Handler handler;

		public void init(Context context) {
			this.context = context;
			this.handler = new Handler() {
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					WebActivity.JSObject.this.onMessage(msg.arg1,
							(String) msg.obj);
				}
			};
		}

		@SuppressLint({ "JavascriptInterface" })
		@JavascriptInterface  
		public void info(String msg) {
			sendMessage(1001, msg);
		}

		@SuppressLint({ "JavascriptInterface" })
		@JavascriptInterface  
		public void debug(String msg) {
			sendMessage(1002, msg);
		}

		@SuppressLint({ "JavascriptInterface" })
		@JavascriptInterface  
		public void error(String msg) {
			sendMessage(1003, msg);
		}

		@SuppressLint({ "JavascriptInterface" })
		@JavascriptInterface  
		public void warn(String msg) {
			sendMessage(1003, msg);
		}

		@SuppressLint({ "JavascriptInterface" })
		@JavascriptInterface  
		public void toast(String msg) {
			sendMessage(1005, msg);
		}

		@SuppressLint({ "JavascriptInterface" })
		@JavascriptInterface  
		public void showWait() {
			sendMessage(1006);
		}

		@SuppressLint({ "JavascriptInterface" })
		@JavascriptInterface  
		public void hideWait() {
			sendMessage(1007);
		}
		
		@SuppressLint({ "JavascriptInterface" })
		@JavascriptInterface  
		public void getLocation(){
			sendMessage(1);
		}

		public void sendMessage(int type, Object obj) {
			Message msg = new Message();
			msg.arg1 = type;
			msg.obj = obj;
			this.handler.sendMessage(msg);
		}

		public void sendMessage(int type) {
			sendMessage(type, null);
		}

		protected void onMessage(int method, String str) {
			switch (method) {
			case 1001:
				Utils.info(str);
				break;
			case 1002:
				Utils.debug(str);
				break;
			case 1003:
				Utils.error(str);
				break;
			case 1004:
				Utils.warn(str);
				break;
			case 1005:
				Utils.toast(this.context, str);
				break;
			case 1006:
				QingyuSDK.showWait();
				break;
			case 1007:
				QingyuSDK.hideWait();
			}
		}

		@SuppressLint({ "JavascriptInterface" })
		public void call(String str) {
			try {
				JSONObject obj = new JSONObject(str);
				String args = "";
				if (obj.has("args")) {
					args = obj.getString("args");
				}
				sendMessage(obj.getInt("method"), args);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
}
