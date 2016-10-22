package com.qingyu.fanya.webview;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class LocationSDK {
	  
    public  LocationManager lm;  
    private static final String TAG="GPS Services";  
    
    //经纬度回调
    private LocationCallback callback;
    /**
     * 获取经纬度的回调
     * @author admin
     *
     */
    public interface LocationCallback{
    	public void callback( String longitude ,String latitude ); 
    }
    
    
    public void getLocation(Activity activity , LocationCallback callback){  
    	this.callback = callback;
    	
        lm=(LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);  
          
        //判断GPS是否正常启动  
       /* if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
            Toast.makeText(activity, "请开启GPS导航...", Toast.LENGTH_SHORT).show();  
            //返回开启GPS导航设置界面  
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);     
            activity.startActivityForResult(intent,0);   
            return;  
        }  */
        
       /* if(!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){  
            Toast.makeText(activity, "请开启GPS导航...", Toast.LENGTH_SHORT).show();  
            //返回开启GPS导航设置界面  
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);     
            activity.startActivityForResult(intent,0);   
            return;  
        }  */
          
        //为获取地理位置信息时设置查询条件  
        String bestProvider = lm.getBestProvider(getCriteria(), true);  
        //获取位置信息  
        //如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER  
        Location location= lm.getLastKnownLocation(bestProvider);  
//        Location location= lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);   
        successCallback(location);  
        //监听状态  
//        lm.addGpsStatusListener(listener);  
        //绑定监听，有4个参数      
        //参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种  
        //参数2，位置信息更新周期，单位毫秒      
        //参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息      
        //参数4，监听      
        //备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新     
          
        // 1秒更新一次，或最小位移变化超过1米更新一次；  
        //注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置  
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);  
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);  
    }  
          
    //位置监听  
    private LocationListener locationListener=new LocationListener() {  
          
        /** 
         * 位置信息变化时触发 
         */  
        public void onLocationChanged(Location location) {  
            successCallback(location);  
            Log.i(TAG, "时间："+location.getTime());   
            Log.i(TAG, "经度："+location.getLongitude());   
            Log.i(TAG, "纬度："+location.getLatitude());   
            Log.i(TAG, "海拔："+location.getAltitude());   
        }  
          
        /** 
         * GPS状态变化时触发 
         */  
        public void onStatusChanged(String provider, int status, Bundle extras) {  
            switch (status) {  
            //GPS状态为可见时  
            case LocationProvider.AVAILABLE:  
                Log.i(TAG, "当前GPS状态为可见状态");  
                break;  
            //GPS状态为服务区外时  
            case LocationProvider.OUT_OF_SERVICE:  
                Log.i(TAG, "当前GPS状态为服务区外状态");  
                break;  
            //GPS状态为暂停服务时  
            case LocationProvider.TEMPORARILY_UNAVAILABLE:  
                Log.i(TAG, "当前GPS状态为暂停服务状态");  
                break;  
            }  
        }  
      
        /** 
         * GPS开启时触发 
         */  
        public void onProviderEnabled(String provider) {  
            Location location=lm.getLastKnownLocation(provider);  
            successCallback(location);  
        }  
      
        /** 
         * GPS禁用时触发 
         */  
        public void onProviderDisabled(String provider) {  
            successCallback(null);  
        }  
    };  
          
    //状态监听  
   /* GpsStatus.Listener listener = new GpsStatus.Listener() {  
        public void onGpsStatusChanged(int event) {  
            switch (event) {  
            //第一次定位  
            case GpsStatus.GPS_EVENT_FIRST_FIX:  
                Log.i(TAG, "第一次定位");  
                break;  
            //卫星状态改变  
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:  
                Log.i(TAG, "卫星状态改变");  
                //获取当前状态  
                GpsStatus gpsStatus=lm.getGpsStatus(null);  
                //获取卫星颗数的默认最大值  
                int maxSatellites = gpsStatus.getMaxSatellites();  
                //创建一个迭代器保存所有卫星   
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();  
                int count = 0;       
                while (iters.hasNext() && count <= maxSatellites) {       
                    GpsSatellite s = iters.next();       
                    count++;       
                }     
                //System.out.println("搜索到："+count+"颗卫星");  
               // tv_gps.setText("搜索到："+count+"颗卫星");  
                break;  
            //定位启动  
            case GpsStatus.GPS_EVENT_STARTED:  
                Log.i(TAG, "定位启动");  
                break;  
            //定位结束  
            case GpsStatus.GPS_EVENT_STOPPED:  
                Log.i(TAG, "定位结束");  
                break;  
            }  
        };  
    };  */
          
    /** 
     * 实时更新文本内容 
     *  
     * @param location 
     */  
    private void successCallback(Location location){  
    	if( callback != null && location!=null ){
    		callback.callback(location.getLongitude()+"", location.getLatitude() +"");
    	}
        /*if(location!=null){  
            editText.setText("设备位置信息\n\n经度：");  
            editText.append(String.valueOf(location.getLongitude()));  
            editText.append("\n纬度：");  
            editText.append(String.valueOf(location.getLatitude()));  
        }else{  
            //清空EditText对象  
            editText.getEditableText().clear();  
        }  */
    }  
      
    /** 
     * 返回查询条件 
     * @return 
     */  
    private Criteria getCriteria(){  
        Criteria criteria=new Criteria();  
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细   
        criteria.setAccuracy(Criteria.ACCURACY_FINE);      
        //设置是否要求速度  
        criteria.setSpeedRequired(false);  
        // 设置是否允许运营商收费    
        criteria.setCostAllowed(false);  
        //设置是否需要方位信息  
        criteria.setBearingRequired(false);  
        //设置是否需要海拔信息  
        criteria.setAltitudeRequired(false);  
        // 设置对电源的需求    
        criteria.setPowerRequirement(Criteria.POWER_LOW);  
        return criteria;  
    }  
      

     protected void onDestroy() {  
      lm.removeUpdates(locationListener);  
     }  
      

}
