package com.guide.conf.fdfs;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix ="tourguide.upload" )
public class UploadProperties {
    private String baseUrl;
    private List<String> imageAllowTypes;
}
