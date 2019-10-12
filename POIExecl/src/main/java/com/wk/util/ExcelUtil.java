package com.wk.util;

import com.wk.bean.ImportBean;

import java.util.List;

public interface ExcelUtil {

    <T> List<T> readExcel();

    void writeJson(List<ImportBean>lists);
}
