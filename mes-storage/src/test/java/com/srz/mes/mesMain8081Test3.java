package com.srz.mes;

import com.srz.mes.service.ProductionDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author srz
 * @create 2022/8/29 22:40
 */

@SpringBootTest
public class mesMain8081Test3 {

    @Autowired
    ProductionDataService productionDataService;
    @Test
    public void feigntest() throws IOException{

    }

    @Test
    public void tiger7000() throws IOException {

        String filePathw = "C:/Users/oWo/Desktop/2022-10-27-ci/dan_out_end6.txt";
        String filePathw2 = "C:/Users/oWo/Desktop/2022-10-27-ci/ci_out_end6.txt";
       // filePathw2 = "C:/Users/oWo/Desktop/2022-10-27-ci/ci_out_endtest.txt";

        //FileInputStream fin = new FileInputStream("C:/Users/oWo/Desktop/2022-10-27-ci/ci_out_sort.txt");
        FileInputStream fin = new FileInputStream("C:/Users/oWo/Desktop/2022-10-27-ci/ci_out_code3.txt");
        FileInputStream fin2 = new FileInputStream("C:/Users/oWo/Desktop/2022-10-27-ci/dan_out_sort.txt");
        FileInputStream dw3 = new FileInputStream( "C:/Users/oWo/Desktop/2022-10-27-ci/delete.txt");

        InputStreamReader reader = new InputStreamReader(fin);
        InputStreamReader reader2 = new InputStreamReader(fin2);
        InputStreamReader deleteci = new InputStreamReader(dw3);

        BufferedReader buffReader = new BufferedReader(reader);
        BufferedReader buffReader2 = new BufferedReader(reader2);
        BufferedReader delci = new BufferedReader(deleteci);

        FileWriter fw = new FileWriter(filePathw, true);
        FileWriter fw2 = new FileWriter(filePathw2, true);

        List<String> cia = new ArrayList<>();
        List<String> ci = new ArrayList<>();
        List<String> ci1 = new ArrayList<>();
        List<String> ci2 = new ArrayList<>();
        List<String> ci3 = new ArrayList<>();
        List<String> cie = new ArrayList<>();
        List<String> ciSame = new ArrayList<>();
        String sci = "";


        Map<String, Integer> mapdel = new HashMap();
        while((sci = delci.readLine()) != null){
            mapdel.put(sci.split("\t")[0],1);
        }
        //导入词并去除重码词
        while((sci = buffReader.readLine())!=null){
//            if(mapdel.containsKey(sci.split("\t")[0])){
//
//            } else {
//                cia.add(sci);
//                ciSame.add(sci.split("\t")[2]);
//            }
            cia.add(sci);
        }
        //导入单字
        while((sci = buffReader2.readLine())!=null){
            cia.add(sci);
        }


        //排序
        cia.stream().sorted((s1,s2)->Integer.parseInt(s2.split("\t")[1]) - Integer.parseInt(s1.split("\t")[1])).forEach((s)->{
            ci.add(s);
            ci1.add(s.split("\t")[0]);
            ci2.add(s.split("\t")[1]);
            ci3.add(s.split("\t")[2]);
        });

        System.out.println("加载码表1加载完成============================================");

     //选出四码字
/*
        for (int i = 0; i < ci3.size(); i++) {
            if(ci3.get(i).length()==4){
                fw.write(ci.get(i)+"\r\n");
            }
        }
*/
        char a1 = 'a';
        char a2 = 'a';
        char a3 = 'a';
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

        System.out.println("加载编码完成============================================");
        Map<String, Integer> map = new HashMap();
        //计算重码词组
        for (String c : ciSame) {
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            }else{
                map.put(c,1);
            }
        }
        Map<String, Integer> map2 = new HashMap();
        for (String s : map.keySet()) {
            if(map.get(s) == 2){
                map2.put(s,map.get(s));
            }
        }
        int rangking = 20000000;
        int dlenght = 1;
        String len = "";
        List<String> same = new ArrayList<>();
        for (int i = 0; i < cie.size(); i++) {
            int a = 0;
            len = "^"+cie.get(i)+".*";
            for (int j = 0; j < ci3.size(); j++) {
                if (ci3.get(j).matches(len)) {
                    if (cie.get(i).length() == 1) {
                        //首码
                        if (ci1.get(j).length() == 1) {
                            //写入单字文件
                            fw.write(ci.get(j).replaceAll("[a-z]+", cie.get(i)) + "\r\n");
                            ci.remove(j);
                            ci1.remove(j);
                            ci2.remove(j);
                            ci3.remove(j);
                            j--;
                            a++;
                        }

                    } else {
                        if (cie.get(i).length() <= 2) {
                            if (ci1.get(j).length() == 1) {
                                if (dlenght > 1) {
                                    continue;
                                }
                                if (cie.get(i).length() == 2){
                                    //写入单字文件
                                    fw.write(ci.get(j).replaceAll("[a-z]+", cie.get(i)).replaceAll("[0-9]+", String.valueOf(rangking)) + "\r\n");
                                    rangking--;
                                } else {
                                    //写入单字文件
                                    fw.write(ci.get(j).replaceAll("[a-z]+", cie.get(i)) + "\r\n");
                                }
                                ci.remove(j);
                                ci1.remove(j);
                                ci2.remove(j);
                                ci3.remove(j);
                                dlenght++;
                                j--;
                                a++;
                            } else {
//                                if (map2.get(ci3.get(j)) == null) {
//                                    continue;
//                                }

                                //if (map2.get(ci3.get(j)) > 1){
                                    //if (map2.containsKey(ci3.get(j))) {

                                        //写入词库文件
                                        fw2.write(ci.get(j).replaceAll("[a-z]+", cie.get(i)) + "\r\n");

//                                        int temp =map2.get(ci3.get(j));
//                                        map2.remove(ci3.get(j));
//                                        map2.put(ci3.get(j),temp-1);

                                        ci.remove(j);
                                        ci1.remove(j);
                                        ci2.remove(j);
                                        ci3.remove(j);
                                        j--;
                                        a++;
                                    //}
                                //}
                            }

                        } else {
                            if (ci1.get(j).length() == 1) {
                                if (dlenght > 1) {
                                    continue;
                                }
                                if (cie.get(i).length() == 2){
                                    //写入单字文件
                                    fw.write(ci.get(j).replaceAll("[a-z]+", cie.get(i)).replaceAll("[0-9]+", String.valueOf(rangking)) + "\r\n");
                                    rangking--;
                                } else {
                                    //写入单字文件
                                    fw.write(ci.get(j).replaceAll("[a-z]+", cie.get(i)) + "\r\n");
                                }
                                dlenght++;
                                a++;
                            } else {
                                //if (map2.containsKey(ci3.get(j))) {
                                    fw2.write(ci.get(j).replaceAll("[a-z]+", cie.get(i)) + "\r\n");
                                    a++;
                                //}
                            }
                        }
                    }

                }
                if (a == 5){
                    break;
                }

            }

            ///================================================================
            dlenght = 1;
            if (i%100==0){
                System.out.println(cie.size()-i);
            }

        }
        //将剩下的写入文件
        for (String s : ci) {
            if(s.split("\t")[0].length()==1){
                try {
                    fw.write(s+"\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    fw2.write(s+"\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        fw.close();
        fw2.close();
        buffReader.close();
        buffReader2.close();
    }



    //==============================================
    @Test
    public void tiger7001() throws IOException {
        String filePath = "C:/Users/oWo/Desktop/2022-10-27-ci/ci_out_sort.txt";
        //filePath = "C:/Users/oWo/Desktop/2022-10-27-ci/ci_g.txt";
        String filePath2 = "C:/Users/oWo/Desktop/2022-10-27-ci/dan_out_sort.txt";
        String filePath3 = "C:/Users/oWo/Desktop/2022-10-27-ci/dan_out_sort.txt";
        FileInputStream dw3 = new FileInputStream( "C:/Users/oWo/Desktop/2022-10-27-ci/delete.txt");

        String filePathw = "C:/Users/oWo/Desktop/2022-10-27-ci/dan_out_end5.txt";
        String filePathw2 = "C:/Users/oWo/Desktop/2022-10-27-ci/ci_out_end5.txt";
        filePathw2 = "C:/Users/oWo/Desktop/2022-10-27-ci/ci_out_endtest.txt";

        FileInputStream fin = new FileInputStream(filePath);
        FileInputStream fin2 = new FileInputStream(filePath2);
        FileInputStream fin3 = new FileInputStream(filePath3);

        InputStreamReader deleteci = new InputStreamReader(dw3);

        BufferedReader delci = new BufferedReader(deleteci);

        InputStreamReader reader = new InputStreamReader(fin);
        InputStreamReader reader2 = new InputStreamReader(fin2);
        InputStreamReader reader3 = new InputStreamReader(fin3);

        BufferedReader buffReader = new BufferedReader(reader);
        BufferedReader buffReader2 = new BufferedReader(reader2);

        FileWriter fw = new FileWriter(filePathw, true);
        FileWriter fw2 = new FileWriter(filePathw2, true);

        List<String> cia = new ArrayList<>();
        List<String> ci = new ArrayList<>();
        List<String> ciout = new ArrayList<>();
        List<String> ci1 = new ArrayList<>();
        List<String> ci2 = new ArrayList<>();
        List<String> ci3 = new ArrayList<>();
        List<String> ci3t = new ArrayList<>();
        List<String> cie = new ArrayList<>();
        List<String> ciSame = new ArrayList<>();
        String sci = "";



        Map<String, Integer> mapdel = new HashMap();
        while((sci = delci.readLine()) != null){
            mapdel.put(sci.split("\t")[0],1);
        }
        //导入词并去除重码词
        while((sci = buffReader.readLine())!=null){
            if(mapdel.containsKey(sci.split("\t")[0])){

            } else {
                cia.add(sci);
                ciSame.add(sci.split("\t")[2]);
            }
        }


        //排序
        cia.stream().sorted((s1,s2)->Integer.parseInt(s2.split("\t")[1]) - Integer.parseInt(s1.split("\t")[1])).forEach((s)->{
            ci.add(s);
            ci1.add(s.split("\t")[0]);
            ci2.add(s.split("\t")[1]);
            ci3.add(s.split("\t")[2]);
            ci3t.add(s.split("\t")[2]);
        });

        System.out.println("加载码表1加载完成============================================");
        System.out.println("加载编码完成============================================");
        Map<String, Integer> map = new HashMap();
        //计算重码词组
        for (String c : ciSame) {
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            }else{
                map.put(c,1);
            }
        }

        int b = 0;
        Map<String, Integer> map2 = new HashMap();
        for (String s : map.keySet()) {
            if(map.get(s) > 1){
                map2.put(s,map.get(s));
            }
        }
        for (int i = 0; i < ci3.size(); i++) {
            if (map2.containsKey(ci3.get(i))){
                ciout.add(ci.get(i));

            }

        }

        System.out.println("map2:"+ map2.size());
        List<String> samelist = new ArrayList<>();

        //排序
        ciout.stream().sorted((s1,s2)->Integer.parseInt(s2.split("\t")[1]) - Integer.parseInt(s1.split("\t")[1])).forEach((s)->{
            samelist.add(s);
            try {
                if(s.split("\t")[0].length()>2){

                    fw2.write(s+"\r\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

//        Map<String,Integer> code = new LinkedHashMap<>();
//        for (int i = 0; i < samelist.size(); i++) {
//            code.put(samelist.get(i).split("\t")[2],i);
//        }
//
//        Object[] objects = code.keySet().toArray();
//        for (int i = 0; i < objects.length; i++) {
//            for (int j = 0; j < samelist.size(); j++) {
//                String sa = samelist.get(j).split("\t")[2];
//                String co = (String) objects[i];
//                if (sa.equals(co)) {
//                    fw2.write(samelist.get(j)+"\r\n");
//                    b++;
//                }
//            }
//            if (b%100==0){
//                System.out.println(b);
//            }
//        }



//        for (int i = 0; i < code.size(); i++) {
//            System.out.println(samelist.size());
//            System.out.println(code.get(i));
//            for (int j = 0; j < samelist.size(); j++) {
//                if (samelist.get(j).split("\t")[2].equals(code.get(i))) {
//                    fw2.write(samelist.get(j)+"\r\n");
//                }
//            }
//        }
        fw.close();
        fw2.close();
        buffReader.close();
        buffReader2.close();

    }


    @Test
    public void tiger3() throws IOException {
        String filePath = "C:/Users/oWo/Desktop/2022-10-27-ci/ci_out_sort.txt";
        //filePath = "C:/Users/oWo/Desktop/2022-10-27-ci/ci_g.txt";
        String filePath2 = "C:/Users/oWo/Desktop/2022-10-27-ci/dan_out_sort.txt";
        String filePath3 = "C:/Users/oWo/Desktop/2022-10-27-ci/dan_out_sort.txt";
        FileInputStream dw3 = new FileInputStream( "C:/Users/oWo/Desktop/2022-10-27-ci/delete.txt");

        String filePathw = "C:/Users/oWo/Desktop/2022-10-27-ci/dan_out_end5.txt";
        String filePathw2 = "C:/Users/oWo/Desktop/2022-10-27-ci/ci_out_end5.txt";
        filePathw2 = "C:/Users/oWo/Desktop/2022-10-27-ci/ci_out_code3.txt";

        FileInputStream fin = new FileInputStream(filePath);
        FileInputStream fin2 = new FileInputStream(filePath2);
        FileInputStream fin3 = new FileInputStream(filePath3);

        InputStreamReader deleteci = new InputStreamReader(dw3);

        BufferedReader delci = new BufferedReader(deleteci);

        InputStreamReader reader = new InputStreamReader(fin);
        InputStreamReader reader2 = new InputStreamReader(fin2);
        InputStreamReader reader3 = new InputStreamReader(fin3);

        BufferedReader buffReader = new BufferedReader(reader);
        BufferedReader buffReader2 = new BufferedReader(reader2);

        FileWriter fw = new FileWriter(filePathw, true);
        FileWriter fw2 = new FileWriter(filePathw2, true);

        List<String> cia = new ArrayList<>();
        List<String> ci = new ArrayList<>();
        List<String> ciout = new ArrayList<>();
        List<String> ci1 = new ArrayList<>();
        List<String> ci2 = new ArrayList<>();
        List<String> ci3 = new ArrayList<>();
        List<String> ci3t = new ArrayList<>();
        List<String> cie = new ArrayList<>();
        List<String> ciSame = new ArrayList<>();
        String sci = "";



        while((sci = buffReader.readLine()) != null){
            if(sci.split("\t")[0].length()>2){

                fw2.write(sci+"\r\n");
            }
        }




        fw.close();
        fw2.close();
        buffReader.close();
        buffReader2.close();

    }

}




