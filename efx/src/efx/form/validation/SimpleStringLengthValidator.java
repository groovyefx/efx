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
package efx.form.validation;

import java.lang.annotation.Annotation;
import java.util.ResourceBundle;


import efx.data.model.AbstractModel;
import efx.form.validation.annotation.StringLengthValidator;
import efx.util.StringUtil;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public class SimpleStringLengthValidator implements AnnotationValidator {

	@Override
	public boolean validate(AbstractModel model, Annotation annotation, Object fieldValue,String fieldName) {
		if (fieldValue instanceof String) {
			String value = (String) fieldValue;
			if (StringUtil.isNotEmpty(value)) {
				StringLengthValidator anno = (StringLengthValidator) annotation;
				if(anno.trim()){
					value = value.trim();
				}
				if (anno.min() > value.length() || anno.max() < value.length()) {
					if (StringUtil.isNotEmpty(anno.message())) {
						model.addFieldError(fieldName, anno.message());
					} else if(StringUtil.isNotEmpty(anno.key())){
						ResourceBundle bundle = ResourceBundle.getBundle(model.getClass().getName());
						String message = bundle.getString(anno.key());
						if (StringUtil.isNotEmpty(message)) {
							message = message.replace("{min}", String.valueOf(anno.min())).replace("{max}", String.valueOf(anno.max()));
						}
						model.addFieldError(fieldName, message);
					} else{
						model.addFieldError(fieldName, String.format("字符串长度必须在%d与%d之间", anno.min(),anno.max()));
					}
					return false;
				}
			}
			
		}
		return true;
	}

}
