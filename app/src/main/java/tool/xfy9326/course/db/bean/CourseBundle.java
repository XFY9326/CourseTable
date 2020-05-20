package tool.xfy9326.course.db.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

import tool.xfy9326.course.bean.Course;
import tool.xfy9326.course.bean.CourseStyle;
import tool.xfy9326.course.bean.CourseTime;

public class CourseBundle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Embedded
    @NonNull
    private Course course;

    @Relation(parentColumn = Course.COLUMN_COURSE_ID, entityColumn = Course.COLUMN_COURSE_ID)
    @NonNull
    private List<CourseTime> courseTimeList;

    @Relation(parentColumn = Course.COLUMN_COURSE_ID, entityColumn = Course.COLUMN_COURSE_ID)
    @Nullable
    private CourseStyle courseStyle;

    public CourseBundle(@NonNull Course course, @NonNull List<CourseTime> courseTimeList, @Nullable CourseStyle courseStyle) {
        this.course = course;
        this.courseTimeList = courseTimeList;
        this.courseStyle = courseStyle;
    }

    @Nullable
    public CourseStyle getCourseStyle() {
        return courseStyle;
    }

    @NonNull
    public Course getCourse() {
        return course;
    }

    @NonNull
    public List<CourseTime> getCourseTimeList() {
        return courseTimeList;
    }
}
