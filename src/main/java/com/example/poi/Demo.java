package com.example.poi;

import cn.hutool.core.io.FileUtil;
import org.apache.poi.ss.formula.eval.FunctionEval;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @author LTJ
 * @date 2022/8/31
 */
public class Demo {

    public static void main(String[] args) {
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File homeDirectory = fileSystemView.getHomeDirectory();
        File xlsx = FileUtil.file(homeDirectory, "latheTemplate.xlsx");
        try {
            XSSFWorkbook workbook = XSSFWorkbookFactory.createWorkbook(xlsx, true);
            XSSFSheet sheet = workbook.getSheet(WorkbookUtil.createSafeSheetName("8.30"));
            if(sheet==null){
                System.out.printf("%s sheet不存在", "8.30");
                return;
            }
            XSSFFormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            CellAddress m51 = new CellAddress("M51");
            Row row = CellUtil.getRow(m51.getRow(), sheet);
            Cell cell = CellUtil.getCell(row, m51.getColumn());
            CellType cellType = cell.getCellType();
            switch (cellType) {
                case FORMULA:
                    String cellFormula = cell.getCellFormula();
                    System.out.println("cellFormula = " + cellFormula);
                    CellType cachedFormulaResultType = cell.getCachedFormulaResultType();
                    System.out.println("cachedFormulaResultType = " + cachedFormulaResultType);
//                    CellValue evaluate = formulaEvaluator.evaluate(cell);
                    switch (cachedFormulaResultType) {
                        case NUMERIC:
                            double numericCellValue = cell.getNumericCellValue();
                            System.out.println("numericCellValue = " + numericCellValue);
                            break;
                        default:
                            String stringCellValue = cell.getStringCellValue();
                            System.out.println("formual string = " + stringCellValue);
                    }
                    break;
                default:
                    String stringCellValue = cell.getStringCellValue();
                    System.out.println("stringCellValue = " + stringCellValue);
            }



            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
