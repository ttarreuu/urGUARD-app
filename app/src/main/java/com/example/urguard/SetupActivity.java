package com.example.urguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetupActivity extends AppCompatActivity {

    Button btn_done1;
    EditText etName, etContact1, etContact2, etContact3, etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        DBAdapter dbAdapter = new DBAdapter(getBaseContext());

        // If user exist, go to dashboard
        SharedPreferences prefs = getSharedPreferences(getPackageName() + "_preferences", Context.MODE_PRIVATE);
        boolean hasSetup = prefs.getBoolean("hasSetup", false); // get value of last login status
        if (hasSetup) {
            // Change activity
            Intent intent = new Intent(SetupActivity.this, DashboardActivity.class);
            startActivity(intent);
        }

        etName = findViewById(R.id.etname1_1);
        etContact1 = findViewById(R.id.etcontact1_1);
        etContact2 = findViewById(R.id.etcontact1_2);
        etContact3 = findViewById(R.id.etcontact1_3);
        etMessage = findViewById(R.id.etmessage1_1);

        btn_done1 = (Button) findViewById(R.id.btn_done1_1);
        btn_done1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create User
                String name = etName.getText().toString();
                String contact1 = etContact1.getText().toString();
                String contact2 = etContact2.getText().toString();
                String contact3 = etContact3.getText().toString();
                String message = etMessage.getText().toString();
                user user = new user(name, contact1, contact2, contact3, message);
                dbAdapter.insertUser(user);
                prefs.edit().putBoolean("hasSetup", true).commit();


                // Change activity
                Intent intent = new Intent(SetupActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}