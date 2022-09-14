package com.example.poi;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.StyleSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.*;

import javax.swing.filechooser.FileSystemView;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;

import static cn.hutool.poi.excel.style.StyleUtil.cloneCellStyle;

/**
 * @author LTJ
 * @date 2022/8/31
 */
@Slf4j
public class Demo2 {

    public static SecureRandom secureRandom = new SecureRandom();

    public static void main(String[] args) {
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File homeDirectory = fileSystemView.getHomeDirectory();
        File xlsx = FileUtil.file(homeDirectory, "fomulaPractice.xlsx");
        try {
            XSSFWorkbook workbook = XSSFWorkbookFactory.createWorkbook();
            XSSFCreationHelper creationHelper = workbook.getCreationHelper();
            XSSFDataFormat dataFormat = creationHelper.createDataFormat();

            XSSFSheet sheet = workbook.createSheet();
            int defaultColumnWidth = sheet.getDefaultColumnWidth();
            short defaultRowHeight = sheet.getDefaultRowHeight();
            float defaultRowHeightInPoints = sheet.getDefaultRowHeightInPoints();
            log.info("defaultColumnWidth:{} 1/256 character",defaultColumnWidth);
            log.info("defaultColumnWidth:{} 1/20 point",defaultRowHeight);
            log.info("defaultColumnWidth:{} point",defaultRowHeightInPoints);

//            sheet = workbook.createSheet();
//            sheet = workbook.createSheet();
            StyleSet styleSet = new StyleSet(workbook);
            CellStyle headCellStyle = styleSet.getHeadCellStyle();
            CellStyle digitalStyle = styleSet.getCellStyleForNumber();
            CellStyle redDigitalStyle = cloneCellStyle(workbook, digitalStyle);
            Font font = workbook.createFont();
            font.setColor(Font.COLOR_RED);
            font.setBold(true);
            redDigitalStyle.setFont(font);
            for(int i=0;i<100;i++){
                XSSFRow row = sheet.createRow(i);
                for(int j=0;j<100;j++){
                    XSSFCell cell = row.createCell(j);
                    int value = secureRandom.nextInt(100);
                    cell.setCellValue(Double.valueOf(value));
                    cell.setCellStyle(redDigitalStyle);
                }
            }
            XSSFRow row0 = sheet.getRow(2);
            row0.setHeight((short) 220);
            sheet.setColumnWidth(2,256);

            XSSFRow row1 = sheet.getRow(3);
            row1.setHeight((short) (2 * defaultRowHeight));
            sheet.setColumnWidth(3, 4 * 256);

            CellUtil.setCellStyleProperty(CellUtil.getCell(row1, 4),CellUtil.FILL_BACKGROUND_COLOR,(short)2);
            CellUtil.setCellStyleProperty(CellUtil.getCell(row1, 4),CellUtil.FILL_PATTERN,FillPatternType.SOLID_FOREGROUND);

            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            CellUtil.getCell(row1, 8).setCellStyle(cellStyle);

            int columnWidth = sheet.getColumnWidth(4);
            log.info("columnWidth 4 width：{}",columnWidth);
//            int sheetIndex = workbook.getSheetIndex(sheet);
//            workbook.setActiveSheet(sheetIndex);
//            sheet.setColumnHidden(2, true);
//            sheet.shiftRows(0, sheet.getLastRowNum(), 1);
//            CellAddress a11 = new CellAddress("A11");
//            XSSFRow row1 = sheet.createRow(a11.getRow());
//            XSSFCell cell1 = row1.createCell(a11.getColumn());
//            cell1.setCellFormula("COUNTIFS(A1:A10,\">=-5\",A1:A10,\"<=5\")");
//            sheet.setAutoFilter(CellRangeAddress.valueOf("A1:D10"));
//            sheet.createFreezePane(3, 2);
            BufferedOutputStream outputStream = FileUtil.getOutputStream(xlsx);
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
