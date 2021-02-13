package com.guide.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.guide.conf.fdfs.UploadProperties;
import com.guide.utils.exception.ExceptionEnum;
import com.guide.utils.exception.GuideException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Slf4j
@Service
public class UploadService {

    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private UploadProperties prop;

    public String uploadImage(MultipartFile file) {
        //保存文件到本地
        try {
            //校验文件后缀
            String contentType = file.getContentType();
            if (!prop.getImageAllowTypes().contains(contentType)) {
                throw new GuideException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new GuideException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //上传到FastDFS
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            //返回路径
            return prop.getBaseUrl() + storePath.getFullPath();
        } catch (IOException e) {
            //上传失败
            log.error("【文件上传】上传文件失败！", e);
            throw new GuideException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }
    }
}
