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
package efx.data.store;

import java.lang.reflect.Field;

import javafx.beans.value.ObservableValue;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
@SuppressWarnings("rawtypes")
public class SimpleFilter implements Filter {

	private Object value;
	private String fieldName;
	
	public SimpleFilter(String fieldName) {
		this(null,fieldName);
	}

	public SimpleFilter(Object value, String fieldName) {
		this.value = value;
		this.fieldName = fieldName;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public boolean doFilter(Object obj) {
		try {
			Field f = obj.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			Object v = f.get(obj);
			if (v == value) {
				return true;
			}

			if (v != null) {
				if (v instanceof ObservableValue) {
					Object ov = ((ObservableValue) v).getValue();
					if (ov != null) {
						return ov.equals(value);
					}
				}
				return v.equals(value);
			}
			return value == null;
		} catch (Exception e) {
			throw new SimpleFilterException(String.format(
					"在过滤'%s'时发生错误，字段名为'%s'，值为'%s'", obj, fieldName, value), e);
		}
	}

}
