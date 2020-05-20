package tool.xfy9326.course.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("WeakerAccess")
public class TimePeriod implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String SYMBOL_CONNECT_TIME_PERIOD = "-";

    private int start;
    private int end;

    public TimePeriod(int start) {
        this(start, start);
    }

    public TimePeriod(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("Error! start > end");
        }
        this.start = start;
        this.end = end;
    }

    public TimePeriod(@NonNull String str) {
        if (str.isEmpty()) {
            throw new IllegalArgumentException("Error! start > end");
        }
        if (str.contains(SYMBOL_CONNECT_TIME_PERIOD)) {
            String[] numArr = str.split(SYMBOL_CONNECT_TIME_PERIOD);
            this.start = Integer.parseInt(numArr[0]);
            this.end = Integer.parseInt(numArr[1]);
        } else {
            this.start = Integer.parseInt(str);
            this.end = this.start;
        }
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getLength() {
        return end - start + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePeriod that = (TimePeriod) o;
        return start == that.start &&
                end == that.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @NonNull
    @Override
    public String toString() {
        if (start == end) {
            return String.valueOf(start);
        } else {
            return start + SYMBOL_CONNECT_TIME_PERIOD + end;
        }
    }
}
