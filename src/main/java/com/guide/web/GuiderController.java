package com.guide.web;

import com.guide.pojo.Guider;
import com.guide.service.GuiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("guider")
public class GuiderController {
    @Autowired
    private GuiderService guiderService;

    /**
     * 上传证书、身份证等图片调用这个接口
     * @param file
     * @return
     */
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.accepted().body(guiderService.uploadImage(file));
    }

    /**
     * 导游提交认证申请（导游注册）
     * @param guider
     * @param code
     * @return
     */
    @PostMapping("/qualification")
    public ResponseEntity<Boolean> qualification(Guider guider, String code) {
        return  ResponseEntity.ok(guiderService.insert(guider,code));
    }

}
