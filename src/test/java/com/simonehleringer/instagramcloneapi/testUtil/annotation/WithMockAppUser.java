package com.simonehleringer.instagramcloneapi.testUtil.annotation;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAppUserSecurityContextFactory.class)
public @interface WithMockAppUser {
    String userIdAsString() default "11111111-1111-1111-1111-111111111111";
}
