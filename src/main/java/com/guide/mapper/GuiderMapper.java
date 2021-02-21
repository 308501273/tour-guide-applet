package com.guide.mapper;

import com.guide.pojo.Guider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface GuiderMapper extends Mapper<Guider> {

    @Update("UPDATE tb_guider SET phone='' WHERE phone=#{phone}")
    int transferPhone(@Param("phone") String phone);
}