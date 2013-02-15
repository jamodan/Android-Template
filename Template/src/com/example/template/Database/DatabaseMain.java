/*
 * Created By : Daniel Jamison
 * Copyright (c) 2013 South Dakota State University. All rights reserved.
 */

package com.example.template.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseMain
{
	public static SQLiteDatabase database;
	public static DatabaseHelper dbHelper;
	
	public class DatabaseHelper extends SQLiteOpenHelper
	{
		//Initialize properties of the class
		private static final String DATABASE_NAME = "data"; 
		private static final int DATABASE_VERSION = 1;	
	
		public DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			DatabaseTableOptions.onCreate(db);
			DatabaseTableData.onCreate(db);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			DatabaseTableOptions.onUpgrade(db, oldVersion, newVersion);
			DatabaseTableData.onUpgrade(db, oldVersion, newVersion);
		}
	}
	
	public DatabaseMain (Context context)
	{
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		
		// If the line below is uncommented then the database will perform a reset each time the app is opened
		dbHelper.onCreate(database);
	}

	public void close() {
		dbHelper.close();
	}
	
	public Cursor Select(String sqlStatement)
	{
		Cursor c = database.rawQuery(sqlStatement, null);
		if(c.getCount()==0)
		{
			return null;
		}
		return c;
	}
}