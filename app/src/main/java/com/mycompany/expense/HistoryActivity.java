package com.mycompany.expense;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import android.content.Intent;

public class HistoryActivity extends Activity {
    private Database db;
	private ListView historyList;
	private ArrayList<CashTransaction> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
		
		try{
			//Initialization
			db = new Database(this);
			historyList = findViewById(R.id.historyList);
			historyList.setOnItemClickListener(new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
						// View details
						CashTransaction t = items.get(p3);
						Global.currentTransaction = t;
						Intent i = new Intent(HistoryActivity.this, ViewActivity.class);
						startActivity(i);
					}
				});
			items = new ArrayList<CashTransaction>();
			registerForContextMenu(historyList);
			getHistory();
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
    }
	
	// Context menu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.historyList) {
			ListView lv = (ListView) v;
			AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
			CashTransaction obj = (CashTransaction) lv.getItemAtPosition(acmi.position);

			menu.add("Edit");
			menu.add("Delete");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int listPosition = info.position;
        CashTransaction todo = items.get(listPosition);
	    String id = todo.id;

		if (item.getTitle().equals("Delete")) {
            Toast.makeText(getApplicationContext(), "del", Toast.LENGTH_LONG).show();
		}else if(item.getTitle().equals("Edit")){
			Toast.makeText(getApplicationContext(), "edit", Toast.LENGTH_LONG).show();
		}
		
		return true;
	}
	
	void getHistory(){
		try{
			Cursor curr = db.getAllData();
			while(curr.moveToNext()){
				CashTransaction item = new CashTransaction();
				item.id = curr.getString(0);
				item.name = curr.getString(1);
				item.type = curr.getString(2);
				item.date = curr.getString(3);
				item.description = curr.getString(4);
				item.value = Integer.parseInt(curr.getString(5));
				items.add(item);
			}
			HistoryAdapter adapter = new HistoryAdapter(this, items);
			historyList.setAdapter(adapter);
		}catch(Exception ex){
			Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
		}
		
	}
    
}
