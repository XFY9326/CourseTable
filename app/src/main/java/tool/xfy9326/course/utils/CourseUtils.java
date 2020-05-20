package tool.xfy9326.course.utils;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tool.xfy9326.course.Const;
import tool.xfy9326.course.bean.Course;
import tool.xfy9326.course.bean.CourseCell;
import tool.xfy9326.course.bean.CourseTable;
import tool.xfy9326.course.bean.CourseTime;
import tool.xfy9326.course.bean.SchedulerTable;
import tool.xfy9326.course.bean.TimePeriod;
import tool.xfy9326.course.bean.WeekMode;

public class CourseUtils {
    @NonNull
    public static CourseTable getCourseTableByWeekNum(@NonNull SchedulerTable schedulerTable, @NonNull List<Course> courses, @IntRange(from = 1) int weekNum) {
        if (weekNum < 0 || weekNum > schedulerTable.getMaxWeekNum()) {
            throw new IllegalArgumentException("WeekNum error! (>0 && <" + schedulerTable.getMaxWeekNum() + ") WeekNum: " + weekNum);
        }
        if (courses.isEmpty()) {
            return new CourseTable(schedulerTable, weekNum, new CourseCell[Const.MAX_WEEK_DAY][0]);
        }

        boolean overWrite;
        boolean thisWeekCourse;
        CourseCell temp;
        int weekDayNum;
        int ignoreWeekDayNum;
        if (weekNum == 1) {
            ignoreWeekDayNum = TimeUtils.getWeekDayNum(schedulerTable.getTermStartDate()) - 1;
        } else {
            ignoreWeekDayNum = 0;
        }

        CourseCell[][] tempCourseCells = new CourseCell[Const.MAX_WEEK_DAY][schedulerTable.getMaxCoursePerDay()];
        for (Course course : courses) {
            for (CourseTime time : course.getCourseTimeList()) {
                weekDayNum = TimeUtils.getWeekDayNum(time.getCalWeekDay());

                thisWeekCourse = weekDayNum > ignoreWeekDayNum && isThisWeekCourse(time, weekNum);
                temp = tempCourseCells[weekDayNum - 1][time.getCourseTime().getStart() - 1];

                if (temp != null && thisWeekCourse) {
                    overWrite = false;
                } else {
                    overWrite = thisWeekCourse || temp == null || shouldOverwriteCourseTime(temp.getCourseTime(), time);
                }

                if (overWrite) {
                    tempCourseCells[weekDayNum - 1][time.getCourseTime().getStart() - 1] = new CourseCell(course.getCourseId(), course.getName(), time, thisWeekCourse, weekNum, course.getCourseStyle());
                }
            }
        }

        CourseCell[][] resultCourseCells = shrinkCourseCellTable(schedulerTable.getMaxCoursePerDay() / 2, tempCourseCells);
        return new CourseTable(schedulerTable, weekNum, resultCourseCells);
    }

    public static boolean hasWeekendCourse(CourseTable courseTable) {
        return courseTable.getCourseCells()[TimeUtils.getWeekDayNum(Calendar.SUNDAY) - 1].length > 0 || courseTable.getCourseCells()[TimeUtils.getWeekDayNum(Calendar.SATURDAY) - 1].length > 0;
    }

    private static CourseCell[][] shrinkCourseCellTable(int initDayCellSize, CourseCell[][] courseCells) {
        ArrayList<CourseCell> dayCells = new ArrayList<>(initDayCellSize);
        CourseCell[][] resultCourseCells = new CourseCell[Const.MAX_WEEK_DAY][];
        for (int i = 0; i < courseCells.length; i++) {
            if (courseCells[i].length > 0) {
                for (CourseCell cell : courseCells[i]) {
                    if (cell != null) {
                        dayCells.add(cell);
                    }
                }
                resultCourseCells[i] = dayCells.toArray(new CourseCell[0]);
                dayCells.clear();
            } else {
                resultCourseCells[i] = new CourseCell[0];
            }
        }

        return resultCourseCells;
    }

    private static boolean shouldOverwriteCourseTime(@NonNull CourseTime old, @NonNull CourseTime overwrite) {
        if (!old.getWeekNumList().isEmpty() && !overwrite.getWeekNumList().isEmpty() && overwrite.getWeekNumList().get(0).getStart() > old.getWeekNumList().get(0).getStart()) {
            return true;
        } else if (TimeUtils.getWeekDayNum(overwrite.getCalWeekDay()) > TimeUtils.getWeekDayNum(old.getCalWeekDay())) {
            return true;
        } else if (overwrite.getCourseTime().getStart() > old.getCourseTime().getStart()) {
            return true;
        } else return overwrite.getWeekMode().ordinal() > old.getWeekMode().ordinal();
    }

    private static boolean isThisWeekCourse(@NonNull CourseTime time, int weekNum) {
        for (TimePeriod period : time.getWeekNumList()) {
            if (weekNum >= period.getStart() && weekNum <= period.getEnd()) {
                if (time.getWeekMode() == WeekMode.ANY_WEEKS) {
                    return true;
                } else if (time.getWeekMode() == WeekMode.ODD_WEEK_ONLY) {
                    return weekNum % 2 != 0;
                } else if (time.getWeekMode() == WeekMode.EVEN_WEEK_ONLY) {
                    return weekNum % 2 == 0;
                }
            }
        }
        return false;
    }
}
