package rango.tool.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by baiyuanwei on 17/11/20.
 */

public class TimeUtills {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static long strToMills(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date date = dateFormat.parse(dateStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String milllsToStr(long mills) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date date = new Date();
        date.setTime(mills);
        return dateFormat.format(date);
    }

    public static long getMillsDistance24() {
        String timeStr = getCurrentYMD() + " 24:00:00";
        return strToMills(timeStr) - System.currentTimeMillis();
    }

    /**
     * Get current year/month/day
     *
     * @return
     */
    public static String getCurrentYMD() {
        long currentMills = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date.setTime(currentMills);
        return dateFormat.format(date);
    }

}
