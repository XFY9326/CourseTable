package tool.xfy9326.course.db.base;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

import tool.xfy9326.course.App;

@SuppressWarnings("WeakerAccess")
public abstract class DBProvider<T extends RoomDatabase> {
    @NonNull
    private final String dbName;
    private T db;

    public DBProvider(@NonNull String dbName) {
        this.dbName = dbName;
    }

    public synchronized T getDB() {
        if (db == null) {
            db = onBuildDB(Room.databaseBuilder(App.instance, getDBClass(), dbName));
        }
        return db;
    }

    protected T onBuildDB(RoomDatabase.Builder<T> builder) {
        return builder.build();
    }

    private Class<T> getDBClass() {
        //noinspection unchecked
        return (Class<T>) ((ParameterizedType) Objects.requireNonNull(getClass().getGenericSuperclass())).getActualTypeArguments()[0];
    }
}
