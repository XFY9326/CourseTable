package tool.xfy9326.course.tool.trigger;

import androidx.annotation.Nullable;

import java.util.Hashtable;

@SuppressWarnings({"WeakerAccess", "unused"})
class Event<T> {
    private final Hashtable<String, Boolean> handledTagTable = new Hashtable<>();
    private Container<T> container;
    private boolean manuallyCheckHandled;
    private boolean hasNullTagBeenHandled = false;

    Event(@Nullable T content, boolean manuallyCheckHandled) {
        this.container = new Container<>(content);
        this.manuallyCheckHandled = manuallyCheckHandled;
    }

    @Nullable
    synchronized Container<T> getContentIfNotHandled(@Nullable String tag) {
        if (container != null) {
            if (tag == null) {
                if (hasNullTagBeenHandled) {
                    return null;
                } else {
                    if (!manuallyCheckHandled) {
                        hasNullTagBeenHandled = true;
                    }
                    return container;
                }
            } else {
                Boolean handled = handledTagTable.get(tag);
                if (handled != null && handled) {
                    return null;
                } else {
                    handledTagTable.put(tag, true);
                    return container;
                }
            }
        } else {
            return null;
        }
    }

    synchronized void setNullTagHandled() {
        if (manuallyCheckHandled) {
            hasNullTagBeenHandled = true;
            container = null;
            handledTagTable.clear();
        }
    }

    @Nullable
    public T peekContent() {
        Container<T> tContainer = container;
        if (tContainer == null) {
            return null;
        } else {
            return container.content;
        }
    }

    static class Container<T> {
        @Nullable
        T content;

        Container(@Nullable T content) {
            this.content = content;
        }
    }
}
