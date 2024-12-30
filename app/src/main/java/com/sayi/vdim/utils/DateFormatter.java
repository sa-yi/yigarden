package com.sayi.vdim.utils;
import java.text.*;
import java.util.Date;
public class DateFormatter {
    public static String format(long timestamp){
        // 将时间戳转换为Date对象
        Date date = new Date(timestamp * 1000); // 时间戳是以秒为单位的，而Date的构造函数是以毫秒为单位的

        // 设置日期时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // 格式化日期时间
        String formattedDate = sdf.format(date);

        // 打印结果
        System.out.println(formattedDate);
        return formattedDate;
    }

    public static String format(String time){
        return format(Long.valueOf(time));
    }
}
