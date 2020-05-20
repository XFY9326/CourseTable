package tool.xfy9326.course.tool.trigger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

@SuppressWarnings({"WeakerAccess", "unused"})
public class NotifyTrigger {
    private EventTrigger<Object> eventTrigger = new EventTrigger<>();

    public void postNotification() {
        eventTrigger.postValue(null);
    }

    public void setNotification() {
        eventTrigger.setValue(null);
    }

    @NonNull
    private Observer<Object> getObserver(NotifyObserver observer) {
        return o -> observer.onNotify();
    }

    public void observe(@NonNull LifecycleOwner owner, @NonNull NotifyObserver observer) {
        observe(owner, observer, null);
    }

    public void observe(@NonNull LifecycleOwner owner, @NonNull NotifyObserver observer, @Nullable String tag) {
        eventTrigger.observe(owner, getObserver(observer), tag);
    }

    @NonNull
    public Observer<Event<Object>> observeForever(@NonNull NotifyObserver observer) {
        return observeForever(observer, null);
    }

    @NonNull
    public Observer<Event<Object>> observeForever(@NonNull NotifyObserver observer, @Nullable String tag) {
        return eventTrigger.observeForever(getObserver(observer), tag);
    }

    public boolean hasObservers() {
        return eventTrigger.hasObservers();
    }

    public boolean hasActiveObservers() {
        return eventTrigger.hasActiveObservers();
    }

    public void removeObserver(Observer<Event<Object>> observer) {
        eventTrigger.removeObserver(observer);
    }

    public void removeObservers(LifecycleOwner owner) {
        eventTrigger.removeObservers(owner);
    }
}
