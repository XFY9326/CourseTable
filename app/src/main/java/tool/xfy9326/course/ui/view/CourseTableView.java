package tool.xfy9326.course.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

import tool.xfy9326.course.Const;
import tool.xfy9326.course.R;
import tool.xfy9326.course.bean.CourseCell;
import tool.xfy9326.course.bean.CourseTable;
import tool.xfy9326.course.bean.CourseTimeCell;
import tool.xfy9326.course.bean.SchedulerTable;

@SuppressLint("ViewConstructor")
public class CourseTableView extends GridLayout implements OnCourseCellClickListener {
    private CourseTable courseTable;

    @Nullable
    private OnCourseCellClickListener cellClickListener;

    public CourseTableView(@NonNull Context context, @NonNull CourseTable courseTable, @NonNull Style style) {
        super(context);
        initView();
        setCourseTable(courseTable, style);
    }

    @NonNull
    private static View[] getFillColumnSizeViews(@NonNull Context context, int rowCount, int colCount) {
        View[] result = new View[colCount];
        int rowNum = rowCount - 1;
        for (int i = 0; i < result.length; i++) {
            result[i] = new View(context);
            result[i].setVisibility(View.INVISIBLE);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(rowNum), GridLayout.spec(i));
            if (i == 0) {
                layoutParams.width = context.getResources().getDimensionPixelSize(R.dimen.course_time_row_width);
            } else {
                layoutParams.width = 0;
            }
            layoutParams.height = 0;
            result[i].setLayoutParams(layoutParams);
        }
        return result;
    }

    @NonNull
    private static List<View> getCourseCellViews(@NonNull Context context, @NonNull CourseTable courseTable, @NonNull final OnCourseCellClickListener listener) {
        ArrayList<View> result = new ArrayList<>(courseTable.getCourseCells().length * 6);
        for (CourseCell[] weekDayCells : courseTable.getCourseCells()) {
            for (CourseCell cell : weekDayCells) {
                CourseCellView cellView = new CourseCellView(context, cell);
                cellView.setOnClickListener(v -> listener.onCourseCellClick(cell.getCourseId()));
                result.add(cellView);
            }
        }
        return result;
    }

    @NonNull
    private static View[] getCourseTimeColViews(@NonNull Context context, @NonNull SchedulerTable schedulerTable) {
        View[] result = new View[schedulerTable.getMaxCoursePerDay()];
        for (int i = 0; i < result.length; i++) {
            result[i] = new CourseCellView(context, new CourseTimeCell(i + 1));
        }
        return result;
    }

    private static int getCellHeightByWidth(View view, int width, int divideNum) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);
        return (int) Math.ceil(view.getMeasuredHeight() * 1f / divideNum);
    }

    private void initView() {
        ScrollView.LayoutParams layoutParams = new ScrollView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        setOrientation(VERTICAL);
    }

    public void setCourseTable(@NonNull CourseTable courseTable, @NonNull Style style) {
        if (!courseTable.equals(this.courseTable)) {
            setCourseTableInternal(courseTable, style);
        }
    }

    private synchronized void setCourseTableInternal(@NonNull CourseTable courseTable, @NonNull Style style) {
        this.courseTable = courseTable;
        removeAllViews();
        applyColRowCount(style.showWeekendCourses);
        View[] fillCell = getFillColumnSizeViews(getContext(), getRowCount(), getColumnCount());
        View[] timeCell = getCourseTimeColViews(getContext(), courseTable.getSchedulerTable());
        List<View> courseCell = getCourseCellViews(getContext(), courseTable, this);
        for (View view : fillCell) {
            addViewInLayout(view, -1, view.getLayoutParams(), true);
        }
        for (View view : timeCell) {
            addViewInLayout(view, -1, view.getLayoutParams(), true);
        }
        for (View view : courseCell) {
            addViewInLayout(view, -1, view.getLayoutParams(), true);
        }
        invalidate();
        requestLayout();
    }

    public void setStyle(@NonNull Style style) {
        CourseTable courseTable = this.courseTable;
        if (courseTable != null) {
            setCourseTableInternal(courseTable, style);
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int width = MeasureSpec.getSize(widthSpec);
        int height = MeasureSpec.getSize(heightSpec);
        resizeChildCell(width, height);
        super.onMeasure(widthSpec, heightSpec);
    }

    private void resizeChildCell(int width, int height) {
        int timeCellWidth = getResources().getDimensionPixelSize(R.dimen.course_time_row_width);
        int courseCellWidth = (width - timeCellWidth) / (getColumnCount() - 1);
        int rowHeight = height / (getRowCount() - 1);

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof CourseCellView) {
                CourseCellView cellView = (CourseCellView) view;
                if (cellView.isCourseCellView()) {
                    ((LayoutParams) cellView.getLayoutParams()).width = courseCellWidth;
                }
                int cellHeight = getCellHeightByWidth(cellView, ((LayoutParams) cellView.getLayoutParams()).width, cellView.getRowSize());
                rowHeight = Math.max(rowHeight, cellHeight);
            } else {
                if (view.getLayoutParams().width == 0) {
                    view.getLayoutParams().width = courseCellWidth;
                }
            }
        }

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof CourseCellView) {
                CourseCellView cellView = (CourseCellView) view;
                ((LayoutParams) cellView.getLayoutParams()).height = rowHeight;
            }
        }
    }

    private void applyColRowCount(boolean showWeekendCourses) {
        if (showWeekendCourses) {
            setColumnCount(Const.MAX_WEEK_DAY + 1);
        } else {
            setColumnCount(Const.SIMPLE_WEEK_DAY + 1);
        }
        setRowCount(courseTable.getSchedulerTable().getMaxCoursePerDay() + 1);
    }

    @Override
    public void onCourseCellClick(long courseId) {
        OnCourseCellClickListener listener = cellClickListener;
        if (listener != null) {
            listener.onCourseCellClick(courseId);
        }
    }

    public void setCellClickListener(@Nullable OnCourseCellClickListener cellClickListener) {
        this.cellClickListener = cellClickListener;
    }

    public static class Style {
        private boolean showWeekendCourses;

        public Style(boolean showWeekendCourses) {
            this.showWeekendCourses = showWeekendCourses;
        }
    }
}
