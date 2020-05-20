package tool.xfy9326.course.utils;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;

import tool.xfy9326.course.App;
import tool.xfy9326.course.R;

public class ColorUtils {
    @FloatRange(from = 0.0, to = 1.0)
    private static final float LUMINANCE_IS_LIGHT_COLOR = 0.7f;

    private static final int[] MATERIAL_COLOR = App.instance.getResources().getIntArray(R.array.material_colors);

    public static boolean isLightColor(@ColorInt int colorInt) {
        return androidx.core.graphics.ColorUtils.calculateLuminance(colorInt) >= LUMINANCE_IS_LIGHT_COLOR;
    }

    @ColorInt
    static int getRandomMaterialColor() {
        return MATERIAL_COLOR[(int) (Math.random() * MATERIAL_COLOR.length)];
    }
}
