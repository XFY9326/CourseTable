package tool.xfy9326.course.ui.base;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModel;

import tool.xfy9326.course.tool.ThreadScheduler;

@SuppressWarnings("WeakerAccess")
public abstract class SimpleViewModel extends ViewModel implements LifecycleOwner {
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private final ThreadScheduler threadScheduler = new ThreadScheduler(getLifecycle());
    private boolean hasInit = false;

    public SimpleViewModel() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.INITIALIZED);
    }

    protected final ThreadScheduler getThreadScheduler() {
        return threadScheduler;
    }

    final void dispatchHostCreated() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
        boolean initCreate = !hasInit;
        if (!hasInit) {
            hasInit = true;
            onInit();
        }
        onHostCreated(initCreate);
    }

    protected void onInit() {
    }

    protected void onHostCreated(boolean initCreate) {
    }

    final void dispatchViewBuilt() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);
        onViewBuilt();
    }

    protected void onViewBuilt() {
    }

    final void dispatchHostResumed() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.RESUMED);
        onHostResumed();
    }

    protected void onHostResumed() {
    }

    @NonNull
    @Override
    public final Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    @CallSuper
    @Override
    protected void onCleared() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED);
    }
}
