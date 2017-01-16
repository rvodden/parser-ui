package com.vodden.math.parser.test.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cucumber.runtime.java.guice.InjectorSource;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface GuiceCucumberOptions {

    Class<? extends InjectorSource> injectorSource();

}
