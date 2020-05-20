package tool.xfy9326.course.utils;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import tool.xfy9326.course.App;
import tool.xfy9326.course.R;
import tool.xfy9326.course.tool.CalendarWeekDay;

public class I18NUtils {
    @NonNull
    public static String getAppString(@StringRes int resId) {
        return App.instance.getString(resId);
    }

    @NonNull
    public static String getAppString(@StringRes int resId, Object... formatArgs) {
        return App.instance.getString(resId, formatArgs);
    }

    @NonNull
    public static String getWeekDayShowText(@NonNull Context context, @CalendarWeekDay int weekDay) {
        return context.getResources().getStringArray(R.array.weekday_show_text)[weekDay - 1];
    }

    @NonNull
    public static String getWeekDayFullShowText(@NonNull Context context, @CalendarWeekDay int weekDay) {
        return context.getString(R.string.week_day_text, context.getResources().getStringArray(R.array.weekday_show_text)[weekDay - 1]);
    }

    @NonNull
    public static String getMonthDayShowText(@NonNull Context context, @IntRange(from = 1, to = 12) int monthNum, @IntRange(from = 1, to = 31) int dateNum) {
        return context.getString(R.string.month_day, monthNum, dateNum);
    }
}
