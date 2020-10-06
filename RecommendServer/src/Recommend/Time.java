package Recommend;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Time
{
    public final static int SECOND_IN_MINUTE = 60;
    public final static int SECOND_IN_HOUR   = 60 * SECOND_IN_MINUTE;
    public final static int SECOND_IN_DAY    = 24 * SECOND_IN_HOUR;
    public final static int SECOND_IN_30_DAY = 30 * SECOND_IN_DAY;
    public final static int SECOND_IN_7_DAY  = 7 * SECOND_IN_DAY;

    public final static long MILLISECOND_IN_MINUTE = 1000 * SECOND_IN_MINUTE;
    public final static long MILLISECOND_IN_HOUR   = 1000 * SECOND_IN_HOUR;
    public final static long MILLISECOND_IN_DAY    = 1000 * SECOND_IN_DAY;
    public final static long MILLISECOND_IN_7_DAY  = 1000 * SECOND_IN_7_DAY;
    public final static long MILLISECOND_IN_30_DAY = 1000 * SECOND_IN_30_DAY;

    public final static ZoneOffset zone = OffsetDateTime.now().getOffset();

    public final static DateTimeFormatter formatterBackup = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");


    public static int getUnixTime ()
    {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static long getTimeMillis ()
    {
        return System.currentTimeMillis();
    }

    public static String toString (int unixTime)
    {
        return LocalDateTime.ofEpochSecond(unixTime, 0, zone).toString();
    }
}
