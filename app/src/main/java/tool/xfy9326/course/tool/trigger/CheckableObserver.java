package tool.xfy9326.course.tool.trigger;

@SuppressWarnings("unused")
public interface CheckableObserver<T> {
    boolean onChanged(T t);
}
