package com.guide.service;

import com.guide.mapper.UserMapper;
import com.guide.pojo.User;
import com.guide.pojo.WeChatProperties;
import com.guide.pojo.WeChatResultType;
import com.guide.utils.common.ConstantClassField;
import com.guide.utils.common.HttpUtil;
import com.guide.utils.common.Tool;
import com.guide.utils.common.WeChatUtil;
import com.guide.utils.exception.ExceptionEnum;
import com.guide.utils.exception.GuideException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties properties;
    @Autowired
    private UploadService uploadService;

    public User login(String code, String iv, String encryptedData) {
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(iv) || StringUtils.isEmpty(encryptedData)) {
            throw new GuideException(ExceptionEnum.INVALID_REQUEST_PARAM);
        }
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("appid", properties.getAppid());
        loginMap.put("secret", properties.getSecret());
        loginMap.put("grant_type", properties.getGrant_type());
        loginMap.put("url", properties.getUrl());
        loginMap.put("code", code);
        WeChatResultType result = HttpUtil.send(loginMap);
        Map<String, String> resultMap = new HashMap<>();

        if (result.getErrcode() != null) {
            resultMap.put("loginflag", result.getErrcode().toString());
        }
        if (result == null || result.getErrcode().equals(ConstantClassField.WECHATLOGINFAIL_CODE)) {
            throw new GuideException((ExceptionEnum.WECHATLOGINFAIL));
        }
        if (result.getErrcode().equals(ConstantClassField.WECHATLOGINFREQUENT_CODE)) {
            throw new GuideException(ExceptionEnum.WECHATLOGINFREQUENT);
        }
        if (result.getErrcode().equals(ConstantClassField.WECHATLOGINTIMEOUT_CODE)) {
            throw new GuideException(ExceptionEnum.WECHATLOGINFREQUENT);
        }

        Map<String, Object> usermap = WeChatUtil.getUserInfo(encryptedData, result.getSession_key(), iv);
        User user = null;
        try {
            user = Tool.mapToBean(usermap, User.class);
        } catch (Exception e) {
            throw new GuideException(ExceptionEnum.PARSING_FAIL);
        }
        User sessionUser = getUserByOpenId(user.getOpenId());
        Date date = new Date();
        if (sessionUser != null) {
            user = new User();
            user.setOpenId(result.getOpenid());
            user.setLastLoginTime(date);
            updateUserInfoByOpenId(user);
        } else {
            Double longitude = ConstantClassField.DEFAULT_LONGITUDE;
            Double latitude = ConstantClassField.DEFAULT_LATITUDE;
            String address = ConstantClassField.DEFAULT_ADDRESS;
            user.setLongitude(longitude);
            user.setLatitude(latitude);
            user.setAddress(address);
            user.setCredit(ConstantClassField.DEFAULT_CREDIT);
            user.setCreateTime(date);
            user.setUpdateTime(date);
            user.setLastLoginTime(date);
            insertUser(user);
        }
        return user;
    }

    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    public User getUserByOpenId(String openId) {
        User user = new User();
        user.setOpenId(openId);
        return userMapper.selectOne(user);
    }

    public Boolean updateUserInfoByOpenId(User user) {
        if (StringUtils.isBlank(user.getOpenId())) {
            throw new GuideException(ExceptionEnum.INVALID_REQUEST_PARAM);
        }
        user.setId(null);
        user.setLongitude(null);
        user.setLatitude(null);
        user.setLastLoginTime(null);
        user.setPhone(null);
        user.setEmail(null);
        user.setUpdateTime(new Date());
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("openId", user.getOpenId());
        return userMapper.updateByExampleSelective(user, example)==1;
    }

    public Boolean uploadAvatarUrl(String openId, MultipartFile file) {
        String avatarUrl = uploadService.uploadImage(file);
        if(StringUtils.isNotBlank(avatarUrl)){
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("openId",openId);
            User user = new User();
            user.setAvatarUrl(avatarUrl);
            return userMapper.updateByExampleSelective(user,example)==1;
        }
        return false;
    }
}
