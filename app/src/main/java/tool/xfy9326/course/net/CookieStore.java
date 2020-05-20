package tool.xfy9326.course.net;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

class CookieStore implements CookieJar {
    private final HashMap<String, List<Cookie>> cookieMap;

    CookieStore() {
        this.cookieMap = new HashMap<>();
    }

    @Override
    public void saveFromResponse(HttpUrl httpUrl, @NotNull List<Cookie> list) {
        cookieMap.put(httpUrl.host(), list);
    }

    @NotNull
    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        List<Cookie> cookies = cookieMap.get(httpUrl.host());
        return cookies != null ? cookies : new ArrayList<>();
    }
}
