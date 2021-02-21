package com.guide.web;

import com.guide.pojo.Guider;
import com.guide.pojo.User;
import com.guide.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * @param code
     * @param iv
     * @param encryptedData
     * @return
     * @throws Exception
     */
    //微信登陆授权
    @PostMapping("login")
    public ResponseEntity<User> login(@RequestParam("code") String code,
                                      @RequestParam("iv") String iv,
                                      @RequestParam("encryptedData") String encryptedData) {
        //传回user信息
        return ResponseEntity.ok(userService.login(code, iv, encryptedData));
    }

    @GetMapping
    public ResponseEntity<User> getUserByOpenId(String openId) {
        return ResponseEntity.ok(userService.getUserByOpenId(openId));
    }

    @PutMapping
    public ResponseEntity<Boolean> updateUserByOpenId(User user) {
        return ResponseEntity.accepted().body(userService.updateUserInfoByOpenId(user));
    }

    @PostMapping(value = "avatar_url", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> updateAvatarUrl(String openId, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.accepted().body(userService.updateAvatarUrl(openId, file));
    }

    @GetMapping("message_code")
    public ResponseEntity<Void> getPhoneCode(String phone) {
        userService.getMessageCode(phone);
        return ResponseEntity.ok().build();
    }

    @PutMapping("phone")
    public ResponseEntity<Boolean> updatePhone(String openId, String phone, String code) {
        return ResponseEntity.accepted().body(userService.updatePhone(openId, phone, code));
    }



}
