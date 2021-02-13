package com.guide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@SpringBootApplication
@MapperScan("com.guide.mapper")
public class BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class);
    }
    /**
     * 文件上传临时路径
     */
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory multipartConfigFactory = new MultipartConfigFactory();
        String location ="/data/tmp";
        File file = new File(location);
        if(!file.exists()){
            file.mkdirs();
        }
        multipartConfigFactory.setLocation(location);
        return multipartConfigFactory.createMultipartConfig();
    }
}
