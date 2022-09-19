package com.example.poi.xwpf;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author LTJ
 * @date 2022/9/16
 */
public class XwpfDemo {

    public static void main(String[] args) throws IOException, InvalidFormatException {
        String[] colors = new String[]{"000000", "888888", "eeeeee"};

        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File homeDirectory = fileSystemView.getHomeDirectory();
        File docx = FileUtil.file(homeDirectory, "output.docx");
        XWPFDocument xwpfDocument = new XWPFDocument();
        CTBody body = xwpfDocument.getDocument().getBody();
        if (!body.isSetSectPr()) {
            body.addNewSectPr();
        }
        CTSectPr sectPr = body.getSectPr();
        if (!sectPr.isSetPgSz()) {
            sectPr.addNewPgSz();
        }
        CTPageSz pgSz = sectPr.getPgSz();
        pgSz.setOrient(STPageOrientation.LANDSCAPE);
        pgSz.setW(BigInteger.valueOf(842 * 20));
        pgSz.setH(BigInteger.valueOf(595 * 20));

        XWPFTable table = xwpfDocument.createTable(52,8);
        table.setWidth("100%");

        {
            XWPFTableRow row = table.getRow(0);
            // 设置行高度
            row.setHeight(50*20);
            List<XWPFTableCell> tableCells = row.getTableCells();
            mergeCol(row, 0, tableCells.size()-1);
            for(int i=0;i<tableCells.size();i++){
                setNoBorder(tableCells.get(i));
            }
            XWPFTableCell cell = tableCells.get(0);
            System.out.println("cell.getParagraphs().size() = " + cell.getParagraphs().size());
            XWPFParagraph paragraph = cell.getParagraphArray(0);
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(14);
            run.setBold(true);
            run.setText("轮径值及轮对外形尺寸测量记录表");
        }

        {
            XWPFTableRow row = table.getRow(1);
            // 设置行高度
            row.setHeight(50*20);
            List<XWPFTableCell> tableCells = row.getTableCells();
            mergeCol(row, 0, tableCells.size()-1);
            for(int i=0;i<tableCells.size();i++){
                setNoBorder(tableCells.get(i));
            }
            XWPFTableCell cell = tableCells.get(0);
            System.out.println("cell.getParagraphs().size() = " + cell.getParagraphs().size());
            XWPFParagraph paragraph = cell.getParagraphArray(0);
            paragraph.setAlignment(ParagraphAlignment.DISTRIBUTE);
            XWPFRun run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setText("列车号：");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setUnderline(UnderlinePatterns.SINGLE);
            run.setText("   ");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setText("       ");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setText("修程：");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setUnderline(UnderlinePatterns.SINGLE);
            run.setText("   ");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setText("       ");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setText("检修班组：");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setUnderline(UnderlinePatterns.SINGLE);
            run.setText("   ");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setText("       ");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setText("作业日期：");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setUnderline(UnderlinePatterns.SINGLE);
            run.setText("   ");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setText("年");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setUnderline(UnderlinePatterns.SINGLE);
            run.setText("   ");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setText("月");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setUnderline(UnderlinePatterns.SINGLE);
            run.setText("   ");

            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setUnderline(UnderlinePatterns.SINGLE);
            run.setText("   ");
        }

        {
            XWPFTableRow row = table.getRow(2);
            // 设置行高度
            row.setHeight(50*20);
            String[] headers = new String[]{"车号", "轮位", "轮缘高", "轮缘厚", "轮径", "同轴轮径差", "同转向架轮径差", "同车厢轮径差"};
            List<XWPFTableCell> tableCells = row.getTableCells();
            for(int i=0;i<tableCells.size();i++){
                XWPFTableCell xwpfTableCell = tableCells.get(i);
                xwpfTableCell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                xwpfTableCell.setColor("f2f2f2");
                XWPFParagraph paragraph = xwpfTableCell.getParagraphArray(0);
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun run = paragraph.createRun();
                run.setFontFamily("宋体");
                run.setFontSize(12);
                run.setBold(true);
                run.setText(headers[i % headers.length]);

            }
        }

        int carriageSize=6;
        int wheelSize=48;
        int dataStartRow=3;
        List<XWPFTableRow> rows = table.getRows();
        int dataEndRow=rows.size()-2;
        int totalDataRows = dataEndRow - dataStartRow+1;


        for(int i=0;i<totalDataRows;i++){
            int curRow = dataStartRow + i;
            for(int j=0;j<2;j++){
                if(j==0){
                    if(i%8==0){
                        XWPFTableCell cell = table.getRow(curRow).getCell(j);
                        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                        XWPFParagraph paragraph = cell.getParagraphArray(0);
                        paragraph.setAlignment(ParagraphAlignment.CENTER);
                        XWPFRun run = paragraph.createRun();
                        run.setFontFamily("宋体");
                        run.setFontSize(10);
                        run.setBold(true);
                        int carriageNum = i / 8 + 1;
                        run.setText("车厢"+carriageNum);
                        merge(table, curRow, curRow + 7, j, j);
                    }
                }else if(j==1){
                    XWPFTableCell cell = table.getRow(curRow).getCell(j);
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                    XWPFParagraph paragraph = cell.getParagraphArray(0);
                    paragraph.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun run = paragraph.createRun();
                    run.setFontFamily("宋体");
                    run.setFontSize(10);
                    run.setBold(true);
                    int carriageNum = i + 1;
                    run.setText("车轮"+carriageNum);
                }
            }
        }

        {
            int remarkRow = rows.size() - 1;
            XWPFTableRow row = table.getRow(remarkRow);
            row.setHeight(100*20);
            XWPFTableCell cell = row.getCell(0);
            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            XWPFParagraph paragraph = cell.getParagraphArray(0);
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setText("备注");
            // ===
            cell = row.getCell(1);
            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            paragraph = cell.getParagraphArray(0);
            paragraph.setAlignment(ParagraphAlignment.LEFT);
            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setText("标准值：车轮直径≥770mm；轮缘厚度：23～33 mm；轮缘高度：27～35 mm；同轴差≤2mm；同架差≤4mm；同车差≤7mm。");

            paragraph= cell.addParagraph();
            paragraph.setAlignment(ParagraphAlignment.LEFT);
            run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setText("填写要求：①轮径值及轮对外形尺寸由动检数据导出打印，打印时注意表格中的数字格式，请勿出现字体色彩差异、下划线符号等现象。②对于超标准数据，需人工进行复测，复测后将测量值填写在超限数值旁。③如使用到测量设备需填写出厂编号及测量记录人员，未使用划 “/”。");
            merge(table,remarkRow,remarkRow,1,7);
        }

        {
            XWPFParagraph paragraph = xwpfDocument.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setText("测量设备名称：  车辆轮径测量仪      出厂编号           测量人员：            记录人员：       ");
        }
        {
            XWPFParagraph paragraph = xwpfDocument.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setFontFamily("宋体");
            run.setFontSize(10);
            run.setBold(true);
            run.setText("测量设备名称： 车轮第四种检查器      出厂编号           测量人员：            记录人员：      ");
        }

        xwpfDocument.write(new FileOutputStream(docx));
        xwpfDocument.close();
    }




