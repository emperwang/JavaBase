package com.wk.demo;

import com.wk.util.AbstractExcelUtil;
import com.wk.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TestMain {

    public static void main(String[] args) {
        String filePath = "F:\\github_code\\Mine\\JavaBase\\POIExecl\\src\\main\\resources\\import.xlsx";
        log.info(String.valueOf(filePath.endsWith("xlsx")));
        ExcelUtil excelUtil = new AbstractExcelUtil(filePath);
        List<Object> objects = excelUtil.readExcel();

        log.info("read result is :"+objects);
    }
}
