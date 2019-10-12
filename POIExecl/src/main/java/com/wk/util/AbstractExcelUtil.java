package com.wk.util;

import com.wk.bean.ImportBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AbstractExcelUtil implements ExcelUtil{
    private String filePath = "";
    private String suffixXls = "xls";
    private String suffixXlsx = "xlsx";
    private String SaveFilePath="H:\\FTPTest\\output.xls";


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

    /**
     *  把数据写入到excel中
     * @param lists
     * @param
     */
    @Override
    public  void writeJson(List<ImportBean> lists) {
        try {
            checkParam(lists);
        } catch (IOException e) {
            log.error("writeJson IOException,msg is :{}",e.getMessage());
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
        String sheetName = "demo";
        writeJsonToWorkBook(workbook,sheetName,lists);
    }

    /**
     *  写数据到sheet中
     * @param workbook
     * @param sheetName
     * @param lists
     * @param
     */
    private void writeJsonToWorkBook(HSSFWorkbook workbook,String sheetName, List<ImportBean> lists) {
        HSSFSheet sheet = workbook.createSheet(sheetName);
        int columnLenth = ImportBean.class.getDeclaredFields().length;
        int size = lists.size();
        int totalRowNum = size + 3;
        // 设置列宽
        for (int i=0; i<columnLenth;i++){
            if (i == 0){
                sheet.setColumnWidth(i,4800);
            }else{
                sheet.setColumnWidth(i,2000);
            }
        }
        // 创建第一行
        HSSFRow row0 = sheet.createRow(0);
        // 设置行高
        short height = 36*20;
        row0.setHeight(height);
        // 创建单元格
        HSSFCell cellTitle = row0.createCell(0);
        // 设置单元样式
        HSSFCellStyle cellStyle = createStyle(workbook, "黑体", 20, HSSFCellStyle.ALIGN_CENTER, false, true);
        cellTitle.setCellStyle(cellStyle);
        // 设置单元格内容
        cellTitle.setCellValue("Write Demo");
        // 合并单元格 CellRangeAddress 构造函数依次表示:起始行，截止行，起始列，截止列
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,18));

        // 第二行
        HSSFRow row1 = sheet.createRow(1);
        short height2 = 22*20;
        row1.setHeight(height2);
        HSSFCell cell1_1 = row1.createCell(0);
        HSSFCellStyle cellStyle1 = createStyle(workbook, "宋体", 12,
                HSSFCellStyle.ALIGN_CENTER, false, false);
        cell1_1.setCellStyle(cellStyle1);
        cell1_1.setCellValue("年级:六年级");

        HSSFCell cell1_2 = row1.createCell(1);
        HSSFCellStyle cellStyle2 = createStyle(workbook, "宋体", 12,
                HSSFCellStyle.ALIGN_RIGHT, false, false);
        cell1_2.setCellStyle(cellStyle2);
        cell1_2.setCellValue("科目三:"+sheetName);
        sheet.addMergedRegion(new CellRangeAddress(1,1,1,18));

        // 标题栏
        HSSFCellStyle styleHead = createStyle(workbook, "宋体", 12, HSSFCellStyle.ALIGN_CENTER,
                true, true);
        HSSFRow head = sheet.createRow(2);
        short headHeight = 49*20;
        head.setHeight(headHeight);
        HSSFCell ticketNumber = head.createCell(0);
        ticketNumber.setCellStyle(styleHead);
        ticketNumber.setCellValue("准考证号");

        HSSFCell detectionNumber = head.createCell(1);
        detectionNumber.setCellStyle(styleHead);
        detectionNumber.setCellValue("监测号");

        HSSFCell name = head.createCell(2);
        name.setCellStyle(styleHead);
        name.setCellValue("姓名");

        HSSFCell school = head.createCell(3);
        school.setCellStyle(styleHead);
        school.setCellValue("学校");

        HSSFCell chineseScore = head.createCell(4);
        chineseScore.setCellStyle(styleHead);
        chineseScore.setCellValue("语文");

        HSSFCell mathScore = head.createCell(5);
        mathScore.setCellStyle(styleHead);
        mathScore.setCellValue("数学");

        HSSFCell societyScore = head.createCell(6);
        societyScore.setCellStyle(styleHead);
        societyScore.setCellValue("品社");

        HSSFCell scienceScore = head.createCell(7);
        scienceScore.setCellStyle(styleHead);
        scienceScore.setCellValue("科学");

        HSSFCell englishScore = head.createCell(8);
        englishScore.setCellStyle(styleHead);
        englishScore.setCellValue("英语");

        // 录入真实的数据
        HSSFCellStyle contentStyle = createStyle(workbook, "宋体", 12, HSSFCellStyle.ALIGN_CENTER,
                true, false);

        int rowNum = 3;
        for (ImportBean bean:lists){
            HSSFRow rowsIndex = sheet.createRow(rowNum);
            short heightContent = 49*20;
            rowsIndex.setHeight(heightContent);

            HSSFCell cellConent_0 = rowsIndex.createCell(0);
            cellConent_0.setCellStyle(contentStyle);
            cellConent_0.setCellValue(bean.getTicketNumber());

            HSSFCell cellConent_1 = rowsIndex.createCell(1);
            cellConent_1.setCellStyle(contentStyle);
            cellConent_1.setCellValue(bean.getDetectionNumber());

            HSSFCell cellConent_2 = rowsIndex.createCell(2);
            cellConent_2.setCellStyle(contentStyle);
            cellConent_2.setCellValue(bean.getName());

            HSSFCell cellConent_3 = rowsIndex.createCell(3);
            cellConent_3.setCellStyle(contentStyle);
            cellConent_3.setCellValue(bean.getSchool());

            HSSFCell cellConent_4 = rowsIndex.createCell(4);
            cellConent_4.setCellStyle(contentStyle);
            cellConent_4.setCellValue(bean.getChineseScore());

            HSSFCell cellConent_5 = rowsIndex.createCell(5);
            cellConent_5.setCellStyle(contentStyle);
            cellConent_5.setCellValue(bean.getMathScore());

            HSSFCell cellConent_6 = rowsIndex.createCell(6);
            cellConent_6.setCellStyle(contentStyle);
            cellConent_6.setCellValue(bean.getSocietyScore());

            HSSFCell cellConent_7 = rowsIndex.createCell(7);
            cellConent_7.setCellStyle(contentStyle);
            cellConent_7.setCellValue(bean.getScienceScore());

            HSSFCell cellConent_8 = rowsIndex.createCell(8);
            cellConent_8.setCellStyle(contentStyle);
            cellConent_8.setCellValue(bean.getEnglishScore());

            // 下一行
            rowNum++;
        }

        // 最后一行
        HSSFRow lastRow = sheet.createRow(totalRowNum);
        short lastHeight = 22*20;
        lastRow.setHeight(lastHeight);

        HSSFCell cellLast = lastRow.createCell(0);
        HSSFCellStyle lastRowStyle = createStyle(workbook, "宋体", 12, HSSFCellStyle.ALIGN_CENTER,
                false, false);
        cellLast.setCellStyle(lastRowStyle);
        cellLast.setCellValue("备注:六年级检测科目");
        sheet.addMergedRegion(new CellRangeAddress(totalRowNum,totalRowNum,0,18));

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(SaveFilePath));
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            // workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HSSFCellStyle createStyle(HSSFWorkbook workbook,String fontName,int fontHeightInPoints,short aligment,
                                     boolean border,boolean boldweight){

        HSSFFont font = workbook.createFont();
        if (boldweight){
            // 字体加粗
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        }
        font.setFontName(fontName);
        font.setFontHeightInPoints((short)fontHeightInPoints);
        //创建样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(aligment);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 设置边界
        if (border){
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);

            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);

            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setRightBorderColor(HSSFColor.BLACK.index);

            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
        }
        return cellStyle;
    }

    // 参数检验
    private <ImportBean> void checkParam(List<ImportBean> lists) throws IOException {
        if (lists == null || lists.size() <= 0){
            log.error("data must be given");
            throw new IOException("parameter can not be null");
        }
    }
}
