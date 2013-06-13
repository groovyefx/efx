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

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WritableValue;
import efx.form.BindDirection;
import efx.util.converter.Converter;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public class BindUtil {

	/**
	 * 
	 * @param source
	 * @param target
	 * @param direction
	 *            {@link efx.form.BindDirection}
	 *            当为单向时，只向node的某个属性添加changelistener且不会设置node的某个属性的初始值
	 *            当为反向时，只向property添加changelistener
	 * @param converter {@linkplain efx.util.converter.Converter 类型转换器}
	 * @return
	 */
	public static Binding binding(ReadOnlyProperty source, ReadOnlyProperty target, BindDirection direction, Converter converter,String targetProperty = "") {
		PropertyListener pl = bind(source, target,direction, converter, targetProperty);
		return new Binding(source, target, pl, direction);
	}

	public static PropertyListener bind(ReadOnlyProperty source, ReadOnlyProperty target, BindDirection direction, Converter converter,String targetProperty = "") {
		PropertyListener pl = new PropertyListener(source, target, converter, targetProperty);
		bind(source, target, direction, pl, converter,targetProperty);
		return pl;
	}

	public static PropertyListener bind(ReadOnlyProperty source, ReadOnlyProperty target, BindDirection direction) {
		return bind(source, target, direction, (Converter)null);
	}

	/**
	 * 单向绑定，source 绑定 target，target的值发生变化时，source同步更新
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static PropertyListener bind(ReadOnlyProperty source, ReadOnlyProperty target) {
		return bind(source, target, BindDirection.Unidirectional);
	}

	public static void bind(ReadOnlyProperty source, ReadOnlyProperty target, BindDirection direction, ChangeListener listener, Converter converter,String targetProperty = "") {		
		switch (direction) {
			case BindDirection.Bidirectional:
			case BindDirection.Unidirectional:
				if (source instanceof WritableValue) {
					if (converter != null) {
						((WritableValue) source).setValue(converter.toSource(target.getValue()));
					}else if(targetProperty){
						((WritableValue) source).setValue(target.getValue()?."${targetProperty}Property"()?.value);
					}else {
						((WritableValue) source).setValue(target.getValue());
					}
				}
				break;
			case BindDirection.Reverse:
				if (target instanceof WritableValue) {
					if (converter != null) {
						((WritableValue) target).setValue(converter.toTarget(source.getValue()));
					}else if(targetProperty){
						((WritableValue) target).setValue(source.getValue()?."${targetProperty}Property"()?.value);
					}else {
						((WritableValue) target).setValue(source.getValue());
					}
				}
		}

		switch (direction) {
			case BindDirection.Bidirectional:
				source.addListener(listener);
				target.addListener(listener);
				break;
			case BindDirection.Unidirectional:
				target.addListener(listener);
				break;
			case BindDirection.Reverse:
				source.addListener(listener);
		}
	}
}
