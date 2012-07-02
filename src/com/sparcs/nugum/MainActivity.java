package com.sparcs.nugum;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.nugum.*;

public class MainActivity extends Activity {
	List<Person> listData;
	ArrayList<String> listNames;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getAddress();
        
        ListView list=(ListView) findViewById(R.id.ListView01);
        final EditText edit = (EditText)findViewById(R.id.EditText01);
        ArrayAdapter<String> Adapter;
        Adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listNames);
        
        list.setAdapter(Adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
	
	public void mOnClick(View v){
	
		AdapterView.OnItemClickListener mItemClickListener=
			new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView parent,View view,int position,long id){
					final String pager = listData.get(position).pager;
					new AlertDialog.Builder(MainActivity.this)
					.setTitle("전화를 걸겠어요?")
					.setMessage("전화번호 :" + pager)
					.setPositiveButton("걸기",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							call(pager);
						}
					})
					.setNegativeButton("닫기",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					})
					.show();
				}
		};
	}
				
	
	/*
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		final String pager = listData.get(position).pager;
		new AlertDialog.Builder(MainActivity.this)
		.setTitle("전화를 걸겠어요?")
		.setMessage("전화번호 :" + pager)
		.setPositiveButton("걸기",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				call(pager);
			}
		})
		.setNegativeButton("닫기",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.show();
		
	}
	*/
	private void call(String phonenum){
		try{
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:"+phonenum.replace("-","")));
			startActivity(callIntent);
		}catch(ActivityNotFoundException e){
			Log.e("Dialing error","Call falied",e);
		}
	}
	
	private void getAddress() {
		try
		{
		        HttpClient client = new DefaultHttpClient();  
		        String getURL = "http://bit.sparcs.org/~rodumani/nugu.php?key=asdf";
		        HttpGet get = new HttpGet(getURL);
		        HttpResponse responseGet = client.execute(get);  
		        HttpEntity resEntityGet = responseGet.getEntity();
		     
		        if (resEntityGet != null)
		        {  
		            // 결과를 처리합니다.
		        	Gson gson = new Gson();
		        	Type type = new TypeToken<List<Person>>(){}.getType();
		        	String jsonString = EntityUtils.toString(resEntityGet);
		        	listData=gson.fromJson(jsonString,type);
		        	listNames=new ArrayList<String>();
		        	for (Person i : listData) {
						listNames.add(i.name);
					}
		        }
		}
		catch (Exception e)
		{
		        e.printStackTrace();
		}
	}

}
