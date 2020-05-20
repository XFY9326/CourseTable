package tool.xfy9326.course.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import tool.xfy9326.course.BuildConfig;
import tool.xfy9326.course.R;
import tool.xfy9326.course.databinding.DialogAboutBinding;
import tool.xfy9326.course.databinding.DialogCourseControlPanelBinding;
import tool.xfy9326.course.databinding.DialogLoginBinding;

public class DialogUtils {

    public static Dialog getLoginDialog(@NonNull Context context, @NonNull Lifecycle lifecycle, @NonNull OnGetLoginInfoListener loginInfoListener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        DialogLoginBinding binding = DialogLoginBinding.inflate(LayoutInflater.from(context));
        builder.setView(binding.getRoot());
        builder.setTitle(R.string.load_courses);
        builder.setPositiveButton(R.string.load_courses, (dialog, which) -> {
            Editable idEditable = binding.editTextUserId.getText();
            Editable pwEditable = binding.editTextUserPassword.getText();
            if (idEditable == null || pwEditable == null) {
                Toast.makeText(context, R.string.login_error, Toast.LENGTH_SHORT).show();
                return;
            }
            String id = idEditable.toString();
            String pw = pwEditable.toString();
            if (id.isEmpty() || pw.isEmpty()) {
                Toast.makeText(context, R.string.login_error, Toast.LENGTH_SHORT).show();
                return;
            }
            loginInfoListener.onGetLoginInfo(id, pw);
        });
        builder.setCancelable(false);
        builder.setNegativeButton(android.R.string.cancel, null);
        Dialog dialog = builder.create();
        applyAutoClose(lifecycle, dialog);
        return dialog;
    }

    public static Dialog getAboutDialog(@NonNull Context context, @NonNull Lifecycle lifecycle) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        DialogAboutBinding binding = DialogAboutBinding.inflate(LayoutInflater.from(context));
        builder.setView(binding.getRoot());
        binding.textViewAboutVersion.setText(context.getString(R.string.version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
        builder.setBackground(context.getDrawable(android.R.color.transparent));
        Dialog dialog = builder.create();
        applyAutoClose(lifecycle, dialog);
        return dialog;
    }

    public static Dialog getCourseControlBottomDialog(@NonNull Context context, @NonNull Lifecycle lifecycle, int nowWeekNum, int nowShowWeekNum, int maxWeekNum, @NonNull OnWeekNumChangedListener listener) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        DialogCourseControlPanelBinding binding = DialogCourseControlPanelBinding.inflate(dialog.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        Window window = dialog.getWindow();
        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.getAttributes().dimAmount = 0.5f;

            ViewGroup parentView = window.findViewById(R.id.design_bottom_sheet);
            if (parentView != null) {
                parentView.setBackgroundResource(android.R.color.transparent);
            }
        }

        if (nowShowWeekNum != 0) {
            binding.textViewCurrentWeekNum.setText(context.getString(R.string.current_week, nowWeekNum));
        }

        binding.sliderWeekNum.setValueTo(maxWeekNum);
        binding.sliderWeekNum.setValue(nowShowWeekNum);
        binding.sliderWeekNum.setLabelFormatter(value -> context.getString(R.string.week_num_text, (int) value));
        binding.sliderWeekNum.setSlideFinishListener((slider, value) -> listener.onWeekNumChanged((int) value));

        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        applyAutoClose(lifecycle, dialog);

        return dialog;
    }

    private static void applyAutoClose(Lifecycle lifecycle, Dialog dialog) {
        LifecycleObserver observer = new DefaultLifecycleObserver() {
            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                if (dialog.isShowing()) dialog.dismiss();
                lifecycle.removeObserver(this);
            }
        };
        lifecycle.addObserver(observer);
        dialog.setOnDismissListener(dialog1 -> lifecycle.removeObserver(observer));
    }

    public interface OnWeekNumChangedListener {
        void onWeekNumChanged(int weekNum);
    }

    public interface OnGetLoginInfoListener {
        void onGetLoginInfo(String id, String pw);
    }
}
