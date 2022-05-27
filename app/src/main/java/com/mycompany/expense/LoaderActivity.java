package com.mycompany.expense;

import android.app.Activity;
import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;

public class LoaderActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loader);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
	        @Override
            public void run(){
                Intent i = new Intent(LoaderActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
        }, 3000);
    }
    
}
