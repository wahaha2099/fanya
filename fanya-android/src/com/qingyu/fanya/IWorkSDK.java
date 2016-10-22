package com.qingyu.fanya;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.fiberhome.mobileark.sso_v2.GetParamListener;
import com.fiberhome.mobileark.sso_v2.GetTokenListener;
import com.fiberhome.mobileark.sso_v2.MobileArkSSOAgent;
import com.fiberhome.mobileark.sso_v2.SSOStatusListener;

public class IWorkSDK {

	public static String token ; 
	
	public static String loginName ; 
	
	public static IWorkSDK instance = new IWorkSDK(); 
	
	public static LoginCallback callback ;
	
	MobileArkSSOAgent ssoAgent ; 
	
	public void login( final Activity activity ) {
		ssoAgent = MobileArkSSOAgent.getInstance( activity.getApplicationContext());
		String packagename = "com.saicgmuat.mobileark";
		ssoAgent.setMobilearkPackagename(packagename);//不设置则使用默认值
		ssoAgent.initSSOAgent(new SSOStatusListener() {
			@Override
			public void finishCallBack(int resultCode, String resultMessage) {
				//
				if( resultCode != 0 )Toast.makeText(activity ,"登录失败，请重试："+resultCode + "," + resultMessage,	Toast.LENGTH_SHORT).show();
				Log.e("SDK", resultCode +" "+ resultMessage);
				if (resultCode == 0) {
					getToken();
					getUserName();
				}
			}
		});
	}
	
	private void getToken(){
		ssoAgent.getToken(new GetTokenListener() {
			@Override
			public void finishCallBack(int resultCode, String resultMessage, String token) {
				// do something
				Log.e("SDK", " login finish " + resultCode +" token "+ token  );
				
				IWorkSDK.token = token ;
				
				instance.loginSuccess();
			}
		});
	}
	
	protected void loginSuccess() {
		if(callback != null) callback.loginCallback();
	}

	private void getUserName(){
		String key = "loginname";// 要获取的登录参数Key 值，可输入值为“loginname”,“password”,”ecid”

		ssoAgent.getParam(key, new GetParamListener() {
			@Override
			public void finishCallBack(int resultCode, String resultMessage, String value) {
				Log.e("SDK", " login finish " + resultCode +" value "+ value  );
			
				IWorkSDK.loginName = value;
				
				instance.loginSuccess();
			}
		});
	}
	
	interface LoginCallback {
		
		public void loginCallback();
	}

	
}
