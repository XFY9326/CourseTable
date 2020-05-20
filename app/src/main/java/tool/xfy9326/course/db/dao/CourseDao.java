package tool.xfy9326.course.db.dao;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.List;

import tool.xfy9326.course.R;
import tool.xfy9326.course.bean.Course;
import tool.xfy9326.course.bean.CourseStyle;
import tool.xfy9326.course.bean.CourseTime;
import tool.xfy9326.course.bean.SchedulerTable;
import tool.xfy9326.course.db.bean.CourseBundle;
import tool.xfy9326.course.utils.I18NUtils;

@Dao
public abstract class CourseDao {
    @WorkerThread
    @Transaction
    public void saveCourses(Course... courses) {
        for (Course course : courses) {
            long rowId = putCourse(course);
            CourseTime[] times = course.getCourseTimeList().toArray(new CourseTime[0]);
            for (CourseTime time : times) {
                time.setCourseId(rowId);
            }
            course.getCourseStyle().setCourseId(rowId);
            putCourseStyles(course.getCourseStyle());
            putCourseDetails(times);
        }
    }

    @NonNull
    public LiveData<List<Course>> readCoursesAsync(long tableId) {
        LiveData<List<CourseBundle>> listLiveData = getCoursesAsyncByTableId(tableId);
        return Transformations.map(listLiveData, list -> {
            ArrayList<Course> result = new ArrayList<>(list.size());
            for (CourseBundle bundle : list) {
                Course course = bundle.getCourse();
                course.setCourseTimeList(bundle.getCourseTimeList());
                course.setCourseStyle(bundle.getCourseStyle());
                result.add(course);
            }
            return result;
        });
    }

    @Transaction
    @Query("SELECT * FROM Course WHERE tableId=:tableId")
    abstract LiveData<List<CourseBundle>> getCoursesAsyncByTableId(long tableId);

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] putSchedulerTables(SchedulerTable... courseTables);

    @WorkerThread
    @Transaction
    public List<SchedulerTable> getSchedulerTables() {
        List<SchedulerTable> tableList = getRawSchedulerTables();
        if (tableList.isEmpty()) {
            SchedulerTable table = new SchedulerTable(I18NUtils.getAppString(R.string.default_str));
            long rowId = putSchedulerTables(table)[0];
            table.setTableId(rowId);
            tableList.add(table);
        }
        return tableList;
    }

    @WorkerThread
    @Query("SELECT * FROM SchedulerTable")
    abstract List<SchedulerTable> getRawSchedulerTables();

    @WorkerThread
    @Query("SELECT * FROM SchedulerTable WHERE tableId=:tableId")
    public abstract List<SchedulerTable> getSchedulerTableByTableId(long tableId);

    @WorkerThread
    @Delete
    public abstract void deleteSchedulerTable(SchedulerTable courseTable);

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long putCourse(Course course);

    @WorkerThread
    @Delete
    public abstract void deleteCourse(Course course);

    @WorkerThread
    @Query("DELETE FROM Course WHERE tableId=:tableId")
    public abstract void deleteCourseByTableId(long tableId);

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void putCourseDetails(CourseTime... courseDetails);

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void putCourseStyles(CourseStyle... courseStyles);

    @WorkerThread
    @Delete
    public abstract void deleteCourseDetail(CourseTime course);
}
