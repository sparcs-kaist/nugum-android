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
        Button callButton = (Button)findViewById(R.id.PersonActivityButtonCall);
        intent=getIntent();
        nameText.setText(intent.getStringExtra("name"));
        pagerText.setText(intent.getStringExtra("pager"));
        if(intent.getStringExtra("pager").equals(""))
        {
        	pagerText.setText("전화번호가 없습니다.");
        	callButton.setVisibility(4);
        }
        
        callButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				call(intent.getStringExtra("pager"));	
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
