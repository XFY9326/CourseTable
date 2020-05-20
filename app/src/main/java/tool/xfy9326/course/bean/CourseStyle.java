package tool.xfy9326.course.bean;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(foreignKeys = @ForeignKey(entity = Course.class, parentColumns = Course.COLUMN_COURSE_ID, childColumns = Course.COLUMN_COURSE_ID, onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class CourseStyle implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @ColumnInfo(name = Course.COLUMN_COURSE_ID)
    private long courseId;

    @ColorInt
    private int cellColor;

    public CourseStyle(long courseId, @ColorInt int cellColor) {
        this.courseId = courseId;
        this.cellColor = cellColor;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public int getCellColor() {
        return cellColor;
    }

    public void setCellColor(@ColorInt int cellColor) {
        this.cellColor = cellColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseStyle that = (CourseStyle) o;
        return courseId == that.courseId &&
                cellColor == that.cellColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, cellColor);
    }

    @NonNull
    @Override
    public String toString() {
        return "CourseStyle{" +
                "courseId=" + courseId +
                ", cellColor=" + cellColor +
                '}';
    }
}
