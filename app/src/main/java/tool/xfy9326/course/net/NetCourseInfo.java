package tool.xfy9326.course.net;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import tool.xfy9326.course.bean.Course;

public class NetCourseInfo {
    @NonNull
    private List<Course> courses;
    private int maxCoursePerDay;
    private int maxWeekNum;
    @NonNull
    private Date termStartDate;

    NetCourseInfo(int maxCoursePerDay, int maxWeekNum, @NonNull Date termStartDate) {
        this.courses = Collections.emptyList();
        this.maxCoursePerDay = maxCoursePerDay;
        this.maxWeekNum = maxWeekNum;
        this.termStartDate = termStartDate;
    }

    @NonNull
    public List<Course> getCourses() {
        return courses;
    }

    void setCourses(@NonNull List<Course> courses) {
        this.courses = courses;
    }

    public int getMaxCoursePerDay() {
        return maxCoursePerDay;
    }

    public int getMaxWeekNum() {
        return maxWeekNum;
    }

    @NonNull
    public Date getTermStartDate() {
        return termStartDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "NetCourseInfo{" +
                "courses=" + courses +
                ", maxCoursePerDay=" + maxCoursePerDay +
                ", maxWeekNum=" + maxWeekNum +
                ", termStartDate=" + termStartDate +
                '}';
    }
}
