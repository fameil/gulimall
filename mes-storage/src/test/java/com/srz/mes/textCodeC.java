package com.srz.mes;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author srz
 * @create 2022/8/29 22:40
 */

@SpringBootTest
public class textCodeC {

    @Test
    public void test1()  {
        String s = "天地竟是如此之宁静，大风拂过了空旷的天泉桥，在四周传来的阵阵风声中，王守仁高声吟道：无善无恶心之体，有善有恶意之动。知善知恶是良知，为善去恶是格物。钱德洪与王畿一言不发，屏气凝神，记下了这四句话。此即为所谓心学四诀，流传千古，至今不衰。吟罢，王守仁仰首向天，大笑之间飘然离去：天地虽大，但有一念向善，心存良知，虽凡夫俗子，皆可为圣贤！";
        System.out.println(s.hashCode());
    }



}




