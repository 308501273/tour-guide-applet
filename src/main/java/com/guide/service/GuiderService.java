package com.guide.service;

import com.guide.common.exception.ExceptionEnum;
import com.guide.common.exception.GuideException;
import com.guide.common.utils.ConstantClassField;
import com.guide.common.utils.Tool;
import com.guide.mapper.GuiderMapper;
import com.guide.pojo.Guider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

@Service
public class GuiderService {
    @Autowired
    private GuiderMapper guiderMapper;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    public String uploadImage(MultipartFile file) {
        String url = uploadService.uploadImage(file);
        return url;
    }

    public Boolean insert(Guider guider, String code) {
        String key = Tool.encryptRedisKey(ConstantClassField.GUIDER_UPDATE_PHONE_HEAD,guider.getPhone());
        if (!Tool.encryptRedisValue(guider.getPhone(),code).equals(redisTemplate.opsForValue().get(key))) {
            throw new GuideException(ExceptionEnum.VERIFICATION_CODE_ERROR);
        }
        Guider tmpGuider = new Guider();
        tmpGuider.setOpenId(guider.getOpenId());
        tmpGuider = guiderMapper.selectOne(tmpGuider);
        if (tmpGuider != null) {
            if (tmpGuider.getStatus().equals(ConstantClassField.TEACHER_STATUS_NORMAL)) {
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
        guider.setStatus(ConstantClassField.GUIDER_STATUS_WAITING_AUDIT);         //设置为审核状态
        // 如果注册成功，删除redis中的code
        Boolean flag = guiderMapper.insert(guider) == 1;
        if (flag) {
            redisTemplate.delete(key);
        }
        return flag;
    }
}
