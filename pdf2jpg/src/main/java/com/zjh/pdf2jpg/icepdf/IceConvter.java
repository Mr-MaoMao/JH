package com.zjh.pdf2jpg.icepdf;

import com.xiaoleilu.hutool.collection.CollUtil;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class IceConvter {

    /**
     * 图片旋转角度
     */
    private static final float rotation = 0f;
    /**
     * 图片缩放比 2.7f 等于 200dpi
     */
    private static final float scale = 2.7f;

    public static List<byte[]> icepdf(byte[] pdfData) {
        List<byte[]> res = CollUtil.newArrayList();
        Document doc = null;
        BufferedImage bim = null;
        try {
            doc = new Document();
            InputStream inputStream = new ByteArrayInputStream(pdfData);
            doc.setInputStream(inputStream, null);
            int pageNum = doc.getNumberOfPages();
            System.out.println("总页数为"+pageNum);
            for (int i = 0; i < pageNum; i++) {
                // 3、pdf -> jpg
                bim = (BufferedImage) doc.getPageImage(i,
                        GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX,
                        rotation, scale);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(bim, "jpg", os);
                byte[] datas = os.toByteArray();
                res.add(datas);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("转换出错了");
            return res;
        } finally {
            if (doc != null) {
                doc.dispose();
                doc = null;
            }
            if (bim != null) {
                bim.flush();
                bim = null;
            }
            System.gc();
        }
        return res;
    }
}
