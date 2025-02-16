package org.vivi.framework.excel.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

public static String localDateToString(LocalDate object) {
    LocalDate localDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return localDate.format(formatter);
}

    public static Object localDateTimeToString(LocalDateTime object) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return object.format(formatter);
    }
}
