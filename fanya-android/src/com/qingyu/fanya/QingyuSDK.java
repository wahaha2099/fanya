package com.qingyu.fanya;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import java.util.ArrayList;
import java.util.List;
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
public final class QingyuSDK {
	static AccountInfo loginInfo;
	static Context context;
	static Settings settings;

	private static SignDialog join;
//	private static SelectDialog select;
//	private static LoginDialog login;
//	private static WaitDialog wait;
//	private static PaymentDialog payment;
//	private static UpdateDetailDialog detail;
//	private static UpdatePasswordDialog password;

	public static void init(Context context, Settings settings) {
		QingyuSDK.context = context;
		QingyuSDK.settings = settings;

		join = new SignDialog(context, settings);
		
//		payment = new PaymentDialog(context, settings);
//		select = new SelectDialog(context, settings);
//		login = new LoginDialog(context, settings);
//		detail=new UpdateDetailDialog(context,settings);
//		password=new UpdatePasswordDialog(context,settings);
//		wait = new WaitDialog(context);
	}

	public static void login(LoginCallback callback) {
		if (Utils.getAccountCount(context) > 0) {
//			showSelect(callback);
			return;
		}
		showLogin(callback);
	}

	public static void pay(PaymentParams params, PaymentCallback callback) {
		if (loginInfo == null) {
			callback.onError("您尚未登录，不允许做该操作");
			return;
		}
		showPayment(callback, params);
	}
	public static void detail(DetailCallback callback) {
		if (loginInfo == null) {
			callback.onError("您尚未登录，不允许做该操作");
			return;
		}
		showDetail(callback);
	}
	public static void password(PasswordCallback callback) {
		showPassword(callback);
	}
	public static void sendActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (data == null)
			return;
		Message msg = new Message();
		msg.arg1 = 3;
		msg.obj = data.getExtras().getString("pay_result");
//		Uppay.sendMessage(msg);
	}
	/**登录页面**/
	static void showLogin(LoginCallback callback) {
		hideLogin();
//		login.setCallback(callback);
//		login.show();
	}
	static void hideLogin() {
//		if (login != null)
//			login.hide();
	}
	/**注册页面**/
	static void showJoin(LoginCallback callback) {
		hideJoin();
		join.setCallback(callback);
		join.show();
	}
	static void hideJoin() {
		if (join != null)
			join.hide();
	}
	/**详细信息页面**/
	static void showDetail(DetailCallback callback) {
		hideAll();
//		detail.setCallback(callback);
//		detail.setAccountInfo(loginInfo);
//		detail.show();
	}
	static void hideDetail() {
//		if (detail != null)
//			detail.hide();
	}
	/**修改密码页面**/
	static void showPassword(PasswordCallback callback) {
		hideAll();
//		password.setCallback(callback);
//		password.show();
	}
	static void hidePassword() {
//		if (password != null)
//			password.hide();
	}
	/**隐藏所有页面**/
	public static void hideAll(){
		hideDetail();
		hidePassword();
	}
	/**等待**/
	public static void showWait() {
		hideWait();
//		wait.show();
	}
	public static void hideWait() {
//		if (wait != null)
//			wait.hide();
	}
	/**支付页面**/
	static void showPayment(PaymentCallback callback, PaymentParams params) {
		hidePayment();
//		payment.setParams(params, callback);
//		payment.show();
	}
	static void hidePayment() {
//		if (payment != null)
//			payment.hide();
	}
	static AccountInfo getLoginInfo() {
		return loginInfo;
	}

	static void setLoginInfo(AccountInfo loginInfo) {
		QingyuSDK.loginInfo = loginInfo;
	}
    /********************************************/
	/**账号信息静态类**/
	public static class AccountInfo extends JSONObject {
		private String accountID;
		private String username;
		private String code;

		public AccountInfo(JSONObject obj) throws JSONException {
			super();
			// userId":13545034,"userName":"xx123456","token":"fe16107d0d157c63b03c3dc1954f7575"}
			this.accountID = obj.getString("userId");
			this.username = obj.getString("userName");
			this.code = obj.getString("token");
		}

		public String getAccountID() {
			return this.accountID;
		}

		public String getUsername() {
			return this.username;
		}

		public String getCode() {
			return this.code;
		}
	}
	
	/**登录回调接口**/
	public static abstract interface LoginCallback {
		public abstract void onComplete(QingyuSDK.AccountInfo paramAccountInfo);

		public abstract void onCancel();

		public abstract void onError(String paramString);
	}
	/**详细信息回调接口**/
	public static abstract interface DetailCallback {
		public abstract void onComplete();

		public abstract void onCancel();

		public abstract void onError(String paramString);
	}
	/**修改密码回调接口**/
	public static abstract interface PasswordCallback {
		public abstract void onComplete();
		
		public abstract void onCancel();
		
		public abstract void onError(String paramString);
	}
	public static class PaymentArgs {
	}
	/**支付回调接口**/
	public static abstract interface PaymentCallback {
		public abstract void onComplete();

		public abstract void onCancel();

		public abstract void onError(String paramString);
	}
	/**支付参数静态类**/
	public static class PaymentParams {
		private String url;
		private String name;
		private String desc;
		private double money;
		private String ext;

		public PaymentParams(String name, String desc, double money, String url) {
			this.name = name;
			this.desc = desc;
			this.url = url;
			this.money = money;
			this.ext = "";
		}

		public PaymentParams(String name, String desc, double money,
				String url, String ext) {
			this.name = name;
			this.desc = desc;
			this.url = url;
			this.money = money;
			this.ext = ext;
		}

		public String getURL() {
			return this.url;
		}

		public void setURL(String url) {
			this.url = url;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDesc() {
			return this.desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public double getMoney() {
			return this.money;
		}

		public void setMoney(double money) {
			this.money = money;
		}

		public String getExt() {
			return this.ext;
		}

		public void setExt(String ext) {
			this.ext = ext;
		}

//		public List<NameValuePair> toHttpParams(int type) {
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//			
//			params.add(new BasicNameValuePair("appId", "10"));
//			params.add(new BasicNameValuePair("playerId", QingyuSDK.getLoginInfo().getAccountID()));
//			params.add(new BasicNameValuePair("customOrderId", "0000001"));
//			params.add(new BasicNameValuePair("platfromId", "1"));
//			params.add(new BasicNameValuePair("productName", "钻石"));
//			params.add(new BasicNameValuePair("unitPrice", "25.00"));
//			params.add(new BasicNameValuePair("count", "1"));
//			params.add(new BasicNameValuePair("amount", String.valueOf(this.money)));
//			params.add(new BasicNameValuePair("customInfo", ext));
//			params.add(new BasicNameValuePair("user", QingyuSDK.getLoginInfo().getUsername()));
//			
//			switch (type) {
//			case 1:
//				params.add(new BasicNameValuePair("payType", "A1"));
//				break;
//			case 2:
//				params.add(new BasicNameValuePair("payType", "U1"));
//				break;
//			case 3:
//			    params.add(new BasicNameValuePair("payType", "W1"));
//			    break;
//			default:
//				break;
//			
//			}
//			
//			
//			
//			return params;
//		}
	}
	/**初始化参数静态类**/
	public static class Settings {
		private String appid; 
		private String appkey;
		private String channel;

		public Settings(String appid, String appkey, String channel) {
			this.appid = appid;
			this.appkey = appkey;
			this.channel = channel;
		}

		public String getAppID() {
			return this.appid;
		}

		public void setAppID(String appid) {
			this.appid = appid;
		}

		public String getAppkey() {
			return this.appkey;
		}

		public void setAppkey(String appkey) {
			this.appkey = appkey;
		}

		public String getChannel() {
			return this.channel;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}
	}
}
