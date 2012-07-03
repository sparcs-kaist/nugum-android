package com.sparcs.nugum;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class PersonActivity extends Activity {
	private Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        
        TextView nameText= (TextView)findViewById(R.id.PersonActivityTextName);
        TextView pagerText= (TextView)findViewById(R.id.PersonActivityTextPager);
        TextView sparcsmailText=(TextView)findViewById(R.id.PersonActivityTextSPARCSMail);
        Button callButton = (Button)findViewById(R.id.PersonActivityButtonCall);
        Button textButton = (Button)findViewById(R.id.PersonActivityButtonText);
        Button sparcsmailButton = (Button)findViewById(R.id.PresonActivitySendSPARCSMail);
        
        intent=getIntent();
        nameText.setText(intent.getStringExtra("name"));
        pagerText.setText(intent.getStringExtra("pager"));
        sparcsmailText.setText(intent.getStringExtra("sparcsID")+"@sparcs.org");
        if(intent.getStringExtra("pager").equals(""))
        {
        	pagerText.setText("전화번호가 없습니다.");
        	callButton.setVisibility(4);
        	textButton.setVisibility(4);
        }
        
        callButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				call(intent.getStringExtra("pager"));	
			}
		});
        
        textButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("address", intent.getStringExtra("pager"));
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivity(sendIntent);
			}
		});
        
        sparcsmailButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("mailto:"+intent.getStringExtra("sparcsID")+"@sparcs.org");
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);
				startActivity(it);
			}
		});
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_person, menu);
        return true;
    }

	private void call(String phonenum){
		try{
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:"+phonenum.replace("-","")));
			startActivity(callIntent);
		}catch(ActivityNotFoundException e){
			Log.e("Dialing error","Call falied",e);
		}
	}
}
