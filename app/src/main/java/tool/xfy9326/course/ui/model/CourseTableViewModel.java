package tool.xfy9326.course.ui.model;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.List;

import tool.xfy9326.course.Const;
import tool.xfy9326.course.bean.Course;
import tool.xfy9326.course.bean.CourseBuildBundle;
import tool.xfy9326.course.bean.SchedulerTable;
import tool.xfy9326.course.bean.ShowWeekNum;
import tool.xfy9326.course.bean.WeekNum;
import tool.xfy9326.course.db.CourseDBProvider;
import tool.xfy9326.course.tool.AppPref;
import tool.xfy9326.course.tool.ThreadScheduler;
import tool.xfy9326.course.ui.base.SimpleViewModel;
import tool.xfy9326.course.utils.TimeUtils;

public class CourseTableViewModel extends SimpleViewModel {
    public final MutableLiveData<SchedulerTable> currentSchedulerTable = new MutableLiveData<>();
    public final MutableLiveData<HashMap<SchedulerTable, Boolean>> schedulerTables = new MutableLiveData<>();
    public final MutableLiveData<WeekNum> nowWeekNum = new MutableLiveData<>();
    public final MutableLiveData<ShowWeekNum> nowShowWeekNum = new MutableLiveData<>();
    public final MediatorLiveData<CourseBuildBundle> coursesLiveData = new MediatorLiveData<>();

    private final Object nowWeekNumInit = new Object();

    private LiveData<List<Course>> courseSource;

    @Override
    protected void onInit() {
        getThreadScheduler().post(ThreadScheduler.ExecutorType.DEFAULT, () -> {
            SchedulerTable schedulerTable = getCurrentSchedulerTable(getSchedulerTables());
            currentSchedulerTable.postValue(schedulerTable);
            bindCourseLiveData(schedulerTable);

            nowWeekNum.postValue(TimeUtils.getCurrentWeekNum(schedulerTable.getTermStartDate(), schedulerTable.getMaxWeekNum()));
            synchronized (nowWeekNumInit) {
                nowWeekNumInit.notifyAll();
            }
        });
    }

    @Override
    protected void onHostCreated(boolean initCreate) {
        getThreadScheduler().post(ThreadScheduler.ExecutorType.DEFAULT, () -> {
            if (!initCreate) {
                SchedulerTable schedulerTable = currentSchedulerTable.getValue();
                if (schedulerTable != null) {
                    WeekNum weekNum = TimeUtils.getCurrentWeekNum(schedulerTable.getTermStartDate(), schedulerTable.getMaxWeekNum());
                    weekNum.setUpdateOnly(true);
                    nowWeekNum.postValue(weekNum);
                }
            }
        });
    }

    @NonNull
    private synchronized SchedulerTable getCurrentSchedulerTable(@NonNull List<SchedulerTable> tableList) {
        long currentTableId = AppPref.use(Const.PREF_NAME_COURSE).getLong(Const.PREF_CURRENT_SCHEDULER_TABLE_ID, Const.DEFAULT_CURRENT_SCHEDULER_TABLE_ID);
        if (currentTableId != Const.DEFAULT_CURRENT_SCHEDULER_TABLE_ID) {
            for (SchedulerTable table : tableList) {
                if (table.getTableId() == currentTableId) return table;
            }
        }
        if (tableList.isEmpty()) {
            throw new IllegalStateException("Empty Table List!");
        } else {
            AppPref.use(Const.PREF_NAME_COURSE).putLong(Const.PREF_CURRENT_SCHEDULER_TABLE_ID, tableList.get(0).getTableId());
            return tableList.get(0);
        }
    }

    @WorkerThread
    @NonNull
    private List<SchedulerTable> getSchedulerTables() {
        return CourseDBProvider.getCourseDao().getSchedulerTables();
    }

    public void requireShowWeekNumChanged(int newWeekNum) {
        getThreadScheduler().post(ThreadScheduler.ExecutorType.DEFAULT, () -> {
            while (true) {
                WeekNum weekNum = nowWeekNum.getValue();
                if (weekNum != null) {
                    nowShowWeekNum.postValue(new ShowWeekNum(newWeekNum, weekNum));
                    break;
                } else {
                    synchronized (nowWeekNumInit) {
                        try {
                            nowWeekNumInit.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }
        });
    }

    public void requireSchedulerTables() {
        getThreadScheduler().post(ThreadScheduler.ExecutorType.DEFAULT, () -> {
            List<SchedulerTable> tableList = getSchedulerTables();
            SchedulerTable schedulerTable = getCurrentSchedulerTable(tableList);
            HashMap<SchedulerTable, Boolean> result = new HashMap<>();
            for (SchedulerTable table : tableList) {
                if (table == schedulerTable) {
                    result.put(table, true);
                } else {
                    result.put(table, false);
                }
            }
            schedulerTables.postValue(result);
        });
    }

    public synchronized void setNewSchedulerTable(@NonNull SchedulerTable schedulerTable) {
        currentSchedulerTable.postValue(schedulerTable);
        if (courseSource != null) {
            coursesLiveData.removeSource(courseSource);
            courseSource = null;
        }
        AppPref.use(Const.PREF_NAME_COURSE).putLong(Const.PREF_CURRENT_SCHEDULER_TABLE_ID, schedulerTable.getTableId());
        bindCourseLiveData(schedulerTable);
    }

    private void bindCourseLiveData(@NonNull final SchedulerTable schedulerTable) {
        courseSource = CourseDBProvider.getCourseDao().readCoursesAsync(schedulerTable.getTableId());
        coursesLiveData.addSource(courseSource, courses -> coursesLiveData.setValue(new CourseBuildBundle(courses, schedulerTable)));
    }
}
