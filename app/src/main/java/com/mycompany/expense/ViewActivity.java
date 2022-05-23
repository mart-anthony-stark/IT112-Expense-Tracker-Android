package com.mycompany.expense;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Color;

public class ViewActivity extends Activity {
    private TextView viewName, viewDesc, viewValue, viewType, viewDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_item);
		
		viewName = findViewById(R.id.viewName);
		viewDesc = findViewById(R.id.viewDesc);
		viewDate = findViewById(R.id.viewDate);
		viewValue = findViewById(R.id.viewValue);
		viewType = findViewById(R.id.viewType);
		
		viewName.setText(Global.currentTransaction.name);
		viewDesc.setText(Global.currentTransaction.description);
		viewType.setText(Global.currentTransaction.type);
		viewValue.setText("P "+ Global.currentTransaction.value);
		viewDate.setText(Global.currentTransaction.date);
		
		int color = Global.currentTransaction.type.equalsIgnoreCase("income") ? Color.parseColor("#FF2FC03F") : Color.parseColor("#FFC02F2F");
		viewValue.setTextColor(color);
    }
    
}
