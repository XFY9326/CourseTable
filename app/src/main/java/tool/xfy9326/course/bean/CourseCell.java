package tool.xfy9326.course.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class CourseCell implements Serializable {
    private static final long serialVersionUID = 1L;

    private long courseId;
    @NonNull
    private String courseName;
    @NonNull
    private CourseTime courseTime;
    private boolean thisWeekCourse;
    private int weekNum;
    @NonNull
    private CourseStyle courseStyle;

    public CourseCell(long courseId, @NonNull String courseName, @NonNull CourseTime courseTime, boolean thisWeekCourse, int weekNum, @NonNull CourseStyle courseStyle) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseTime = courseTime;
        this.thisWeekCourse = thisWeekCourse;
        this.weekNum = weekNum;
        this.courseStyle = courseStyle;
    }

    public long getCourseId() {
        return courseId;
    }

    @NonNull
    public String getCourseName() {
        return courseName;
    }

    @NonNull
    public CourseTime getCourseTime() {
        return courseTime;
    }

    public boolean isThisWeekCourse() {
        return thisWeekCourse;
    }

    public int getWeekNum() {
        return weekNum;
    }

    @NonNull
    public CourseStyle getCourseStyle() {
        return courseStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseCell that = (CourseCell) o;
        return courseId == that.courseId &&
                thisWeekCourse == that.thisWeekCourse &&
                weekNum == that.weekNum &&
                courseName.equals(that.courseName) &&
                courseTime.equals(that.courseTime) &&
                courseStyle.equals(that.courseStyle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, courseName, courseTime, thisWeekCourse, weekNum, courseStyle);
    }

    @NonNull
    @Override
    public String toString() {
        return "CourseCell{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", courseTime=" + courseTime +
                ", thisWeekCourse=" + thisWeekCourse +
                ", weekNum=" + weekNum +
                ", courseStyle=" + courseStyle +
                '}';
    }
}
