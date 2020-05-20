package tool.xfy9326.course.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import tool.xfy9326.course.bean.Course;
import tool.xfy9326.course.bean.CourseStyle;
import tool.xfy9326.course.bean.CourseTime;
import tool.xfy9326.course.bean.SchedulerTable;
import tool.xfy9326.course.db.base.DBProvider;
import tool.xfy9326.course.db.base.DBTypeConverter;
import tool.xfy9326.course.db.dao.CourseDao;

public class CourseDBProvider extends DBProvider<CourseDBProvider.DB> {
    private static final String COURSE_DB_NAME = "Course.db";
    private static final int COURSE_DB_VERSION = 1;
    private static CourseDBProvider instance;

    private CourseDBProvider() {
        super(COURSE_DB_NAME);
    }

    @NonNull
    private synchronized static CourseDBProvider getInstance() {
        if (instance == null) {
            instance = new CourseDBProvider();
        }
        return instance;
    }

    @NonNull
    public static CourseDao getCourseDao() {
        return getInstance().getDB().getCourseDao();
    }

    @TypeConverters(DBTypeConverter.class)
    @Database(entities = {SchedulerTable.class, Course.class, CourseTime.class, CourseStyle.class}, version = COURSE_DB_VERSION)
    static abstract class DB extends RoomDatabase {
        public abstract CourseDao getCourseDao();
    }
}
