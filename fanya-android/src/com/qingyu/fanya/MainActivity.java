package com.qingyu.fanya;

import com.fiberhome.mobileark.sso_v2.GetParamListener;
import com.fiberhome.mobileark.sso_v2.GetTokenListener;
import com.fiberhome.mobileark.sso_v2.MobileArkSSOAgent;
import com.fiberhome.mobileark.sso_v2.SSOStatusListener;
import com.qingyu.fanya.QingyuSDK.LoginCallback;
import com.qingyu.fanya.QingyuSDK.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

	QingyuSDK sdk = new QingyuSDK();
	
	MobileArkSSOAgent ssoAgent ; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		login();
	}

	private void showPage(){
		if( IWorkSDK.token == null || IWorkSDK.loginName == null )
			return ; 
		
		 
		Intent bintent = new Intent(this, LoginActivity.class);
		//设置 bintent的Bundle的一个值
		String bsay = "Hello, this is B speaking"; 
		bintent.putExtra("listenB", bsay);
		startActivity(bintent);
	}
	
	public void login() {
		ssoAgent = MobileArkSSOAgent.getInstance( getApplicationContext());
		String packagename = "com.saicgmuat.mobileark";
		ssoAgent.setMobilearkPackagename(packagename);//不设置则使用默认值
		ssoAgent.initSSOAgent(new SSOStatusListener() {
			@Override
			public void finishCallBack(int resultCode, String resultMessage) {
				Toast.makeText(MainActivity.this,
						resultCode + "," + resultMessage,
						Toast.LENGTH_SHORT).show();
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
				
				showPage();
			}
		});
	}
	
	private void getUserName(){
		String key = "loginname";// 要获取的登录参数Key 值，可输入值为“loginname”,“password”,”ecid”

		ssoAgent.getParam(key, new GetParamListener() {
			@Override
			public void finishCallBack(int resultCode, String resultMessage, String value) {
				Log.e("SDK", " login finish " + resultCode +" value "+ value  );
			
				IWorkSDK.loginName = value;
				
				showPage();
			}
		});
	}
}


