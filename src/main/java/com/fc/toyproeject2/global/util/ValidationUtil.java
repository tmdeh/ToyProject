package com.fc.toyproeject2.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ValidationUtil {

    public static boolean checkTime(LocalDateTime startTime, LocalDateTime endTime){
        return !startTime.isBefore(endTime);
    }

    public static boolean checkDate(LocalDate startDate, LocalDate endDate){
        return !startDate.isBefore(endDate);
    }

}
