package com.example.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        //탐색 시작 경로
        basePackages = { "com.example.core", "com.example.core.member"},
        //탐색 시작 클래스(클래스를 포함한 패키지)
        basePackageClasses = AutoAppConfig.class,
        //컴포넌트 스캔 과정 중에 제외할 목록
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = Configuration.class
        )
)
public class AutoAppConfig {
}
