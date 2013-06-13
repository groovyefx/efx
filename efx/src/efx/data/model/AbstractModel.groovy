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

import java.lang.annotation.Annotation
import java.lang.reflect.Field

import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.beans.property.ReadOnlyProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections
import javafx.collections.ObservableList

import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.reflect.MethodUtils

import efx.form.validation.AnnotationValidator
import efx.form.validation.annotation.DateRangeValidator
import efx.form.validation.annotation.RequiredFieldValidator
import efx.form.validation.annotation.RequiredStringValidator
import efx.form.validation.annotation.StringLengthValidator
import efx.form.validation.annotation.Validator
import groovy.transform.CompileStatic

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public abstract class AbstractModel implements efx.form.validation.Validator<AbstractModel> {

	def isChange = false;

	def fieldMonitor = {
		o1,old,newValue->
		isChange= true
	} as ChangeListener;

	private HashMap<String, List<String>> fieldErrors = new HashMap<String, List<String>>();

	private ObservableList<FieldValidationListener> fieldValidationHandlers = FXCollections.observableArrayList();

	private boolean initFields = false;

	private HashMap<String, Field> fieldMap = new HashMap<String, Field>();



	private String checkingSingleField;

	private final ReadOnlyBooleanWrapper validProperty = new ReadOnlyBooleanWrapper();

	public boolean isValidProperty() {
		return validProperty.get();
	}

	public ReadOnlyBooleanProperty validPropertyProperty() {
		return validProperty.getReadOnlyProperty();
	}

	public Map getFields(){
		if(!initFields){
			initFields()
		}
		return fieldMap
	}

	@CompileStatic
	private void initFields(){
		Field[] fields = getClass().getDeclaredFields();
		for(Field field:fields){
			Annotation[] annos = field.getAnnotations();
			for(Annotation anno:annos){
				if(anno instanceof efx.data.model.annotation.Field){
					field.setAccessible(true);
					fieldMap.put(field.getName(), field);
					break;
				}
			}
		}
		initFields = true;
	}

	private String[] getFieldNames(){
		if(!initFields){
			initFields();
		}
		return fieldMap.keySet().toArray(new String[0]);
	}

	private Object getFieldValue(String fieldName){
		if(getField(fieldName)==null){
			throw new RuntimeException(String.format("未在model '%s' 中找到名称为 '%s' 的字段", ObjectUtils.toString(this),fieldName));
		}
		try {
			return getField(fieldName).get(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Field getField(String fieldName){
		if(!initFields){
			initFields();
		}
		return fieldMap.get(fieldName);
	}

	void addFieldValidationHandler(FieldValidationListener fieldErrorListener) {
		this.fieldValidationHandlers.add(fieldErrorListener);
	}

	void removeFieldValidationHandler(FieldValidationListener fieldErrorListener) {
		this.fieldValidationHandlers.remove(fieldErrorListener);
	}

	void fireFieldValidationEvent(String fieldName, ValidationResultType type, String errorMsg) {
		fireFieldValidationEvent(new FieldValidationResult(errorMsg, type, fieldName));
	}

	void fireFieldValidationEvent(FieldValidationResult result){
		FieldValidationEvent evt = new FieldValidationEvent(result);
		for (FieldValidationListener l : fieldValidationHandlers) {
			l.onValidate(evt);
		}
	}

	void setErrorField(String fieldName, String errorMsg) {
		clearFieldError(fieldName);
		addFieldError(fieldName, errorMsg);
		fireFieldValidationEvent(fieldName, ValidationResultType.ERROR, errorMsg);
	}

	void setErrorField(String fieldName, List errorMsgs){
		clearFieldError(fieldName);
		errorMsgs.each { addFieldError(fieldName,it) }
		fireFieldValidationEvent(fieldName, ValidationResultType.ERROR, errorMsgs.join("<br/>"));
	}

	void addFieldError(String fieldName, String errorMsg) {
		if(StringUtils.isNotEmpty(checkingSingleField) && !checkingSingleField.equals(fieldName)){
			return;
		}
		if (fieldErrors.containsKey(fieldName)) {
			List<String> errors = fieldErrors.get(fieldName);
			errors.add(errorMsg);
		} else {
			List<String> errors = new ArrayList<String>();
			errors.add(errorMsg);
			fieldErrors.put(fieldName, errors);
		}
	}

	List<String> getFieldErrors(String fieldName) {
		return fieldErrors.get(fieldName);
	}

	String getFieldError(String fieldName) {
		List<String> errors = getFieldErrors(fieldName);
		if (errors != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < errors.size(); i++) {
				if (i != 0) {
					sb.append("\n");
				}
				sb.append(errors.get(i));
			}
			return sb.toString();
		}
		return null;
	}

	void clearFieldError(String fieldName) {
		fieldErrors.remove(fieldName);
		fireFieldValidationEvent(fieldName, ValidationResultType.SUCCESS, null);
	}

	void clearErrors() {
		Set<String> keys = fieldErrors.keySet();
		for (String fieldName : keys) {
			fireFieldValidationEvent(fieldName, ValidationResultType.SUCCESS, null);
		}
		fieldErrors.clear();
	}

	boolean checkFieldByName(String fieldName) {
		clearFieldError(fieldName);

		Field field = getField(fieldName);
		Annotation[] annos = field.getAnnotations();
		boolean valid = true;
		Object fieldValue = getFieldValue(fieldName);

		for (Annotation anno : annos) {
			if (accept(anno)) {
				if (anno instanceof Validator) {
					Class<?> clazz = ((Validator) anno).value();
					if (!validate(clazz,anno,fieldValue,fieldName)) {
						valid = false;
						break;
					}
				} else {
					if (!validate(getValidatorClass(anno),anno,fieldValue,fieldName)) {
						valid = false;
						break;
					}
				}
			}
		}
		return valid;
	}

	private boolean accept(Annotation anno) {
		[
			anno instanceof DateRangeValidator,
			anno instanceof RequiredStringValidator,
			anno instanceof RequiredFieldValidator,
			anno instanceof StringLengthValidator,
			anno instanceof Validator
		].any()
	}

	private Class<?> getValidatorClass(Annotation anno) {
		try {
			return (Class<?>) MethodUtils.invokeExactMethod(anno, "validator");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	boolean checkFieldWithMark(String fieldName){
		FieldValidationResult rs = checkField(fieldName);
		fireFieldValidationEvent(rs);
		return rs.getType() == ValidationResultType.SUCCESS;
	}

	/**
	 * 校验某个名称为fieldName的字段，此方法会调用validate方法获取验证信息
	 * 
	 * @param fieldName
	 * @return
	 */
	FieldValidationResult checkField(String fieldName) {
		return checkField(fieldName, true);
	}

	/**
	 * 校验某个名称为fieldName的字段
	 * 
	 * @param fieldName
	 *            字段名
	 * @param invokeValidateMethod
	 *            是否调用validate方法获取验证信息
	 * @return
	 */
	FieldValidationResult checkField(String fieldName, boolean invokeValidateMethod) {
		try {
			checkFieldByName(fieldName);
			if (invokeValidateMethod) {
				checkingSingleField = fieldName;
				validate(this);
			}
			if (fieldErrors.containsKey(fieldName)) {
				return new FieldValidationResult(getFieldError(fieldName), ValidationResultType.ERROR, fieldName);
			} else {
				return new FieldValidationResult(null, ValidationResultType.SUCCESS, fieldName);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally{
			checkingSingleField = null;
		}
	}

	/**
	 * 校验Model的有效性
	 */
	boolean checkValid() {
		boolean valid = true;
		String[] fieldNames = getFieldNames();
		for (String fieldName: fieldNames) {
			FieldValidationResult rs = checkField(fieldName,false);
			if (rs.getType()==ValidationResultType.ERROR) {
				valid = false;
			}
		}
		if(!validate(this)){
			valid = false;
		}

		for(String fieldName:fieldNames){
			String errorMessage = getFieldError(fieldName);
			if(StringUtils.isNotEmpty(errorMessage)){
				fireFieldValidationEvent(fieldName, ValidationResultType.ERROR, errorMessage);
			}else{
				fireFieldValidationEvent(fieldName, ValidationResultType.SUCCESS, null);
			}
		}

		validProperty.set(valid);
		return valid;
	}

	boolean validate(Class<?> clazz,Annotation anno,Object fieldValue,String fieldName) {
		try {
			AnnotationValidator v = (AnnotationValidator) clazz.newInstance();
			if(fieldValue instanceof ReadOnlyProperty){
				fieldValue = ((ReadOnlyProperty) fieldValue).getValue();
			}
			return v.validate(this,anno,fieldValue,fieldName);
		} catch (Exception e) {
			throw new RuntimeException(String.format("加载验证器%s出错", clazz), e);
		}
	}

	boolean isValid() {
		checkValid();
		return validProperty.get();
	}

	/**
	 * 子类重写该方法以实现自己的校验规则，返回true表示验证通过
	 */
	@Override
	boolean validate(AbstractModel model) {
		return true;
	}

	Map<String,Object> toMap(){
		if(!initFields){
			initFields()
		}
		Map<String,Object> map = new HashMap<String,Object>();
		Set<String> keySet = fieldMap.keySet();
		for(String key:keySet){
			Field field = fieldMap.get(key);
			try {
				Object value = field.get(this);
				map.put(key, ModelValueParser.parse(value));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return map;
	}

	def copyTo(AbstractModel target){
		getFields().each {fieldName,field->
			target."set${fieldName[0].toUpperCase()}${fieldName[1..-1]}"("get${fieldName[0].toUpperCase()}${fieldName[1..-1]}"())
		}
	}

	def copyFrom(AbstractModel target){
		removeMonitor()
		getFields().each {fieldName,field->
			"set${fieldName[0].toUpperCase()}${fieldName[1..-1]}"(target."get${fieldName[0].toUpperCase()}${fieldName[1..-1]}"())
		}
		addMonitor()
	}

	def addMonitor(){
		fieldMap.each {fieldName,filed->
			if(filed.get(this)){
				filed.get(this).addListener(fieldMonitor)
			}
		}
	}

	def removeMonitor(){
		isChange= false
		fieldMap.each {fieldName,filed->
			if(filed.get(this)){
				filed.get(this).removeListener(fieldMonitor)
			}
		}
	}
}
