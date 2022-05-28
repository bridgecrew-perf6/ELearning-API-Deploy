package com.ptit.Elearning.AutogeneateNotification;


import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationForExcercise {
    public static String newExcercise(String className, Date timestamp){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd ");
        return "Bạn có bài tập mới trong: "+className + " vào lúc: "+ simpleDateFormat.format(timestamp);
    }

}
