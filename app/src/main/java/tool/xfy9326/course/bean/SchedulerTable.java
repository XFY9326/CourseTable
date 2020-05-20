package tool.xfy9326.course.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import tool.xfy9326.course.Const;
import tool.xfy9326.course.utils.TimeUtils;

@Entity
public class SchedulerTable implements Serializable {
    static final String COLUMN_TABLE_ID = "tableId";
    private static final long serialVersionUID = 1L;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_TABLE_ID)
    private long tableId;

    @NonNull
    private String tableName;

    @NonNull
    private Date termStartDate = TimeUtils.getDefaultTermStartDate();

    private int maxWeekNum = Const.DEFAULT_MAX_WEEK_NUM;

    private int maxCoursePerDay = Const.DEFAULT_MAX_COURSE_PER_DAY;

    public SchedulerTable(@NonNull String tableName) {
        this.tableId = Const.DEFAULT_DB_ID;
        this.tableName = tableName;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    @NonNull
    public String getTableName() {
        return tableName;
    }

    public void setTableName(@NonNull String tableName) {
        this.tableName = tableName;
    }

    @NonNull
    public Date getTermStartDate() {
        return termStartDate;
    }

    public void setTermStartDate(@NonNull Date termStartDate) {
        this.termStartDate = termStartDate;
    }

    public int getMaxWeekNum() {
        return maxWeekNum;
    }

    public void setMaxWeekNum(int maxWeekNum) {
        this.maxWeekNum = maxWeekNum;
    }

    public int getMaxCoursePerDay() {
        return maxCoursePerDay;
    }

    public void setMaxCoursePerDay(int maxCoursePerDay) {
        this.maxCoursePerDay = maxCoursePerDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulerTable that = (SchedulerTable) o;
        return tableId == that.tableId &&
                maxWeekNum == that.maxWeekNum &&
                maxCoursePerDay == that.maxCoursePerDay &&
                tableName.equals(that.tableName) &&
                termStartDate.equals(that.termStartDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableId, tableName, termStartDate, maxWeekNum, maxCoursePerDay);
    }

    @NonNull
    @Override
    public String toString() {
        return "CourseTable{" +
                "tableId=" + tableId +
                ", tableName='" + tableName + '\'' +
                ", termStartDate=" + termStartDate +
                ", maxWeekNum=" + maxWeekNum +
                ", maxCoursePerDay=" + maxCoursePerDay +
                '}';
    }
}