    private static void merge(XWPFTable table,int startRow,int endRow,int startCol,int endCol){
        Assert.isTrue(startRow <= endRow);
        Assert.isTrue(startCol <= endCol);
        int rowSpan = endRow - startRow + 1;
        int colSpan = endCol - startCol+1;

        if(rowSpan>1){
            XWPFTableCell cell = table.getRow(startRow).getCell(startCol);
            setCellVMergeVal(cell, STMerge.RESTART);
            for(int i=startRow+1;i<=endRow;i++){
                cell = table.getRow(i).getCell(startCol);
                setCellVMergeVal(cell, STMerge.CONTINUE);
            }
        }

        if(colSpan>1){
            for(int i=startRow;i<=endRow;i++){
                XWPFTableCell cell = table.getRow(i).getCell(startCol);
                setCellHMergeVal(cell, STMerge.RESTART);
                for(int j=startCol+1;j<=endCol;j++){
                    cell = table.getRow(i).getCell(j);
                    setCellHMergeVal(cell, STMerge.CONTINUE);
                }
            }
        }
    }




    private static void mergeCol(XWPFTableRow row,int startCol,int endCol){
        Assert.isTrue(startCol < endCol);
        XWPFTableCell cell = row.getCell(startCol);
        setCellHMergeVal(cell, STMerge.RESTART);

        for(int i=startCol+1;i<=endCol;i++){
            cell = row.getCell(i);
            setCellHMergeVal(cell, STMerge.CONTINUE);
        }

    }

