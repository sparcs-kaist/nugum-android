package com.sparcs.nugum;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.widget.Toast;


public final class Util {
    public static String Post(String url, String[] name, String[] data) throws Exception {
    	HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        int size = name.length;
        if (name.length != data.length) throw new Exception("NOT EQUAL LENGTH");
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(size);
        for (int i=0;i<size;i++) {
           	nameValuePairs.add(new BasicNameValuePair(name[i], data[i]));
        }
            
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost);
            
        String tmp = EntityUtils.toString(response.getEntity());
        return tmp;            
    }
    public static String getAndroidID(ContentResolver cr) {
    	return Secure.getString(cr, Secure.ANDROID_ID);
    }
    public static void toastString(Context c, String s) {
    	Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
    }
    public static float convertDpToPixel(float dp,Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi/160f);
        return px;
    }
    public static float convertPixelsToDp(float px,Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi/160f);
        return dp;
    }
}