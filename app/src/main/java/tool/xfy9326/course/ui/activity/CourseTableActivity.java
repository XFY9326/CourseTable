package tool.xfy9326.course.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import tool.xfy9326.course.R;
import tool.xfy9326.course.bean.WeekDay;
import tool.xfy9326.course.bean.WeekNum;
import tool.xfy9326.course.databinding.LayoutCourseTableBinding;
import tool.xfy9326.course.ui.adapter.CourseTableViewPagerAdapter;
import tool.xfy9326.course.ui.base.ViewModelActivity;
import tool.xfy9326.course.ui.dialog.CourseDetailDialog;
import tool.xfy9326.course.ui.model.CourseTableViewModel;
import tool.xfy9326.course.utils.DialogUtils;
import tool.xfy9326.course.utils.I18NUtils;
import tool.xfy9326.course.utils.TimeUtils;

public class CourseTableActivity extends ViewModelActivity<CourseTableViewModel> implements DialogUtils.OnWeekNumChangedListener {
    private LayoutCourseTableBinding viewBinding;
    private CourseTableViewPagerAdapter tableViewPagerAdapter;
    private CourseTableViewPagerCallback viewPagerCallback;

    @Override
    protected void onSetContentView() {
        viewBinding = LayoutCourseTableBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        setSupportActionBar(viewBinding.toolBarCourseTable);
    }

    @Override
    protected void onBindObserver(@NonNull CourseTableViewModel vm) {
        vm.currentSchedulerTable.observe(this, schedulerTable -> {
            if (tableViewPagerAdapter == null) {
                tableViewPagerAdapter = new CourseTableViewPagerAdapter(this, schedulerTable);
                viewBinding.viewPagerCourseTable.setAdapter(tableViewPagerAdapter);
            } else {
                tableViewPagerAdapter.updateSchedulerTable(schedulerTable);
            }
        });
        vm.nowWeekNum.observe(this, weekNum -> {
            if (weekNum.isInVacation()) {
                viewBinding.textViewNowShowWeekNum.setText(R.string.in_vacation);
                viewBinding.textViewWeekInfo.setVisibility(View.GONE);
            } else {
                if (!weekNum.isUpdateOnly()) {
                    viewBinding.textViewNowShowWeekNum.setText(getString(R.string.week_num_text, weekNum.getWeekNum()));
                    viewBinding.textViewWeekInfo.setVisibility(View.VISIBLE);
                    viewBinding.textViewWeekInfo.setText(I18NUtils.getWeekDayFullShowText(this, weekNum.getTodayCalWeekDay()));
                    viewBinding.viewPagerCourseTable.setCurrentItem(weekNum.getWeekNum() - 1, false);
                }
            }
        });
        vm.nowShowWeekNum.observe(this, showWeekNum -> {
            if (!showWeekNum.isInVacation()) {
                viewBinding.textViewNowShowWeekNum.setText(getString(R.string.week_num_text, showWeekNum.getWeekNum()));
                viewBinding.textViewWeekInfo.setVisibility(View.VISIBLE);
                if (showWeekNum.isCurrentWeek()) {
                    viewBinding.textViewWeekInfo.setText(I18NUtils.getWeekDayFullShowText(this, showWeekNum.getCurrentWeek().getTodayCalWeekDay()));
                } else {
                    viewBinding.textViewWeekInfo.setText(R.string.not_current_week);
                }
            }
        });
        vm.showCourseDetail.observe(this, course -> CourseDetailDialog.showDialog(getSupportFragmentManager(), course));
    }

    @Override
    protected void onBuildContentView(@NonNull CourseTableViewModel vm, @Nullable Bundle savedInstanceState) {
        viewBinding.viewPagerCourseTable.setOffscreenPageLimit(1);
        viewPagerCallback = new CourseTableViewPagerCallback();
        viewBinding.viewPagerCourseTable.registerOnPageChangeCallback(viewPagerCallback);

        WeekDay weekDay = TimeUtils.getTodayDate();
        viewBinding.textViewTodayDate.setText(I18NUtils.getMonthDayShowText(this, weekDay.getMonth(), weekDay.getDay()));

        viewBinding.layoutDateInfoBar.setOnClickListener(v -> {
            WeekNum weekNum = vm.nowWeekNum.getValue();
            if (weekNum != null) {
                viewBinding.viewPagerCourseTable.setCurrentItem(weekNum.getWeekNum() - 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_table, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tableControl:
                WeekNum weekNum = requireViewModel().nowWeekNum.getValue();
                if (weekNum != null && tableViewPagerAdapter != null) {
                    DialogUtils.getCourseControlBottomDialog(this, getLifecycle(), weekNum.getWeekNum(), viewBinding.viewPagerCourseTable.getCurrentItem() + 1, tableViewPagerAdapter.getCurrentSchedulerTable().getMaxWeekNum(), this).show();
                }
                break;
            case R.id.menu_about:
                DialogUtils.getAboutDialog(this, getLifecycle()).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWeekNumChanged(int weekNum) {
        viewBinding.viewPagerCourseTable.setCurrentItem(weekNum - 1);
    }

    @Override
    protected void onDestroy() {
        if (viewPagerCallback != null) {
            viewBinding.viewPagerCourseTable.unregisterOnPageChangeCallback(viewPagerCallback);
        }
        super.onDestroy();
    }

    private class CourseTableViewPagerCallback extends ViewPager2.OnPageChangeCallback {
        @Override
        public void onPageSelected(int position) {
            requireViewModel().requireShowWeekNumChanged(position + 1);
        }
    }
}
