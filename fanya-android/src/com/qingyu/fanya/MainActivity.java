package com.qingyu.fanya;

import com.qingyu.fanya.QingyuSDK.LoginCallback;
import com.qingyu.fanya.QingyuSDK.Settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends Activity {

	QingyuSDK sdk = new QingyuSDK();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginCallback callback = new LoginCallback(){
        	public  void onComplete(QingyuSDK.AccountInfo paramAccountInfo){};

    		public  void onCancel(){};

    		public  void onError(String paramString){};
        };
        Settings settings = new Settings("1", "1", "1");
		sdk.init(this, settings);
		//        setContentView(R.layout.activity_main);
        sdk.showJoin(callback);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
