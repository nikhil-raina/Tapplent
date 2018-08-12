package com.example.nikhi_000.tapplent_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 *
 * Created by nikhi_000 on 1/6/2018.
 */

public class editText_Screen extends AppCompatActivity {

    private Button buttonSend;
    private EditText editTextBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text);

        buttonSend = (Button) findViewById(R.id.sendButton);
        editTextBox = (EditText) findViewById(R.id.enterText);

    }


    public void sendMessage(View view) {
        String message = editTextBox.getText().toString();
        Intent intent = new Intent();

        intent.putExtra("Message", message);
        setResult(2, intent);
        finish();
    }
}
