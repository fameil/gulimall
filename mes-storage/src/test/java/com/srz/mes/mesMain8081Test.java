package com.srz.mes;

import com.srz.mes.entity.ProductionDataEntity;
import com.srz.mes.service.ProductionDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author srz
 * @create 2022/8/29 22:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class mesMain8081Test {

    @Autowired
    ProductionDataService productionDataService;


    //@Test
    public void tiger7000() throws IOException {
        String filePath = "C:/Users/oWo/Desktop/7000.txt";

        String filePathw = "C:/Users/oWo/Desktop/7000_out_p.txt";

        FileInputStream fin = new FileInputStream(filePath);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);

        FileWriter fw = new FileWriter(filePathw, true);

        List<String> ci = new ArrayList<>();
        List<String> ci1 = new ArrayList<>();
        List<String> ci2 = new ArrayList<>();
        List<String> ci3 = new ArrayList<>();
        List<String> cie = new ArrayList<>();
        String sci = "";
        //单字排首位
        int max = 20000000;
        while((sci = buffReader.readLine())!=null){
            //ci.add(sci);
            //ci1.add(sci.split("\t")[0]);
            //ci2.add(sci.split("\t")[1]);
           //ci3.add(sci.split("\t")[2]);

            max--;
            fw.write(sci.replaceAll("[0-9]+", max+"")+"\r\n");
        }
        System.out.println("加载词库完成============================================");




/*      //选出四码字
        for (int i = 0; i < ci3.size(); i++) {
            if(ci3.get(i).length()==4){
                fw.write(ci.get(i)+"\r\n");
            }
        }
*/


/*
        char a1 = 'a';
        char a2 = 'a';
        char a3 = 'a';
        char a4 = 'a';
*/
/*        for (int i = 0; i < 26; i++) {
            cie.add(a1+"");
            a1++;
        }*/
/*

        a1 = 'a';
        for (int i = 0; i < 26; i++) {
            a2 = 'a';
            for (int j = 0; j < 26; j++) {
                cie.add(a1+""+a2);
                a2++;
            }
            a1++;
        }
        a1 = 'a';
        for (int i = 0; i < 26; i++) {
            a2 = 'a';
            for (int j = 0; j < 26; j++) {
                a3 = 'a';
                for (int l = 0; l <26 ; l++) {
                    cie.add(a1+""+a2+a3);
                    a3++;
                }
                a2++;
            }
            a1++;
        }
        a1 = 'a';

*/
/*        for (int i = 0; i < 26; i++) {
            a2 = 'a';
            for (int j = 0; j < 26; j++) {
                a3 = 'a';
                for (int l = 0; l <26 ; l++) {
                    a4 = 'a';
                    for (int m = 0; m < 26; m++) {
                        cie.add(a1+""+a2+a3+a4);
                        a4++;
                    }
                    a3++;
                }
                a2++;
            }
            a1++;
        }*/
/*

        System.out.println("加载编码完成============================================");

        String len = "";
        for (int i = 0; i < cie.size(); i++) {
            int a = 0;
            for (int j = 0; j < ci3.size(); j++) {
                len = "^"+cie.get(i)+".*";
                if(ci3.get(j).matches(len)){
                    fw.write(ci.get(j).replaceAll("[a-z]+", cie.get(i))+"\r\n");
                    a++;
                }
                if (a == 6){
                    break;
                }
            }
            if (i%100==0){
                System.out.println(cie.size()-i);
            }
        }
*/





        fw.close();


        buffReader.close();



    }

    @Test
    public void tiger() throws IOException {
        String filePath = "C:/Users/oWo/Desktop/dan.txt";

        String filePathw = "C:/Users/oWo/Desktop/dan_out.txt";

        FileInputStream fin = new FileInputStream(filePath);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);

        FileWriter fw = new FileWriter(filePathw, true);

        List<String> ci = new ArrayList<>();
        List<String> ci1 = new ArrayList<>();
        List<String> ci2 = new ArrayList<>();
        List<String> ci3 = new ArrayList<>();
        List<Integer> ciOut = new ArrayList<>();
        String sci = "";

        while((sci = buffReader.readLine())!=null){
            ci.add(sci);
            ci1.add(sci.split("\t")[0]);
            ci2.add(sci.split("\t")[1]);
            ci3.add(sci.split("\t")[2]);
        }
        for (int i = 0; i < ci.size(); i++) {
            for (int j = i+1; j < ci.size(); j++) {
                if (ci1.get(i).equals(ci1.get(j))){
                    if (ci3.get(i).length()<=ci3.get(j).length()){
                        ci.remove(j);
                        ci1.remove(j);
                        ci3.remove(j);
                        j--;
                    }else {
                        ci.remove(j);
                        ci1.remove(j);
                        ci3.remove(j);
                        j--;
                    }
                }
            }

            if (i%100==0){
                System.out.println(ci.size()-i);
            }
        }

        for (String s : ci) {
            fw.write(s+"\r\n");
        }


        System.out.println("加载词库完成============================================");

        fw.close();


        buffReader.close();





    }


    @Test
    public void contextLoadabc() throws IOException {
        System.out.println("abc");
    }

    @Test
    public void contextLoadsJSON() throws IOException {
/*
        String filePath = "C:/Users/oWo/Desktop/2.jpg";

        String filePathw = "C:/Users/oWo/Desktop/3.jpg";

        FileInputStream fin = new FileInputStream(filePath);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);


        BufferedOutputStream bos = null;
        FileOutputStream fos = null;

        fos = new FileOutputStream(filePathw);
        bos = new BufferedOutputStream(fos);

        byte[] img = null;
        //获取数据长度
        img = new byte[fin.available()];
        //获取数据存入数组中
        fin.read(img);
        //将数组转成字符串
        byte[] encode = Base64.getEncoder().encode(img);

        bos.write(encode);
        

        String sci;
        while((sci = buffReader.readLine())!=null){
            //将字符串转成数组
            byte[] bytes = sci.getBytes(StandardCharsets.UTF_8);
            //将数组转成图片格式
            byte[] decode = Base64.getDecoder().decode(bytes);
            //写入文件
            bos.write(decode);

        }



        buffReader.close();
        bos.close();*/
    }



    @Test
    public void contextLoads2() throws IOException {

      /*  String filePath = "C:/Users/oWo/Desktop/abcd.txt";

        String filePathw = "C:/Users/oWo/Desktop/abcdff.txt";

        FileInputStream fin = new FileInputStream(filePath);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);



        FileWriter fw = new FileWriter(filePathw, true);
        BufferedWriter bw = new BufferedWriter(fw);






        String strTmp = "";
        String strTmp2 = "";

        String temp = "";
        String temp2 = "";

        String str;
        String str2;
        String len;

        int k = 0;

        String regex = "[\t]";

        List<String> ci = new ArrayList<>();
        List<String> ci1 = new ArrayList<>();
        List<String> ci2 = new ArrayList<>();
        List<String> ci3 = new ArrayList<>();
        List<String> ciOut = new ArrayList<>();
        String sci = "";

        int max=199999;
        while((sci = buffReader.readLine())!=null){
            ci.add(sci);
            ci1.add(sci.split("\t")[0]);
            ci2.add(sci.split("\t")[1]);
            ci3.add(sci.split("\t")[2]);
*//*            length = sci.split("\t")[0].length();
            if (length == 1){
                //bw.write(sci+"\r\n");
                if (sci.split("\t")[2].length()<4){
                 ci.add(sci);
                    ci2.add(sci.split("\t")[1]);
                    ci3.add(sci.split("\t")[2]);

                    max--;
                    bw.write(sci.replaceAll("[0-9]+", max+"")+"\r\n");
                }
            }else {
               // ci.add(sci);
               // ci3.add(sci.split("\t")[2]);
               // if (sci.split("\t")[2].length()==4){
                    //bw.write(sci+"\r\n");
              //  }
            }*//*
        }

        int size = ci.size();
        String mix = "";
        int length;
        for (int i = 0; i < size; i++) {
            mix = ci.get(i);
            length = ci3.get(i).length();
            for (int i1 = (i+1); i1 < size; i1++) {
                if (ci1.get(i).equals(ci1.get(i1))){
                    if (length > ci3.get(i1).length()){
                        mix = ci.get(i1);
                        length = ci3.get(i1).length();
                    }
                }
            }
            ciOut.add(mix);
            if (i%10==0){
                System.out.println(size-i);
            }
        }

        for (String s : ciOut) {
            bw.write(s+"\r\n");
        }




        System.out.println("加载词库完成============================================");
*//*
        char a1 = 'a';
        char a2 = 'a';
        char a3 = 'a';
        char a4 = 'a';

        for (int i = 0; i < 26; i++) {
            cie.add(a1+"");
            a1++;
        }
        a1 = 'a';
        for (int i = 0; i < 26; i++) {
            a2 = 'a';
            for (int j = 0; j < 26; j++) {
                cie.add(a1+""+a2);
                a2++;
            }
            a1++;
        }
        a1 = 'a';
        for (int i = 0; i < 26; i++) {
            a2 = 'a';
            for (int j = 0; j < 26; j++) {
                a3 = 'a';
                for (int l = 0; l <26 ; l++) {
                    cie.add(a1+""+a2+a3);
                    a3++;
                }
                a2++;
            }
            a1++;
        }
        a1 = 'a';


        for (int i = 0; i < 26; i++) {
            a2 = 'a';
            for (int j = 0; j < 26; j++) {
                a3 = 'a';
                for (int l = 0; l <26 ; l++) {
                    a4 = 'a';
                    for (int m = 0; m < 26; m++) {
                        cie.add(a1+""+a2+a3+a4);
                        a4++;
                    }
                    a3++;
                }
                a2++;
            }
            a1++;
        }



        System.out.println("加载编码完成============================================");


        for (int i = 0; i < cie.size(); i++) {
            int a = 0;
            for (int j = 0; j < ci3.size(); j++) {
                len = "^"+cie.get(i)+".*";
                if(ci3.get(j).matches(len)){
                    bw.write(ci.get(j).replaceAll("[a-z]+", cie.get(i))+"\r\n");
                    //System.out.println(ci.get(j).replaceAll("[a-z]+", cie.get(i))+"\r\n");
                    a++;
                }
                if (a == 6){
                    break;
                }

            }

            if (i%10==0){
                System.out.println(cie.size()-i);
            }
        }
*//*




*//*


        String[] split;
        String[] split2;
        for (int i = 0; i < ci.size(); i++) {
            //str = ci.get(i).replaceAll("\\s*", "").replaceAll("[^(A-Za-z)]", "");
            split = ci.get(i).split("\t");
            str = split[2];
            len = "^"+str+".*";
            int a = 0;
            for (int j = i; j < ci.size(); j++) {
                //str2 = ci.get(j).replaceAll("\\s*", "").replaceAll("[^(A-Za-z)]", "");
                split2 = ci.get(j).split("\t");
                str2 = split2[2];
                if(str2.matches(len)){
                    bw.write(ci.get(j).replaceAll("[a-z]+", str)+"\r\n");
                    //System.out.println(ci.get(j).replaceAll("[a-z]+", str)+"\r\n");
                    a++;
                }
                if (a == 6){
                    break;
                }

            }
            if (k%10==0){
                System.out.println(k);
            }
            k++;

        }

*//*



        bw.close();
        fw.close();


        buffReader.close();


*/
    }
















}
