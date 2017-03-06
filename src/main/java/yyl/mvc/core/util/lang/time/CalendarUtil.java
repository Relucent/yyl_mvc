package yyl.mvc.core.util.lang.time;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class CalendarUtil {

	/**
	 * 转换Date为Calendar类型
	 * @param date
	 * @return
	 */
	public static Calendar toCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 得到某一时间指定周期的起始时间
	 * @param calendar 指定的时间
	 * @param unit 指定的单位类型
	 * @return 指定时间指定周期的起始时间
	 */
	public static Calendar getStart(Calendar cal, TimeField unit) {
		Calendar start = Calendar.getInstance();
		switch (unit) {
		case YEAR: {// 年
			int year = cal.get(Calendar.YEAR);
			start.set(year, Calendar.JANUARY, 1, 0, 0, 0);// _年1月0日0时0分0秒
			break;
		}
		case MONTH: {// 月份
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			start.set(year, month, 1, 0, 0, 0);// 年月日时分秒
			break;
		}
		case DATE: {// 日期
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int date = cal.get(Calendar.DATE);
			start.set(year, month, date, 0, 0, 0);// 年月日时分秒
			break;
		}
		default:
			// Ignore
		}
		return start;
	}

	/**
	 * 得到某一时间指定周期的結束时间
	 * @param calendar 指定的时间
	 * @param unit 指定的单位类型
	 * @return 指定时间指定周期的結束时间
	 */
	public static Calendar getEnd(Calendar cal, TimeField field) {
		Calendar end = Calendar.getInstance();// calendar.clone();
		switch (field) {
		case YEAR: { // 年
			int year = cal.get(Calendar.YEAR);
			end.set(year, Calendar.DECEMBER, 31, 23, 59, 59);// _年12月31日23时59分59秒
			break;
		}
		case MONTH: {// 月份
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			end.set(Calendar.YEAR, year);
			end.set(Calendar.MONTH, month);
			end.set(Calendar.DATE, 1);
			int dayOfEndMonth = end.getActualMaximum(Calendar.DAY_OF_MONTH);// 该月天数
			end.set(Calendar.DATE, dayOfEndMonth);// 设置季度结束日期的日子
			end.set(Calendar.HOUR_OF_DAY, 23); // 时
			end.set(Calendar.MINUTE, 59);// 分
			end.set(Calendar.SECOND, 59);// 秒
			break;
		}
		case DATE: {// 日期
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int date = cal.get(Calendar.DATE);
			end.set(year, month, date, 23, 59, 59);// 年月日时分秒
			break;
		}
		default:
			// Ignore
		}
		return end;
	}
}
