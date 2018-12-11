package com.example.nikhi_000.tapplent_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 *
 * Created by nikhi_000 on 1/5/2018.
 */

public class displayGroup extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_group_contacts);

        Intent i = getIntent();
        String title = i.getStringExtra("title");
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
        List<DataPerson> list = (List<DataPerson>) i.getSerializableExtra("LIST");
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.display_Names);

        for(int j = 0; j< list.size(); j++){
            TextView textView = new TextView(getApplicationContext());
            textView.setText(list.get(j).personName);
            textView.setTextSize(30);
            linearLayout.addView(textView);
        }
    }
}
