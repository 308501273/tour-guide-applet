package com.guide.web;

import com.guide.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("upload")
public class UploadController {

     @Autowired
     private UploadService uploadService;

     /**
      * 文件上传
      * @param file
      * @return
      */
     @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     public String uploadImage(@RequestPart("file") MultipartFile file) {
          return uploadService.uploadImage(file);
     }

}
