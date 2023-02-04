package com.srz.mes;

import com.srz.mes.service.ProductionDataService;
import org.apache.coyote.http11.InputFilter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.Buffer;
import java.util.*;

/**
 * @author srz
 * @create 2022/8/29 22:40
 */

@SpringBootTest
public class ZhhMain {


    //二简字
    @Test
    public void two() throws IOException{
        BufferedReader tuWordReader = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-1-23/二简.txt"));
        BufferedWriter tuWordFormatWriter = new BufferedWriter(new FileWriter("C:/Users/oWo/Desktop/2023-1-23/二简单字.txt"));

        String line;
        while ((line = tuWordReader.readLine()) != null){
            String str2 = line.split("\t")[2];
            if (str2.length()==2) {
                tuWordFormatWriter.write(line.split("\t")[0]);
            }
        }



        tuWordFormatWriter.close();
        tuWordReader.close();
    }
    //转换格式
    @Test
    public void conversionFormat() throws IOException{
        //转换格式
        //的	u	10359470
        //转换为
        //的	10359470	u
        BufferedReader tuWordReader = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-1-23/TuWord.txt"));
        BufferedWriter tuWordFormatWriter = new BufferedWriter(new FileWriter("C:/Users/oWo/Desktop/2023-1-23/tuWordFormat.txt"));

        String line;
        while ((line = tuWordReader.readLine()) != null){
            String str0 = line.split("\t")[0];
            String str1 = line.split("\t")[1];
            String str2 = line.split("\t")[2];
            tuWordFormatWriter.write(str0+"\t"+str2+"\t"+str1 + "\r\n");
        }



        tuWordFormatWriter.close();
        tuWordReader.close();
    }
    //去除重复的字，去简
    @Test
    public void delRepeat() throws IOException{
        //去除重复的字，去简
        BufferedReader tuWordReader = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-1-23/TuWordFormat.txt"));
        BufferedWriter tuWordRepeat = new BufferedWriter(new FileWriter("C:/Users/oWo/Desktop/2023-1-23/tuWordRepeat.txt"));

        String line;
        List<String> ci = new ArrayList<>();
        while ((line = tuWordReader.readLine()) != null){
            ci.add(line);
        }

        //计算重码词组
        Map<String, Integer> map = new HashMap();
        String c;
        for (String str : ci) {
            c = str.split("\t")[0];
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            }else{
                map.put(c,1);
                tuWordRepeat.write(str+ "\r\n");
            }
        }


        tuWordRepeat.close();
        tuWordReader.close();
    }

    //查出重复的单字
    @Test
    public void queryRepeat() throws IOException{
        //查出重复的单字
        BufferedReader tuWordRepeat = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-1-23/tuWordRepeat.txt"));
        BufferedWriter tuWordQuery = new BufferedWriter(new FileWriter("C:/Users/oWo/Desktop/2023-1-23/tuWordQuery.txt"));

        String line;
        List<String> ci = new ArrayList<>();
        while ((line = tuWordRepeat.readLine()) != null){
            ci.add(line);
        }

        //计算重码词组
        Map<String, Integer> map = new HashMap();
        String c;
        for (String str : ci) {
            c = str.split("\t")[0];
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            }else{
                map.put(c,1);
                //tuWordQuery.write(str+ "\r\n");
            }
        }

        Set<String> key = map.keySet();
        for (String s : key) {
            if (map.get(s)>1) {

            }
        }
        
        


        tuWordRepeat.close();
        tuWordQuery.close();
    }



}




