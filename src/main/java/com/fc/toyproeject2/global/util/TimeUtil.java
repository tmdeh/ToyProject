package com.fc.toyproeject2.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    public static String localDateTimeToString(LocalDateTime time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(dtf);
    }

    public static String localDateTimeOnlyDateToString(LocalDateTime date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(dtf);
    }
}
