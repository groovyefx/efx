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
package efx.sample.form.groovy

import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener;

import org.apache.commons.lang3.StringUtils


import efx.data.model.AbstractModel
import efx.data.model.Option;
import efx.data.model.annotation.FXBindable;
import efx.form.Bind
import efx.form.BindDirection
import efx.form.validation.annotation.RequiredStringValidator

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
@FXBindable
class Person extends AbstractModel{
	
	@RequiredStringValidator
	String name = 'test'
	
	int age = 5
	
	@Bind(value = "genderOption",direction = BindDirection.Unidirectional, targetProperty = "value")
	String gender 
	
	Option genderOption  
	
	@Override
	public boolean validate(AbstractModel model) {
		if(getAge()<=0 || getAge() > 110){
			addFieldError("age", "年龄必须在1到110之间！");
			return false;
		}
		if(StringUtils.isEmpty(getGender())){			
			addFieldError("genderOption", "请选择性别！");
			return false;
		}
		return true;
	}
	
}
