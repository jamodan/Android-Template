/*
 * Created By : Daniel Jamison
 * Copyright (c) 2013 South Dakota State University. All rights reserved.
 */

package com.example.template.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class DatabaseTableOptions 
{
	// Properties of the table
	public static final String TABLE_NAME = "Options";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_OPTION_NAME = "optionName";
	public static final String COLUMN_VALUE = "value";
	public static final String COLUMN_DESCRIPTION = "description";
	
	// Structure of one row of the database
	public class Item
	{
		public String optionName = "";
		public String value = "";
		public String description = "";
		
		public Item(String optionName, String value, String description)
		{
			this.optionName = optionName;
			this.value = value;
			this.description = description;
		}
	}
	
	// Table statements
	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME 
			+ " ("
			+ COLUMN_ID + 			" INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_OPTION_NAME + 	" TEXT NOT NULL, "
			+ COLUMN_VALUE + 		" TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + 	" TEXT NOT NULL"
			+ ");";
	private static final String INSERT_ROW = "INSERT INTO " + TABLE_NAME 
			+ " ("
			//+ COLUMN_ID + ", "
			+ COLUMN_OPTION_NAME + ", "
			+ COLUMN_VALUE + ", "
			+ COLUMN_DESCRIPTION
			+ ") "
			+ "VALUES ";
	
	// Initial/Default values
	private static final String[] DEFAULT_VALUES = {
		"('Last DB Update', 'Jan 1 2000 1:29PM', 'Holds the date of the last database update check')",
		"('Hide disclaimer', 'false', 'Shows the disclaimer on the main screen when the app is launched')"
	};
	
	//##########################################################################################
	// Table Specific Methods
	
	public static void insert(String optionName, String value, String description)
	{
		DatabaseMain.database.execSQL(INSERT_ROW
				+ "('" 		+ optionName
				+ "', '" 	+ value 
				+ "', '" 	+ description 
				+ "')");
	}

	/**
	Convenience method for updating rows in the database.
	<p>
	<b>Sample</b>
	<br>&nbsp &nbsp &nbsp &nbsp
	<b>values</b> = new DatabaseSampleTable.Item(...)
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>selection</b> = DatabaseSampleTable.COLUMN_ID + " = ?"
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>selectionArgs</b> = new String[] { "3" }
	
	@param values  the full record to be used to update. null is a valid value that will be translated to NULL. 
	@param selection  the optional WHERE clause to apply when updating. Passing null will update all rows. 
	@param selectionArgs  You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings. 

	@return the number of rows affected 
	 */
	public static int update(String optionName, String value, String description, String selection, String[] selectionArgs)
	{
		ContentValues args = new ContentValues();
		args.put(COLUMN_OPTION_NAME, optionName);
		args.put(COLUMN_VALUE, value);
		args.put(COLUMN_DESCRIPTION, description);
		
		return DatabaseMain.database.update(TABLE_NAME, args, selection, selectionArgs);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{		
		int currentVersion = oldVersion;
		
		switch (oldVersion){
		// sample update format only
			case 1:
				Log.w(DatabaseTableOptions.class.getName(), "Upgrading database table " + TABLE_NAME + " from version " + currentVersion + " to " + ++currentVersion);
				onCreate(database);
				
				// No Break Statement so that the DB will perform all updates required to go from the current version to the newest version
			case 2: 
				Log.w(DatabaseTableOptions.class.getName(), "Upgrading database " + TABLE_NAME + " from version " + currentVersion + " to " + ++currentVersion);
				onCreate(database);
				
				// No Break Statement so that the DB will perform all updates required to go from the current version to the newest version
		}
	}
	
	//##########################################################################################
	// Generic Methods
	
	public static void onCreate(SQLiteDatabase database) 
	{
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		database.execSQL(CREATE_TABLE);
		
		// Add each item from the defaults
		database.beginTransaction();
		try
		{
			for(String value : DEFAULT_VALUES)
			{
				database.execSQL(INSERT_ROW + value);
			}
			
			database.setTransactionSuccessful();
		}
		finally
		{
			database.endTransaction();
		}
	}

	/**
	Perform a query by combining all current settings and the information passed into this method.
	<p>
	<b>Sample</b>
	<br>&nbsp &nbsp &nbsp &nbsp
	<b>returnColumns</b> = new String[] { DatabaseSampleTable.COLUMN_ID, DatabaseSampleTable.COLUMN_SAMPLE_NAME }
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>selection</b> = DatabaseSampleTable.COLUMN_ID + " = ?"
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>selectionArgs</b> = new String[] { "3" }
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>groupBy</b> = DatabaseSampleTable.COLUMN_TYPE
	
	@param returnColumns  A list of which columns to return. Passing null will return all columns, which is discouraged to prevent reading data from storage that isn't going to be used. 
	@param selection  A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given URL. 
	@param selectionArgs  You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings. 
	@param groupBy  A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.  
	
	@return a single Float value, NULL on error
	 */
	public static Float getFloat(String[] returnColumns, String selection, String[] selectionArgs, String groupBy)
	{
	    Cursor c = get(returnColumns, selection, selectionArgs, groupBy);
	    if(c.getCount() == 1 && c.getColumnCount() == 1)
	    {
	    	return c.getFloat(0);
	    }
	    else
	    {
	    	return null;
	    }
	}

	/**
	Perform a query by combining all current settings and the information passed into this method.
	<p>
	<b>Sample</b>
	<br>&nbsp &nbsp &nbsp &nbsp
	<b>returnColumns</b> = new String[] { DatabaseSampleTable.COLUMN_ID, DatabaseSampleTable.COLUMN_SAMPLE_NAME }
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>selection</b> = DatabaseSampleTable.COLUMN_ID + " = ?"
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>selectionArgs</b> = new String[] { "3" }
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>groupBy</b> = DatabaseSampleTable.COLUMN_TYPE
	
	@param returnColumns  A list of which columns to return. Passing null will return all columns, which is discouraged to prevent reading data from storage that isn't going to be used. 
	@param selection  A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given URL. 
	@param selectionArgs  You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings. 
	@param groupBy  A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.  
	
	@return a single integer value, -1 on error
	 */
	public static int getInt(String[] returnColumns, String selection, String[] selectionArgs, String groupBy)
	{
	    Cursor c = get(returnColumns, selection, selectionArgs, groupBy);
	    if(c.getCount() == 1 && c.getColumnCount() == 1)
	    {
	    	return c.getInt(0);
	    }
	    else
	    {
	    	return -1;
	    }
	}
	

	/**
	Perform a query by combining all current settings and the information passed into this method.
	<p>
	<b>Sample</b>
	<br>&nbsp &nbsp &nbsp &nbsp
	<b>returnColumns</b> = new String[] { DatabaseSampleTable.COLUMN_ID, DatabaseSampleTable.COLUMN_SAMPLE_NAME }
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>selection</b> = DatabaseSampleTable.COLUMN_ID + " = ?"
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>selectionArgs</b> = new String[] { "3" }
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>groupBy</b> = DatabaseSampleTable.COLUMN_TYPE
	
	@param returnColumns  A list of which columns to return. Passing null will return all columns, which is discouraged to prevent reading data from storage that isn't going to be used. 
	@param selection  A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given URL. 
	@param selectionArgs  You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings. 
	@param groupBy  A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.  
	
	@return a single string value, null on error
	 */
	public static String getString(String[] returnColumns, String selection, String[] selectionArgs, String groupBy)
	{
	    Cursor c = get(returnColumns, selection, selectionArgs, groupBy);
	    if(c.getCount() == 1 && c.getColumnCount() == 1)
	    {
	    	return c.getString(0);
	    }
	    else
	    {
	    	return null;
	    }
	}
	
	/**
	Perform a query by combining all current settings and the information passed into this method.
	<p>
	<b>Sample</b>
	<br>&nbsp &nbsp &nbsp &nbsp
	<b>returnColumns</b> = new String[] { DatabaseSampleTable.COLUMN_ID, DatabaseSampleTable.COLUMN_SAMPLE_NAME }
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>selection</b> = DatabaseSampleTable.COLUMN_ID + " = ?"
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>selectionArgs</b> = new String[] { "3" }
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>groupBy</b> = DatabaseSampleTable.COLUMN_TYPE
	
	@param returnColumns  A list of which columns to return. Passing null will return all columns, which is discouraged to prevent reading data from storage that isn't going to be used. 
	@param selection  A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given URL. 
	@param selectionArgs  You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings. 
	@param groupBy  A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.  
	
	@return a cursor over the result set
	 */
	public static Cursor get(String[] returnColumns, String selection, String[] selectionArgs, String groupBy)
	{
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	    queryBuilder.setTables(TABLE_NAME);
	    
	    return queryBuilder.query(DatabaseMain.database, returnColumns, selection, selectionArgs, groupBy, null, null);
	}
	
	/**
	Convenience method for deleting rows in the database.
	<p>
	<b>Sample</b>
	<br>&nbsp &nbsp &nbsp &nbsp
	<b>selection</b> = DatabaseSampleTable.COLUMN_ID + " = ?"
	<p>&nbsp &nbsp &nbsp &nbsp
	<b>selectionArgs</b> = new String[] { "3" }
	 
	@param selection  the optional WHERE clause to apply when deleting. Passing null will delete all rows. 
	@param selectionArgs  You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings. 

	@return the number of rows affected if a selection is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the selection.
	 */
	public static int delete(String selection, String[] selectionArgs)
	{
		return DatabaseMain.database.delete(TABLE_NAME, selection, selectionArgs);
	}
} 