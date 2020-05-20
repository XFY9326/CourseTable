package tool.xfy9326.course.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class CourseDetail {
    private static final long serialVersionUID = 1L;

    @Nullable
    private String teacher;

    @Nullable
    private String courseClass;

    public CourseDetail(@Nullable String teacher, @Nullable String courseClass) {
        this.teacher = teacher;
        this.courseClass = courseClass;
    }

    @Nullable
    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(@Nullable String teacher) {
        this.teacher = teacher;
    }

    @Nullable
    public String getCourseClass() {
        return courseClass;
    }

    public void setCourseClass(@Nullable String courseClass) {
        this.courseClass = courseClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDetail that = (CourseDetail) o;
        return Objects.equals(teacher, that.teacher) &&
                Objects.equals(courseClass, that.courseClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacher, courseClass);
    }

    @NonNull
    @Override
    public String toString() {
        return "CourseDetail{" +
                "teacher='" + teacher + '\'' +
                ", courseClass='" + courseClass + '\'' +
                '}';
    }
}
