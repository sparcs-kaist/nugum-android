package com.sparcs.nugum;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity<listNames> extends Activity {
	List<Person> listData;
    ArrayAdapter<Person> Adapter;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getAddress();
        
        ListView list=(ListView) findViewById(R.id.ListView01);
        final EditText edit = (EditText)findViewById(R.id.EditText01);

        Adapter= new ArrayAdapter<Person>(this, android.R.layout.simple_list_item_1, listData);
        
        list.setAdapter(Adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setTextFilterEnabled(true);
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
					
				
				InputMethodManager inputManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(edit.getWindowToken(),0); 
			
				Intent intent = new Intent(MainActivity.this,PersonActivity.class);
				Person selectedPerson = Adapter.getItem(position);
				intent.putExtra("name", selectedPerson.name);
				intent.putExtra("pager", selectedPerson.pager);
				intent.putExtra("sparcsID",selectedPerson.id);
				intent.putExtra("email", selectedPerson.email);
				startActivity(intent);
			};
        	
		});
        
        
        edit.addTextChangedListener(new TextWatcher()
        {
           @Override
           public void onTextChanged( CharSequence s, int start, int count, int after)
           {
               // TODO Auto-generated method stub
        	   Adapter.getFilter().filter(s);
           }

           @Override
           public void beforeTextChanged( CharSequence arg0, int arg1, int arg2, int arg3)
           {
               // TODO Auto-generated method stub

           }

           @Override
           public void afterTextChanged( Editable arg0)
           {
               // TODO Auto-generated method stub
           }
        });
        edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager inputManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(edit,0); 
			}
		});
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
		        }
		}
		catch (Exception e)
		{
		        //e.printStackTrace();
		        Toast.makeText(MainActivity.this, "네트워크 연결상태가 좋지 않습니다.", Toast.LENGTH_SHORT).show();
		}
	}

}
