package tool.xfy9326.course.bean;

import androidx.annotation.NonNull;

public class WeekNum {
    private boolean inVacation;
    private int weekNum;
    private int todayCalWeekDay;
    private boolean isUpdateOnly = false;

    public WeekNum() {
        this(0, 0);
    }

    public WeekNum(int weekNum, int todayCalWeekDay) {
        this.inVacation = weekNum == 0;
        this.weekNum = weekNum;
        this.todayCalWeekDay = todayCalWeekDay;
    }

    public boolean isInVacation() {
        return inVacation;
    }

    public int getWeekNum() {
        return weekNum;
    }

    public int getTodayCalWeekDay() {
        return todayCalWeekDay;
    }

    public boolean isUpdateOnly() {
        return isUpdateOnly;
    }

    public void setUpdateOnly(boolean updateOnly) {
        isUpdateOnly = updateOnly;
    }

    @NonNull
    @Override
    public String toString() {
        return "WeekNum{" +
                "inVacation=" + inVacation +
                ", weekNum=" + weekNum +
                ", todayCalWeekDay=" + todayCalWeekDay +
                ", isUpdateOnly=" + isUpdateOnly +
                '}';
    }
}
