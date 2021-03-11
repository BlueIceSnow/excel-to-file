package com.tianqi;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.tianqi.entity.FileEntity;
import com.tianqi.win.MainWindows;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yuantianqi
 */
public class MainClass {

    private static ExcelReader excelReader = null;

    public static void main(String[] args) {

        new MainWindows();

    }
}
