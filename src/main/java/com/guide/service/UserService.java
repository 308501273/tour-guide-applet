package com.guide.service;

import com.guide.common.utils.*;
import com.guide.mapper.UserMapper;
import com.guide.pojo.User;
import com.guide.conf.wechat.WeChatProperties;
import com.guide.conf.wechat.WeChatResultType;
import com.guide.common.exception.ExceptionEnum;
import com.guide.common.exception.GuideException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties properties;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;

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
        if (result == null) {
            throw new GuideException(ExceptionEnum.WECHATLOGINFAIL);
        }
        if (result.getErrcode() != null) {
            if (result.getErrcode().equals(ConstantClassField.WECHATLOGINFAIL_CODE)) {
                throw new GuideException((ExceptionEnum.WECHATLOGINFAIL));
            }
            if (result.getErrcode().equals(ConstantClassField.WECHATLOGINFREQUENT_CODE)) {
                throw new GuideException(ExceptionEnum.WECHATLOGINFREQUENT);
            }
            if (result.getErrcode().equals(ConstantClassField.WECHATLOGINTIMEOUT_CODE)) {
                throw new GuideException(ExceptionEnum.WECHATLOGINFREQUENT);
            }
            if (StringUtils.isBlank(result.getSession_key())) {
                throw new GuideException(ExceptionEnum.WECHATLOGINFAIL);
            }
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
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("openId", user.getOpenId());
            userMapper.updateByExampleSelective(user, example);
            sessionUser.setLastLoginTime(user.getLastLoginTime());
            return sessionUser;
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
        return userMapper.updateByExampleSelective(user, example) == 1;
    }

    public Boolean uploadAvatarUrl(String openId, MultipartFile file) {
        String avatarUrl = uploadService.uploadImage(file);
        if (StringUtils.isNotBlank(avatarUrl)) {
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("openId", openId);
            User user = new User();
            user.setAvatarUrl(avatarUrl);
            return userMapper.updateByExampleSelective(user, example) == 1;
        }
        return false;
    }

    public void getMessageCode(String phone) {
        Map<String, String> msg = new HashMap<>();
        String code = NumberUtils.generateCode(6);
        msg.put("phone", phone);
        msg.put("code", code);
        //通过rabbitmq发送短信验证码
        amqpTemplate.convertAndSend("tourguide.sms.exchange", "tourguide.sms.code", msg);
        //生成一个redis的key
        String key = Tool.encryptRedisKey(ConstantClassField.USER_UPDATE_PHONE_HEAD, phone);
        String value = Tool.encryptRedisValue(phone, code);
        //保存验证码到redis中
        redisTemplate.opsForValue().set(key, value, 5, TimeUnit.MINUTES);
    }

    @Transactional
    public Boolean updatePhone(String openId, String phone, String code) {
        String key = Tool.encryptRedisKey(ConstantClassField.USER_UPDATE_PHONE_HEAD, phone);
        if (!Tool.encryptRedisValue(phone, code).equals(redisTemplate.opsForValue().get(key))) {
            throw new GuideException(ExceptionEnum.VERIFICATION_CODE_ERROR);
        }
        userMapper.transferPhone(phone);
        User user = new User();
        user.setPhone(phone);
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("openId", openId);
        Boolean flag = userMapper.updateByExampleSelective(user, example) == 1;
        // 如果注册成功，删除redis中的code
        if (flag) {
            try {
                redisTemplate.delete(key);
            } catch (Exception e) {
                log.error("【用户服务】删除Redis手机验证码失败，phone：{}", phone, e);
            }
        }
        return flag;
    }
}
