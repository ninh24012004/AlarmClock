package com.example.alarmclock;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Alarm implements Parcelable {
    private int id;
    private int hour;
    private int minute;
    private boolean status;


    public Alarm() {
    }


    public Alarm(int hour, int minute, boolean status) {
        this.hour = hour;
        this.minute = minute;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeByte((byte) (status ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Creator for Parcelable
    public static final Parcelable.Creator<Alarm> CREATOR = new Parcelable.Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    protected Alarm(Parcel in) {
        id = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        status = in.readByte() != 0;
    }

    @NonNull
    @Override
    public String toString() {
        String hourString, minuteString, format;
        if (hour > 12) {
            hourString = (hour - 12) + "";
            format = " PM";
        } else if (hour == 0) {
            hourString = "12";
            format = " AM";
        } else if (hour == 12) {
            hourString = "12";
            format = " PM";
        } else {
            hourString = hour + "";
            format = " AM";
        }

        if (minute < 10) {
            minuteString = "0" + minute;
        } else {
            minuteString = "" + minute;
        }
        return hourString + ":" + minuteString + format;
    }
}
