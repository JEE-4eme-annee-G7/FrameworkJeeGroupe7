package com.esgi.framework_JEE.kernel.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManipulator {
    public static Date stringToDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return simpleDateFormat.parse(date);
    }

    public static String dateToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return simpleDateFormat.format(date);
    }

    public static Date reformatDate(Date date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return simpleDateFormat.parse(
                simpleDateFormat.format(
                        date
                )
        );
    }
}
