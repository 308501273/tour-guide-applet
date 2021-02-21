package com.guide.mapper;

import com.guide.pojo.User;
import com.guide.common.baseMapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author fengenchun
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 更改用户手机号
     * 单独写出这个方法的原因是，必须得将原来绑定过的手机号设置为空，才能绑定成自己的
     * @param phone
     * @return
     */
    @Update("UPDATE tb_user SET phone='' WHERE phone=#{phone}")
    int transferPhone(@Param("phone") String phone);
}