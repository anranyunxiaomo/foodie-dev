package com.moxuan.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class FileUtils {


    public String getFileType(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (!StrUtil.isBlankIfStr(originalFilename)) {
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return null;
    }


    /**
     * @param driveLetter 文件盘符 Linux 加上/ 即可 win c:// 即可
     * @param userId
     * @param pathPrefix  文件存储位置的路径 除了 {driveLetter}
     * @param fileType
     * @return
     */
    public String fileNameSplicingPath(String userId, String pathPrefix, String fileType) {
        StringBuffer stringBuffer = new StringBuffer();
        // 文件名称重新命名
        String[] split = null;
        if (pathPrefix.contains("/")) {
            split = pathPrefix.split("/");
        } else if (pathPrefix.contains("\\")) {
            split = pathPrefix.replace("\\", "/").split("/");
        }
        for (String path : split) {
            stringBuffer.append(path).append(File.separator);
        }
        // 在路径上为每一个用户增加一个userid，用于区分不同用户上传
        stringBuffer.append(userId).append(File.separator);
        return stringBuffer.append("face-").append(userId).append(".").append(fileType).toString();
    }


}
