package com.android.foodorderapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

public class Hours implements Parcelable {

    private static final String TIME_SEPERATOR =" - " ;
    String Sunday;
    String Monday;
    String Tuesday;
    String Wednesday;
    String Thursday;
    String Friday;
    String Saturday;

    protected Hours(Parcel in) {
        Sunday = in.readString();
        Monday = in.readString();
        Tuesday = in.readString();
        Wednesday = in.readString();
        Thursday = in.readString();
        Friday = in.readString();
        Saturday = in.readString();
    }

    public static final Creator<Hours> CREATOR = new Creator<Hours>() {
        @Override
        public Hours createFromParcel(Parcel in) {
            return new Hours(in);
        }

        @Override
        public Hours[] newArray(int size) {
            return new Hours[size];
        }
    };

    public String getSunday() {
        return Sunday;
    }

    public void setSunday(String sunday) {
        Sunday = sunday;
    }

    public String getMonday() {
        return Monday;
    }

    public void setMonday(String monday) {
        Monday = monday;
    }

    public String getTuesday() {
        return Tuesday;
    }

    public void setTuesday(String tuesday) {
        Tuesday = tuesday;
    }

    public String getWednesday() {
        return Wednesday;
    }

    public void setWednesday(String wednesday) {
        Wednesday = wednesday;
    }

    public String getThursday() {
        return Thursday;
    }

    public void setThursday(String thursday) {
        Thursday = thursday;
    }

    public String getFriday() {
        return Friday;
    }

    public void setFriday(String friday) {
        Friday = friday;
    }

    public String getSaturday() {
        return Saturday;
    }

    public void setSaturday(String saturday) {
        Saturday = saturday;
    }

    public int getHrsLeft(){
        String time=getTodaysHours();
        String[] parts =time.split(TIME_SEPERATOR);
        String openingTime=parts[0];
        String closingTime=parts[1];
        String openingTimeIn24 = "";
        String closingTimeIn24 = "";
        try {
            Date dot =new SimpleDateFormat("h:mm a").parse(openingTime);
            Date dct =new SimpleDateFormat("h:mm a").parse(closingTime);
            openingTimeIn24 =new SimpleDateFormat("HH").format(dot);
            closingTimeIn24 =new SimpleDateFormat("HH").format(dct);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int hrsLeft;
        Calendar calendar = Calendar.getInstance();
        String currenttime =new SimpleDateFormat("HH").format(calendar.getTime());
        int ct=Integer.parseInt(currenttime);
        int ot=Integer.parseInt(openingTimeIn24);
        int clt=Integer.parseInt(closingTimeIn24);

       // Log.i("Time", "getHRSLeft "+currenttime);
        if(ct>=ot&&ct<(clt-1)){
            hrsLeft =clt-ct;
        }else if(clt-1==ct){
            hrsLeft=1;
        }
        else {
            hrsLeft = 0;
        }
        return hrsLeft;
    }


    public String getTodaysHours() {
        Calendar calendar = Calendar.getInstance();
        Date date  = calendar.getTime();
        String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        switch (day) {
            case "Sunday":
                return this.Sunday;
            case "Monday":
                return this.Monday;
            case "Tuesday":
                return this.Tuesday;
            case "Wednesday":
                return this.Wednesday;
            case "Thursday":
                return this.Thursday;
            case "Friday":
                return this.Friday;
            case "Saturday":
                return this.Saturday;
            default:
                return this.Sunday;

        }
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Sunday);
        dest.writeString(Monday);
        dest.writeString(Tuesday);
        dest.writeString(Wednesday);
        dest.writeString(Thursday);
        dest.writeString(Friday);
        dest.writeString(Saturday);
    }
}
