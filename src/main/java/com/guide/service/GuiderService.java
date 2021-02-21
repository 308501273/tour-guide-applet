package com.guide.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.guide.common.exception.ExceptionEnum;
import com.guide.common.exception.GuideException;
import com.guide.common.utils.ConstantClassField;
import com.guide.common.utils.NumberUtils;
import com.guide.common.utils.PageResult;
import com.guide.common.utils.Tool;
import com.guide.conf.fdfs.AvatarUrlProperties;
import com.guide.conf.rabbitmq.MessageProperties;
import com.guide.mapper.GuiderMapper;
import com.guide.pojo.Guider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GuiderService {
    @Autowired
    private GuiderMapper guiderMapper;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private AvatarUrlProperties avatarUrlProperties;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MessageProperties messageProperties;
    @Autowired
    private AmqpTemplate amqpTemplate;

    public String uploadImage(MultipartFile file) {
        String url = uploadService.uploadImage(file);
        return url;
    }

    public void getMessageCode(String phone) {
        Map<String, String> msg = new HashMap<>();
        String code = NumberUtils.generateCode(6);
        System.out.println("code = " + code);
        msg.put("phone", phone);
        msg.put("code", code);
        //通过rabbitmq发送短信验证码
        amqpTemplate.convertAndSend("tourguide.sms.exchange", "tourguide.sms.code", msg);
        //生成一个redis的key
        String key = Tool.encryptRedisKey(ConstantClassField.GUIDER_UPDATE_PHONE_HEAD, phone);
        String value = Tool.encryptRedisValue(phone, code);
        //保存验证码到redis中
        redisTemplate.opsForValue().set(key, value, Long.valueOf(messageProperties.getEffectiveTime()), TimeUnit.MINUTES);
    }


    public Boolean applyToGuider(Guider guider, String code) {
        String key = Tool.encryptRedisKey(ConstantClassField.GUIDER_UPDATE_PHONE_HEAD, guider.getPhone());
        if (!Tool.encryptRedisValue(guider.getPhone(), code).equals(redisTemplate.opsForValue().get(key))) {
            throw new GuideException(ExceptionEnum.VERIFICATION_CODE_ERROR);
        }
        if (guider.getIdCardUrlList().size() != 3) {
            throw new GuideException(ExceptionEnum.GUIDER_INCOMPLETE_CERTIFICATES);
        }
        guider.setIdCardUrl(JSONObject.toJSONString(guider.getIdCardUrlList()));
        Guider tmpGuider = new Guider();
        tmpGuider.setOpenId(guider.getOpenId());
        tmpGuider = guiderMapper.selectOne(tmpGuider);
        if (tmpGuider != null) {
            if (tmpGuider.getStatus().equals(ConstantClassField.GUIDER_STATUS_NORMAL)) {
                return false;
            }
            guider.setMoney(null);
            guider.setComprehensiveScore(null);
            guider.setProfessionalScore(null);
            guider.setServiceAttitudeScore(null);
            guider.setId(null);
            guider.setStatus(ConstantClassField.GUIDER_STATUS_WAITING_AUDIT);
            Example example = new Example(Guider.class);
            example.createCriteria().andEqualTo("openId", guider.getOpenId());
            Boolean updateflag = guiderMapper.updateByExampleSelective(guider, example) == 1;
            if (updateflag) {
                redisTemplate.delete(key);
            }
            return updateflag;
        }
        guider.setId(null);
        guider.setMoney(0);
        guider.setServiceAttitudeScore(100d);
        guider.setProfessionalScore(100d);
        guider.setComprehensiveScore(100d);
        guider.setServedPeopleNum(0);
        if(guider.getGender()==null){
            guider.setGender(ConstantClassField.GUIDER_MALE);
        }
        if(guider.getGender().equals(ConstantClassField.GUIDER_MALE)){
            guider.setAvatarUrl(avatarUrlProperties.getDefaultMaleAvatarUrl());
        }else{
            guider.setAvatarUrl(avatarUrlProperties.getDefaultFemaleAvatarUrl());
        }
        //设置状态为审核状态
        guider.setStatus(ConstantClassField.GUIDER_STATUS_WAITING_AUDIT);
        // 如果注册成功，删除redis中的code
        Boolean flag = guiderMapper.insert(guider) == 1;
        if (flag) {
            redisTemplate.delete(key);
        }
        return flag;
    }

    public PageResult<Guider> getGuiders(Integer page, Integer rows, String sortBy, boolean desc, String key) {

        PageHelper.startPage(page, rows);
        Example example = new Example(Guider.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().orLike("name", "%" + key + "%");
        }
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        example.createCriteria().andEqualTo("status", ConstantClassField.GUIDER_STATUS_NORMAL);
        List<Guider> guiderList = guiderMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(guiderList)) {
            throw new GuideException(ExceptionEnum.GUIDER_NOT_FOUND);
        }
        PageInfo<Guider> result = new PageInfo<>(guiderList);
        guiderList = guiderList.stream().map(guider -> {
            guider.setIdCardUrlList(JSONObject.parseArray(guider.getIdCardUrl(), String.class));
            return guider;
        }).collect(Collectors.toList());
        return new PageResult<>(result.getTotal(), page, guiderList, rows);
    }

    public Guider getGuiderByopenId(String openid) {
        Guider guider = new Guider();
        guider.setOpenId(openid);
        return guiderMapper.selectOne(guider);
    }

    public Boolean updateGuiderByOpenId(Guider guider) {
        Guider tmpGuider = new Guider();
        tmpGuider.setName(guider.getName());
        tmpGuider.setGender(guider.getGender());
        tmpGuider.setAddress(guider.getAddress());
        tmpGuider.setUnitFee(guider.getUnitFee());
        tmpGuider.setServiceTime(guider.getServiceTime());
        tmpGuider.setIntroduce(guider.getIntroduce());
        Example example = new Example(Guider.class);
        example.createCriteria().andEqualTo("openId",guider.getOpenId());
        return guiderMapper.updateByExampleSelective(guider,example)==1;
    }

    public Boolean updateAvatarUrl(String openId, MultipartFile file) {
        String avatarUrl = uploadService.uploadImage(file);
        if (StringUtils.isNotBlank(avatarUrl)) {
            Example example = new Example(Guider.class);
            example.createCriteria().andEqualTo("openId", openId);
            Guider guider = new Guider();
            guider.setAvatarUrl(avatarUrl);
            return guiderMapper.updateByExampleSelective(guider, example) == 1;
        }
        return false;
    }
    @Transactional
    public Boolean updatePhone(String openId, String phone, String code) {
        String key = Tool.encryptRedisKey(ConstantClassField.GUIDER_UPDATE_PHONE_HEAD, phone);
        if (!Tool.encryptRedisValue(phone, code).equals(redisTemplate.opsForValue().get(key))) {
            throw new GuideException(ExceptionEnum.VERIFICATION_CODE_ERROR);
        }
        guiderMapper.transferPhone(phone);
        Guider guider = new Guider();
        guider.setPhone(phone);
        Example example = new Example(Guider.class);
        example.createCriteria().andEqualTo("openId", openId);
        Boolean flag = guiderMapper.updateByExampleSelective(guider, example) == 1;
        // 如果注册成功，删除redis中的code
        if (flag) {
            try {
                redisTemplate.delete(key);
            } catch (Exception e) {
                log.error("【导游服务】删除Redis手机验证码失败，phone：{}", phone, e);
            }
        }
        return flag;
    }



}
