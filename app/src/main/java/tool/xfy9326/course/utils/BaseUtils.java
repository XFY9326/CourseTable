package tool.xfy9326.course.utils;

import android.content.Intent;

import androidx.annotation.NonNull;

public class BaseUtils {
    public static boolean hasActivityInStack(@NonNull Intent intent) {
        return (intent.getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0;
    }
}
