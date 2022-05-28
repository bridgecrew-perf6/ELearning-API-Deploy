package com.ptit.Elearning.AutogeneateNotification;

import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationForJoinClass {
    public static String addToClass(String className, Date timestamp, String authorName){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd ");
        return "Bạn vừa được add vào lớp "+className + " vào lúc: "+ simpleDateFormat.format(timestamp)+" bởi "+authorName;
    }
    public static String removeFromClass(String className, Date timestamp, String authorName){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd ");
        return "Bạn bị xóa khỏi lớp "+className + " vào lúc: "+ simpleDateFormat.format(timestamp)+" bởi "+authorName;
    }

    public static String joinClass(String className, Date timestamp){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd ");
        return "Bạn vừa được tham gia vào lớp "+className + " vào lúc: "+ simpleDateFormat.format(timestamp);
    }
}
