package id.strade.android.seller.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Wahyu Prihantoro on 22-Aug-16.
 */
public class DateTimeUtils {
    public static final long A_DAY = 86400000;

    public static String simpleDateFormat = "yyyy-MM-dd";

    public static String getSimpleDateFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(simpleDateFormat);
        return dateFormat.format(date);
    }

    public static String getLocalDate(Date date) {
        if (date == null) {
            date = new Date();
        }
        String bln = getMonthString(formatDate(date, "MM"));
        return formatDate(date, "dd") + " " + bln + " " + formatDate(date, "yyyy");
    }

    public static String getMonthString(String monthDateFormat) {
        String[] monthStringArr = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        int monthInt = Integer.parseInt(monthDateFormat);
        return monthStringArr[monthInt - 1];
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return "date can't be null";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }

    }
}
