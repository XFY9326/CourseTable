package tool.xfy9326.course.ui.adapter;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import tool.xfy9326.course.bean.SchedulerTable;
import tool.xfy9326.course.ui.fragment.TableFragment;

public class CourseTableViewPagerAdapter extends FragmentStateAdapter {
    private SchedulerTable currentSchedulerTable;

    public CourseTableViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, @NonNull SchedulerTable schedulerTable) {
        super(fragmentActivity);
        this.currentSchedulerTable = schedulerTable;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateSchedulerTable(@NonNull SchedulerTable schedulerTable) {
        if (!schedulerTable.equals(currentSchedulerTable)) {
            this.currentSchedulerTable = schedulerTable;
            notifyDataSetChanged();
        }
    }

    @NonNull
    public SchedulerTable getCurrentSchedulerTable() {
        return currentSchedulerTable;
    }

    @Override
    public int getItemCount() {
        return currentSchedulerTable.getMaxWeekNum();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        TableFragment tableFragment = new TableFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TableFragment.EXTRA_WEEK_NUM, position + 1);
        tableFragment.setArguments(bundle);
        return tableFragment;
    }
}
