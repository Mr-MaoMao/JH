package com.zjh.pdf2jpg.ghostscript;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.xiaoleilu.hutool.io.FileUtil;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GhostScriptConvter {
    /**
     * ghost安装路径
     */
    private static String GS_BASEURL = "D:\\Program Files\\gs\\gs9.26\\bin\\gswin64c";

    /**
     * 临时文件存放路径
     */
    private static String fileConvertPath = "D:\\thefuckingpdf\\ghostscripte";

    public static File[] pdf2Image(byte[] pdf, String fileExt, String imageExt, Integer resolution) {
        String[] gsArgs = new String[]{GhostScriptConvter.GS_BASEURL, "-dSAFER", "-dBATCH", "-dNOPAUSE", "-dTextAlphaBits=4", "-dDownScaleFactor=2", "-dGraphicsAlphaBits=4"};
        List<String> argList = new ArrayList(Arrays.asList(gsArgs));
        if (!"jpg".equalsIgnoreCase(imageExt) && !"jpeg".equalsIgnoreCase(imageExt)) {
            argList.add("-sDEVICE=" + imageExt);
        } else {
            argList.add("-sDEVICE=jpeg");
            argList.add("-dJPEGQ=75");
        }

        if (resolution == null) {
            try {
                PdfReader pdfReader = new PdfReader(pdf);
                int pageCount = pdfReader.getNumberOfPages();
                if (pageCount > 0) {
                    Rectangle rectangle = pdfReader.getPageSize(1);
                    float width = rectangle.getWidth();
                    float height = rectangle.getHeight();
                    if (width <= 2479.0F && height <= 3508.0F) {
                        resolution = 300;
                    } else if ((double) width * 0.5D <= 2479.0D && (double) height * 0.5D <= 3508.0D) {
                        resolution = 150;
                    } else if ((double) width * 0.4D <= 2479.0D && (double) height * 0.4D <= 3508.0D) {
                        resolution = 120;
                    } else {
                        resolution = 72;
                    }

                }
            } catch (IOException var20) {
            }
        }

        argList.add("-r" + resolution);
        final String suffix = "." + imageExt;
        String basePath = GhostScriptConvter.fileConvertPath + File.separator + UUID.randomUUID();
        String imagePath = basePath + File.separator + "%d" + suffix;
        argList.add("-sOutputFile=" + imagePath);

        try {
            String pdfPath = GhostScriptConvter.byte2File(pdf, fileExt, basePath);
            System.out.println("pdf临时路径为:" + pdfPath);
            argList.add(pdfPath);
            gsArgs = (String[]) argList.toArray(new String[0]);
            Process proc = (new ProcessBuilder(gsArgs)).redirectErrorStream(true).start();

            while (proc.isAlive()) {
                List<String> output = IOUtils.readLines(proc.getInputStream());
                output.forEach((line) -> {
                    System.out.println(line);
                });
            }
            FileUtil.del(pdfPath);
            int exitValue;
            if ((exitValue = proc.waitFor()) != 0) {
                throw new RuntimeException();
            } else {
                File imageDirectory = new File(basePath);
                File[] imageFiles = imageDirectory.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.endsWith(suffix);
                    }
                });
                return imageFiles;
            }
        } catch (Exception var19) {
            return null;
        }
    }

    public static String byte2File(byte[] fileByte, String fileExt, String dirPath) {
        String filename = UUID.randomUUID() + "." + fileExt;
        String filepath = dirPath + File.separator + filename;
        FileUtil.writeBytes(fileByte, new File(filepath));
        return filepath;
    }
}
