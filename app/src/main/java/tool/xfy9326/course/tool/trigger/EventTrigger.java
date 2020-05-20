package tool.xfy9326.course.tool.trigger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

@SuppressWarnings({"WeakerAccess", "unused"})
public class EventTrigger<T> {
    private final MutableLiveData<Event<T>> mutableLiveData;

    public EventTrigger() {
        mutableLiveData = new MutableLiveData<>();
    }

    public EventTrigger(T value) {
        mutableLiveData = new MutableLiveData<>(new Event<>(value, false));
    }

    public EventTrigger(T value, boolean manuallyCheckHandled) {
        mutableLiveData = new MutableLiveData<>(new Event<>(value, manuallyCheckHandled));
    }

    public void postValue(T value) {
        mutableLiveData.postValue(new Event<>(value, false));
    }

    public void postCheckableValue(T value) {
        mutableLiveData.postValue(new Event<>(value, true));
    }

    public void setCheckableValue(T value) {
        mutableLiveData.setValue(new Event<>(value, true));
    }

    @Nullable
    public T getValue() {
        Event<T> event = mutableLiveData.getValue();
        if (event != null) {
            return event.peekContent();
        }
        return null;
    }

    public void setValue(T value) {
        mutableLiveData.setValue(new Event<>(value, false));
    }

    private Observer<Event<T>> getEventObserver(final Observer<T> observer, @Nullable final String tag) {
        return tEvent -> {
            Event.Container<T> container = tEvent.getContentIfNotHandled(tag);
            if (container != null) {
                observer.onChanged(container.content);
            }
        };
    }

    private Observer<Event<T>> getCheckableEventObserver(final CheckableObserver<T> observer) {
        return tEvent -> {
            Event.Container<T> container = tEvent.getContentIfNotHandled(null);
            if (container != null) {
                if (observer.onChanged(container.content)) {
                    tEvent.setNullTagHandled();
                }
            }
        };
    }

    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        observe(owner, observer, null);
    }

    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer, @Nullable final String tag) {
        mutableLiveData.observe(owner, getEventObserver(observer, tag));
    }

    @NonNull
    public Observer<Event<T>> observeForever(Observer<T> observer) {
        return observeForever(observer, null);
    }

    @NonNull
    public Observer<Event<T>> observeForever(Observer<T> observer, @Nullable final String tag) {
        Observer<Event<T>> eventObserver = getEventObserver(observer, tag);
        mutableLiveData.observeForever(eventObserver);
        return eventObserver;
    }

    public void observeCheckable(@NonNull LifecycleOwner owner, @NonNull CheckableObserver<T> observer) {
        mutableLiveData.observe(owner, getCheckableEventObserver(observer));
    }

    @NonNull
    public Observer<Event<T>> observeCheckableForever(CheckableObserver<T> observer) {
        Observer<Event<T>> eventObserver = getCheckableEventObserver(observer);
        mutableLiveData.observeForever(eventObserver);
        return eventObserver;
    }

    public boolean hasObservers() {
        return mutableLiveData.hasObservers();
    }

    public boolean hasActiveObservers() {
        return mutableLiveData.hasActiveObservers();
    }

    public void removeObserver(Observer<Event<T>> observer) {
        mutableLiveData.removeObserver(observer);
    }

    public void removeObservers(LifecycleOwner owner) {
        mutableLiveData.removeObservers(owner);
    }
}