    private static void setCellHMergeVal(XWPFTableCell cell,STMerge.Enum stMerge){
        CTTc ctTc = cell.getCTTc();
        CTTcPr tcPr=null;
        if (ctTc.isSetTcPr()) {
            tcPr = ctTc.getTcPr();
        }else{
            tcPr = ctTc.addNewTcPr();
        }
        boolean isSetHmerge = tcPr.isSetHMerge();
        CTHMerge cthMerge = null;
        if(isSetHmerge){
            cthMerge = tcPr.getHMerge();
        }else{
            cthMerge = tcPr.addNewHMerge();
        }
        cthMerge.setVal(stMerge);
    }

    private static void setCellVMergeVal(XWPFTableCell cell,STMerge.Enum stMerge){
        CTTc ctTc = cell.getCTTc();
        CTTcPr tcPr=null;
        if (ctTc.isSetTcPr()) {
            tcPr = ctTc.getTcPr();
        }else{
            tcPr = ctTc.addNewTcPr();
        }
        CTVMerge ctvMerge = null;
        if(tcPr.isSetVMerge()){
            ctvMerge = tcPr.getVMerge();
        }else{
            ctvMerge = tcPr.addNewVMerge();
        }
        ctvMerge.setVal(stMerge);
    }

    private static void setNoBorder(XWPFTableCell cell){
        setNoLeft(cell);
        setNoTop(cell);
        setNoRight(cell);
        setNoBottom(cell);
    }

    private static void setNoBottom(XWPFTableCell cell) {
        CTTc ctTc = cell.getCTTc();
        CTTcPr tcPr=null;
        if (ctTc.isSetTcPr()) {
            tcPr = ctTc.getTcPr();
        }else{
            tcPr = ctTc.addNewTcPr();
        }

        CTTcBorders tcBorders=null;
        if (tcPr.isSetTcBorders()) {
            tcBorders= tcPr.getTcBorders();
        }else{
            tcBorders = tcPr.addNewTcBorders();
        }
        CTBorder border =null;
        if (tcBorders.isSetBottom()) {
            border = tcBorders.getBottom();
        }else{
            border = tcBorders.addNewBottom();
        }
        // 设置单元格底部边框无
        border.setVal(STBorder.NIL);
    }

    private static void setNoTop(XWPFTableCell cell) {
        CTTc ctTc = cell.getCTTc();
        CTTcPr tcPr=null;
        if (ctTc.isSetTcPr()) {
            tcPr = ctTc.getTcPr();
        }else{
            tcPr = ctTc.addNewTcPr();
        }

        CTTcBorders tcBorders=null;
        if (tcPr.isSetTcBorders()) {
            tcBorders= tcPr.getTcBorders();
        }else{
            tcBorders = tcPr.addNewTcBorders();
        }
        CTBorder border =null;
        if (tcBorders.isSetTop()) {
            border = tcBorders.getTop();
        }else{
            border = tcBorders.addNewTop();
        }
        // 设置单元格底部边框无
        border.setVal(STBorder.NIL);
    }

    private static void setNoLeft(XWPFTableCell cell) {
        CTTc ctTc = cell.getCTTc();
        CTTcPr tcPr=null;
        if (ctTc.isSetTcPr()) {
            tcPr = ctTc.getTcPr();
        }else{
            tcPr = ctTc.addNewTcPr();
        }

        CTTcBorders tcBorders=null;
        if (tcPr.isSetTcBorders()) {
            tcBorders= tcPr.getTcBorders();
        }else{
            tcBorders = tcPr.addNewTcBorders();
        }
        CTBorder border =null;
        if (tcBorders.isSetLeft()) {
            border = tcBorders.getLeft();
        }else{
            border = tcBorders.addNewLeft();
        }
        // 设置单元格底部边框无
        border.setVal(STBorder.NIL);
    }

    private static void setNoRight(XWPFTableCell cell) {
        CTTc ctTc = cell.getCTTc();
        CTTcPr tcPr=null;
        if (ctTc.isSetTcPr()) {
            tcPr = ctTc.getTcPr();
        }else{
            tcPr = ctTc.addNewTcPr();
        }

        CTTcBorders tcBorders=null;
        if (tcPr.isSetTcBorders()) {
            tcBorders= tcPr.getTcBorders();
        }else{
            tcBorders = tcPr.addNewTcBorders();
        }
        CTBorder border =null;
        if (tcBorders.isSetRight()) {
            border = tcBorders.getRight();
        }else{
            border = tcBorders.addNewRight();
        }
        // 设置单元格底部边框无
        border.setVal(STBorder.NIL);
    }

}
