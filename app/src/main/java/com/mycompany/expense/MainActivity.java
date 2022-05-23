package com.mycompany.expense;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.time.format.DateTimeFormatter;
import android.database.Cursor;
import android.widget.TextView;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    public static Database db;
	private ArrayList<CashTransaction> items;
	private ListView TransactionListView;
	private Dialog addDialog;
	private Button showAddBtn, addBtn, cancelAddBtn,viewHistoryBtn;
	private EditText addName, addDesc, addValue;
	private RadioGroup addTypeGrp;
	private TextView recentName, recentValue, recentType, totalIncome, totalExpense, balanceText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		//Instantiate essentials
		db = new Database(this);
		items = new ArrayList<CashTransaction>();
		//TransactionListView = findViewById(R.id.historyList);
        viewHistoryBtn = findViewById(R.id.viewHistoryBtn);
		viewHistoryBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					Intent i = new Intent(MainActivity.this, HistoryActivity.class);
					startActivity(i);
				}
			});
		
		// Initialize recent transaction
		
		recentName = findViewById(R.id.recentName);
		recentType = findViewById(R.id.recentType);
		recentValue = findViewById(R.id.recentValue);
		totalIncome = findViewById(R.id.totalIncomeTxt);
		totalExpense = findViewById(R.id.totalExpenseTxt);
		balanceText = findViewById(R.id.balanceText);
		
		getData();
		
		//Setup add todo modal dialog
		addDialog = new Dialog(MainActivity.this);
		addDialog.setContentView(R.layout.add_modal);
		addDialog.setTitle("Add Transaction");
		addDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		addDialog.setCancelable(true);
		
		addBtn = addDialog.findViewById(R.id.addAddBtn);
		cancelAddBtn = addDialog.findViewById(R.id.addCancelBtn);
		addValue = addDialog.findViewById(R.id.addValue);
		addDesc = addDialog.findViewById(R.id.addDesc);
		addTypeGrp = addDialog.findViewById(R.id.typeRadioGrp);
		addName = addDialog.findViewById(R.id.addName);
		
		cancelAddBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					addDialog.hide();
				}
			});
		
		showAddBtn = findViewById(R.id.showAddModal);
		showAddBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					addDialog.show();
				}
			});
			
		
			
		// Handle add record
		addBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					try{
						int radioId = addTypeGrp.getCheckedRadioButtonId();
						RadioButton btn = addDialog.findViewById(radioId);
                        
						String date = java.time.LocalDate.now().toString();
						String name = addName.getText().toString();
						String desc = addDesc.getText().toString();
						String type = btn.getText().toString();
						String value = addValue.getText().toString();

						Toast.makeText(MainActivity.this,name+desc+type+value+date, Toast.LENGTH_LONG).show();
						if(name.length()==0 || desc.length()==0 || type.length()==0 || value.length()==0){
							Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
							return;
						}
						
						db.insertData(name,type,date,desc,value);
						Toast.makeText(MainActivity.this, "Transaction added successfully", Toast.LENGTH_LONG).show();
						addDialog.hide();
						getData();
						clearAddData();
					}catch(Exception e){
						//Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
					}
					
				}
			});
			
			
			
    }
	
	//Get recent transaction
	void getData(){
		try{
			Cursor cur = db.getLatest();
			Cursor allData = db.getAllData();
			while(cur.moveToNext()){
				recentName.setText(cur.getString(1));
				recentType.setText(cur.getString(2));
				recentValue.setText(cur.getString(5));
			}
			
			//Compute total balance, expense, and income
			int expense = 0;
			int income = 0;
			
			while(allData.moveToNext()){
				String currentType = allData.getString(2);
				int currentValue = Integer.parseInt(allData.getString(5));
				if(currentType.equalsIgnoreCase("income")){
					income += currentValue;
				}else if(currentType.equalsIgnoreCase("expense")){
					expense += currentValue;
				}
			}
			int balance = income-expense;
			totalIncome.setText(String.valueOf(income));
			totalExpense.setText(String.valueOf(expense));
			balanceText.setText(String.valueOf(balance));
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	void clearAddData(){
		addName.setText("");
		addDesc.setText("");
		addValue.setText("");
	}
}
