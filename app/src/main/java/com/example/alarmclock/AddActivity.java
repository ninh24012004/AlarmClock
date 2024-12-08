package com.example.alarmclock;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private EditText editText;
    private Button buttonSave, buttonCancel;

    private Alarm alarm;
    private boolean needRefresh;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        timePicker = findViewById(R.id.timePicker);
        editText = findViewById(R.id.name);
        buttonCancel = findViewById(R.id.cancel);
        buttonSave = findViewById(R.id.save);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                String name = editText.getText().toString();

                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

                alarm = new Alarm(hour, minute, true, name);
                databaseHelper.addAlarm(alarm);

                needRefresh = true;
                onBackPressed();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("needRefresh", needRefresh);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}
