package com.srz.mes;

import com.srz.mes.service.ProductionDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author srz
 * @create 2022/8/29 22:40
 */
//@RunWith(SpringRunner.class)
@SpringBootTest
public class leetcodeTest {

    @Test
    public void test1() throws IOException {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);

    }

    @Test
    public void lettcode10(){
        String s = "aaa";
        String p = "a.*";

        Map<String,String> map = new HashMap<>();
        map.put(".",".");
        map.put("*","*");

        for (int i = 0; i < p.length(); i++) {
            if (!map.containsKey(p.charAt(i)+"")) {
                System.out.println(s.equals(p));
                return;
            }
        }
        String str = "";
        String strtemp = "";
        String ps = "";
        String pstemp = "";
        for (int i = 0; i < s.length(); i++) {
            str = str+s.charAt(i);
            for (int j = 0; j < str.length(); j++) {

            }
        }
        System.out.println(true);
    }
    @Test
    public void lettcode9(){
        int x = 12321;
        String sx = String.valueOf(x);
        int s2length = sx.length();
        int let = s2length/2;
        for (int k = 0; k < let ; k++) {
            if (sx.charAt(k)==sx.charAt(s2length-1-k)) {

            } else  {
                System.out.println(false);
                return;
            }
        }
        System.out.println(true);
        return;


    }
    @Test
    public void lettcode8(){
        String s = "  -0012a42";
        Long l1 = 0L;
        String s2 = "";
        boolean b = true;
        String sl1 = "";
        int result = 0;

        Map<String,Integer> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.put(i+"",i);
        }
        if(s.length()==1){
            if (map.containsKey(s)) {
                System.out.println(Integer.parseInt(s));
                return;
            }else {
                System.out.println(result);
                return;
            }
        }
        for (int i = 0; i < s.length(); i++) {
            s2=s.charAt(i)+"";

            if (l1==0 && s2.equals("0") && (i+1)<s.length()){
                if (map.containsKey(s.charAt(i+1)+"")) {
                    continue;
                }else {
                    break;
                }
            }
            if (l1==0 && s2.equals("-")){
                if (map.containsKey(s.charAt(i+1)+"")) {
                    b = false;
                    continue;
                }else {
                    break;
                }
            }
            if (l1==0 && s2.equals(" ")){
                continue;
            }
            if (l1==0 && s2.equals("+")){
                if (map.containsKey(s.charAt(i+1)+"")) {
                    continue;
                }else {
                    break;
                }
            }
            if (l1==0 && !map.containsKey(s2)){
                break;
            }
            if (s2.equals(".")){
                if (b!=true){
                    String s1 = String.valueOf(l1);
                    s1 = "-"+s1;
                    result = Integer.parseInt(s1);
                    System.out.println(result);
                    return;
                }else {
                    System.out.println(Math.toIntExact(l1));
                    return;
                }
            }
            if (map.containsKey(s2)) {
                sl1 = l1+s2;
                l1 = Long.parseLong(sl1);

            }
            if (l1>Integer.MAX_VALUE){
                if (b==true){
                    System.out.println(Integer.MAX_VALUE);
                }else {
                    System.out.println(Integer.MIN_VALUE);
                }
                return;
            }
            if (l1>0 &&(i+1)<s.length()){
                if (map.containsKey(s.charAt(i+1)+"")) {
                    continue;
                }else {
                    break;
                }
            }
        }

        if (b!=true) {
            String s1 = String.valueOf(l1);
            s1 = "-"+s1;
            result = Integer.parseInt(s1);
        }else {
            result = Math.toIntExact(l1);
        }
        System.out.println(result);
        //System.out.println(Integer.parseInt(String.valueOf(result)));
        
    }
    @Test
    public void lettcode7(){
        int x = 123;
        String s = String.valueOf(x);
        String s2 = s.charAt(0)+"";
        String sint = "";
        int slength = s.length();
        long l = 0;
        if (s2.equals("-")){
            for (int i = slength-1; i > 0; i--) {
                sint = sint+s.charAt(i);
            }
            l = Long.parseLong("-" + sint);
        } else {
            for (int i = slength-1; i >= 0; i--) {
                sint = sint+s.charAt(i);
            }
            l = Long.parseLong(sint);
        }
        if (l>Integer.MAX_VALUE || l<Integer.MIN_VALUE){
            System.out.println(0);
        } else {
            System.out.println(Integer.parseInt(String.valueOf(l)));
        }



    }
    @Test
    public void lettcode6(){
        String s = "PAYPALISHIRING";
        int numRows=2;
        String s2 = "";
        if (numRows==1){
            System.out.println(s);
        }

        List<String> list = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            list.add("");
        }

        int i1 = 0;
        boolean b = true;
        for (int i = 0; i < s.length(); i++) {
            list.set(i1, list.get(i1)+s.charAt(i));
            if (b){
                i1++;
                if(i1==numRows){
                    i1=i1-2;
                    b=false;
                }
            }else {
                i1--;
                if(i1<0){
                    i1=i1+2;
                    b=true;
                }
            }

        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            s2=s2+list.get(i2);
        }
        System.out.println(s2);


    }
    @Test
    public void lettcode5() throws IOException {
        long l = System.currentTimeMillis();
        String s = "abcdedcbaqwewqeewqe";


        String s2 = "";
        String max = s.charAt(0)+"";
        int slength = s.length();

        if (slength==1){
            System.out.println(s);
        }
        if (slength==2){
            if (s.charAt(0)==s.charAt(1)) {
                System.out.println(s);
            }
        }
        for (int i = 0; i < slength; i++) {
            s2=s.charAt(i)+"";

            for (int j = i+1; j < slength; j++) {
                s2=s2+""+s.charAt(j);
                int s2length=s2.length();
                int let = s2length/2;
                for (int k = 0; k < let ; k++) {
                    if (s2.charAt(k)==s2.charAt(s2length-1-k)) {
                        if (k==let-1){
                            if (s2length>max.length()){
                                max=s2;
                            }
                        }
                    } else  {
                        break;
                    }
                }
            }
            if (max.length()==slength-i){
                break;
            }
        }
        System.out.println(max);
        long l2 = System.currentTimeMillis();
        System.out.println(l2-l);
    }

}
