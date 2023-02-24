package com.srz.mes;

import com.srz.mes.service.ProductionDataService;
import org.apache.coyote.http11.InputFilter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.Buffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author srz
 * @create 2022/8/29 22:40
 */

@SpringBootTest
public class ZhhMain {

    @Test
    public void Ohh() throws IOException, NoSuchAlgorithmException {
        BufferedReader exist_ci = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-2-20/exist_ci.txt"));
        BufferedReader exist_O = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-2-20/exist_O.txt"));
        BufferedWriter out = new BufferedWriter(new FileWriter("C:/Users/oWo/Desktop/2023-2-20/out.txt"));

        List<String> tiger = new ArrayList<>();

        String line;
        Map<String,Integer> existMap = new HashMap<>();
        while ((line = exist_ci.readLine()) != null){
            existMap.put(line.split("\t")[2],1);
        }

        while ((line = exist_O.readLine()) != null){
            if (!existMap.containsKey(line.split("\t")[2])){
                out.write(line+ "\r\n");
                existMap.put(line.split("\t")[2],1);
            }

        }

        out.close();
        exist_O.close();
        exist_ci.close();


    }

    @Test
    public void ttto() throws IOException, NoSuchAlgorithmException {
        String passworf = "第3954段 速度138.68 击键6.12 码长2.65 字数96 时间00:41.533 回改12 退格1 回车0 键数254 键准83.86% 打词65.63% 校检:ea48f0 哈希";
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(passworf.getBytes());
        byte[] digestArray = digest.digest();
        StringBuilder str = new StringBuilder();
        for (byte b : digestArray) {
            str.append(String.format("%02x", b));
        }
        System.out.println(str);


    }


    //二简字
    @Test
    public void two() throws IOException{
        BufferedReader tuWordReader = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-1-23/二简.txt"));
        BufferedReader del = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-1-23/五简单字.txt"));
        BufferedWriter tuWordFormatWriter = new BufferedWriter(new FileWriter("C:/Users/oWo/Desktop/2023-1-23/八简单字.txt"));

        List<String> cie = new ArrayList<>();


        char a1 = 'a';
        char a2 = 'a';
        for (int i = 0; i < 26; i++) {
            a2 = 'a';
            for (int j = 0; j < 26; j++) {
                cie.add(a1+""+a2);
                a2++;
            }
            a1++;
        }

        String linet;
        Map<String,Integer> delMap = new HashMap<>();
        while ((linet = del.readLine()) != null){
            delMap.put(linet.split("\t")[0],1);
        }


        String line;
        List<String> ci = new ArrayList<>();
        while ((line = tuWordReader.readLine()) != null){
            if(delMap.containsKey(line.split("\t")[0])){

            } else {
                ci.add(line);
            }
        }


        for (String s : cie) {
            int i = 1;
            int sort = 500;
            for (String lie : ci) {
                String str2 = lie.split("\t")[2];
                if (str2.length()>2) {
                    if (i > 4){
                        break;
                    }
                    if (str2.startsWith(s)){
                        tuWordFormatWriter.write(lie.split("\t")[0] + "\t" + sort + "\t" + s + "\r\n");
                        i++;
                        sort = sort - 100;
                    }
                }
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




