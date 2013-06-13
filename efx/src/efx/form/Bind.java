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
package efx.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import efx.data.model.DefaultPropertyConversionExceptionHandler;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Bind {
	/**
	 * 要绑定的model的属性
	 * 
	 * @return
	 */
	String value();

	/**
	 * 绑定方向，默认为双向
	 * 
	 * @return
	 */
	BindDirection direction() default BindDirection.Bidirectional;

	/**
	 * 要绑定的属性
	 * 
	 * @return
	 */
	String property() default "";

	/**
	 * 绑定时使用的Converter
	 * 
	 * @return
	 */
	Class<?> converter() default Void.class;

	/**
	 * {@linkplain efx.data.model.PropertyConversionExceptionHandler 转换异常处理器} <br/>
	 * <font color=red>注意：此属性只有在应用@Bind注解的类型为Node时才有效</font>
	 * 
	 * @return
	 */
	Class<?> handler() default DefaultPropertyConversionExceptionHandler.class;

	/**
	 * 当property属性间转换发生异常时，显示的异常描述信息<br/>
	 * <font color=red>注意：此属性只有在应用@Bind注解的类型为Node时才有效</font>
	 * 
	 * @return
	 */
	String message() default "";
	
	/**
	 * 目标属性名称
	 * @return
	 */
	String targetProperty() default "";
}
