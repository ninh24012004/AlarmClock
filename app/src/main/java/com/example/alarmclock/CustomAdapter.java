package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<Alarm> alarmList;
    private LayoutInflater layoutInflater;

    @Override
    public int getCount() {
        return alarmList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.row_item, null);
        Alarm selectedAlarm = alarmList.get(i);
        final TextView alarmTV = view.findViewById(R.id.timeTextView);
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmTV.setText(selectedAlarm.toString());

        final Intent serviceIntent = new Intent(context, AlarmReceiver.class);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, selectedAlarm.getHour());
        calendar.set(Calendar.MINUTE, selectedAlarm.getMinute());
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE,1);
        }

        ToggleButton toggleButton = view.findViewById(R.id.toggle);
        toggleButton.setChecked(selectedAlarm.isStatus());
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                selectedAlarm.setStatus(b);
                DatabaseHelper db = new DatabaseHelper(context);
                db.updateAlarm(selectedAlarm);

                MainActivity.alarmList.clear();
                List<Alarm> list = db.getAllAlarms();
                MainActivity.alarmList.addAll(list);
                notifyDataSetChanged();

                if (!b && selectedAlarm.toString().equals(MainActivity.activeAlarm)) {
                    serviceIntent.putExtra("extra", "off");

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, serviceIntent, PendingIntent.FLAG_IMMUTABLE);
                    alarmManager.cancel(pendingIntent);
                    context.sendBroadcast(serviceIntent);
                }
            }
        });

        if (selectedAlarm.isStatus()) {
            serviceIntent.putExtra("extra", "on");
            serviceIntent.putExtra("active", selectedAlarm.toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, serviceIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        return view;
    }

    public CustomAdapter(Context context, List<Alarm> alarmList) {
        this.context = context;
        this.alarmList = alarmList;
        layoutInflater = (LayoutInflater.from(context));
    }
}
