package com.mycompany.expense;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class HistoryActivity extends Activity {
    private Database db;
	private ListView historyList;
	private ArrayList<CashTransaction> items;
	private Dialog editDialog;
	private EditText editName, editValue, editDesc;
	private RadioGroup editTypeGrp;
	private Button editSaveBtn, editCancelBtn;
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
			
			//Setup edit modal dialog
			editDialog = new Dialog(HistoryActivity.this);
			editDialog.setContentView(R.layout.edit_modal);
			editDialog.setTitle("Add Transaction");
			editDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
			editDialog.setCancelable(true);
			
			editName = editDialog.findViewById(R.id.editName);
			editDesc = editDialog.findViewById(R.id.editDesc);
			editValue = editDialog.findViewById(R.id.editValue);
			editTypeGrp = editDialog.findViewById(R.id.EditTypeRadioGrp);
			editSaveBtn = editDialog.findViewById(R.id.editSaveBtn);
			editCancelBtn = editDialog.findViewById(R.id.editCancelBtn);
			
			editCancelBtn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View p1) {
						editDialog.hide();
					}
				});
				
			editSaveBtn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View p1) {
						try{
							int typeId = editTypeGrp.getCheckedRadioButtonId();
							RadioButton selected = editDialog.findViewById(typeId);
							
							String name = editName.getText().toString();
							String val = editValue.getText().toString();
							String desc = editDesc.getText().toString();
							String type = selected.getText().toString();
							
							if(name.length()==0 || val.length()==0 || desc.length()==0 || type.length()==0){
								Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
								
							}else{
								db.updateData(Global.currentTransaction.id, name, type, desc, val);
								getHistory();
								Toast.makeText(getApplicationContext(), "Successfully updated data", Toast.LENGTH_LONG).show();
								editDialog.hide();
							}
						}catch(Exception e){}
					}
				});
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
        CashTransaction tr = items.get(listPosition);
	    final String id = tr.id;

		if (item.getTitle().equals("Delete")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Confirmation");
			builder.setMessage("Are you sure you want to delete this record?");
			builder.setPositiveButton("YES", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int p2) {
						db.deleteOne(id);
						getHistory();
						Toast.makeText(getApplicationContext(), "Successfully deleted record", Toast.LENGTH_LONG).show();
						dialog.dismiss();
					}
				});
			builder.setNegativeButton("NO", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int p2) {
						dialog.dismiss();
					}
				});
			AlertDialog alert = builder.create();
			alert.show();
		}else if(item.getTitle().equals("Edit")){
			try{
				Global.currentTransaction.id = id;
				int selected = tr.type.equalsIgnoreCase("income") ?
					R.id.editIncome : R.id.editExpense;
				editName.setText(tr.name);
				editDesc.setText(tr.description);
				editValue.setText(String.valueOf(tr.value));
				editTypeGrp.check(selected);
				editDialog.show();
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			}
			
		}
		
		getHistory();
		return true;
	}
	
	void getHistory(){
		try{
			items.clear();
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
