package com.sparcs.nugum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class LoginActivity extends Activity {

	private EditText textID;
    private EditText textPW;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        textID = (EditText)findViewById(R.id.LoginActivityIDEdit);
        textPW = (EditText)findViewById(R.id.LoginActivityPWEdit);
        Button loginBT= (Button)findViewById(R.id.LoginActivityBT);
        
        loginBT.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login(textID.getText().toString(), textPW.getText().toString());
				InputMethodManager inputManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(textPW.getWindowToken(),0); 
			}
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
    
    private void login(String ID,String PW)
    {
    	HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://sparcs.org/ssoauth/");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", ID));
            nameValuePairs.add(new BasicNameValuePair("password", PW));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            
            String tmp = EntityUtils.toString(response.getEntity());
            if (tmp.equals("AuthChecked"))
            {
            	Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
            }
            else
            {
            	Toast.makeText(LoginActivity.this, "인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	System.out.println("WTF");
        }
    }

    
}
