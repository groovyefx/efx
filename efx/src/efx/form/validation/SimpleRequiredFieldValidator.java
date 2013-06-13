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

import javafx.beans.property.ReadOnlyProperty;
import efx.data.model.AbstractModel;
import efx.form.validation.annotation.RequiredFieldValidator;
import efx.util.StringUtil;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public class SimpleRequiredFieldValidator implements AnnotationValidator {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean validate(AbstractModel model, Annotation annotation, Object fieldValue,String fieldName) {
		
		if (fieldValue == null || ((fieldValue instanceof ReadOnlyProperty)&&((ReadOnlyProperty)fieldValue).getValue()==null)) {
			RequiredFieldValidator anno = (RequiredFieldValidator) annotation;
			if (StringUtil.isNotEmpty(anno.message())) {
				model.addFieldError(fieldName, anno.message());
			} else if (StringUtil.isNotEmpty(anno.key())) {
				ResourceBundle bundle = ResourceBundle.getBundle(model.getClass().getName());
				String message = bundle.getString(anno.key());
				model.addFieldError(fieldName, message);
			} else {
				model.addFieldError(fieldName, "不能为空");
			}
			return false;
		}
		return true;
	}

}
