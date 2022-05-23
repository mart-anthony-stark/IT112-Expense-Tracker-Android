package com.mycompany.expense;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import android.widget.Toast;
import android.graphics.Color;

public class HistoryAdapter extends ArrayAdapter<CashTransaction> {
    public HistoryAdapter(Context context, ArrayList<CashTransaction> transactions){
		super(context,0,transactions);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        try{
			CashTransaction tr = getItem(position);
			if(convertView == null){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
			}

			TextView title = convertView.findViewById(R.id.item_name);
			title.setText(tr.name);

			TextView val  = convertView.findViewById(R.id.item_value);
			val.setText(String.valueOf(tr.value));

			LinearLayout itemType = convertView.findViewById(R.id.item_type);
			int color = tr.type.equalsIgnoreCase("income") ? Color.parseColor("#FF2FC03F") : Color.parseColor("#FFC02F2F");
			itemType.setBackgroundColor(color);
		}catch(Exception e){
			
		}
		
		return convertView;
	}

}
