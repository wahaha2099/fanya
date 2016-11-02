package com.qingyu.fanya;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.qingyu.fanya.IWorkSDK.LoginCallback;
import com.qingyu.fanya.webview.LocationSDK;
import com.qingyu.fanya.webview.LocationSDK.LocationCallback;
import com.qingyu.fanya.webview.WebActivity;

/**
 * 嵌入webview的activity
 * @author admin
 */
public class LoginActivity extends WebActivity {

	LocationSDK sdk = new LocationSDK();
	String longitude ;
	String latitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setUrl("http://120.26.55.126/");
		//setUrl("http://192.168.1.35/");
		show();
		
		getLocation( false );
		sdkLogin();
	}
	
	public void getLocation(final  boolean sendToPage ){
		Log.i("LoginActivity", "  开始获取经纬度 " );
		LocationCallback callback = new LocationCallback(){
			@Override
			public void callback(String longitude, String latitude) {
				Log.i("LoginActivity", " longitude = " + longitude );
				LoginActivity.this.longitude = longitude;
				LoginActivity.this.latitude = latitude;
				if( sendToPage ){
					sendLocationToPage();
				}
			}};
		sdk.getLocation(this, callback);
	}
	
	public void sdkLogin(){
		IWorkSDK.callback = new LoginCallback(){
			public void loginCallback(){
				notifyPage();
			}
		};
		IWorkSDK.instance.login(this);
	}
	
	public void nativeLogin(){
		webview.post(new Runnable(){

			@Override
			public void run() {
				sdkLogin();
			}});
	}
	
	public JSObject getJSObject() {
		LoginJSObject json = new LoginJSObject();
		json.init(getApplicationContext());
		json.setActivity(this);
		return json;
	}
	
	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		
	}
	
	/**
	 * 通知页面登录成功
	 */
	public void notifyPage(){
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("token", IWorkSDK.token);
		hash.put("loginname", IWorkSDK.loginName );
		hash.put("serAddress", Utils.platformip());
		JSONObject obj = new JSONObject(hash);
		call("setContent", obj);
	}

	public void sendLocationToPage(){
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("longitude", longitude);
		hash.put("latitude", latitude );
		JSONObject obj = new JSONObject(hash);
		call("setLocation", obj);
	}
	
	public void hide() {
		call("resetControl");
	}

	/**
	 * activity和webview通信的js对象
	 * @author admin
	 */
	static class LoginJSObject extends WebActivity.JSObject {

		LoginActivity activity ;
		
		@JavascriptInterface  
		public String toString() { return "android"; }  
		
		public void setActivity( LoginActivity activity ){
			this.activity = activity;
		};
		 
		@JavascriptInterface  
		public void nativeLogin(){
			activity.nativeLogin();
		}
		
		@JavascriptInterface  
		protected void onMessage(int method, String str) {
			super.onMessage(method, str);

			Utils.info("join callback " + str);

			switch (method) {
			case 1://1 获取 经纬度
				if(activity != null){
					if( activity.longitude == null || activity.longitude.length() == 0 ){
						Toast.makeText(activity, "请开启定位服务后重试", Toast.LENGTH_SHORT).show();
						activity.getLocation( true );
					}else{
						activity.sendLocationToPage();
					}
				}
				break;
			case 3://2发送消息
				try {
					//发送消息{"message":"berhasil"}
					JSONObject objRs = new JSONObject(str);
					
					if( objRs.getString("message") != null ){
						Toast.makeText(this.context, objRs.getString("message"), Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					Log.e("LoginActivity", "调用失败");
				}
				break;
			}
		}
	}
}
