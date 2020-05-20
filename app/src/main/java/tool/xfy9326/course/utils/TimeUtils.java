package tool.xfy9326.course.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tool.xfy9326.course.Const;
import tool.xfy9326.course.bean.TimePeriod;
import tool.xfy9326.course.bean.WeekDay;
import tool.xfy9326.course.bean.WeekNum;
import tool.xfy9326.course.tool.CalendarWeekDay;

public class TimeUtils {
    private static final String LIST_SPLIT = ",";

    @NonNull
    public static List<TimePeriod> fromTimePeriodListStr(@NonNull String value) {
        if (value.isEmpty()) {
            return Collections.emptyList();
        } else if (value.contains(LIST_SPLIT)) {
            String[] values = value.split(LIST_SPLIT);
            ArrayList<TimePeriod> result = new ArrayList<>(values.length);
            for (String s : values) {
                result.add(new TimePeriod(s.trim()));
            }
            return result;
        } else {
            ArrayList<TimePeriod> result = new ArrayList<>(1);
            result.add(new TimePeriod(value));
            return result;
        }
    }

    @NonNull
    public static String timePeriodListToStr(@NonNull List<TimePeriod> timePeriods) {
        if (timePeriods.isEmpty()) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder(timePeriods.size() * 3);
            for (TimePeriod timePeriod : timePeriods) {
                builder.append(timePeriod.toString()).append(LIST_SPLIT);
            }
            builder.deleteCharAt(builder.length() - 1);
            return builder.toString();
        }
    }

    static int getWeekDayNum(@NonNull Date date) {
        Calendar calendar = getChinaCalendar();
        calendar.setTime(date);
        return getWeekDayNum(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static int getWeekDayNum(@CalendarWeekDay int dayOfWeek) {
        Calendar calendar = getChinaCalendar();
        int minDayOfWeek = calendar.getFirstDayOfWeek();
        if (dayOfWeek >= minDayOfWeek) {
            return dayOfWeek - minDayOfWeek + 1;
        } else {
            return 8 - minDayOfWeek + dayOfWeek;
        }
    }

    public static WeekNum getCurrentWeekNum(@NonNull Date termStartDate, int maxWeekNum) {
        long startWeekDayTime = getFirstDayOfThisWeek(termStartDate).getTime();
        long currentTime = System.currentTimeMillis();
        if (currentTime > startWeekDayTime) {
            int weekNum = (int) Math.ceil((currentTime - startWeekDayTime) / (7 * 24 * 60 * 60 * 1000f));
            if (weekNum > maxWeekNum) {
                return new WeekNum();
            } else {
                Calendar calendar = getChinaCalendar();
                return new WeekNum(weekNum, calendar.get(Calendar.DAY_OF_WEEK));
            }
        } else {
            return new WeekNum();
        }
    }

    public static WeekDay getTodayDate() {
        Calendar calendar = getChinaCalendar();
        return new WeekDay(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE), calendar.get(Calendar.DAY_OF_WEEK), true);
    }

    @NonNull
    public static WeekDay[] getWeekDayByWeekNum(@NonNull Date termStartDate, int weekNum, boolean fullWeek) {
        Date startWeekDay = getFirstDayOfThisWeek(termStartDate);
        Calendar calendar = getOnlyDateCalendar();
        int todayMonth = calendar.get(Calendar.MONTH) + 1;
        int todayDate = calendar.get(Calendar.DATE);
        calendar.setTime(startWeekDay);
        calendar.add(Calendar.DATE, (weekNum - 1) * 7 - 1);
        WeekDay[] result = new WeekDay[fullWeek ? Const.MAX_WEEK_DAY : Const.SIMPLE_WEEK_DAY];
        for (int i = 0; i < result.length; i++) {
            calendar.add(Calendar.DATE, 1);
            int dayMonth = calendar.get(Calendar.MONTH) + 1;
            int dayDate = calendar.get(Calendar.DATE);
            result[i] = new WeekDay(dayMonth, dayDate, calendar.get(Calendar.DAY_OF_WEEK), dayMonth == todayMonth && dayDate == todayDate);
        }
        return result;
    }

    @NonNull
    private static Date getFirstDayOfThisWeek(@NonNull Date date) {
        Calendar calendar = getOnlyDateCalendar(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    @NonNull
    public static Date getDefaultTermStartDate() {
        Calendar calendar = getOnlyDateCalendar();

        if (calendar.get(Calendar.MONTH) >= Calendar.JULY) {
            calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
            return calendar.getTime();
        } else {
            calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
            calendar.set(Calendar.DATE, 15);
            return calendar.getTime();
        }
    }

    @NonNull
    private static Calendar getOnlyDateCalendar(@NonNull Date date) {
        Calendar calendar = getChinaCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        return calendar;
    }

    @NonNull
    private static Calendar getChinaCalendar() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar;
    }

    @NonNull
    private static Calendar getOnlyDateCalendar() {
        return getOnlyDateCalendar(new Date());
    }
}
