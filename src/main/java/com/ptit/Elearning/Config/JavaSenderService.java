package com.ptit.Elearning.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class JavaSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;


    private final String ACCOUNT_SID = "AC03518d4294fe2e36bd1c3aaa0a3a8e5c";
    private final String AUTH_TOKEN = "0d0fbd80285e736e0b6fc9d7b91a80ce";
    private final String FROM_NUMBER = "+19899997161";

    public void sendMailVerifyCode(String toEmail, String body,int status) {
        String emailFrom = env.getProperty("spring.mail.username");
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(emailFrom);
        mail.setTo(toEmail);
        mail.setText(body);

        if(status==1){
            mail.setSubject("Xác minh mật khẩu hai bước tại PTIT-ELearning");
        }else if(status==0){
            mail.setSubject("Xác minh tạo tài khoản tại PTIT-ELearning");
        }else if(status==2){
            mail.setSubject("Xác minh khôi phục tài khoản tại PTIT-ELearning");
        }else{
            throw new IllegalArgumentException("Status is not legal");
        }
        mailSender.send(mail);

    }

//    public void sendSmsVerifyCode(String smsTo, String code) {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
//        String msg ="Your OTP - "+code+ " please verify this OTP in Bookstore.com";
//
//        Message message = Message.creator(new PhoneNumber("+84"+smsTo.substring(1, smsTo.length())), new PhoneNumber(FROM_NUMBER), msg)
//                .create();
//    }
}
