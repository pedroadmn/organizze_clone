package helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String currentDate() {
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
        return simpleDateFormat.format(date);
    }
}
