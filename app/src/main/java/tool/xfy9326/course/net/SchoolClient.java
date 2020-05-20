package tool.xfy9326.course.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import tool.xfy9326.course.App;
import tool.xfy9326.course.bean.Course;
import tool.xfy9326.course.bean.CourseDetail;
import tool.xfy9326.course.bean.CourseTime;
import tool.xfy9326.course.bean.TimePeriod;
import tool.xfy9326.course.bean.WeekMode;
import tool.xfy9326.course.utils.TimeUtils;

public class SchoolClient {
    private static final String JWC_SERVER_URL = "http://jwc.nau.edu.cn/";
    private static final String JWC_LOGOUT_URL = JWC_SERVER_URL + "LoginOut.aspx";
    private static final String SSO_JWC_LOGIN_URL = "http://sso.nau.edu.cn/sso/login?service=http%3a%2f%2fjwc.nau.edu.cn%2fLogin_Single.aspx";
    private static final String COURSE_TABLE_URL = JWC_SERVER_URL + "/Students/MyCourseScheduleTable.aspx";
    private static SimpleDateFormat DATE_FORMAT_YMD = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final OkHttpClient client;

    public SchoolClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.cookieJar(new CookieStore());
        clientBuilder.cache(new Cache(App.instance.getCacheDir(), 1024 * 1024 * 10L));
        clientBuilder.retryOnConnectionFailure(true);
        client = clientBuilder.build();
    }

    private static NetCourseInfo readHtmlForBaseInfo(String html) throws ParseException {
        Document document = Jsoup.parse(html);
        Elements span = document.body().getElementById("TermInfo").getElementsByTag("span");

        Date startDate = DATE_FORMAT_YMD.parse(span.get(3).text());
        Date endDate = DATE_FORMAT_YMD.parse(span.get(4).text());

        assert endDate != null;
        assert startDate != null;

        int maxWeekNum = (int) Math.ceil((endDate.getTime() - startDate.getTime()) / (7 * 24 * 60 * 60 * 1000f));

        return new NetCourseInfo(13, maxWeekNum, startDate);
    }

    private static List<Course> readHtmlForCourses(String html) throws IOException {
        Document document = Jsoup.parse(html);
        ArrayList<Course> courseList = new ArrayList<>();
        if (document != null) {
            Elements tr = document.body().getElementById("content").getElementsByTag("tr");
            if (tr.isEmpty()) {
                return courseList;
            }

            for (int i = 1; i < tr.size(); i++) {
                Elements td = tr.get(i).getElementsByTag("td");

                if (td.size() < 8) throw new IOException();

                String detail = td.get(8).text().trim();
                if (!detail.contains("上课地点：")) {
                    continue;
                }

                detail = detail.substring(detail.indexOf("上课地点：") + "上课地点：".length());
                String[] details = detail.split("上课地点：");
                ArrayList<CourseTime> courseTimeArrayList = new ArrayList<>(details.length);

                String location;
                int weekDay;
                WeekMode weekMode;
                List<TimePeriod> weekTimes;
                TimePeriod courseTime;

                for (String s : details) {
                    String[] temp = s.split("上课时间：");
                    location = temp[0];
                    String[] timeTemp = temp[1].split(" ");
                    weekDay = Integer.parseInt(timeTemp[2]);
                    if (weekDay == 7) {
                        weekDay = 1;
                    } else {
                        weekDay += 1;
                    }

                    timeTemp[0] = timeTemp[0].trim();
                    if (timeTemp[0].contains("单")) {
                        weekMode = WeekMode.ODD_WEEK_ONLY;
                        weekTimes = TimeUtils.fromTimePeriodListStr(timeTemp[0].substring(0, timeTemp[0].indexOf("之")).trim());
                    } else if (timeTemp[0].contains("双")) {
                        weekMode = WeekMode.EVEN_WEEK_ONLY;
                        weekTimes = TimeUtils.fromTimePeriodListStr(timeTemp[0].substring(0, timeTemp[0].indexOf("之")).trim());
                    } else {
                        weekMode = WeekMode.ANY_WEEKS;
                        String weekStr;
                        if (timeTemp[0].startsWith("第")) {
                            weekStr = timeTemp[0].substring(1, timeTemp[0].indexOf("周")).trim();
                        } else {
                            weekStr = timeTemp[0].substring(0, timeTemp[0].indexOf("周")).trim();
                        }
                        weekTimes = TimeUtils.fromTimePeriodListStr(weekStr);
                    }

                    String time = timeTemp[4].substring(0, timeTemp[4].indexOf("节")).trim();
                    courseTime = new TimePeriod(time);

                    CourseTime ct = new CourseTime(weekDay, courseTime, weekMode, weekTimes);
                    ct.setLocation(location);

                    courseTimeArrayList.add(ct);
                }

                Course course = new Course(1, td.get(2).text(), new CourseDetail(td.get(7).text(), td.get(5).text()));
                course.setCourseTimeList(courseTimeArrayList);
                course.refreshCourseStyle();
                courseList.add(course);
            }
        }
        return courseList;
    }

    @Nullable
    public NetCourseInfo getCourseData(@NonNull String userId, @NonNull String userPw, boolean tryReLogin) throws Exception {
        Response response = client.newCall(new Request.Builder().url(SSO_JWC_LOGIN_URL).build()).execute();
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            FormBody postForm = getSSOPostForm(userId, userPw, responseBody.string());
            response.close();

            Response loginResponse = client.newCall(new Request.Builder().url(SSO_JWC_LOGIN_URL).post(postForm).build()).execute();
            ResponseBody loginResponseBody = loginResponse.body();
            if (loginResponseBody != null) {
                String body = loginResponseBody.string();
                String query = loginResponse.request().url().query();
                loginResponse.close();

                if (body.contains("密码错误") || body.contains("南京审计大学统一身份认证登录") || body.contains("请勿输入非法字符")) {
                    return null;
                } else if ((body.contains("当前你已经登录") || body.contains("同一时间内只允许登陆一次")) && tryReLogin) {
                    logout();
                    return getCourseData(userId, userPw, false);
                } else if (body.contains("南京审计大学教学信息管理系统") && query != null && query.contains("r=") && query.contains("&d=")) {
                    NetCourseInfo netCourseInfo = readHtmlForBaseInfo(body);
                    List<Course> courses = getCourses();
                    if (courses != null) {
                        netCourseInfo.setCourses(courses);
                    }
                    logout();
                    return netCourseInfo;
                }
            }
        }
        return null;
    }

    @Nullable
    private List<Course> getCourses() throws Exception {
        Response response = client.newCall(new Request.Builder().url(COURSE_TABLE_URL).build()).execute();
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String html = responseBody.string();
            response.close();

            if (!html.isEmpty()) {
                return readHtmlForCourses(html);
            }
        }
        return null;
    }

    private void logout() throws IOException {
        client.newCall(new Request.Builder().url(JWC_LOGOUT_URL).build()).execute().close();
    }

    private FormBody getSSOPostForm(String userId, String userPw, String ssoHtml) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", userId);
        formBuilder.add("password", userPw);

        Document document = Jsoup.parse(ssoHtml);
        Elements nameList = document.select("input[name]");
        for (Element element : nameList) {
            String value = element.attr("value");
            String name = element.attr("name");
            if ("lt".equals(name) || "execution".equals(name) || "_eventId".equals(name) || "useVCode".equals(name) || "isUseVCode".equals(name) || "sessionVcode".equals(name) || "errorCount".equals(name)) {
                formBuilder.add(name, value);
            }
        }
        return formBuilder.build();
    }
}
