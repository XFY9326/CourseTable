package tool.xfy9326.course.tool;

import androidx.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Calendar;

@Documented
@IntDef({Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY})
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD,})
@Retention(RetentionPolicy.SOURCE)
public @interface CalendarWeekDay {
}
