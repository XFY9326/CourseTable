package tool.xfy9326.course.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import tool.xfy9326.course.Const;
import tool.xfy9326.course.utils.CourseStyleUtils;

@Entity(foreignKeys = @ForeignKey(entity = SchedulerTable.class, parentColumns = SchedulerTable.COLUMN_TABLE_ID, childColumns = SchedulerTable.COLUMN_TABLE_ID, onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE))
public class Course implements Serializable {
    public static final String COLUMN_COURSE_ID = "courseId";
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_COURSE_ID)
    private long courseId;

    @ColumnInfo(name = SchedulerTable.COLUMN_TABLE_ID, index = true)
    private long tableId;

    @ColumnInfo(index = true)
    @NonNull
    private String name;

    @Embedded
    @NonNull
    private CourseDetail courseDetail;

    @Ignore
    private List<CourseTime> courseTimeList = Collections.emptyList();

    @Nullable
    @Ignore
    private CourseStyle courseStyle;

    public Course(long tableId, @NonNull String name, @NonNull CourseDetail courseDetail) {
        this.courseId = Const.DEFAULT_DB_ID;
        this.tableId = tableId;
        this.name = name;
        this.courseDetail = courseDetail;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public List<CourseTime> getCourseTimeList() {
        return courseTimeList;
    }

    public void setCourseTimeList(List<CourseTime> courseTimeList) {
        this.courseTimeList = courseTimeList;
    }

    @NonNull
    public CourseDetail getCourseDetail() {
        return courseDetail;
    }

    public void setCourseDetail(@NonNull CourseDetail courseDetail) {
        this.courseDetail = courseDetail;
    }

    @NonNull
    public CourseStyle getCourseStyle() {
        if (courseStyle == null) {
            courseStyle = CourseStyleUtils.getNewCourseStyle(courseId);
        }
        return courseStyle;
    }

    public void setCourseStyle(@Nullable CourseStyle courseStyle) {
        this.courseStyle = courseStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return name.equals(course.name) &&
                courseDetail.equals(course.courseDetail) &&
                Objects.equals(courseTimeList, course.courseTimeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, courseDetail, courseTimeList);
    }

    @NonNull
    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", tableId=" + tableId +
                ", name='" + name + '\'' +
                ", courseDetail=" + courseDetail +
                ", courseTimeList=" + courseTimeList +
                ", courseStyle=" + courseStyle +
                '}';
    }
}
