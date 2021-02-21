package com.guide.conf.fdfs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author fengenchun
 */
@Getter
@Component
public class AvatarUrlProperties {
    @Value("${tourguide.avatar.defaultMaleAvatarUrl}")
    private String defaultMaleAvatarUrl;
    @Value("${tourguide.avatar.defaultFemaleAvatarUrl}")
    private String defaultFemaleAvatarUrl;
}
