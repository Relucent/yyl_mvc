package yyl.mvc.core.util.lang.time;

import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {

	/**
	 * 得到某一时间指定周期的起始时间
	 * @param date 指定的时间
	 * @param unit 指定的单位类型
	 * @return 指定时间指定周期的起始时间
	 */
	public static Date getStart(Date date, TimeField unit) {
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
	public static Date getEnd(Date date, TimeField unit) {
		if (date == null) {
			return null;
		}
		return CalendarUtil.getEnd(CalendarUtil.toCalendar(date), unit).getTime();
	}
}