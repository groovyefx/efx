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
package efx.util

import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

/**
 * 
 * 请参见{@link efx.sample.form.groovy}包下的表单例子
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
@Deprecated
class FXBeanUtil {

	private static final typeMap = [
		(Integer.class):[
			parent:'IntegerProperty',
			subClass:'SimpleIntegerProperty',
			rtn:'int'
		],
		(int.class):[
			parent:'IntegerProperty',
			subClass:'SimpleIntegerProperty',
			rtn:'int'
		],
		(Long.class):[
			parent:'LongProperty',
			subClass:'SimpleLongProperty',
			rtn:'long'
		],
		(long.class):[
			parent:'LongProperty',
			subClass:'SimpleLongProperty',
			rtn:'long'
		],
		(Double.class):[
			parent:'DoubleProperty',
			subClass:'SimpleDoubleProperty',
			rtn:'double'
		],
		(double.class):[
			parent:'DoubleProperty',
			subClass:'SimpleDoubleProperty',
			rtn:'double'
		],
		(Float.class):[
			parent:'FloatProperty',
			subClass:'SimpleFloatProperty',
			rtn:'float'
		],
		(float.class):[
			parent:'FloatProperty',
			subClass:'SimpleFloatProperty',
			rtn:'float'
		],
		(Boolean.class):[
			parent:'BooleanProperty',
			subClass:'SimpleBooleanProperty',
			rtn:'boolean'
		],
		(boolean.class):[
			parent:'BooleanProperty',
			subClass:'SimpleBooleanProperty',
			rtn:'boolean'
		],
		(String.class):[
			parent:'StringProperty',
			subClass:'SimpleStringProperty',
			rtn:'String'
		],
		(Map.class):[
			parent:'MapProperty',
			subClass:'SimpleMapProperty',
			rtn:'ObservableMap'
		],
		(Set.class):[
			parent:'SetProperty',
			subClass:'SimpleSetProperty',
			rtn:'ObservableSet'
		],
		(List.class):[
			parent:'ListProperty',
			subClass:'SimpleListProperty',
			rtn:'ObservableList'
		]
	]
	
	private static final typeList = [
		Integer.class,
		int.class,
		Long.class,
		long.class,
		Double.class,
		double.class,
		Float.class,
		float.class,
		Boolean.class,
		boolean.class,
		String.class,
		Map.class,
		Set.class,
		List.class
	]

	private static final genericTypePattern = ~/<.+>/
		
	/**
	 * 
	 * @param clazz 待转换的java类
	 * @param file java源文件
	 * @param className 要生成的类名
	 * @return
	 */
	def static generateFXBean(Class clazz,File file,String fullClassName){
		def sb = new StringBuilder(),
		className = fullClassName.tokenize('.')[-1]
		def	pkg = ''
		(fullClassName =~/(.*)\.\w+/).each { pkg = it[1] }
		if(pkg){
			pkg = "package ${pkg};"
		}

		file.delete()

		clazz.getDeclaredFields().each {
			def typeMap = getTargetFieldType(it.getType())
			def fieldName = it.getName()
			def generic = ''			
			if(typeMap['parent'] == 'ObjectProperty'){
				generic = "<${typeMap['rtn']}>"
			}else{
				(it.getGenericType().toString()=~genericTypePattern).each {
					generic = it
				}
			}
			
			sb.append("""
	////////////////////////////////////////////////////////////////////////////////
	private final ${typeMap['parent']}${generic} ${fieldName} = new ${typeMap['subClass']}${generic}();

	public ${typeMap['rtn']} ${getGetterName(it)}(){
		return ${fieldName}.get();
	}

	public void ${getSetterName(fieldName)}(${typeMap['rtn']} value){
		${fieldName}.set(value);
	} 

	public ${typeMap['parent']} ${fieldName}Property() {
        return ${fieldName};
    }
""")
		}

		file.append("""
${pkg}
import javafx.beans.property.*;
import javafx.collections.*;

public class ${className}{
	${sb}
}
""")
	}

	def static getTargetFieldType(Class clazz){
		def types = typeList.grep{it.isAssignableFrom(clazz)}
		if(types){
			return typeMap[types[0]]
		}else{
			return [
				parent:'ObjectProperty',
				subClass:'SimpleObjectProperty',
				rtn:clazz.getName()
			]
		}
	}

	def static getGetterName(Field field){
		def type = field.getType()
		def name = field.getName()
		if(type==Boolean.class || type==boolean.class){
			return "is${name[0].toUpperCase()}${name.substring(1)}"
		}
		return "get${name[0].toUpperCase()}${name.substring(1)}"
	}

	def static getSetterName(String name){
		return "set${name[0].toUpperCase()}${name.substring(1)}"
	}
	
	def static copyFXBeanToJavaBean(fxBean,javaBean){
		if(!javaBean || !fxBean){
			return;
		}
		def fxBeanFields = fxBean.class.getDeclaredFields();
		for(Field fxBeanField:fxBeanFields){
			if(ReadOnlyProperty.class.isAssignableFrom(fxBeanField.getType())){
				fxBeanField.setAccessible(true);
				def value = fxBeanField.get(fxBean).getValue();				
				def javaBeanField = FieldUtils.getField(javaBean.class, fxBeanField.getName(), true);
				if(javaBeanField){
					javaBeanField.set(javaBean, value);
				}				
			}
		}
	}
	
	def static copyJavaBeanToFXBean(javaBean,fxBean){
		if(!javaBean || !fxBean){
			return;
		}
		def fxBeanFields = fxBean.class.getDeclaredFields();
		for(Field fxBeanField:fxBeanFields){
			if(WritableValue.class.isAssignableFrom(fxBeanField.getType())){
				fxBeanField.setAccessible(true);				
				def javaBeanField = FieldUtils.getField(javaBean.class, fxBeanField.getName(), true);
				if(javaBeanField){
					def value = javaBeanField.get(javaBean);
					fxBeanField.get(fxBean).setValue(value);
				}				
			}
		}
	}
		
	static main(args) {
		A a = new A();
		a.setCardNum("abc");
		B b = new B();
		copyFXBeanToJavaBean(a,b);
		println(b.cardNum);
		
		b.cardNum = "def";
		b.name = "ghi";
		copyJavaBeanToFXBean(b, a);
		println(a.getCardNum());
		println(a.getName());
	}
	
	static class A{
		private final StringProperty cardNum = new SimpleStringProperty();

		public String getCardNum() {
			return cardNum.get();
		}

		public void setCardNum(String value) {
			cardNum.set(value);
		}

		public StringProperty cardNumProperty() {
			return cardNum;
		}
		
		private final StringProperty name = new SimpleStringProperty();

		public String getName() {
			return name.get();
		}

		public void setName(String value) {
			name.set(value);
		}

		public StringProperty nameProperty() {
			return name;
		}
	}
	
	static class B{
		private String cardNum;
		private String name;
	}
}
