package tool.xfy9326.course.tool;

import androidx.annotation.NonNull;

public class SimpleRunnable implements Runnable {
    private Runnable runnable;

    SimpleRunnable(@NonNull Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public final void run() {
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
