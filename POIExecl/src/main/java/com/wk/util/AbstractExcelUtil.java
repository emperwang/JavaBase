package com.wk.util;

import com.wk.bean.ImportBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AbstractExcelUtil implements ExcelUtil{
    private String filePath = "";
    private String suffixXls = "xls";
    private String suffixXlsx = "xlsx";

    public AbstractExcelUtil(String filePath){
        this.filePath = filePath;
    }

    @Override
    public  List<ImportBean> readExcel() {
        ArrayList<ImportBean> beans = new ArrayList<>();
        File file = new File(filePath);
        // check file
        try {
            checkFile(file);
        } catch (IOException e) {
            log.error("readExcel IOException,msg is {}",e.getMessage());
            return null;
        }
        Workbook workBook = getWorkBook(file);
        if (workBook != null){
            int sheetNums = workBook.getNumberOfSheets();
            for (int sheetNum = 0;sheetNum < sheetNums;sheetNum++){
                // get current sheet
                Sheet sheet = workBook.getSheetAt(sheetNum);
                if (sheet == null){
                    continue;
                }
                // 获取有效行的第一行 以及 最后一行的标号
                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                // 从第二行开始读取数据
                for (int rowNum = firstRowNum+1; rowNum <= lastRowNum; rowNum++){
                    // 获取到整行
                    Row row = sheet.getRow(rowNum);
                    if(row == null){
                        continue;
                    }
                    // 获取开始列
                    int firstCellNum = row.getFirstCellNum();
                    // 获取有效列数
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    String[] cells = new String[physicalNumberOfCells];
                    // 循环当前行
                    for (int cellNum =firstCellNum;cellNum < physicalNumberOfCells;cellNum++){
                        Cell cell = row.getCell(cellNum);
                        String value = getCellValue(cell);
                        cells[cellNum] = value;
                    }
                    ImportBean bean = excelToObject(cells);
                    beans.add(bean);
                }
            }
        }
        return beans;
    }

    /**
     *  封装信息到 bean中
     * @param cells
     * @return
     */
    private ImportBean excelToObject(String[] cells) {
        ImportBean importBean = new ImportBean();
        for (int i=0;i < cells.length; i++){
            switch (i){
                case 0:
                    importBean.setTicketNumber(Integer.parseInt(cells[i]));
                    break;
                case 1:
                    importBean.setDetectionNumber(cells[i]);
                    break;
                case 2:
                    importBean.setName(cells[i]);
                    break;
                case 3:
                    importBean.setSchool(cells[i]);
                    break;
                case 4:
                    importBean.setChineseScore(Integer.parseInt(cells[i]));
                    break;
                case 5:
                    importBean.setMathScore(Integer.parseInt(cells[i]));
                    break;
                case 6:
                    importBean.setSocietyScore(Integer.parseInt(cells[i]));
                    break;
                case 7:
                    importBean.setScienceScore(Integer.parseInt(cells[i]));
                    break;
                case 8:
                    importBean.setEnglishScore(Integer.parseInt(cells[i]));
                    break;
                default:
                    log.error("not a valid value");
                    break;
            }
        }

        return importBean;
    }

    /**
     *  根据cellType获取value
     * @param cell
     * @return
     */
    protected String getCellValue(Cell cell){
        String cellValue = "";
        if (cell == null){
            return cellValue;
        }
        // 把数字当成String来读, 避免出现 1 读成 1.0的情况
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }

        switch (cell.getCellType()){
            //数字
            case Cell.CELL_TYPE_NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
             // 公式
            case Cell.CELL_TYPE_FORMULA:
                cellValue = String.valueOf(cell.getCellFormula());
                break;
                // 空值
            case Cell.CELL_TYPE_BLANK:
                cellValue = "";
                break;
                // 故障
            case Cell.CELL_TYPE_ERROR:
                cellValue = "invalid char";
                break;
            default:
                cellValue = "Unknown value";
                break;
        }
        return cellValue;
    }

    /**
     *  获取workBook
     * @param file
     * @return
     */
    public Workbook getWorkBook(File file){
        // get name
        String name = file.getName();
        log.info("file name is :{}",name);
        Workbook workbook = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            // 2003
            if (name.endsWith(suffixXls)){
                workbook = new HSSFWorkbook(fileInputStream);
            }else{ // 2007
                workbook = new XSSFWorkbook(fileInputStream);
            }
        } catch (FileNotFoundException e) {
            log.error("getWorkBook FileNotFoundException,msg is :{}",e.getMessage());
        } catch (IOException e) {
            log.error("getWorkBook IOException,msg is: {}",e.getMessage());
        }

        return workbook;
    }

    /**
     *  文件检测
     * @param file
     * @throws IOException
     */
    public void checkFile(File file) throws IOException {
        if (file == null){
            log.error("file not exist");
            throw new FileNotFoundException("file is not exist");
        }
        String name = file.getName();
        log.info("file name is :{}",name);
        if (!name.endsWith(suffixXls) && !name.endsWith(suffixXlsx)){
            log.error("file is not a excel file");
            throw new IOException(name + " is not excel file");
        }
    }

}
