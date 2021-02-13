package com.guide.utils.exception;

import com.guide.utils.exception.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GuideException extends RuntimeException {
    private ExceptionEnum exceptionEnum;
}
