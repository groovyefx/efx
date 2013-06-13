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
package efx.util;

import java.lang.ref.WeakReference;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

import org.apache.commons.lang3.ObjectUtils;

import efx.util.converter.Converter;


/**
 * 
 * 非线程安全的，当在多线程下对property进行更新操作时，与之相对应的property可能不能被及时更新
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public class PropertyListener implements ChangeListener {

	private final WeakReference<ReadOnlyProperty> sourcePropertyRef;
	private final WeakReference<ReadOnlyProperty> targetPropertyRef;
	private Converter converter;
	private String targetProperty;
	private boolean updating;

	public PropertyListener(ReadOnlyProperty source, ReadOnlyProperty target, Converter converter, String targetProperty) {
		this.sourcePropertyRef = new WeakReference<ReadOnlyProperty>(source);
		this.targetPropertyRef = new WeakReference<ReadOnlyProperty>(target);
		this.converter = converter;
		this.targetProperty = targetProperty;
	}
	
	public void removeSourceListener() {
		ReadOnlyProperty prop1 = this.sourcePropertyRef.get();
		if (prop1 != null) {
			prop1.removeListener(this);
		}
	}

	public void removeTargetListener() {
		ReadOnlyProperty prop2 = this.targetPropertyRef.get();
		if (prop2 != null) {
			prop2.removeListener(this);
		}
	}

	@Override
	public void changed(ObservableValue paramObservableValue, Object arg1, Object arg2) {
		if (!updating) {
			ReadOnlyProperty prop1 = this.sourcePropertyRef.get();
			ReadOnlyProperty prop2 = this.targetPropertyRef.get();
			if ((prop1 == null) || (prop2 == null)) {
				removeSourceListener();
				removeTargetListener();
			} else {
				try {
					this.updating = true;
					if (prop1.equals(paramObservableValue)) {
						if (prop2 instanceof WritableValue) {
							WritableValue v = (WritableValue) prop2;
							if (converter != null) {
								try{
									v.setValue(converter.toTarget(prop1.getValue()));								
								}catch(Exception e){
									
								}								
							}else if(targetProperty){
								v.setValue(prop1.getValue()?."${targetProperty}Property"()?.value)
							} else {
								v.setValue(prop1.getValue());
							}
						} else {
							throw new IllegalArgumentException(String.format("targetProperty '%s'不是可写的，targetProperty必须是一个 '%s' 类型", ObjectUtils.toString(prop2),WritableValue.class.getName()));
						}

					} else {
						if (prop1 instanceof WritableValue) {
							WritableValue v = (WritableValue) prop1;
							if (converter != null) {				
								try{
									v.setValue(converter.toSource(prop2.getValue()));								
								}catch(Exception e){
									
								}																							
							}else if(targetProperty){
								v.setValue(prop2.getValue()?."${targetProperty}Property"()?.value)
							}else {
								v.setValue(prop2.getValue());
							}
						} else {
							throw new IllegalArgumentException(String.format("sourceProperty '%s'不是可写的，sourceProperty必须是一个 '%s' 类型", ObjectUtils.toString(prop2),WritableValue.class.getName()));
						}
					}
				} finally {
					this.updating = false;
				}
			}
		}
	}

}
