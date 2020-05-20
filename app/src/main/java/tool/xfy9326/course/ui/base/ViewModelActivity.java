package tool.xfy9326.course.ui.base;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

import tool.xfy9326.course.tool.ThreadScheduler;

public abstract class ViewModelActivity<T extends SimpleViewModel> extends AppCompatActivity {
    private T currentViewModel;
    private ThreadScheduler threadScheduler = new ThreadScheduler(getLifecycle());

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentViewModel = new ViewModelProvider(this).get(getViewModelClass());
        currentViewModel.dispatchHostCreated();
        onSetContentView();
        onBuildContentView(currentViewModel, savedInstanceState);
        onBindObserver(currentViewModel);
        currentViewModel.dispatchViewBuilt();
    }

    protected final ThreadScheduler getThreadScheduler() {
        return threadScheduler;
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        currentViewModel.dispatchHostResumed();
    }

    protected T requireViewModel() {
        T viewModel = currentViewModel;
        if (viewModel == null) {
            throw new IllegalStateException("ViewModel has't init yet!");
        }
        return viewModel;
    }

    private Class<T> getViewModelClass() {
        //noinspection unchecked
        return (Class<T>) ((ParameterizedType) Objects.requireNonNull(getClass().getGenericSuperclass())).getActualTypeArguments()[0];
    }

    protected void onSetContentView() {
    }

    protected void onBindObserver(@NonNull T vm) {
    }

    protected void onBuildContentView(@NonNull T vm, @Nullable Bundle savedInstanceState) {
    }
}
