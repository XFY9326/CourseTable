package tool.xfy9326.course.bean;

import java.io.Serializable;

import tool.xfy9326.course.tool.CalendarWeekDay;

public class WeekDay implements Serializable {
    private static final long serialVersionUID = 1L;

    private int month;
    private int day;
    @CalendarWeekDay
    private int calWeekDay;
    private boolean isToday;

    public WeekDay(int month, int day, @CalendarWeekDay int calWeekDay, boolean isToday) {
        this.month = month;
        this.day = day;
        this.calWeekDay = calWeekDay;
        this.isToday = isToday;
    }

    public boolean isToday() {
        return isToday;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @CalendarWeekDay
    public int getCalWeekDay() {
        return calWeekDay;
    }

    public void setCalWeekDay(@CalendarWeekDay int calWeekDay) {
        this.calWeekDay = calWeekDay;
    }
}
