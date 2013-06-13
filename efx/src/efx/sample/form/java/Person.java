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
package efx.sample.form.java;

import org.apache.commons.lang3.StringUtils;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import efx.data.model.AbstractModel;
import efx.data.model.annotation.Field;
import efx.form.validation.annotation.*;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public class Person extends AbstractModel{
    
	@Field
	@RequiredStringValidator
    private final StringProperty name = new SimpleStringProperty("test");

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public StringProperty nameProperty() {
        return name;
    }
    
    @Field    
    private final IntegerProperty age = new SimpleIntegerProperty();

    public int getAge() {
        return age.get();
    }

    public void setAge(int value) {
        age.set(value);
    }

    public IntegerProperty ageProperty() {
        return age;
    }
    
    @Field
    @RequiredStringValidator
    private final StringProperty gender = new SimpleStringProperty();

    public String getGender() {
        return gender.get();
    }

    public void setGender(String value) {
        gender.set(value);
    }

    public StringProperty genderProperty() {
        return gender;
    }
    
    @Override
    public boolean validate(AbstractModel model) {
    	if(getAge()<=0 || getAge() > 150){
    		addFieldError("age", "年龄必须在1到150之间！");
    		return false;
    	}
    	if(StringUtils.isEmpty(getGender())){
    		addFieldError("gender", "请选择性别！");
    		return false;
    	}
    	return true;
    }
            
}
