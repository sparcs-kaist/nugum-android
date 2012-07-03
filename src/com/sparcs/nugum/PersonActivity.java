package com.sparcs.nugum;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PersonActivity extends Activity {
	private Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        
        TextView nameText= (TextView)findViewById(R.id.PersonActivityTextName);
        //TextView pagerText= (TextView)findViewById(R.id.PersonActivityTextPager);
        //TextView sparcsmailText=(TextView)findViewById(R.id.PersonActivityTextSPARCSMail);
        Button callButton = (Button)findViewById(R.id.PersonActivityButtonCall);
        Button textButton = (Button)findViewById(R.id.PersonActivityButtonText);
        Button sparcsmailButton = (Button)findViewById(R.id.PresonActivitySendSPARCSMail);
        Button emailButton = (Button)findViewById(R.id.PersonActivitySendMail);
        Button addContactButton = (Button)findViewById(R.id.PersonActivityAddContact);
        
        intent=getIntent();
        nameText.setText(intent.getStringExtra("name"));
        //pagerText.setText(intent.getStringExtra("pager"));
        //sparcsmailText.setText(intent.getStringExtra("sparcsID")+"@sparcs.org");
        callButton.setText("전화번호 : "+intent.getStringExtra("pager"));
        callButton.setBackgroundColor(Color.rgb(240, 240, 240));
        sparcsmailButton.setText("스팍스이메일 : "+intent.getStringExtra("sparcsID")+"@sparcs.org");
        sparcsmailButton.setBackgroundColor(Color.rgb(240, 240, 240));
        emailButton.setText("이메일 : "+intent.getStringExtra("email"));
        emailButton.setBackgroundColor(Color.rgb(240, 240, 240));
        
        if(intent.getStringExtra("pager").equals(""))
        {
        	//pagerText.setText("전화번호가 없습니다.");
        	callButton.setText("전화번호 : 전화번호가 없습니다.");
        	callButton.setEnabled(false);
        	textButton.setVisibility(8);
        }
        if(intent.getStringExtra("email").equals(""))
        {
        	emailButton.setEnabled(false);
        	emailButton.setVisibility(8);
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
        
        emailButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("mailto:"+intent.getStringExtra("email"));
				Intent it=new Intent(Intent.ACTION_SENDTO,uri);
				startActivity(it);
			}
		});
        
        addContactButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				            //Yes button clicked
				        	addContact(intent);
				            break;

				        case DialogInterface.BUTTON_NEGATIVE:
				            //No button clicked
				            break;
				        }
				    }
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(PersonActivity.this);
				builder.setMessage("연락처에 "+intent.getStringExtra("name")+" 님의 연락처를 추가하겠습니까?").setPositiveButton("Yes", dialogClickListener)
				    .setNegativeButton("No", dialogClickListener).show();
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
	
	private void addContact(Intent intent)
	{
		String DisplayName = intent.getStringExtra("name");
		String MobileNumber = intent.getStringExtra("pager");
		String sparcsemailID = intent.getStringExtra("sparcsID")+"@sparcs.org";
		String emailID = intent.getStringExtra("email");

		ArrayList<ContentProviderOperation> ops = 
		    new ArrayList<ContentProviderOperation>();

		ops.add(ContentProviderOperation.newInsert(
		    ContactsContract.RawContacts.CONTENT_URI)
		    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
		    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
		    .build()
		);

		//------------------------------------------------------ Names
		if(DisplayName != null)
		{           
		    ops.add(ContentProviderOperation.newInsert(
		        ContactsContract.Data.CONTENT_URI)              
		        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		        .withValue(ContactsContract.Data.MIMETYPE,
		            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
		        .withValue(
		            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,     
		            DisplayName).build()
		    );
		} 

		//------------------------------------------------------ Mobile Number                      
		if(MobileNumber != null)
		{
		    ops.add(ContentProviderOperation.
		        newInsert(ContactsContract.Data.CONTENT_URI)
		        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		        .withValue(ContactsContract.Data.MIMETYPE,
		        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
		        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, 
		        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
		        .build()
		    );
		}

        //------------------------------------------------------ SPARCSEmail
        if(sparcsemailID != null)
        {
             ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, sparcsemailID)
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .build());
        }
        
        //------------------------------------------------------ Email
        if(emailID != null)
        {
             ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                        .build());
        }
        /*
        //------------------------------------------------------ Home Numbers
		if(HomeNumber != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, 
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }

        //------------------------------------------------------ Work Numbers
        if(WorkNumber != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, 
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }
        //------------------------------------------------------ Organization
        if(!company.equals("") && !jobTitle.equals(""))
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }
*/
        
        // Asking the Contact provider to create a new contact                  
        try 
        {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } 
        catch (Exception e) 
        {               
            e.printStackTrace();
            Toast.makeText(PersonActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        
        Toast.makeText(PersonActivity.this,DisplayName+" 님의 연락처가 추가되었습니다.",Toast.LENGTH_SHORT).show();
	}
}
