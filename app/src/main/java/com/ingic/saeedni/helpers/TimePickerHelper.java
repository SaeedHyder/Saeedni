package com.ingic.saeedni.helpers;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created on 4/29/2017.
 */

public class TimePickerHelper {
    private Dialog dialog;

    public TimePickerHelper() {

    }
    public Dialog initTimeDialog(Context context, int hour, int minute, TimePickerDialog.OnTimeSetListener onTimeSetListener, boolean is24Hour){
        this.dialog = new TimePickerDialog(context, onTimeSetListener, hour, minute,is24Hour );
        return dialog;
    }

    public void showTime() {
        if (this.dialog == null){
            throw  new NullPointerException("Initialize Dialog First");
        }else {
            this.dialog.show();
        }

    }

    public String getTime(int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year,month,day,hourOfDay, minute);
        return  new SimpleDateFormat("HH:mm:ss").format(c.getTime());
    }

}
