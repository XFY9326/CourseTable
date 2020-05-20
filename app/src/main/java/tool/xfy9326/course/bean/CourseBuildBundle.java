package tool.xfy9326.course.bean;

import androidx.annotation.NonNull;

import java.util.List;

public class CourseBuildBundle {
    @NonNull
    private List<Course> courses;
    @NonNull
    private SchedulerTable schedulerTable;

    public CourseBuildBundle(@NonNull List<Course> courses, @NonNull SchedulerTable schedulerTable) {
        this.courses = courses;
        this.schedulerTable = schedulerTable;
    }

    @NonNull
    public List<Course> getCourses() {
        return courses;
    }

    @NonNull
    public SchedulerTable getSchedulerTable() {
        return schedulerTable;
    }
}
