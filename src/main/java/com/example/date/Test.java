package com.example.date;

import cn.hutool.core.date.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author LTJ
 * @date 2022/4/24
 */
public class Test {
    public static void main(String[] args) {
        //获取一个Calendar对象
        Calendar calendar = Calendar.getInstance();
        //设置星期一为一周开始的第一天
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //设置在一年中第一个星期所需最少天数
        calendar.setMinimalDaysInFirstWeek(4);
        //得到当前的年
        int weekYear = calendar.get(Calendar.YEAR);
        //得到当前日期属于今年的第几周
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        //格式化日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = simpleDateFormat.parse("2019-12-31");
            System.out.println("2019-12-31转换后的日期为：" + parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(parse);
        int weekOfYear1 = calendar.get(Calendar.WEEK_OF_YEAR);
        System.out.println("2019-12-31所在周属于第" + weekOfYear1 + "周");
        Calendar c = new GregorianCalendar();
        //设定日期为2019-12-31 23:59:59
        c.set(2019, Calendar.DECEMBER, 31, 23, 59, 59);

        //得到当前日期属于今年的第几周
        Integer weekOfYearLastWeek1 = c.get(Calendar.WEEK_OF_YEAR);
        System.out.println("当前日期属于第" + weekOfYearLastWeek1 + "周");
        //得到指定年的第几周的开始日期（dayOfWeek是从周日开始排序的）
        calendar.setWeekDate(2019, 52, 2);
        //得到Calendar的时间
        Date starttime = calendar.getTime();
        //得到指定年的第几周的结束日期
        calendar.setWeekDate(2019, 52, 1);
        Date endtime = calendar.getTime();
        //将时间戳格式化为指定格式
        String dateStart = simpleDateFormat.format(starttime);
        String dateEnd = simpleDateFormat.format(endtime);
        System.out.println("2019年第52周的开始日期为：" + dateStart);
        System.out.println("2019年第52周的结束日期为：" + dateEnd);

        LocalDateTime localDateTime = LocalDateTime.of(2009, 12, 31, 0, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2010, 1, 4, 0, 0);
        m1(localDateTime);
        m1(localDateTime2);
    }

    private static void m1(LocalDateTime localDateTime){
        String format = localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        System.out.println("format = " + format);
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        System.out.println("dayOfWeek = " + dayOfWeek);
        int weekOfYear = localDateTime.get(WeekFields.ISO.weekOfYear());
        System.out.println("weekOfYear = " + weekOfYear);
        int weekBasedYear = localDateTime.get(WeekFields.ISO.weekBasedYear());
        System.out.println("weekBasedYear = " + weekBasedYear);
        int weekOfWeekBasedYear = localDateTime.get(WeekFields.ISO.weekOfWeekBasedYear());
        System.out.println("weekOfWeekBasedYear = " + weekOfWeekBasedYear);
        int ALIGNED_DAY_OF_WEEK_IN_MONTH = localDateTime.get(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH);
        System.out.println("ALIGNED_DAY_OF_WEEK_IN_MONTH = " + ALIGNED_DAY_OF_WEEK_IN_MONTH);
        int ALIGNED_WEEK_OF_MONTH = localDateTime.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
        System.out.println("ALIGNED_WEEK_OF_MONTH = " + ALIGNED_WEEK_OF_MONTH);
        int ALIGNED_WEEK_OF_YEAR = localDateTime.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        System.out.println("ALIGNED_WEEK_OF_YEAR = " + ALIGNED_WEEK_OF_YEAR);

        Date from = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        int dateutilweek = DateUtil.weekOfYear(from);
        System.out.println("dateutilweek = " + dateutilweek);
    }
}
