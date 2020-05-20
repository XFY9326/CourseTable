package tool.xfy9326.course.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

import tool.xfy9326.course.R;
import tool.xfy9326.course.bean.WeekDay;
import tool.xfy9326.course.utils.CourseStyleUtils;
import tool.xfy9326.course.utils.I18NUtils;

@SuppressLint("ViewConstructor")
public class CourseTableHeaderView extends LinearLayoutCompat {
    private WeekDay[] weekDays;

    public CourseTableHeaderView(@NonNull Context context, @NonNull WeekDay[] weekDays, @NonNull Style style) {
        super(context);
        initView();
        setHeaderData(weekDays, style);
    }

    @NonNull
    private static View buildMonthView(@NonNull Context context, int monthNum) {
        FrameLayout frameLayout = new FrameLayout(context);
        LinearLayoutCompat.LayoutParams parentLayoutParams = new LinearLayoutCompat.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.course_time_row_width), LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(parentLayoutParams);

        TextView monthText = new TextView(context);
        monthText.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams childLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        int margin = context.getResources().getDimensionPixelSize(R.dimen.course_cell_border);
        childLayoutParams.setMargins(margin, margin, margin, margin);
        monthText.setLayoutParams(childLayoutParams);
        monthText.setTextSize(context.getResources().getDimension(R.dimen.course_table_header_month_text_size));
        monthText.setText(context.getString(R.string.month, monthNum));

        frameLayout.addView(monthText);
        return frameLayout;
    }

    @NonNull
    private static View buildWeekDayView(@NonNull Context context, @NonNull WeekDay weekDay, @NonNull Style style) {
        LinearLayoutCompat layoutCompat = new LinearLayoutCompat(context);
        LinearLayoutCompat.LayoutParams parentLayoutParams = new LinearLayoutCompat.LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        int margin = context.getResources().getDimensionPixelSize(R.dimen.course_cell_border);
        parentLayoutParams.setMargins(margin, margin, margin, margin);
        layoutCompat.setLayoutParams(parentLayoutParams);
        layoutCompat.setGravity(Gravity.CENTER);
        layoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);

        TextView dateText = new TextView(context);
        dateText.setTextSize(context.getResources().getDimension(R.dimen.course_table_header_date_text_size));
        dateText.setText(String.valueOf(weekDay.getDay()));
        dateText.setGravity(Gravity.CENTER);
        LinearLayoutCompat.LayoutParams dateLayoutParams = new LinearLayoutCompat.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        dateText.setLayoutParams(dateLayoutParams);
        layoutCompat.addView(dateText);

        TextView weekDayText = new TextView(context);
        weekDayText.setTextSize(context.getResources().getDimension(R.dimen.course_table_header_week_day_text_size));
        weekDayText.setText(I18NUtils.getWeekDayShowText(context, weekDay.getCalWeekDay()));
        weekDayText.setGravity(Gravity.CENTER);
        LinearLayoutCompat.LayoutParams weekDayLayoutParams = new LinearLayoutCompat.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        weekDayLayoutParams.setMargins(0, context.getResources().getDimensionPixelSize(R.dimen.course_table_header_week_day_margin_top), 0, 0);
        weekDayText.setLayoutParams(weekDayLayoutParams);
        weekDayText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        layoutCompat.addView(weekDayText);

        if (weekDay.isToday() && style.highLightTodayText) {
            int backgroundRadius = context.getResources().getDimensionPixelSize(R.dimen.course_time_today_background_radius);
            int backgroundColor = ContextCompat.getColor(context, R.color.course_time_today_background_color);
            int textColor = ContextCompat.getColor(context, R.color.course_time_today_text_color);
            dateText.setTextColor(textColor);
            weekDayText.setTextColor(textColor);
            layoutCompat.setBackground(CourseStyleUtils.getCellBackgroundDrawable(backgroundColor, backgroundRadius));
        }

        return layoutCompat;
    }

    private void initView() {
        setGravity(Gravity.CENTER_VERTICAL);
        setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.course_table_header_min_height));
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, getContext().getResources().getDimensionPixelSize(R.dimen.course_table_header_bottom_margin));
        setLayoutParams(layoutParams);
    }

    public void setHeaderData(@NonNull WeekDay[] weekDays, @NonNull Style style) {
        if (!Arrays.equals(weekDays, this.weekDays)) {
            setHeaderDataInternal(weekDays, style);
        }
    }

    private synchronized void setHeaderDataInternal(@NonNull WeekDay[] weekDays, @NonNull Style style) {
        this.weekDays = weekDays;
        removeAllViewsInLayout();
        View monthView = buildMonthView(getContext(), weekDays[0].getMonth());
        addViewInLayout(monthView, 0, monthView.getLayoutParams(), true);
        for (int i = 0; i < weekDays.length; i++) {
            View weekDayView = buildWeekDayView(getContext(), weekDays[i], style);
            addViewInLayout(weekDayView, i + 1, weekDayView.getLayoutParams(), true);
        }
        invalidate();
        requestLayout();
    }

    public void setStyle(@NonNull Style style) {
        WeekDay[] weekDays = this.weekDays;
        if (weekDays != null) {
            setHeaderDataInternal(weekDays, style);
        }
    }

    public static class Style {
        private boolean highLightTodayText;

        public Style(boolean highLightTodayText) {
            this.highLightTodayText = highLightTodayText;
        }
    }
}
