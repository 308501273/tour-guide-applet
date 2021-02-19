package com.guide.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GuideException extends RuntimeException {
    private ExceptionEnum exceptionEnum;
}
