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
package efx.data.model;

import groovy.transform.ToString;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 * @param <T>
 */
@ToString
public class Option<T> {
	
	public Option() {
	}
	
	public Option(T value,String desc) {
		this.value.set(value);
		this.desc.set(desc);
	}
	
	private final ObjectProperty<T> value = new SimpleObjectProperty<T>();

    public T getValue() {
        return value.get();
    }

    public void setValue(T value) {
        this.value.set(value);
    }

    public ObjectProperty<T> valueProperty() {
        return value;
    }
    private final StringProperty desc = new SimpleStringProperty();

    public String getDesc() {
        return desc.get();
    }

    public void setDesc(String value) {
        desc.set(value);
    }

    public StringProperty descProperty() {
        return desc;
    }

    @Override
    public String toString() {
        return desc.get();
    }



}
