package com.tianqi.entity;

import lombok.Data;

/**
 * @author yuantianqi
 */
@Data
public class FileEntity {

    String title;
    String prefix;
    String dirName;
    boolean isSection;

    public void setPrefix(String prefix) {
        if (prefix!=null){
            this.isSection = prefix.contains("部分");
            prefix = prefix.replace("\u00A0", "");
        }
        this.prefix = prefix;
    }

    public void setDirName(String dirName) {
        if (dirName != null) {
            this.dirName = dirName;
        } else {
            this.dirName = this.prefix + this.title;
        }
    }
}
