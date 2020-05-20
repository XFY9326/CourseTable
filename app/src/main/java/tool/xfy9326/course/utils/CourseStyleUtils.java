package tool.xfy9326.course.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import java.util.List;

import tool.xfy9326.course.bean.CourseStyle;

public class CourseStyleUtils {
    @NonNull
    public static CourseStyle getNewCourseStyle(long courseId) {
        return new CourseStyle(courseId, ColorUtils.getRandomMaterialColor());
    }

    @NonNull
    public static CourseStyle getFixedCourseStyle(long courseId, @NonNull List<CourseStyle> courseStyles) {
        for (CourseStyle style : courseStyles) {
            if (style.getCourseId() == courseId) {
                return style;
            }
        }
        return getNewCourseStyle(courseId);
    }

    public static Drawable getCellBackgroundDrawable(@ColorInt int colorInt, @FloatRange(from = 0) float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setColor(colorInt);
        return drawable;
    }
}
