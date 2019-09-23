package yyl.mvc.common.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 */
public class DateUtil {

    /** ISO日期格式 */
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    /** 0001-01-01T00:00:00 */
    public static final Long MIN_MILLIS = -62135798400000L;
    /** 9999-12-31T23:59:59 */
    public static final Long MAX_MILLIS = 253402271999000L;

    /** 可解析的日期格式列表 */
    private final static String[] PARSE_DATE_PATTERNS = Arrays.asList(//
            ISO_DATETIME_FORMAT, //
            "yyyy-MM-dd HH:mm:ss", //
            "yyyy-MM-dd'T'HH:mm:ss.SSS", //
            "EEE MMM dd HH:mm:ss zzz yyyy", //
            "yyyy-MM-dd HH:mm:ss.SSS", //
            "yyyy-MM-dd HH:mm", //
            "yyyy-MM-dd HH", //
            "yyyy-MM-dd", //
            "yyyy-MM", //
            "d MMM yyyy h:m a", // 30 Jun 2017 2:40 PM
            "MMM d, yyyy HH:mm", //
            "MMM d, yyyy", //
            "MM/dd/yyyy", //
            "yyyy"//
    ).toArray(new String[0]);

    /** DateFormat线程持有(保证线程安全) */
    private static ThreadLocal<DateFormat> ISO_DATEFORMAT_HOLDER = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat(ISO_DATETIME_FORMAT);
        };
    };

    /**
     * 根据字符串解析日期
     * @param value 日期字符串
     * @return 日期对象
     */
    public static Date parse(String value) {
        if (value == null) {
            return null;
        }
        try {
            return forceParseDate(value, Locale.ENGLISH);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 格式化日期对象
     * @param date 日期对象
     * @return 日期对象字符串
     */
    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        try {
            return ISO_DATEFORMAT_HOLDER.get().format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 得到某一时间指定周期的起始时间
     * @param date 指定的时间
     * @param unit 指定的单位类型
     * @return 指定时间指定周期的起始时间
     */
    public static Date getStart(Date date, DateUnit unit) {
        if (date == null) {
            return null;
        }
        return CalendarUtil.getStart(CalendarUtil.toCalendar(date), unit).getTime();
    }

    /**
     * 得到某一时间指定周期的結束时间
     * @param date 指定的时间
     * @param unit 指定的单位类型
     * @return 指定时间指定周期的結束时间
     */
    public static Date getEnd(Date date, DateUnit unit) {
        if (date == null) {
            return null;
        }
        return CalendarUtil.getEnd(CalendarUtil.toCalendar(date), unit).getTime();
    }

    /**
     * 返回给定日历给定周期类型字段的值。
     * @param date 时间
     * @param unit 时间周期
     * @return 所在季度(0-1)
     */
    public static int getFieldValue(Date date, DateUnit unit) {
        return CalendarUtil.getFieldValue(CalendarUtil.toCalendar(date), unit);
    }

    /**
     * 获得两个时间之中最大的时间
     * @param a 第一个时间
     * @param b 第二个时间
     * @return 两个时间之中最大的时间
     */
    public static Date max(Date a, Date b) {
        return a.before(b) ? b : a; // a<b?b:a
    }

    /**
     * 获得两个时间之中最大的时间
     * @param a 第一个时间
     * @param b 第二个时间
     * @return 两个时间之中最大的时间
     */
    public static Date min(Date a, Date b) {
        return b.after(a) ? a : b; // b>a?a:b
    }

    /**
     * 解析日期字符串
     * @param value 日期字符串
     * @param locale 地区对象
     * @return 日期对象
     */
    private static Date forceParseDate(final String str, final Locale locale) throws ParseException {

        if (str == null) {
            throw new IllegalArgumentException("DateString must not be null");
        }

        SimpleDateFormat parser;
        if (locale == null) {
            parser = new SimpleDateFormat();
        } else {
            parser = new SimpleDateFormat("", locale);
        }

        // 不严格模式
        parser.setLenient(true);
        final ParsePosition pos = new ParsePosition(0);
        for (final String parsePattern : PARSE_DATE_PATTERNS) {

            String pattern = parsePattern;

            // LANG-530 - need to make sure 'ZZ' output doesn't get passed to SimpleDateFormat
            if (parsePattern.endsWith("ZZ")) {
                pattern = pattern.substring(0, pattern.length() - 1);
            }

            parser.applyPattern(pattern);
            pos.setIndex(0);

            String str2 = str;
            // LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will
            // ParseException
            if (parsePattern.endsWith("ZZ")) {
                str2 = str.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2");
            }

            final Date date = parser.parse(str2, pos);
            if (date != null && pos.getIndex() == str2.length()) {
                return date;
            }
        }
        throw new ParseException("Unable to parse the date: " + str, -1);
    }
}
