package tool.xfy9326.course.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import tool.xfy9326.course.Const;
import tool.xfy9326.course.tool.CalendarWeekDay;

@Entity(foreignKeys = @ForeignKey(entity = Course.class, parentColumns = Course.COLUMN_COURSE_ID, childColumns = Course.COLUMN_COURSE_ID, onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE))
public class CourseTime implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private long timeId;

    @ColumnInfo(name = Course.COLUMN_COURSE_ID, index = true)
    private long courseId;

    @CalendarWeekDay
    private int calWeekDay;

    @NonNull
    private TimePeriod courseTime;

    @NonNull
    private WeekMode weekMode;

    @NonNull
    private List<TimePeriod> weekNumList;

    @Nullable
    private String location;

    public CourseTime(@CalendarWeekDay int calWeekDay, @NonNull TimePeriod courseTime, @NonNull WeekMode weekMode, @NonNull List<TimePeriod> weekNumList) {
        this.timeId = Const.DEFAULT_DB_ID;
        this.courseId = Const.DEFAULT_DB_ID;
        this.calWeekDay = calWeekDay;
        this.courseTime = courseTime;
        this.weekMode = weekMode;
        this.weekNumList = weekNumList;
    }

    public long getTimeId() {
        return timeId;
    }

    public void setTimeId(long timeId) {
        this.timeId = timeId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    @CalendarWeekDay
    public int getCalWeekDay() {
        return calWeekDay;
    }

    public void setCalWeekDay(@CalendarWeekDay int calWeekDay) {
        this.calWeekDay = calWeekDay;
    }

    @NonNull
    public TimePeriod getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(@NonNull TimePeriod courseTime) {
        this.courseTime = courseTime;
    }

    @NonNull
    public WeekMode getWeekMode() {
        return weekMode;
    }

    public void setWeekMode(@NonNull WeekMode weekMode) {
        this.weekMode = weekMode;
    }

    @NonNull
    public List<TimePeriod> getWeekNumList() {
        return weekNumList;
    }

    public void setWeekNumList(@NonNull List<TimePeriod> weekNumList) {
        this.weekNumList = weekNumList;
    }

    @Nullable
    public String getLocation() {
        return location;
    }

    public void setLocation(@Nullable String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseTime that = (CourseTime) o;
        return calWeekDay == that.calWeekDay &&
                courseTime.equals(that.courseTime) &&
                weekMode == that.weekMode &&
                weekNumList.equals(that.weekNumList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calWeekDay, courseTime, weekMode, weekNumList);
    }

    @NonNull
    @Override
    public String toString() {
        return "CourseDetail{" +
                "timeId=" + timeId +
                ", courseId=" + courseId +
                ", weekDay=" + calWeekDay +
                ", courseTime=" + courseTime +
                ", weekMode=" + weekMode +
                ", weekNumList=" + weekNumList +
                ", location='" + location + '\'' +
                '}';
    }
}
