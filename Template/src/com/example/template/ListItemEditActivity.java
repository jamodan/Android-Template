/*
 * Created By : Daniel Jamison
 * Copyright (c) 2012 South Dakota State University. All rights reserved.
 */

package com.example.template;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.template.R.string;

public class ListItemEditActivity extends Activity {
	public static final String KEY_ID = "ID";
	public int ID = 0;
	public String accessType = "";
	
	private TextView windowTitle = null;
	private Button button_logo = null;
	private Button button_info = null;
	private Button button_done = null;
	Intent intent;
	Context context;
	
	// Generic place holders for data
	private RelativeLayout row1 = null;
	private TextView label1 = null;
	private Object data1 = null;
	private RelativeLayout row2 = null;
	private TextView label2 = null;
	private Object data2 = null;
	
	
	Bundle extras = null;
	DataBaseObject db = null;
	private Boolean updateFlag = false;
	
	SimpleCursorAdapter spinnerAdapter1 = null;

	private int counter1 = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_edit);
        
        db  = new DataBaseObject(this);
        db.open();
        
        windowTitle = (TextView)findViewById(R.id.windowTitle);
        
        // Get the feed ID passed to this class
        extras = getIntent().getExtras();
        if(extras!= null)
        {
        	ID = Integer.parseInt(extras.getString(KEY_ID));
        }
        
        // Make sure the passed inventoryID is a valid number then get its data, set the save flag, 
        //    hide the softtouch keyboard by focusing on the top control bar
        if(ID > 0)
        {
        	//getData();
        	updateFlag = true;
        }
         
        accessType = ListViewActivity.accessType;
           
        if (accessType.equals("Bla1"))
        {
        	windowTitle.setText("Bla1");
        	setupBla1Rows();
        	if(updateFlag)
        	{
        		Cursor c = db.getFeedstuff(ID);
        		if(c != null)
    			{
    				if(c.moveToFirst())
    				{
    					do
    					{
    						int spinnerValueToSet = c.getInt(c.getColumnIndex("column1"));
    						if (spinnerValueToSet != 0)
    						{
    							((Spinner) data1).setSelection(spinnerValueToSet);
    							//row#.setVisibility(View.VISIBLE);
    						}
    						spinnerAdapter1.notifyDataSetChanged();
    						
    						((EditText) data2).setText(c.getString(c.getColumnIndex("column2")));
    					}while(c.moveToNext());
    				}
    			}
        	}
        }
        else
        {
        	Toast.makeText(ListItemEditActivity.this, "Access Type Not Defined", Toast.LENGTH_SHORT).show();
        	finish();
        }
        
        button_logo = (Button)findViewById(R.id.logo);
        button_logo.setOnClickListener(new OnClickListener(){
	    	public void onClick(View view){
	    		// Show the disclaimer
	    		intent = new Intent(ListItemEditActivity.this,SettingsMainActivity.class);
	    	
	    		 try {
	                    startActivity(intent);
	                }
	             catch (ActivityNotFoundException e){
	                    Toast.makeText(ListItemEditActivity.this, "NO Viewer", Toast.LENGTH_SHORT).show();
	                }
	    	}
	    });
        
        button_info = (Button)findViewById(R.id.info);
        button_info.setOnClickListener(new OnClickListener(){
	    	public void onClick(View view){
	    		// Display additional information for feed analysis values
	    		Uri uri = Uri.parse( "http://igrow.org" );
				startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
	    	}
	    });
        
        button_done = (Button)findViewById(R.id.buttonDone);
        button_done.setOnClickListener(new OnClickListener(){
	    	public void onClick(View view){
	    		// Save the feed record and return to the main screen
	    		if (accessType.equals("Bla1"))
	            {
	            	saveBla1();
	            }
	            else
	            {
	            	Toast.makeText(ListItemEditActivity.this, "Access Type Not Defined For Save", Toast.LENGTH_SHORT).show();
	            }
	    	}
	    });
        
        // If a new feedstuff sets values to zero by default otherwise calculates numbers based on the passed feed ID
        //calculations();
    }
	
	// Set all EditTexts to erase a zero when focused
    // Set all EditTexts to recalculate all TextView values on each character change.
	/*private void setupRows()
	{
		// ##### SPINNER FROM DATABASE #####
		row# = (RelativeLayout) findViewById(R.id.data#Stuff);
		row#.setVisibility(View.VISIBLE);
		label# = (TextView) findViewById(R.id.label#);
		label#.setText(string.info#...);
		data# = (Spinner) findViewById(R.id.data#);
        SimpleCursorAdapter spinnerAdapter# = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, db.getAll...(), new String[] { "column_name" }, new int[] { android.R.id.text1 });
        spinnerAdapter#.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) data#).setAdapter(spinnerAdapter#);
        ((Spinner) data#).setOnItemSelectedListener(new OnItemSelectedListener()
        {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				// TODO Auto-generated method stub
			}

			public void onNothingSelected(AdapterView<?> arg0) 
			{
				// TODO Auto-generated method stub
			}
        });
        
        // ##### EDIT TEXT #####
        row# = (RelativeLayout) findViewById(R.id.data#Stuff);
		row#.setVisibility(View.VISIBLE);
		label# = (TextView) findViewById(R.id.label#);
		label#.setText(string.info#...);
    	data# = (EditText)findViewById(R.id.data#);
    	((EditText) data#).setInputType(InputType.bla | InputType.bla);
    	((EditText) data#).setOnFocusChangeListener(new OnFocusChangeListener()
        {
            public void onFocusChange(View v, boolean hasFocus) 
            {
                if (hasFocus==true)
                {
                	String temp = ((EditText) data#).getText().toString();
                	// Check to see if the value in the box is zero, if it is erase the zero so the user can type in a new number
                    if (temp.compareTo("0")==0 || temp.compareTo("0.")==0 || temp.compareTo("0.0")==0 || temp.compareTo("0.00")==0)
                    {
                    	((EditText) data#).setText("");
                    }
                }
            }
        });
    	// After any character change redo all calculations on the screen
    	((EditText) data#).addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				//calculations();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
    	
    	// ##### TEXT VIEW #####
    	row# = (RelativeLayout) findViewById(R.id.data#Stuff);
		row#.setVisibility(View.VISIBLE);
		label# = (TextView) findViewById(R.id.label#);
		label#.setText(string.info#...);
    	data# = (TextView)findViewById(R.id.data#);
    	
	}*/
	
	private void setupBla1Rows()
	{       
        // ##### SPINNER FROM DATABASE #####
        row1 = (RelativeLayout) findViewById(R.id.data1Stuff);
		row1.setVisibility(View.VISIBLE);
		label1 = (TextView) findViewById(R.id.label1);
		label1.setText(string.info1);
        data1 = (Spinner) findViewById(R.id.data1);
        spinnerAdapter1 = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, db.getSpinnerChoices(0), new String[] { "choice" }, new int[] { android.R.id.text1 });
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) data1).setAdapter(spinnerAdapter1);
        ((Spinner) data1).setPrompt("Choose a Feedstuff");
        
        // ##### EDIT TEXT #####
        row2 = (RelativeLayout) findViewById(R.id.data2);
        row2.setVisibility(View.VISIBLE);
		label2 = (TextView) findViewById(R.id.label2);
		label2.setText(string.info2);
    	data2 = (EditText)findViewById(R.id.data2);
    	((EditText) data2).setInputType(InputType.TYPE_CLASS_TEXT);
    	((EditText) data2).setOnFocusChangeListener(new OnFocusChangeListener()
        {
            public void onFocusChange(View v, boolean hasFocus) 
            {
                if (hasFocus==true)
                {
                	String temp = ((EditText) data2).getText().toString();
                	// Check to see if the value in the box is zero, if it is erase the zero so the user can type in a new number
                    if (temp.compareTo("0")==0 || temp.compareTo("0.")==0 || temp.compareTo("0.0")==0 || temp.compareTo("0.00")==0)
                    {
                    	((EditText) data2).setText("");
                    }
                }
            }
        }); 
    	// After any character change redo all calculations on the screen
    	((EditText) data2).addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				bla1Calculations();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
    	
    	//#####################################################################   	
    	((Spinner) data1).setOnItemSelectedListener(new OnItemSelectedListener()
        {
    		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
			{
    			Log.v("Clicks", "Spinner 1 clicks: " + ++counter1);
    			if(!updateFlag || counter1 > 2) // on load spinner may be clicked, counter avoids unwanted opening
				{
	    			Cursor c = (Cursor)(((Spinner) data1).getSelectedItem());
	    			if (pos == 0)
					{
	    				// Do Something
					}
	    			else
	    			{
	    				// Do Something
					}
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) 
			{
				// TODO Auto-generated method stub
			}
        });
	}
	
	private void bla1Calculations()
	{
		double value1 = 0;
		double value2 = 0;
		String value1Str = ((EditText) data1).getText().toString();
		String value2Str = ((EditText) data2).getText().toString();
		
		if (value1Str.length() != 0 && value2Str.length() != 0)
		{
			try{
				value1 = Double.valueOf(value1Str);
	        }catch(Exception e){
	        	value1 = 0;
	        	onCreateDialog("Value1 must be a decimal number.", 1);
				((EditText) data1).requestFocus();
				return;
	        }
			
			try{
				value2 = Double.valueOf(value2Str);
	        }catch(Exception e){
	        	value2 = 0;
	        	onCreateDialog("Value2 must be a number.", 1);
				((EditText) data2).requestFocus();
				return;
	        }

			((TextView) data2).setText(String.format("%.2f", value1 + value2));
		}
	}
	
	// Save the data to the database
	public void saveBla1()
	{
		if(((Spinner) data1).getSelectedItemPosition() == 0)
		{
			onCreateDialog("You must select a Feedstuff type.", 1);
			((Spinner) data1).requestFocus();
			return;
		}
		
		Cursor data1Cursor = (Cursor)(((Spinner) data1).getSelectedItem());	
		String name = data1Cursor.getString(data1Cursor.getColumnIndex("column1"));
		if (name.equals("Buddy") || name.equals("Pal"))
		{
			// do something special
		}
		
		double quantity = 0;
		try{
            quantity = Double.valueOf(((EditText) data2).getText().toString());
        }catch(Exception e){
        	quantity = 0;
        	onCreateDialog("Quantity must be a decimal number.", 1);
			((EditText) data2).requestFocus();
			return;
        }
		if (quantity <= 0)
		{
			onCreateDialog("Quantity must greater than zero.", 1);
			((EditText) data2).requestFocus();
			return;
		}
		
		String note = ((EditText) data2).getText().toString();
		
		
		if(updateFlag == false)
		{
			//db.insertFeedstuff(feedstuffType,feedstuffName,use,quantity,unit,conversion,quality,valuePer,location,note);
		}
		else
		{
			//db.updateFeedstuff(ID,feedstuffType,feedstuffName,use,quantity,unit,conversion,quality,valuePer,location,note);
		}
		
		finish();
		
	}
	
	@SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
        if (Integer.valueOf(android.os.Build.VERSION.SDK) < 11)
        {
        	// Show the disclaimer
        	intent = new Intent(ListItemEditActivity.this,SettingsMainActivity.class);
        	try {
        		startActivity(intent);
        	}
        	catch (ActivityNotFoundException e){
        		Toast.makeText(ListItemEditActivity.this, "NO Viewer", Toast.LENGTH_SHORT).show();
        	}
        }
        else
        {
        	MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main, menu);
	        ImageView iGrowLogo = (ImageView) menu.findItem(R.id.menu_igrow).getActionView();
	        iGrowLogo.setOnClickListener(new OnClickListener(){
		    	public void onClick(View view){
		    		// Display additional information for feed analysis values
		    		Uri uri = Uri.parse( "http://igrow.org" );
					startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
		    	}
		    }
		    );
        }
        //inflater.inflate(menuRes, menu)
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId()) {
	        case R.id.menu_settings:
	            Intent intent = new Intent(this, SettingsMainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		 try {
	                    startActivity(intent);
	                }
	             catch (ActivityNotFoundException e){
	                    Toast.makeText(ListItemEditActivity.this, "NO Viewer", Toast.LENGTH_SHORT).show();
	                }
	            return true;
	        default:
            return super.onOptionsItemSelected(item);
    	}
    }
    

	// Sets functionality of the hard buttons on the device
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
        	finish();
        }
        return true;
    }
	
	// Error messages
	public Dialog onCreateDialog(String error, int id)
	{
	 AlertDialog.Builder builder =null;
	 AlertDialog alert = null;
		switch(id)
		{
		case 1:
			
			builder = new AlertDialog.Builder(this);
			builder.setMessage(error)       
			.setCancelable(false)       
			.setPositiveButton("OK", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id) 
				{
					
				}      
			});
			alert = builder.create();
			alert.show();
			break;
			
		case 2:
			
			builder = new AlertDialog.Builder(this);
			builder.setMessage(error)       
			.setCancelable(false)       
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) 
				{
					finish();
				
				}      
			}) 
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id)
				{	
					
				}       
			});
			alert = builder.create();
			alert.show();
			break;
		}
		return null;
	}
}
