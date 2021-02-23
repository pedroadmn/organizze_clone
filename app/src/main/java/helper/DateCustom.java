package helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String currentDate() {
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
        return simpleDateFormat.format(date);
    }

    public static String monthYearChoseDate(String date) {
        String resultDate[] = date.split("/");
        String month = resultDate[0];
        String year = resultDate[2];

        return month + year;
    }
}
