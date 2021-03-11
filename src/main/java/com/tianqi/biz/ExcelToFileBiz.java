package com.tianqi.biz;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.tianqi.MainClass;
import com.tianqi.entity.FileEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @author yuantianqi
 */
public class ExcelToFileBiz {

    private static ExcelReader excelReader = null;

    private static String excelPath = null;
    private static String targetPath = null;

    public static void init(String excelPath , String targetPath){
        ExcelToFileBiz.excelPath = excelPath;
        ExcelToFileBiz.targetPath = targetPath;
        // 初始化读取文件
        excelReader = ExcelUtil.getReader(new File(excelPath));
    }

    /**
     * 获取真实路径
     *
     * @param newFilePath
     * @return
     */
    private static String getRealPath(Map<String, List<FileEntity>> cutFileSection, String rootPath, String newFilePath) {

        //根据部分替换每级目录的真实文件名
        //去除根路径
        String[] filePaths = newFilePath.replace(rootPath, "")
                .split(Matcher.quoteReplacement(String.valueOf(File.separatorChar)));
        filePaths = ListUtil.toList(filePaths).stream().filter(file -> {
            if ("".equals(file)) {
                return false;
            }
            return true;
        }).collect(Collectors.toList()).toArray(new String[]{});

        List<FileEntity> fileEntityList = cutFileSection.get(filePaths[0]);
        //去除空字符
        int count = 0;
        for (String filePath : filePaths) {
            List<FileEntity> fileEntities = fileEntityList.stream()
                    .filter(fileEntity ->
                            fileEntity.getPrefix() != null && fileEntity.getPrefix().equals(filePath)
                    ).collect(Collectors.toList());
            filePaths[count] = fileEntities.get(0).getDirName();
            count++;
        }

        String dirs = ArrayUtil.join(filePaths, String.valueOf(File.separatorChar));

        String realPath = rootPath + dirs;

        return realPath;
    }

    /**
     * 剪切文件，根据所属部分
     *
     * @param fileEntities
     * @return
     */
    private static Map<String, List<FileEntity>> cutFileBySection(List<FileEntity> fileEntities) {

        Map<String, List<FileEntity>> cutFilesSection = new HashMap<>();

        String currentSectionPrefix = null;
        List<FileEntity> fileEntityList = null;
        for (FileEntity fileEntity : fileEntities) {
            if (fileEntity.getTitle() != null && fileEntity.getPrefix().contains("部分")) {
                //生成一个部分
                currentSectionPrefix = fileEntity.getPrefix();
                fileEntityList = new ArrayList<>();
                cutFilesSection.put(currentSectionPrefix, fileEntityList);
            }

            // 保存文件进入当前Section部分
            if (currentSectionPrefix != null && fileEntityList != null) {
                fileEntityList.add(fileEntity);
            }
        }

        return cutFilesSection;
    }

    /**
     * 将Excel文件读取为实体类集合
     *
     * @return
     */
    public static List<FileEntity> readToFileEntity() {
        List<FileEntity> fileEntityList = new ArrayList<>();
        boolean isStartBuildFileEntity = false;
        List<List<Object>> rows = excelReader.read(0, excelReader.getRowCount());
        for (List<Object> row : rows) {
            if (isStartBuildFileEntity) {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setPrefix(String.valueOf(row.get(0)));
                fileEntity.setTitle(String.valueOf(row.get(1)));
                fileEntity.setDirName(null);
                fileEntityList.add(fileEntity);
            } else {
                if ("索引号".equals(String.valueOf(row.get(0)))) {
                    isStartBuildFileEntity = true;
                }
            }
        }
        return fileEntityList;
    }


    /**
     * 将Excel转为文件
     */
    public static void excelToFile() {
        // 获取文件存储根路径
        String rootPath = targetPath;
        // 文件根目录
        File rootDir = null;

        // 将Excel文件读取为实体类集合
        List<FileEntity> fileEntities = readToFileEntity();

        // 将集合切割为多个部分
        Map<String, List<FileEntity>> cutFiles = cutFileBySection(fileEntities);

        // 遍历读取的Excel文件
        for (FileEntity fileEntity : fileEntities) {
            if (fileEntity.isSection()) {
                // 如果包含"部分"，则创建目录创建顶级部分目录
                rootDir = new File(rootPath + File.separatorChar + fileEntity.getPrefix());
            } else if (rootDir != null) {
                //如果创建了"部分目录"，则创建子目录
                String col = String.valueOf(fileEntity.getPrefix());
                String[] dirNames = col.split("-");
                String newFilePath = rootDir.getPath();
                StringBuilder prefix = new StringBuilder();
                for (String dirName : dirNames) {
                    String newDirName = prefix.toString() + dirName;
                    //生成目录
                    newFilePath = newFilePath + File.separatorChar + newDirName;
                    if ("".equals(prefix.toString())) {
                        prefix = new StringBuilder(dirName).append("-");
                    } else {
                        prefix.append(dirName).append("-");
                    }
                }

                String realPath = getRealPath(cutFiles, rootPath, newFilePath);
                File newFile = new File(realPath);
                if (!newFile.exists()) {
                    System.out.println("生成路径：" + realPath);
                    newFile.mkdirs();
                }
            }

        }
    }

}
