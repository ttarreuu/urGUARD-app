package com.example.urguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    Button btn_about, btn_save, btn_report, btn_feedback;
    ImageButton btn_return;
    EditText etContact1, etContact2, etContact3, etMessage;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        etContact1 = findViewById(R.id.etcontact4_1);
        etContact2 = findViewById(R.id.etcontact4_2);
        etContact3 = findViewById(R.id.etcontact4_3);
        tvName = findViewById(R.id.tvname4_1);
        etMessage = findViewById(R.id.etmessage4_1);

        // Ambil data dari sqlite
        DBAdapter dbAdapter = new DBAdapter(this.getBaseContext());
        user user = dbAdapter.getUser();

        tvName.setText(user.getName());
        etContact1.setText(user.getContact_1());
        etContact2.setText(user.getContact_2());
        etContact3.setText(user.getContact_3());
        etMessage.setText(user.getMessage());


        btn_about = findViewById(R.id.btn_about);
        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        btn_return = findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
        
        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(v -> {
            String name = tvName.getText().toString();
            String newC1 = etContact1.getText().toString();
            String newC2 = etContact2.getText().toString();
            String newC3 = etContact3.getText().toString();
            String newM = etMessage.getText().toString();
            dbAdapter.updateUser(name, newC1, newC2, newC3, newM);
            Toast.makeText(getBaseContext(), "Data Updated", Toast.LENGTH_SHORT).show();
        });

        btn_report = findViewById(R.id.btn_report);
        btn_report.setOnClickListener(v -> {
            Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
            selectorIntent.setData(Uri.parse("mailto:"));

            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"notwatermango@gmail.com"}); // Testing
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[urGUARD][Report]");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            emailIntent.setSelector( selectorIntent );

            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });

        btn_feedback = findViewById(R.id.btn_feedback);
        btn_feedback.setOnClickListener(v -> {
            Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
            selectorIntent.setData(Uri.parse("mailto:"));

            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"notwatermango@gmail.com"}); // Testing
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[urGUARD][Feedback]");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            emailIntent.setSelector( selectorIntent );

            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        });
    }
}