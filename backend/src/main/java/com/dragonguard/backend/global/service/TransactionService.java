package com.dragonguard.backend.global.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 김승진
 * @description 트랜잭션을 사용할 서비스들에 붙을 어노테이션
 */

@Service
@Transactional
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionService {}
