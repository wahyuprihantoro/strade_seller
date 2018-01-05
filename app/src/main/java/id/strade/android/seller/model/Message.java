package id.strade.android.seller.model;


import java.util.Calendar;
import java.util.Date;

import id.strade.android.seller.utils.DateTimeUtils;


/**
 * Created by wahyu on 11 July 2017.
 */

public class Message {
    public long timestamp;
    public int user_id;
    public String text;
    public String imageUrl;
    public String id;

    public String getDateStr() {
        Date date = new Date(timestamp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dateStr = DateTimeUtils.getLocalDate(calendar.getTime());

        Calendar today = Calendar.getInstance();
        String dateTodayStr = DateTimeUtils.getLocalDate(today.getTime());
        if (dateStr.equals(dateTodayStr)) dateStr = "Hari ini";

        return dateStr;
    }
}
