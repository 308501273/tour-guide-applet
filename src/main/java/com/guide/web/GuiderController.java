package com.guide.web;

import com.guide.common.utils.PageResult;
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
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.accepted().body(guiderService.uploadImage(file));
    }

    /**
     * 导游绑定手机号时获取验证码
     *
     * @param phone
     * @return
     */
    @GetMapping("message_code")
    public ResponseEntity<Void> getMessageCode(String phone) {
        guiderService.getMessageCode(phone);
        return ResponseEntity.ok().build();
    }

    /**
     * 导游提交认证申请（导游注册）
     *
     * @param guider
     * @param code
     * @return
     */
    @PostMapping("/application")
    public ResponseEntity<Boolean> applyToGuider(Guider guider, String code) {
        return ResponseEntity.ok(guiderService.applyToGuider(guider, code));
    }

    @GetMapping("list")
    public ResponseEntity<PageResult<Guider>> getGuiders(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                         @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                                         @RequestParam(value = "sortBy", required = false) String sortBy,
                                                         @RequestParam(value = "desc", defaultValue = "true") Boolean desc,
                                                         @RequestParam(value = "key", required = false) String key) {
        return ResponseEntity.ok(guiderService.getGuiders(page, rows, sortBy, desc, key));
    }

    @GetMapping
    public ResponseEntity<Guider> getGuiderByOpenId(String openId) {
        return ResponseEntity.ok(guiderService.getGuiderByopenId(openId));
    }

    @PutMapping
    public ResponseEntity<Boolean> updateGuiderByOpenId(Guider guider) {
        return ResponseEntity.ok(guiderService.updateGuiderByOpenId(guider));
    }

    @PostMapping(value = "avatar_url", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> updateAvatarUrl(String openId, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.accepted().body(guiderService.updateAvatarUrl(openId, file));
    }

    @PutMapping("phone")
    public ResponseEntity<Boolean> updatePhone(String openId, String phone, String code) {
        return ResponseEntity.accepted().body(guiderService.updatePhone(openId, phone, code));
    }

}
