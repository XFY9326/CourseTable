package tool.xfy9326.course.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Set;

import tool.xfy9326.course.App;

@SuppressWarnings({"WeakerAccess", "unused"})
public class AppPref {
    private static Container defaultContainer;
    private static HashMap<String, Container> containerHashMap = new HashMap<>();

    private AppPref() {
    }

    @NonNull
    public synchronized static Container use(@NonNull String name) {
        Container cachedContainer = containerHashMap.get(name);
        if (cachedContainer == null) {
            cachedContainer = new Container(App.instance.getSharedPreferences(name, Context.MODE_PRIVATE));
            containerHashMap.put(name, cachedContainer);
        }
        return cachedContainer;
    }

    @NonNull
    public synchronized static Container useDefault() {
        if (defaultContainer == null) {
            defaultContainer = new Container(PreferenceManager.getDefaultSharedPreferences(App.instance));
        }
        return defaultContainer;
    }

    public static class Container {
        private SharedPreferences sharedPreferences;
        private Editor editor;

        Container(SharedPreferences sharedPreferences) {
            this.sharedPreferences = sharedPreferences;
        }

        @SuppressLint("CommitPrefEdits")
        @NonNull
        private synchronized Editor getEditor() {
            if (editor == null) {
                editor = new Editor(sharedPreferences.edit());
            }
            return editor;
        }

        @Nullable
        public String getString(@NonNull String key, @Nullable String defaultValue) {
            return sharedPreferences.getString(key, defaultValue);
        }

        @Nullable
        public Set<String> getStringSet(@NonNull String key, @Nullable Set<String> defaultValue) {
            return sharedPreferences.getStringSet(key, defaultValue);
        }

        public boolean getBoolean(@NonNull String key, boolean defaultValue) {
            return sharedPreferences.getBoolean(key, defaultValue);
        }

        public int getInt(@NonNull String key, int defaultValue) {
            return sharedPreferences.getInt(key, defaultValue);
        }

        public float getFloat(@NonNull String key, float defaultValue) {
            return sharedPreferences.getFloat(key, defaultValue);
        }

        public long getLong(@NonNull String key, long defaultValue) {
            return sharedPreferences.getLong(key, defaultValue);
        }

        public boolean has(@NonNull String key) {
            return sharedPreferences.contains(key);
        }


        @NonNull
        public Editor putString(@NonNull String key, @Nullable String value) {
            return getEditor().putString(key, value);
        }

        @NonNull
        public Editor putStringSet(@NonNull String key, @Nullable Set<String> value) {
            return getEditor().putStringSet(key, value);
        }

        @NonNull
        public Editor putBoolean(@NonNull String key, boolean value) {
            return getEditor().putBoolean(key, value);
        }

        @NonNull
        public Editor putInt(@NonNull String key, int value) {
            return getEditor().putInt(key, value);
        }

        @NonNull
        public Editor putFloat(@NonNull String key, float value) {
            return getEditor().putFloat(key, value);
        }

        @NonNull
        public Editor putLong(@NonNull String key, long value) {
            return getEditor().putLong(key, value);
        }

        @NonNull
        public Editor autoCommit() {
            return getEditor().autoCommit();
        }

        @NonNull
        public Editor autoApply() {
            return getEditor().autoApply();
        }

        @NonNull
        public Editor disableAutoSave() {
            return getEditor().disableAutoSave();
        }
    }

    public static class Editor {
        private boolean autoCommit = false;
        private boolean autoApply = true;
        private SharedPreferences.Editor editor;

        Editor(SharedPreferences.Editor editor) {
            this.editor = editor;
        }

        @NonNull
        public Editor autoCommit() {
            this.autoCommit = true;
            this.autoApply = false;
            return this;
        }

        @NonNull
        public Editor autoApply() {
            this.autoCommit = false;
            this.autoApply = true;
            return this;
        }

        @NonNull
        public Editor disableAutoSave() {
            this.autoCommit = false;
            this.autoApply = false;
            return this;
        }

        private void tryDoSave() {
            if (autoCommit) {
                editor.commit();
            } else if (autoApply) {
                editor.apply();
            }
        }

        @NonNull
        public Editor putString(@NonNull String key, @Nullable String value) {
            editor.putString(key, value);
            tryDoSave();
            return this;
        }

        @NonNull
        public Editor putStringSet(@NonNull String key, @Nullable Set<String> value) {
            editor.putStringSet(key, value);
            tryDoSave();
            return this;
        }

        @NonNull
        public Editor putBoolean(@NonNull String key, boolean value) {
            editor.putBoolean(key, value);
            tryDoSave();
            return this;
        }

        @NonNull
        public Editor putInt(@NonNull String key, int value) {
            editor.putInt(key, value);
            tryDoSave();
            return this;
        }

        @NonNull
        public Editor putFloat(@NonNull String key, float value) {
            editor.putFloat(key, value);
            tryDoSave();
            return this;
        }

        @NonNull
        public Editor putLong(@NonNull String key, long value) {
            editor.putLong(key, value);
            tryDoSave();
            return this;
        }

        @NonNull
        public Editor remove(@NonNull String key) {
            editor.remove(key);
            tryDoSave();
            return this;
        }

        @NonNull
        public Editor clear() {
            editor.clear();
            tryDoSave();
            return this;
        }

        public boolean commit() {
            return editor.commit();
        }

        public void apply() {
            editor.apply();
        }
    }
}
