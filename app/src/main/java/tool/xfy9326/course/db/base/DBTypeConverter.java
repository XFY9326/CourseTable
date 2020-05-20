package tool.xfy9326.course.db.base;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import tool.xfy9326.course.bean.TimePeriod;
import tool.xfy9326.course.bean.WeekMode;


public class DBTypeConverter {
    private static final String LIST_SPLIT = ",";

    @Nullable
    @TypeConverter
    public Date fromTimestamp(@Nullable Long value) {
        return value == null ? null : new Date(value);
    }

    @Nullable
    @TypeConverter
    public Long dateToTimestamp(@Nullable Date date) {
        return date == null ? null : date.getTime();
    }

    @Nullable
    @TypeConverter
    public TimePeriod fromTimePeriodStr(@Nullable String value) {
        return value == null ? null : new TimePeriod(value);
    }

    @Nullable
    @TypeConverter
    public String timePeriodToStr(@Nullable TimePeriod timePeriod) {
        return timePeriod == null ? null : timePeriod.toString();
    }

    @Nullable
    @TypeConverter
    public WeekMode fromWeekModeStr(@Nullable String value) {
        return value == null ? null : WeekMode.valueOf(value);
    }

    @Nullable
    @TypeConverter
    public String weekModeToStr(@Nullable WeekMode weekMode) {
        return weekMode == null ? null : weekMode.name();
    }

    @Nullable
    @TypeConverter
    public List<TimePeriod> fromTimePeriodListStr(@Nullable String value) {
        if (value == null) {
            return null;
        } else if (value.isEmpty()) {
            return Collections.emptyList();
        } else if (value.contains(LIST_SPLIT)) {
            String[] values = value.split(LIST_SPLIT);
            ArrayList<TimePeriod> result = new ArrayList<>(values.length);
            for (String s : values) {
                result.add(new TimePeriod(s));
            }
            return result;
        } else {
            ArrayList<TimePeriod> result = new ArrayList<>(1);
            result.add(new TimePeriod(value));
            return result;
        }
    }

    @Nullable
    @TypeConverter
    public String timePeriodListToStr(@Nullable List<TimePeriod> timePeriods) {
        if (timePeriods == null) {
            return null;
        } else if (timePeriods.isEmpty()) {
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
}
