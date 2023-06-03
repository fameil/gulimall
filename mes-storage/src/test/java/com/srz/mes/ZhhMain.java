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
    public void teststr(){
        String str="w22";
        System.out.println(str.substring(0,3));

    }
    //高频重码词
    @Test
    public void zhhsort() throws IOException {
        //去除重复的字，去简
        BufferedReader tuWordReader = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-2-20/词表.txt"));
        BufferedWriter tuWordRepeat = new BufferedWriter(new FileWriter("C:/Users/oWo/Desktop/2023-2-20/重码词.txt"));

        String line;
        List<String> ci = new ArrayList<>();
        while ((line = tuWordReader.readLine()) != null){
            ci.add(line);
        }

        //计算重码词组
        Map<String, Integer> map = new HashMap();
        String c;
        for (String str : ci) {
            c = str.split("\t")[2];
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            }else{
                map.put(c,1);
            }
        }

        List<String> ci2 = new ArrayList<>();
        for (String s : ci) {
            c = s.split("\t")[2];
            if (map.get(c)>1 && map.get(c)<100) {
                map.put(c,100);
            }else if (map.get(c)==100){
                tuWordRepeat.write(s+ "\r\n");
            }
        }
//        for (String s : ci) {
//            c = s.split("\t")[2];
//            if (map.get(c)==100) {
//                tuWordRepeat.write(s+ "\r\n");
//            }
//        }


        tuWordRepeat.close();
        tuWordReader.close();
    }

    //三码空码补
    @Test
    public void three_dan() throws IOException, NoSuchAlgorithmException {
        BufferedReader exist_ci = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-2-20/3_3.txt"));
        BufferedWriter out = new BufferedWriter(new FileWriter("C:/Users/oWo/Desktop/2023-2-20/补空码.txt"));
        String line;
        List<String> ci = new ArrayList<>();
        while ((line = exist_ci.readLine()) != null){
            ci.add(line);
        }

        //计算重码词组
        Map<String, String> map = new HashMap();
        String c;
        for (String str : ci) {
            c = str.split("\t")[2];
            if (c.length() >=3){
                c = c.substring(0,3);
                if (map.containsKey(c)) {
                }else{
                    map.put(c,str);
                }
            }
        }

        List<String> cie = new ArrayList<>();
        char a1 = 'a';
        char a2 = 'a';
        char a3 = 'a';
        for (int i = 0; i < 26; i++) {
            a2 = 'a';
            for (int j = 0; j < 26; j++) {
                a3 = 'a';
                for (int k = 0; k < 26; k++){
                    cie.add(a1+""+a2+""+a3);
                    a3++;
                }
                a2++;
            }
            a1++;
        }

        for (String s : cie) {
            if (map.containsKey(s)){

            } else {
                out.write("_" + "\t" +"100" + "\t"+s+"\r\n");
            }

        }

        out.close();
        exist_ci.close();
    }
    //二字词
    @Test
    public void two_ci() throws IOException{
        BufferedReader tuWordReader = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-2-20/tf3.txt"));
        BufferedWriter tuWordFormatWriter = new BufferedWriter(new FileWriter("C:/Users/oWo/Desktop/2023-2-20/二字词.txt"));

        List<String> cie = new ArrayList<>();

        char a2 = 'a';
        for (int i = 0; i < 26; i++) {
            cie.add(a2+"");
            a2++;
        }
        List<String> ci = new ArrayList<>();
        String line;
        while ((line = tuWordReader.readLine()) != null) {
            if (line.split("\t")[0].length() == 2){
                tuWordFormatWriter.write(line + "\r\n");
            }
        }

        tuWordFormatWriter.close();
        tuWordReader.close();
    }
    //一简四码字
    @Test
    public void one() throws IOException{
        BufferedReader tuWordReader = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-1-23/tuWordRepeat.txt"));
        BufferedWriter tuWordFormatWriter = new BufferedWriter(new FileWriter("C:/Users/oWo/Desktop/2023-1-23/2023年03月05日.txt"));

        List<String> cie = new ArrayList<>();

        char a2 = 'a';
        for (int i = 0; i < 26; i++) {
            cie.add(a2+"");
            a2++;
        }
        List<String> ci = new ArrayList<>();
        String line;
        while ((line = tuWordReader.readLine()) != null) {
            ci.add(line);
        }

        for (String s : cie) {
            int i = 1;
            int sort = 50000;
            for (String lie : ci) {
                String str2 = lie.split("\t")[2];
                if (str2.length()==4) {
                    if (i > 4){
                        break;
                    }
                    if (str2.startsWith(s)){
                        tuWordFormatWriter.write(lie.split("\t")[0] + "\t" + sort  + "\t"  + s + "\r\n");
                        i++;
                        sort = sort - 100;
                    }
                }
            }

        }
        tuWordFormatWriter.close();
        tuWordReader.close();
    }
    //鬼虎废词
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
    //发文sha-1
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
        BufferedReader tuWordReader = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-1-23/tuWordRepeat.txt"));
        BufferedReader del = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-1-23/2023年03月05日.txt"));
        BufferedWriter tuWordFormatWriter = new BufferedWriter(new FileWriter("C:/Users/oWo/Desktop/2023-1-23/2023年03月06日二简.txt"));

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
                if (str2.length()==4) {
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
        BufferedReader tuWordReader = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-2-20/单字全.txt"));
        BufferedWriter tuWordFormatWriter = new BufferedWriter(new FileWriter("C:/Users/oWo/Desktop/2023-2-20/单字全格式.txt"));

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
        BufferedReader tuWordReader = new BufferedReader(new FileReader("C:/Users/oWo/Desktop/2023-2-20/单字全格式.txt"));
        BufferedWriter tuWordRepeat = new BufferedWriter(new FileWriter("C:/Users/oWo/Desktop/2023-2-20/单字简.txt"));

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




