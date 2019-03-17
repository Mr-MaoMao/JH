package com.zjh.pdf2jpg;

import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.date.TimeInterval;
import com.xiaoleilu.hutool.io.FileUtil;
import com.zjh.pdf2jpg.ghostscript.GhostScriptConvter;
import com.zjh.pdf2jpg.icepdf.IceConvter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class Pdf2jpgApplicationTests {

    private byte[] pdfdata;

    @Before
    public void initdata() {
        pdfdata = FileUtil.readBytes(new File("D:\\thefuckingpdf\\卷宗能力平台集成手册.pdf"));
    }

    @Test
    public void contextLoads() {
        TimeInterval timer = DateUtil.timer();
        List<byte[]> icepdf = IceConvter.icepdf(pdfdata);
        int index = 0;
        for (byte[] data : icepdf) {
            FileUtil.writeBytes(data, new File("D:\\thefuckingpdf\\ice\\" + index++ + ".jpg"));
        }
        System.out.println("icepdf转换用时:" + timer.interval());
    }

    @Test
    public void testGhost() {
        TimeInterval timer = DateUtil.timer();
        GhostScriptConvter.pdf2Image(pdfdata, "pdf", "jpg", 300);
        System.out.println("ghostscripte转换用时:" + timer.interval());
    }

}
