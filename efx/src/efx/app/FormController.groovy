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
package efx.app;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field

import javafx.beans.property.BooleanProperty
import javafx.beans.property.Property
import javafx.beans.property.ReadOnlyProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.scene.Node
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Labeled
import javafx.scene.control.TextInputControl
import javafx.scene.control.ToggleButton
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane

import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.reflect.FieldUtils
import org.apache.commons.lang3.reflect.MethodUtils

import efx.data.model.AbstractModel
import efx.data.model.FieldValidationListener
import efx.data.model.FieldValidationResult
import efx.data.model.PropertyConversionExceptionHandler
import efx.data.model.ValidationResultType
import efx.data.model.annotation.Model
import efx.form.AnnotatedNode
import efx.form.Bind
import efx.util.BindUtil
import efx.util.Binding
import efx.util.converter.Converter

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public abstract class FormController extends Controller {

	private final BooleanProperty autoValidating = new SimpleBooleanProperty(true);

	public boolean isAutoValidating() {
		return autoValidating.get();
	}

	public void setAutoValidating(boolean value) {
		autoValidating.set(value);
	}

	public BooleanProperty autoValidatingProperty() {
		return autoValidating;
	}

	private final BooleanProperty checkOnFocusLost = new SimpleBooleanProperty(true);

	public boolean isCheckOnFocusLost() {
		return checkOnFocusLost.get();
	}

	public void setCheckOnFocusLost(boolean value) {
		checkOnFocusLost.set(value);
	}

	public BooleanProperty checkOnFocusLostProperty() {
		return checkOnFocusLost;
	}

	private final BooleanProperty showHighlights = new SimpleBooleanProperty(true);

	public boolean isShowHighlights() {
		return showHighlights.get();
	}

	public void setShowHighlights(boolean value) {
		showHighlights.set(value);
	}

	public BooleanProperty showHighlightsProperty() {
		return showHighlights;
	}

	private final BooleanProperty showAnnotations = new SimpleBooleanProperty(true);

	public boolean isShowAnnotations() {
		return showAnnotations.get();
	}

	public void setShowAnnotations(boolean value) {
		showAnnotations.set(value);
	}

	public BooleanProperty showAnnotationsProperty() {
		return showAnnotations;
	}

	private final BooleanProperty showTooltips = new SimpleBooleanProperty(true);

	public boolean isShowTooltips() {
		return showTooltips.get();
	}

	public void setShowTooltips(boolean value) {
		showTooltips.set(value);
	}

	public BooleanProperty showTooltipsProperty() {
		return showTooltips;
	}

	// ///////////////////////////////////////////////////

	@Override
	public void afterInit() {
		def lazyInit = false
		if(hasProperty("lazyInit")){
			lazyInit = getProperty("lazyInit")
		}
		if(!lazyInit){
			doInit()
		}
	}

	def doInit(){
		Field[] controllerFields = getClass().getDeclaredFields();
		def modelFieldMap = processModelAnnotation(controllerFields)
		processBindAnnotation(controllerFields, modelFieldMap);
		
		modelFieldMap.each {k,v->		
			processModelInnerBinding(v.get(this))
		}
	}

	/**
	 * 返回标注有Model注解的field字段
	 * @param fields
	 * @return
	 */
	private HashMap<String, Field> processModelAnnotation(Field[] fields) {
		HashMap<String, Field> fieldMap = new HashMap<String, Field>();
		for (Field f : fields) {
			f.setAccessible(true);
			Model anno = f.getAnnotation(Model);
			if (anno != null) {
				try {
					checkModel(f, f.get(this));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				fieldMap.put(f.getName(), f);
			}
		}
		return fieldMap;
	}

	ReadOnlyProperty getTargetProperty(Field nodeField, String path,owner = this) {
		if (path) {
			return getTargetProperty(nodeField,path.split("\\."),owner,path)
		}
	}

	ReadOnlyProperty getTargetProperty(Field nodeField, String[] a,owner = this,String path) {
		if (a) {
			List<String> paths = new LinkedList<String>(Arrays.asList(a));
			FieldInfo nodePropertyFieldInfo = getFieldInfo(owner, nodeField, paths);
			// 验证node中的property
			checkField("${nodeField.getName()}.$path", nodePropertyFieldInfo.fieldValue);
			return (ReadOnlyProperty) nodePropertyFieldInfo.fieldValue;
		}
	}

	def Converter getConverter(Class<?> converterClazz) {		
		if (!Void.class.equals(converterClazz)) {
			Object obj;
			try {
				obj = converterClazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (obj instanceof Converter) {
				return (Converter) obj;
			} else {
				throw new IllegalArgumentException(String.format("'%s'不是一个'%s'类型", ToStringBuilder.reflectionToString(obj), Converter.class.getName()));
			}
		}
		return null
	}

	private PropertyConversionExceptionHandler getPropertyConversionExceptionHandler(Class<?> handlerClazz, AbstractModel model, String fieldName, String message) {
		PropertyConversionExceptionHandler handler = null;

		Object obj;
		try {
			obj = handlerClazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (obj instanceof PropertyConversionExceptionHandler) {
			handler = (PropertyConversionExceptionHandler) obj;
			handler.setModel(model);
			handler.setFieldName(fieldName);
			handler.setMessage(message);
		} else {
			throw new IllegalArgumentException(String.format("'%s' 不是一个'%s'类型", ToStringBuilder.reflectionToString(obj), PropertyConversionExceptionHandler.class.getName()));
		}

		return handler;
	}

	void processModelInnerBinding(AbstractModel model){
		model.class.declaredFields.each {			
			if(it.getAnnotation(efx.data.model.annotation.Field)){
				Bind anno = it.getAnnotation(Bind.class)				
				if(anno){
					findBindingProperty(it,model,anno).with{						
						getBindingList()<<BindUtil.binding(sourceProperty, targetProperty, anno.direction(), getConverter(anno.converter()),anno.targetProperty())
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param field
	 * @param owner
	 * @param bind
	 * @return [sourceProperty:sourceProperty,targetProperty:targetProperty]	
	 */
	def findBindingProperty(Field field,owner,Bind bind){
		ReadOnlyProperty targetProperty,sourceProperty = getTargetProperty(field, bind.property(),owner)?:owner."${field.name}Property"()

		if(!bind.value()){
			throw new RuntimeException("owner[${owner.getClass().name}], field[${field.name}] @Bind注解的value值不能为空")
		}
		// get target field
		String[] a = bind.value().split("\\.");
		Field targetField = owner.class.declaredFields.find {
			it.name == a[0]
		}

		if(!targetField){
			throw new RuntimeException("找不到要绑定的字段：owner[${owner.getClass().name}], field[${a[0]}]")
		}

		if(a.length == 1){
			targetProperty = owner."${a[0]}Property"()
		}else{
			targetProperty = getTargetProperty(targetField, a[1..-1] as String[],owner,a[1..-1].join("."))
		}
		if(!targetProperty){
			throw new RuntimeException("找不到要绑定的目标属性：owner[${owner.getClass().name}], field[${a[0]}] path[${bind.value()}]")
		}
		return [sourceProperty:sourceProperty,targetProperty:targetProperty]
	}

	/**
	 * 
	 * @param fields all of controller fields
	 * @param modelFieldMap a map contains controller field which has a annotation of {@link efx.data.annotation.Model}
	 */
	private void processBindAnnotation(Field[] fields, HashMap<String, Field> modelFieldMap) {
		for (Field f : fields) {
			Bind bind = f.getAnnotation(Bind);
			if (bind != null) {
				String[] a = bind.value().split("\\.");
				Field modelField = modelFieldMap.get(a[0]);
				if (modelField == null) {
					throw new IllegalStateException(String.format("未能找到绑定的model：%s", a[0]));
				}

				List<String> paths = new LinkedList<String>(Arrays.asList(a));
				paths.remove(0);

				final FieldInfo modelFieldInfo = getFieldInfo(this, modelField, paths);
				// model中的property
				Object target = modelFieldInfo.fieldValue;
				// 验证model中的property
				checkField(bind.value(), target);

				try {
					ReadOnlyProperty targetProperty = (ReadOnlyProperty) target;
					// 使用@bind注解的对象
					Object source = FieldUtils.readField(f, this, true);

					ReadOnlyProperty sourceProperty = getTargetProperty(f, bind.property());

					final Converter converter = getConverter(bind.converter());
					final AbstractModel model = (AbstractModel) FieldUtils.readField(modelField, this, true);
					final PropertyConversionExceptionHandler handler = getPropertyConversionExceptionHandler(bind.handler(), model, modelFieldInfo.fieldName, bind.message());
					Binding binding = null;
					boolean isNode = source instanceof Node, isProperty = source instanceof ReadOnlyProperty;

					if (sourceProperty == null && isNode) {
						sourceProperty = getNodeProperty((Node) source);
					}

					if (isNode) {
						binding = BindUtil.binding(sourceProperty,targetProperty, bind.direction(), converter, bind.targetProperty());
					} else if (isProperty) {
						binding = BindUtil.binding((ReadOnlyProperty) source, targetProperty, bind.direction(), converter,bind.targetProperty());
					}

					if (binding) {
						bindingList.add(binding);
					}

					if (isNode) {
						def fieldContainer = packNode((Node) source);
						if (fieldContainer) {

							// 仅当model.setErrorField或model.checkValid被调用时才会触发FieldErrorEvent
							model.addFieldValidationHandler({event->
								if (modelFieldInfo.fieldName == event.getFieldValidationResult().getFieldName()) {
									processFieldStyle(fieldContainer, event.getFieldValidationResult());
								}
							} as FieldValidationListener)

							if (isAutoValidating()) {
								if (isCheckOnFocusLost()) {
									// target is Node type
									source.focusedProperty().addListener({arg0,arg1,arg2->
										if (!arg2) {// focus lost
											if (converter != null) {
												try {
													converter.toSource(sourceProperty.getValue());
												} catch (Exception e) {
													handler.process();
													return;
												}
											}
											processFieldStyle(fieldContainer, model.checkField(modelFieldInfo.fieldName));
										}
									} as ChangeListener)
								}

								sourceProperty.addListener({arg0,arg1,arg2->
									if (converter != null) {
										try {
											converter.toSource(arg2);
										} catch (Exception e) {
											handler.process();
											return;
										}
									}
									processFieldStyle(fieldContainer, model.checkField(modelFieldInfo.fieldName));
								} as ChangeListener)

							}
						}
					}

				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	protected void processFieldStyle(AnnotatedNode fieldContainer, FieldValidationResult result) {

		ValidationResultType type = result.getType();
		String message = result.getMessage();
		if (isShowTooltips() && StringUtils.isNotBlank(result.getMessage()) && result.getType() == ValidationResultType.ERROR) {
			Tooltip tip = new Tooltip(message);
			Tooltip.install(fieldContainer, tip);
		} else {
			Tooltip.install(fieldContainer, null);
		}

		if (isShowHighlights() && type == ValidationResultType.ERROR) {
			if (!fieldContainer.getStyleClass().contains("error")) {
				fieldContainer.getStyleClass().add("error");
			}
		} else {
			fieldContainer.getStyleClass().remove("error");
		}

		if (isShowAnnotations() && type == ValidationResultType.ERROR) {
			fieldContainer.lookup("#error-icon").setVisible(true);
		} else {
			fieldContainer.lookup("#error-icon").setVisible(false);
		}

	}

	protected AnnotatedNode packNode(Node node) {
		Node parent = node.getParent();
		if (parent instanceof Pane) {
			Pane pane = (Pane) parent;
			pane.getChildren().remove(node);

			AnnotatedNode annotatedNode = new AnnotatedNode(node);
			annotatedNode.setLayoutX(node.getLayoutX());
			annotatedNode.setLayoutY(node.getLayoutY());
			ImageView marker = new ImageView(new Image(getClass().getResourceAsStream("/efx/resource/exclamation.png")));
			marker.setVisible(false);
			marker.setId("error-icon");
			marker.setTranslateX(4);
			marker.setTranslateY(-4);
			annotatedNode.getAnnotations().add(marker);
			pane.getChildren().add(annotatedNode);
			return annotatedNode;
		}
		return null;
	}

	protected ReadOnlyProperty getNodeProperty(Node node) {
		ReadOnlyProperty target = null;
		if (BooleanUtils.or([
			node instanceof ComboBox,
			node instanceof ChoiceBox ] as boolean[])) {
			target = getProperty("valueProperty", node);
		} else if (BooleanUtils.or([
			node instanceof CheckBox,
			node instanceof ToggleButton ] as boolean[])) {
			target = getProperty("selectedProperty", node);
		} else if (BooleanUtils.or([
			node instanceof TextInputControl,
			node instanceof Labeled ] as boolean[])) {
			target = getProperty("textProperty", node);
		} else {
			throw new IllegalArgumentException(String.format("不支持绑定的表单控件类：%s", node.getClass().getName()));
		}
		return target;
	}

	private static Property getProperty(String propertyMethod, Node node) {
		Property target = null;
		try {
			target = (Property) MethodUtils.invokeExactMethod(node, propertyMethod);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return target;
	}

	private void checkModel(Field f, Object obj) {
		if (obj == null) {
			throw new IllegalStateException(String.format("Model字段 '%s'不能为null", f.getName()));
		}
		if (!(obj instanceof AbstractModel)) {
			throw new IllegalArgumentException(String.format("'%s'必须是'%s'类型", f.getName(), AbstractModel.class.getName()));
		}
	}

	private void checkField(String path, Object obj) {		
		if (obj == null) {
			throw new IllegalStateException(String.format("检测到属性'%s'为null", path));
		}
		if (!(obj instanceof ReadOnlyProperty)) {
			throw new IllegalArgumentException(String.format("检测到不正确的类型：'%s'不是'%s'类型", path, ReadOnlyProperty.class.getName()));
		}
	}

	private FieldInfo getFieldInfo(Object owner, Field field, List<String> path) {
		
			field.setAccessible(true);
			if (path.size() == 0) {				
				def value = field.get(owner)
				if(!value){
					value = MethodUtils.invokeExactMethod(owner, "${field.name}Property")
				}
				return [fieldName:field.getName(), fieldValue:value]
			} else {
				String fieldName = path.remove(0);				
				Object obj
				if(owner instanceof AbstractModel){
					obj = MethodUtils.invokeExactMethod(owner, "${field.name}Property").get()														
				}else{
					obj = field.get(owner)
				}								
				Field f = FieldUtils.getField(obj.getClass(), fieldName, true);
				return getFieldInfo(obj, f, path);
			}
		

	}

}
