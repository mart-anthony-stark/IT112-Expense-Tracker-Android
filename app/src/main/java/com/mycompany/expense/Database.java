package com.mycompany.expense;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	public final static String DATABASE_NAME="ExpenseTracker.db";
	public final static String TABLE_NAME="Expenses";
	public final static String COL1="ID";
	public final static String COL2="name";
	public final static String COL3="type";
	public final static String COL4="date";
	public final static String COL5="description";
	public final static String COL6="value";

	private SQLiteDatabase db;

	private ContentValues cv;
	public Database(Context context){
		super(context, DATABASE_NAME,null,1);
		db=this.getWritableDatabase();
		cv = new ContentValues();
	}

	@Override 
    public void onCreate(SQLiteDatabase db) { 
		db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+COL1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL2+" TEXT COLLATE NOCASE,"+COL3+" TEXT COLLATE NOCASE,"+COL4+" TEXT, "+COL5+" TEXT, "+COL6+" INTEGER)"); 
    } 

    @Override 
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME); 
		onCreate(db); 
    }

	public void insertData(String name, String type, String date, String desc, String value) { 
	    ContentValues c = new ContentValues();
		c.put(COL2, name); 
		c.put(COL3, type); 
		c.put(COL4, date);
		c.put(COL5, desc);
		c.put(COL6, value);
		
		db.insert(TABLE_NAME, null, c);
    } 

	public Cursor getData(String title){ 
		SQLiteDatabase db = this.getWritableDatabase(); 
		String query="SELECT * FROM "+TABLE_NAME+" WHERE "+COL2+"='"+title+"'"; 
		Cursor  cursor = db.rawQuery(query,null); 
		return cursor; 
    } 

	public Cursor searchData(String search){ 
		SQLiteDatabase db = this.getWritableDatabase(); 
		String query="SELECT * FROM "+TABLE_NAME+" WHERE "+COL2+"='"+search+"' OR "+COL3+"='"+search+"' COLLATE NOCASE"; 
		Cursor  cursor = db.rawQuery(query,null); 
		return cursor; 
    } 

	public Cursor getAllData() { 
		Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME, null); 
		return res; 
    } 
	
	public Cursor getLatest() { 
		Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY "+COL1+" DESC LIMIT 1", null); 
		return res; 
    } 

	public void updateData(String id,String name, String type, String desc, String value) { 
		db.execSQL("UPDATE "+TABLE_NAME+" SET name='"+name+"', type='"+type+"', description='"+desc+"', value='"+value+"' WHERE ID='"+id+"'");
    }

	public void deleteAll(){
		db.execSQL("DELETE FROM "+TABLE_NAME);
	}

	public void deleteOne(String id){
		db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+COL1+"='"+id+"'");
	}
}
