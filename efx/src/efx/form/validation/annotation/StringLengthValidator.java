/*
 * Copyright 2013-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package efx.form.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import efx.form.validation.SimpleStringLengthValidator;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface StringLengthValidator {		
		
	String message() default "";
	String key() default "";	
	
	Class<?> validator() default SimpleStringLengthValidator.class;
	
	/**
	 * 在编写properties文件时，消息内容可通过{min}来引用min()的返回值
	 * @return
	 */
	int min();
	/**
	 * 在编写properties文件时，消息内容可通过{max}来引用max()的返回值
	 * @return
	 */
	int max();
	
	boolean trim() default true;
}
