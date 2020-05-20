package tool.xfy9326.course.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import tool.xfy9326.course.R;
import tool.xfy9326.course.bean.CourseCell;
import tool.xfy9326.course.bean.CourseTimeCell;
import tool.xfy9326.course.utils.ColorUtils;
import tool.xfy9326.course.utils.CourseStyleUtils;
import tool.xfy9326.course.utils.TimeUtils;

@SuppressLint("ViewConstructor")
public class CourseCellView extends FrameLayout {
    private final int row;
    private final int col;
    private final int rowSize;

    private final boolean courseCellView;

    @Px
    private final int courseTimeRowWidth = getContext().getResources().getDimensionPixelSize(R.dimen.course_time_row_width);

    public CourseCellView(@NonNull Context context, @NonNull CourseTimeCell courseTimeCell) {
        super(context);
        this.courseCellView = false;
        this.row = courseTimeCell.getCourseNum() - 1;
        this.col = 0;
        this.rowSize = 1;
        initGeneralView();
        initView(courseTimeCell);
    }

    public CourseCellView(@NonNull Context context, @NonNull CourseCell courseCell) {
        super(context);
        this.courseCellView = true;
        this.row = courseCell.getCourseTime().getCourseTime().getStart() - 1;
        this.col = TimeUtils.getWeekDayNum(courseCell.getCourseTime().getCalWeekDay());
        this.rowSize = courseCell.getCourseTime().getCourseTime().getLength();
        initGeneralView();
        initView(courseCell);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    private void initGeneralView() {
        int padding = getResources().getDimensionPixelSize(R.dimen.course_cell_border);
        setPadding(padding, padding, padding, padding);
        setClickable(true);
        setFocusable(true);
    }

    private void initView(@NonNull CourseTimeCell courseTimeCell) {
        int verticalPadding = getResources().getDimensionPixelSize(R.dimen.course_table_time_vertical_padding);
        float numTextSize = getResources().getDimension(R.dimen.course_time_cell_num_text_size);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(row, 1f), GridLayout.spec(col));
        layoutParams.width = courseTimeRowWidth;
        setLayoutParams(layoutParams);

        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getContext());
        linearLayoutCompat.setGravity(Gravity.CENTER);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(0, verticalPadding, 0, verticalPadding);
        FrameLayout.LayoutParams innerLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        linearLayoutCompat.setLayoutParams(innerLayoutParams);

        TextView numText = new TextView(getContext());
        numText.setText(String.valueOf(courseTimeCell.getCourseNum()));
        numText.setGravity(Gravity.CENTER);
        numText.setTextSize(numTextSize);
        numText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        LinearLayoutCompat.LayoutParams textLayoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        numText.setLayoutParams(textLayoutParams);

        linearLayoutCompat.addView(numText);
        addView(linearLayoutCompat);
    }

    private void initView(@NonNull CourseCell courseCell) {
        float cellTextSize = getResources().getDimension(R.dimen.course_cell_text_size);
        int cellTextPadding = getResources().getDimensionPixelSize(R.dimen.course_cell_text_padding);
        int cellBackgroundRadius = getResources().getDimensionPixelSize(R.dimen.course_cell_background_radius);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(row, rowSize, 1f), GridLayout.spec(col));
        setLayoutParams(layoutParams);

        TextView cellText = new TextView(getContext());
        LinearLayoutCompat.LayoutParams textLayoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cellText.setLayoutParams(textLayoutParams);
        cellText.setTextSize(cellTextSize);
        cellText.setGravity(Gravity.TOP | Gravity.START);
        cellText.setBackground(CourseStyleUtils.getCellBackgroundDrawable(courseCell.getCourseStyle().getCellColor(), cellBackgroundRadius));
        cellText.setPadding(cellTextPadding, cellTextPadding, cellTextPadding, cellTextPadding);
        if (ColorUtils.isLightColor(courseCell.getCourseStyle().getCellColor())) {
            cellText.setTextColor(ContextCompat.getColor(getContext(), R.color.course_cell_text_dark));
        } else {
            cellText.setTextColor(ContextCompat.getColor(getContext(), R.color.course_cell_text_light));
        }

        String baseText;
        String location = courseCell.getCourseTime().getLocation();
        if (location == null) {
            baseText = courseCell.getCourseName();
        } else {
            baseText = getContext().getString(R.string.course_text_with_location, courseCell.getCourseName(), location);
        }

        if (courseCell.isThisWeekCourse()) {
            cellText.setText(baseText);
        } else {
            String notThisWeekStr = getContext().getString(R.string.not_this_week);
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(notThisWeekStr);
            builder.append(baseText);
            builder.setSpan(new StyleSpan(Typeface.BOLD), 0, notThisWeekStr.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            cellText.setText(builder);
        }

        addView(cellText);
    }

    public boolean isCourseCellView() {
        return courseCellView;
    }

    public int getRowSize() {
        return rowSize;
    }
}
