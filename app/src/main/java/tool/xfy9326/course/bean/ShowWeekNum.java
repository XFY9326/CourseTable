package tool.xfy9326.course.bean;

import androidx.annotation.NonNull;

public class ShowWeekNum {
    private int weekNum;
    private boolean inVacation;
    private boolean isCurrentWeek;
    @NonNull
    private WeekNum currentWeek;

    public ShowWeekNum(int weekNum, @NonNull WeekNum currentWeek) {
        this.weekNum = weekNum;
        this.inVacation = weekNum == 0;
        this.isCurrentWeek = weekNum != 0 && currentWeek.getWeekNum() == weekNum;
        this.currentWeek = currentWeek;
    }

    public int getWeekNum() {
        return weekNum;
    }

    public boolean isInVacation() {
        return inVacation;
    }

    public boolean isCurrentWeek() {
        return isCurrentWeek;
    }

    @NonNull
    public WeekNum getCurrentWeek() {
        return currentWeek;
    }

    @NonNull
    @Override
    public String toString() {
        return "ShowWeekNum{" +
                "weekNum=" + weekNum +
                ", inVacation=" + inVacation +
                ", isCurrentWeek=" + isCurrentWeek +
                ", currentWeek=" + currentWeek +
                '}';
    }
}
