package tool.xfy9326.course.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import tool.xfy9326.course.utils.BaseUtils;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchMainScreen();
    }

    private void launchMainScreen() {
        if (BaseUtils.hasActivityInStack(getIntent())) {
            finish();
        } else {
            startActivity(new Intent(this, CourseTableActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finishAfterTransition();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
