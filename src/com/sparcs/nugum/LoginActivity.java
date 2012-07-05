package com.sparcs.nugum;

import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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
    
	@Override   
	public boolean onKeyDown(int keyCode, KeyEvent event) {    
	    switch(keyCode){
	    case KeyEvent.KEYCODE_BACK:
	    	setResult(2);
	    	finish();
	    	//super.onActivityResult(2, 2, null);
	    	//moveTaskToBack(true);
	    	//ActivityManager am=(ActivityManager)getSystemService(ACTIVITY_SERVICE);
	    	//am.restartPackage(getPackageName());
	    }
	    return true;
	}
	
	public static boolean checkDeviceID(ContentResolver cr) {
		String[] name = {"android_id"};
		String[] data = {Util.getAndroidID(cr)};
		String result;
		try {
			result = Util.Post("http://bit.sparcs.org/~kuss/nugu2.php?action=checkDeviceID", name, data);
			if (result.equals("Checked")) return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
    private void login(String username,String password) {
    	String[] name = {"username", "password", "android_id"};
    	String[] data = {username, password, Util.getAndroidID(getContentResolver())};
    	String result;
    	
		try {
			result = Util.Post("http://bit.sparcs.org/~kuss/nugu2.php?action=registerDeviceID", name, data);
			Util.toastString(this, result);
			if (result.equals("AuthChecked")) finish();
			else Util.toastString(this, "아이디나 비밀번호가 맞지 않습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
  
}
