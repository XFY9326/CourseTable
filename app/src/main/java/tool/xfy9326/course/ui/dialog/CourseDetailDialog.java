package tool.xfy9326.course.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import tool.xfy9326.course.R;
import tool.xfy9326.course.bean.Course;
import tool.xfy9326.course.bean.CourseTime;
import tool.xfy9326.course.bean.TimePeriod;
import tool.xfy9326.course.bean.WeekMode;
import tool.xfy9326.course.databinding.DialogCourseDetailBinding;
import tool.xfy9326.course.databinding.ViewCourseDetailInfoBinding;
import tool.xfy9326.course.databinding.ViewDividerBinding;
import tool.xfy9326.course.utils.ColorUtils;
import tool.xfy9326.course.utils.I18NUtils;
import tool.xfy9326.course.utils.TimeUtils;

public class CourseDetailDialog extends DialogFragment {
    private static final String EXTRA_COURSE = "EXTRA_COURSE";
    private static final float CONTENT_WIDTH_PERCENT = 0.8f;
    private Course course;
    private boolean isMoreInfoExpanded = false;
    private DialogCourseDetailBinding binding;

    public static void showDialog(@NonNull FragmentManager fragmentManager, @NonNull Course course) {
        CourseDetailDialog dialog = new CourseDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_COURSE, course);
        dialog.setArguments(bundle);
        dialog.show(fragmentManager, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        course = (Course) requireArguments().getSerializable(EXTRA_COURSE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = requireDialog().getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }

        binding = DialogCourseDetailBinding.inflate(inflater, container, false);
        binding.layoutCourseTitle.setBackgroundColor(course.getCourseStyle().getCellColor());
        binding.tvCourseName.setText(course.getName());
        if (ColorUtils.isLightColor(course.getCourseStyle().getCellColor())) {
            binding.tvCourseName.setTextColor(ContextCompat.getColor(requireContext(), R.color.course_cell_text_dark));
        } else {
            binding.tvCourseName.setTextColor(ContextCompat.getColor(requireContext(), R.color.course_cell_text_light));
        }

        binding.tvClass.setText(getString(R.string.course_class, course.getCourseDetail().getCourseClass()));
        binding.tvTeacher.setText(getString(R.string.course_teacher, course.getCourseDetail().getTeacher()));

        binding.btnLoadMoreCourseInfo.setOnClickListener(v -> {
            if (isMoreInfoExpanded) {
                isMoreInfoExpanded = false;
                binding.btnLoadMoreCourseInfo.setImageResource(R.drawable.ic_load_more);
                loadLess();
            } else {
                isMoreInfoExpanded = true;
                binding.btnLoadMoreCourseInfo.setImageResource(R.drawable.ic_load_less);
                loadMore();
            }
        });

        return binding.getRoot();
    }

    private void loadMore() {
        List<CourseTime> courseTimeList = course.getCourseTimeList();
        for (int i = 0; i < courseTimeList.size(); i++) {
            CourseTime courseTime = courseTimeList.get(i);

            ViewCourseDetailInfoBinding childBinding = ViewCourseDetailInfoBinding.inflate(getLayoutInflater(), binding.layoutMoreCourseInfo, false);
            childBinding.tvCourseLocation.setText(getString(R.string.course_location, courseTime.getLocation()));

            int timeStringResId;
            if (courseTime.getWeekMode() == WeekMode.ODD_WEEK_ONLY) {
                timeStringResId = R.string.course_odd_week_time;
            } else if (courseTime.getWeekMode() == WeekMode.EVEN_WEEK_ONLY) {
                timeStringResId = R.string.course_even_week_time;
            } else {
                timeStringResId = R.string.course_time;
            }

            ArrayList<TimePeriod> temp = new ArrayList<>();
            temp.add(courseTime.getCourseTime());
            childBinding.tvCourseTime.setText(getString(timeStringResId, TimeUtils.timePeriodListToStr(courseTime.getWeekNumList()), I18NUtils.getWeekDayShowText(requireContext(), courseTime.getCalWeekDay()), TimeUtils.timePeriodListToStr(temp)));

            binding.layoutMoreCourseInfo.addView(childBinding.getRoot());
            if (i != courseTimeList.size() - 1) {
                ViewDividerBinding dividerBinding = ViewDividerBinding.inflate(getLayoutInflater(), binding.layoutMoreCourseInfo, false);
                binding.layoutMoreCourseInfo.addView(dividerBinding.getRoot());
            }
        }
        binding.layoutMoreCourseInfo.setVisibility(View.VISIBLE);
    }

    private void loadLess() {
        binding.layoutMoreCourseInfo.setVisibility(View.GONE);
        binding.layoutMoreCourseInfo.removeAllViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = requireDialog().getWindow();
        if (window != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * CONTENT_WIDTH_PERCENT);
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
