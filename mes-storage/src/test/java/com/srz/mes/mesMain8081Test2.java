package com.srz.mes;

import com.srz.mes.service.ProductionDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author srz
 * @create 2022/8/29 22:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class mesMain8081Test2 {

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


}




