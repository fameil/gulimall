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
import java.util.stream.Collectors;

/**
 * @author srz
 * @create 2022/8/29 22:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class mesMain8081Test3 {

    @Autowired
    ProductionDataService productionDataService;
    @Test
    public void tiger70002() throws IOException {
        System.out.println("决".length());
    }

    @Test
    public void tiger7000() throws IOException {
        String filePath = "C:/Users/oWo/Desktop/2022-10-27-ci/ci_out_sort.txt";
        String filePath2 = "C:/Users/oWo/Desktop/2022-10-27-ci/dan_out.txt";

        String filePathw = "C:/Users/oWo/Desktop/2022-10-27-ci/dan_out_end.txt";
        String filePathw2 = "C:/Users/oWo/Desktop/2022-10-27-ci/ci_out_end.txt";

        FileInputStream fin = new FileInputStream(filePath);
        FileInputStream fin2 = new FileInputStream(filePath2);

        InputStreamReader reader = new InputStreamReader(fin);
        InputStreamReader reader2 = new InputStreamReader(fin2);

        BufferedReader buffReader = new BufferedReader(reader);
        BufferedReader buffReader2 = new BufferedReader(reader2);

        FileWriter fw = new FileWriter(filePathw, true);
        FileWriter fw2 = new FileWriter(filePathw2, true);

        List<String> cia = new ArrayList<>();
        List<String> ci = new ArrayList<>();
        List<String> ci1 = new ArrayList<>();
        List<String> ci2 = new ArrayList<>();
        List<String> ci3 = new ArrayList<>();
        List<String> cie = new ArrayList<>();
        String sci = "";
        //单字排首位
/*
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
*/

        while((sci = buffReader.readLine())!=null){
            cia.add(sci);
        }
        while((sci = buffReader2.readLine())!=null){
            cia.add(sci);
        }


        //排序
        cia.stream().sorted((s1,s2)->Integer.parseInt(s2.split("\t")[1]) - Integer.parseInt(s1.split("\t")[1])).forEach((s)->{
            ci.add(s);
            ci1.add(s.split("\t")[0]);
            ci2.add(s.split("\t")[1]);
            ci3.add(s.split("\t")[2]);
/*            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            //System.out.println(s);
        });


        System.out.println("加载码表排序完成============================================");

        //分割字符
/*        while((sci = buffReader.readLine())!=null){
            ci.add(sci);
            ci1.add(sci.split("\t")[0]);
            ci2.add(sci.split("\t")[1]);
            ci3.add(sci.split("\t")[2]);
        }*/
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
        //try {Thread.sleep(5000); } catch (Exception e) {}
        System.out.println("加载编码完成============================================");

        String len = "";
        for (int i = 0; i < cie.size(); i++) {
            int a = 0;
            len = "^"+cie.get(i)+".*";
            for (int j = 0; j < ci3.size(); j++) {
                if(ci3.get(j).matches(len)){
                    if(cie.get(i).length()==1){
                        //首码
                        //if(a==0){
                            if (ci1.get(j).length()==1){
                                fw.write(ci.get(j).replaceAll("[a-z]+", cie.get(i))+"\r\n");
                                ci.remove(j);
                                ci1.remove(j);
                                ci2.remove(j);
                                ci3.remove(j);
                                j--;
                            }
                        //}
//                        else if(ci1.get(j).length()==1){
//                            continue;
//                        }else {
//                            fw2.write(ci.get(j).replaceAll("[a-z]+", cie.get(i))+"\r\n");
//                            ci.remove(j);
//                            ci1.remove(j);
//                            ci2.remove(j);
//                            ci3.remove(j);
//                            j--;
//                        }

                    } else {
                        if (cie.get(i).length()<=2){
                            if (ci1.get(j).length()==1){
                                fw.write(ci.get(j).replaceAll("[a-z]+", cie.get(i))+"\r\n");
                                ci.remove(j);
                                ci1.remove(j);
                                ci2.remove(j);
                                ci3.remove(j);
                                j--;
                            } else {
                                fw2.write(ci.get(j).replaceAll("[a-z]+", cie.get(i))+"\r\n");
                                ci.remove(j);
                                ci1.remove(j);
                                ci2.remove(j);
                                ci3.remove(j);
                                j--;
                            }
                        }else {
                            if (ci1.get(j).length()==1) {
                                fw.write(ci.get(j).replaceAll("[a-z]+", cie.get(i)) + "\r\n");
                            } else {
                                fw2.write(ci.get(j).replaceAll("[a-z]+", cie.get(i))+"\r\n");
                            }
                        }
                    }
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

    //@Test
    public void tiger() throws IOException {
        String filePath = "C:/Users/oWo/Desktop/2022-10-27-ci/ci_out_sort.txt";
        String filePath2 = "C:/Users/oWo/Desktop/2022-10-27-ci/dan_out2.txt";

        String filePathw = "C:/Users/oWo/Desktop/2022-10-27-ci/duoduo.txt";

        FileInputStream fin = new FileInputStream(filePath);
        FileInputStream fin2 = new FileInputStream(filePath2);

        InputStreamReader reader = new InputStreamReader(fin);
        InputStreamReader reader2 = new InputStreamReader(fin2);

        BufferedReader buffReader = new BufferedReader(reader);
        BufferedReader buffReader2 = new BufferedReader(reader2);


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
            //修改位置
            //fw.write(sci.split("\t")[0]+"\t"+sci.split("\t")[2]+"\t"+sci.split("\t")[1] +"\r\n");
        }


        while((sci = buffReader2.readLine())!=null){
            ci1.add(sci.split("\t")[0]);
            ci2.add(sci.split("\t")[1]);
            ci3.add(sci.split("\t")[2]);
            ci.add(sci);
        }

        //排序
/*
        ci.stream().sorted((s1,s2)->Integer.parseInt(s2.split("\t")[1]) - Integer.parseInt(s1.split("\t")[1])).forEach((s)->{
            try {
                fw.write(s+"\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
*/
        //多多格式
        List<String> list = ci.stream()
                .sorted((s1, s2) -> Integer.parseInt(s2.split("\t")[1]) - Integer.parseInt(s1.split("\t")[1]))
                .collect(Collectors.toList());

        for (int i = 0; i < list.size(); i++) {
            //修改位置
            fw.write(list.get(i).split("\t")[0]+"\t"+list.get(i).split("\t")[2]+"\r\n");
        }

        //去重//
//        for (int i = 0; i < ci.size(); i++) {
//            for (int j = i+1; j < ci.size(); j++) {
//                if (ci1.get(i).equals(ci1.get(j))){
//                    if (ci3.get(i).length()<=ci3.get(j).length()){
//                        ci.remove(j);
//                        ci1.remove(j);
//                        ci3.remove(j);
//                        j--;
//                    }else {
//                        ci.remove(j);
//                        ci1.remove(j);
//                        ci3.remove(j);
//                        j--;
//                    }
//                }
//            }
//
//            if (i%100==0){
//                System.out.println(ci.size()-i);
//            }
//        }

/*        for (String s : ci) {
            fw.write(s+"\r\n");
        }*/
        System.out.println("加载词库完成============================================");
        fw.close();
        buffReader.close();
    }


}




