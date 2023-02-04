package com.srz.mes;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author srz
 * @create 2022/8/29 22:40
 */

@SpringBootTest
public class zhiaishaiwen {

    @Test
    public void feigntest() throws IOException, ParseException {

        Date date = new Date();
        long time = date.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        for (int i = 0; i < 2000; i++){
            String format = sdf.format(time);
            System.out .println(format+"    指爱赛文第"+ (3407+i) +"期");
            System.out.println();
            time = time + 86400000;
        }
    }
    @Test
    public void data() throws IOException, ParseException {

        Date date = new Date();
        long time = date.getTime();
        long day = 86400000l;
        day = day * 365;
        time = time + day;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        for (int i = 0; i < 366; i++){
            String format = sdf.format(time);
            System.out .println(format);
            System.out.println(" ");
            time = time - 86400000;
        }
    }



}




