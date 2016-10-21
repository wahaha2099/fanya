package com.qingyu.fanya;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.qingyu.fanya.IWorkSDK.LoginCallback;
import com.qingyu.fanya.webview.WebActivity;


public class LoginActivity extends WebActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setUrl("http://192.168.1.107/");
		show();
		
		sdkLogin();
	}
	
	public void sdkLogin(){
		IWorkSDK.callback = new LoginCallback(){
			public void loginCallback(){
				notifyPage();
			}
		};
		IWorkSDK.instance.login(this);
	}
	
	public JSObject getJSObject() {
		JoinJSObject json = new JoinJSObject();
		json.init(getApplicationContext());
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

	public void hide() {
		call("resetControl");
	}

	static class JoinJSObject extends WebActivity.JSObject {
		private QingyuSDK.LoginCallback callback;

		@JavascriptInterface  
		public String toString() { return "android"; }  
		 
		@JavascriptInterface  
		protected void onMessage(int method, String str) {
			super.onMessage(method, str);

			Utils.info("join callback " + str);

			switch (method) {
			case 1:
				QingyuSDK.hideJoin();
				if (Utils.getAccountCount(this.context) > 0) {
//					QingyuSDK.showSelect(this.callback);
				} else
					QingyuSDK.showLogin(this.callback);
				break;
			case 2:
				try {
					// {"code":"0000","message":"berhasil","returnObj":{"bbsData":null,"newestServerNo":null,"userName":"xx12345g"}}

					JSONObject objRs = new JSONObject(str);
					if (!"0000".equalsIgnoreCase(objRs.getString("code"))) {
						String err = objRs.getString("message");
						Utils.error(err);
						Toast.makeText(this.context, "注册失败!", Toast.LENGTH_LONG).show();
						
						this.callback.onError(err);
					} else {
						JSONObject obj = (JSONObject) objRs.get("returnObj");
						JSONArray list = Utils.getAccountList(this.context);
						JSONArray clone = new JSONArray();
						clone.put(obj);
						for (int i = 0; i < list.length(); i++) {
							if (i >= 2)
								break;
							JSONObject child = list.getJSONObject(i);
							if (!child.get("userId").equals(obj.get("userId"))) {
								clone.put(child);
							}
						}

						Toast.makeText(this.context, "注册成功", Toast.LENGTH_LONG).show();
						Utils.saveAccoutList(this.context, clone);
						QingyuSDK.hideJoin();
//						QingyuSDK.showSelect(this.callback);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

//		public JoinJSObject(Context context) {
//			super(context);
//		}

		public void setCallback(QingyuSDK.LoginCallback callback) {
			this.callback = callback;
		}
	}
}
