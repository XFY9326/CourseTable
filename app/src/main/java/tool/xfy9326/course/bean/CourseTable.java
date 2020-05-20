package tool.xfy9326.course.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class CourseTable implements Serializable {
    private static final long serialVersionUID = 1L;
    @NonNull
    private SchedulerTable schedulerTable;
    private int weekNum;
    @NonNull
    private CourseCell[][] courseCells;

    public CourseTable(@NonNull SchedulerTable schedulerTable, int weekNum, @NonNull CourseCell[][] courseCells) {
        this.schedulerTable = schedulerTable;
        this.weekNum = weekNum;
        this.courseCells = courseCells;
    }

    @NonNull
    public SchedulerTable getSchedulerTable() {
        return schedulerTable;
    }

    public int getWeekNum() {
        return weekNum;
    }

    @NonNull
    public CourseCell[][] getCourseCells() {
        return courseCells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseTable table = (CourseTable) o;
        return weekNum == table.weekNum &&
                schedulerTable.equals(table.schedulerTable) &&
                Arrays.equals(courseCells, table.courseCells);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(schedulerTable, weekNum);
        result = 31 * result + Arrays.hashCode(courseCells);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "CourseTable{" +
                "schedulerTable=" + schedulerTable +
                ", weekNum=" + weekNum +
                ", courseCells=" + Arrays.deepToString(courseCells) +
                '}';
    }
}
