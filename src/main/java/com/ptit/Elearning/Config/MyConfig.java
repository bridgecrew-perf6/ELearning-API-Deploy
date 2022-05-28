package com.ptit.Elearning.Config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Configuration
public class MyConfig {

    @Bean("schoolYear")
    public List<String> getListShoolYear(){
        List<String> schoolYear = new ArrayList<>();
         final int START_YEAR = 2015;
        Date date = new Date();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=START_YEAR;i<=currentYear;i++){
            schoolYear.add(i+"-"+(++i));
        }
        return schoolYear;
    }
}
