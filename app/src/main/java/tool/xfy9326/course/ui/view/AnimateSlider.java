package tool.xfy9326.course.ui.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.slider.Slider;

import tool.xfy9326.course.R;

public class AnimateSlider extends Slider implements Slider.OnSliderTouchListener {
    private float startValue = 1f;
    private OnSlideFinishListener finishListener;

    public AnimateSlider(@NonNull Context context) {
        this(context, null);
    }

    public AnimateSlider(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarStyle);
    }

    public AnimateSlider(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setTickColor(ColorStateList.valueOf(Color.TRANSPARENT));
        setHaloColor(ColorStateList.valueOf(Color.TRANSPARENT));
        setHaloRadius(0);
        setThumbRadius(getResources().getDimensionPixelSize(R.dimen.slider_thumb_radius_not_touched));
        setThumbColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.slider_thumb)));
        setThumbElevation(getResources().getDimensionPixelSize(R.dimen.slider_thumb_elevation));
        setTrackColorActive(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.slider_tracker_active)));
        setTrackColorInactive(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.slider_tracker_inactive)));
        setTrackHeight(getResources().getDimensionPixelSize(R.dimen.slider_tracker_height));
        addOnSliderTouchListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                animateSlideThumb(true);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                animateSlideThumb(false);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void animateSlideThumb(boolean isTouched) {
        int start;
        int end;
        if (isTouched) {
            start = getResources().getDimensionPixelSize(R.dimen.slider_thumb_radius_not_touched);
            end = getResources().getDimensionPixelSize(R.dimen.slider_thumb_radius_touched);
        } else {
            start = getResources().getDimensionPixelSize(R.dimen.slider_thumb_radius_touched);
            end = getResources().getDimensionPixelSize(R.dimen.slider_thumb_radius_not_touched);
        }
        clearAnimation();
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(getResources().getInteger(R.integer.very_short_anim_time));
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> setThumbRadius((int) animation.getAnimatedValue()));
        animator.start();
    }

    public void setSlideFinishListener(OnSlideFinishListener finishListener) {
        this.finishListener = finishListener;
    }

    @Override
    public void onStartTrackingTouch(@NonNull Slider slider) {
        startValue = slider.getValue();
    }

    @Override
    public void onStopTrackingTouch(@NonNull Slider slider) {
        if (startValue != slider.getValue()) {
            startValue = slider.getValue();
            OnSlideFinishListener finishListener = this.finishListener;
            if (finishListener != null) {
                finishListener.onValueChanged(this, slider.getValue());
            }
        }
    }
}
