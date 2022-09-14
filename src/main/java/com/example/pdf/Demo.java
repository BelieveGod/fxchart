package com.example.pdf;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.CaptionSide;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
 * @author LTJ
 * @date 2022/9/2
 */
public class Demo {

    public static void main(String[] args) throws IOException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String []fontFamilies = ge.getAvailableFontFamilyNames();
        for (String fontFamily : fontFamilies) {
//            System.out.println("fontFamily = " + fontFamily);
        }

        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for (Mixer.Info info : mixerInfo) {
            System.out.printf("name:%s desc:%s version:%s vendor:%s %n", info.getName(), info.getDescription(), info.getVersion(), info.getVendor());
        }
        if(true){
//            return;
        }
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File deskTop = fileSystemView.getHomeDirectory();
        File pdf = FileUtil.file(deskTop, "demo.pdf");
        PdfFont font = PdfFontFactory.createFont("/font/msyh.ttc,0", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        PdfWriter pdfWriter = new PdfWriter(pdf);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
//        pdfDocument.addFont(font);
        Document document = new Document(pdfDocument);
        document.setFont(font);
        /* begin ==========表格============= */
        String trainNo = "G5001";
        String traceTime = "2022-08-01 14:00:00";
        Table table = new Table(4);
        table.setTextAlignment(TextAlignment.CENTER);
        table.setCaption(new Div().add(new Paragraph("限界门确认单")).setTextAlignment(TextAlignment.CENTER), CaptionSide.TOP);

        table.addCell(new Cell().add(new Paragraph(StrUtil.format("车号：{}", trainNo))));
        table.addCell(new Cell(1, 3).add(new Paragraph(StrUtil.format("检查时间：{}", traceTime))));

        URL imgUrl = Demo.class.getResource("/img/outLine.png");
        ImageData imageData = ImageDataFactory.create(imgUrl);
        table.addCell(new Cell(1, 4).setHeight(UnitValue.createPercentValue(25)).add(new Image(imageData)));

        table.addCell(new Cell(1, 1).add(new Paragraph("序号")));
        table.addCell(new Cell(1, 3));

        table.addCell(new Cell(1, 1).add(new Paragraph("位置")));
        table.addCell(new Cell(1, 3));

        table.addCell(new Cell(1, 1).add(new Paragraph("超限")));
        table.addCell(new Cell(1, 3));

        table.addCell(new Cell(1, 1).add(new Paragraph("检测结果")));
        table.addCell(new Cell(1, 3));

        table.addCell(new Cell(2, 1).add(new Paragraph("签字")));
        table.addCell(new Cell(1, 3).setMinHeight(14));

        table.addCell(new Cell(1, 3).setMinHeight(14));

        document.add(table);
        /* end ============表格============ */
        document.close();

    }
}
