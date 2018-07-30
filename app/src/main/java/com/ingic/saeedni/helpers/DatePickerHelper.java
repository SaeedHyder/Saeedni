package com.ingic.saeedni.helpers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created on 4/29/2017.
 */

public class DatePickerHelper {
    private Dialog dialog;

    public DatePickerHelper() {

    }
    public Dialog initDateDialog(Context context, int year, int month, int day, DatePickerDialog.OnDateSetListener dateSetListener,String tag){
        this.dialog = new DatePickerDialog(context, dateSetListener, year, month, day);
        return dialog;
    }

    public void showDate() {
        if (this.dialog == null){
            throw  new NullPointerException("Initialize Dialog First");
        }else {
            this.dialog.show();
        }


    }

    public String getShortDayName(int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month, day);
        return new SimpleDateFormat("EEE", Locale.ENGLISH).format(calendar.getTime());
    }
    public String getFullDayName(int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month, day);
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.getTime());
    }
    public String getShortMonthName(int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month, day);
        return new SimpleDateFormat("MMM", Locale.ENGLISH).format(calendar.getTime());
    }
    public String getFullMonthName(int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month, day);
        return new SimpleDateFormat("MMMM", Locale.ENGLISH).format(calendar.getTime());
    }
    public String getStringDate(int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month, day);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime());
    }

}
