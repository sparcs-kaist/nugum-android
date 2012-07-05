package com.sparcs.nugum;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity<listNames> extends Activity {
	ArrayList<Person> listData = null;
    ArrayAdapter<Person> Adapter; 
    boolean isLoggedin = true;
    boolean isLoaded = false;
    int position = 0;
    int sortConfig = 1;
    ListView list;
    IndexBar indexBar;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        if (!LoginActivity.checkDeviceID(getContentResolver())) {
        	this.isLoggedin = false;
        	Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        	startActivityForResult(intent, 12);
        }
               
    }
	
	@Override
	public void onStart() {
		super.onStart();
		setContentView(R.layout.activity_main);
		if (this.isLoggedin) {
			switch (sortConfig) {
			case 1:
				drawListDataSortByAlphabet();
				break;
			case 2:
				drawListDataSortByStudentNumber();
				break;
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		this.list.setSelection(position);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		position = this.list.getFirstVisiblePosition();
	}
	
	private void drawListData() {
		list = (ListView) findViewById(R.id.ListView01);
		final EditText edit = (EditText) findViewById(R.id.EditText01);
		
		this.isLoaded = true;
		Adapter = new ResultAdapter(this,android.R.layout.simple_list_item_1, listData, sortConfig);
		list.setAdapter(Adapter);
		list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		list.setTextFilterEnabled(true);
		indexBar = (IndexBar) findViewById(R.id.indexbar);
		indexBar.setListView(list);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(edit.getWindowToken(),0);

				Intent intent = new Intent(MainActivity.this,PersonActivity.class);
				Person selectedPerson = Adapter.getItem(position);
				intent.putExtra("name", selectedPerson.name);
				intent.putExtra("pager", selectedPerson.pager);
				intent.putExtra("sparcsID", selectedPerson.id);
				intent.putExtra("email", selectedPerson.email);
				startActivity(intent);
			};
		});

		edit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int count, int after) {
				Adapter.getFilter().filter(s);
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(edit, 0);
			}
		});
	}
	
	private void drawListDataSortByAlphabet() {
		if (!isLoaded)
			if (!getAddress())
				return;
		
		Collections.sort(listData, new alphabetComparator());
		drawListData();
	}
	
	private void drawListDataSortByStudentNumber() {
		if (!isLoaded)
			if (!getAddress())
				return;
		
		Collections.sort(listData, new studentNumberComparator());
		drawListData();
	}
	
	private boolean getAddress() {
		String[] name = {"android_id"};
		String[] data = {Util.getAndroidID(getContentResolver())};
		String url = "http://bit.sparcs.org/~kuss/nugu2.php?action=getContact";
		String result;
		
		try {
			result = Util.Post(url, name, data);
		    if (!result.equals("404")) {  
		      	Gson gson = new Gson();
		       	Type type = new TypeToken<List<Person>>(){}.getType();
		       	listData=gson.fromJson(result,type);
		       	return true;
		    }
		} catch (Exception e) {
		    Util.toastString(this, "네트워크 연결상태가 좋지 않습니다.");
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode) {
		case 1:
			this.isLoggedin = true;
			break;
		case 2:
			finish();
			break;
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_sortbyStudentNumber:
			sortConfig = 2;
			drawListDataSortByStudentNumber();
			break;
		case R.id.menu_sortbyAlphabet:
			sortConfig = 1;
			drawListDataSortByAlphabet();
			break;
		}
		return true;
	}

}
