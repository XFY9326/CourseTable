package tool.xfy9326.course.ui.fragment;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import tool.xfy9326.course.R;
import tool.xfy9326.course.bean.CourseTable;
import tool.xfy9326.course.bean.WeekDay;
import tool.xfy9326.course.databinding.LayoutTableBinding;
import tool.xfy9326.course.tool.ThreadScheduler;
import tool.xfy9326.course.ui.model.CourseTableViewModel;
import tool.xfy9326.course.ui.view.CourseTableHeaderView;
import tool.xfy9326.course.ui.view.CourseTableView;
import tool.xfy9326.course.ui.view.OnCourseCellClickListener;
import tool.xfy9326.course.utils.CourseUtils;
import tool.xfy9326.course.utils.TimeUtils;

public class TableFragment extends Fragment implements OnCourseCellClickListener {
    public static final String EXTRA_WEEK_NUM = "EXTRA_WEEK_NUM";

    private final ThreadScheduler threadScheduler = new ThreadScheduler(getLifecycle());
    private int weekNum;
    private CourseTableViewModel courseTableViewModel;
    private LayoutTableBinding layoutTableBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weekNum = requireArguments().getInt(EXTRA_WEEK_NUM);
        courseTableViewModel = new ViewModelProvider(requireActivity()).get(CourseTableViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getView();
        if (view == null) {
            layoutTableBinding = LayoutTableBinding.inflate(inflater, container, false);
        } else {
            ViewGroup group = (ViewGroup) view.getParent();
            if (group != null) {
                group.removeView(view);
            }
            layoutTableBinding = LayoutTableBinding.bind(view);
        }
        return layoutTableBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        applyLayoutTransition(layoutTableBinding.layoutEmptyCourseTableHeader);
        applyLayoutTransition(layoutTableBinding.layoutEmptyCourseTable);
        courseTableViewModel.coursesLiveData.observe(getViewLifecycleOwner(), courseBuildBundle -> threadScheduler.post(ThreadScheduler.ExecutorType.DEFAULT, () -> {
            CourseTable table = CourseUtils.getCourseTableByWeekNum(courseBuildBundle.getSchedulerTable(), courseBuildBundle.getCourses(), weekNum);
            buildCourseTableView(table);
        }));
    }

    private synchronized void buildCourseTableView(CourseTable courseTable) {
        boolean showWeekendCourses = CourseUtils.hasWeekendCourse(courseTable);

        WeekDay[] weekDays = TimeUtils.getWeekDayByWeekNum(courseTable.getSchedulerTable().getTermStartDate(), weekNum, showWeekendCourses);
        CourseTableHeaderView.Style headerStyle = new CourseTableHeaderView.Style(true);
        if (layoutTableBinding.layoutEmptyCourseTableHeader.getChildCount() > 0) {
            CourseTableHeaderView headerView = (CourseTableHeaderView) layoutTableBinding.layoutEmptyCourseTableHeader.getChildAt(0);
            threadScheduler.post(ThreadScheduler.ExecutorType.MAIN, () -> headerView.setHeaderData(weekDays, headerStyle));
        } else {
            CourseTableHeaderView headerView = new CourseTableHeaderView(requireContext(), weekDays, headerStyle);
            threadScheduler.post(ThreadScheduler.ExecutorType.MAIN, () -> replaceAllView(layoutTableBinding.layoutEmptyCourseTableHeader, headerView));
        }

        CourseTableView.Style tableStyle = new CourseTableView.Style(showWeekendCourses);
        if (layoutTableBinding.layoutEmptyCourseTable.getChildCount() > 0) {
            CourseTableView tableView = (CourseTableView) layoutTableBinding.layoutEmptyCourseTable.getChildAt(0);
            threadScheduler.post(ThreadScheduler.ExecutorType.MAIN, () -> tableView.setCourseTable(courseTable, tableStyle));
        } else {
            CourseTableView courseTableView = new CourseTableView(requireContext(), courseTable, tableStyle);
            courseTableView.setCellClickListener(this);
            threadScheduler.post(ThreadScheduler.ExecutorType.MAIN, () -> replaceAllView(layoutTableBinding.layoutEmptyCourseTable, courseTableView));
        }
    }

    private void applyLayoutTransition(ViewGroup container) {
        LayoutTransition transition = container.getLayoutTransition();
        if (transition != null) {
            transition.setAnimateParentHierarchy(false);
            transition.setDuration(getResources().getInteger(R.integer.course_table_layout_transition_animate));
        }
    }

    private void replaceAllView(ViewGroup container, View view) {
        if (container.getChildCount() > 0) {
            container.removeAllViews();
        }
        container.addView(view);
    }

    @Override
    public void onCourseCellClick(long courseId) {
        courseTableViewModel.showCourseDetailDialog(courseId);
    }
}
