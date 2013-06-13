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

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.WritableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import efx.util.StringUtil;
  
/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FxmlLoader {
	/**
	 * 
	 * @param fxmlUrl
	 * @param styleSheets
	 * @param bundle 
	 * @param initScene 是否初始化场景
	 * @return
	 */
	public static Parent loadPage(URL fxmlUrl, String styleSheets, ResourceBundle bundle) {
		return loadActivity(fxmlUrl, styleSheets, bundle, false, (Object[]) null).getPage();
	}

	/**
	 * 
	 * @param fxmlUrl
	 * @param initScene 是否初始化场景
	 * @return
	 */
	public static Activity loadActivity(URL fxmlUrl, boolean initScene) {
		return loadActivity(fxmlUrl, null, null, initScene, (Object[]) null);
	}

	/**
	 * 加载视图及控制器，并初始化场景
	 * @param fxmlUrl
	 * @return
	 */
	public static Activity loadActivity(URL fxmlUrl) {
		return loadActivity(fxmlUrl, true);
	}

	
	public static Activity loadActivity(URL fxmlUrl, String styleSheets, ResourceBundle bundle, boolean initScene, HashMap<String, Object> params) {
		Activity activity = loadActivity(fxmlUrl, styleSheets, bundle, initScene);
		Controller c = activity.getController();
		if (c != null) {
			if (params != null) {
				Field[] fields = c.getClass().getDeclaredFields();
				for (Field field : fields) {
					Param annotation = field.getAnnotation(Param.class);
					if (annotation != null) {
						String name = annotation.value();
						if (name == null || name.equals("")) {
							name = field.getName();
						}
						try {
							field.setAccessible(true);
							Object value = params.get(name);
							if (WritableValue.class.isAssignableFrom(field.getType())) {
								WritableValue property = (WritableValue) field.get(c);
								property.setValue(value);
							} else {
								field.set(c, value);
							}
						} catch (IllegalAccessException e) {
							throw new ControllerParameterException(String.format("在控制器'%s'中的字段 '%s'上设置属性'%s'错误", c, field.getName(), name), e);
						}

					}
				}
			}
			initController(c, activity.getScene());
		}

		return activity;
	}

	private static void initController(Controller c, Scene scene) {
		c.setScene(scene);
		c.beforeInit();
		c.init();
		c.afterInit();
	}

	private static Activity loadActivity(URL fxmlUrl, String styleSheets, ResourceBundle bundle, boolean initScene) {
		Activity activity = new Activity();
		FXMLLoader loader = new FXMLLoader(fxmlUrl, bundle);
		try {
			Parent page = (Parent) loader.load();
			activity.setPage(page);
			if (initScene) {
				activity.setScene(new Scene(page));
			}
			if (StringUtil.isNotEmpty(styleSheets)) {
				page.getStylesheets().add(styleSheets);
			}
			activity.setController(loader.getController());
		} catch (IOException e) {
			throw new FxmlLoadException(String.format("加载fxml '%s' 出错", fxmlUrl), e);
		}
		return activity;
	}

	public static Activity loadActivity(URL fxmlUrl, String styleSheets, ResourceBundle bundle, boolean initScene, Object... params) {
		Activity activity = loadActivity(fxmlUrl, styleSheets, bundle, initScene);
		Controller c = activity.getController();
		if (c != null) {
			if (params != null && params.length > 0) {
				Field[] fields = c.getClass().getDeclaredFields();
				List<Field> fieldList = new ArrayList<Field>();
				for (Field field : fields) {
					if (field.getAnnotation(Param.class) != null) {
						fieldList.add(field);
					}
				}
				int iMin = Math.min(fieldList.size(), params.length);
				for (int i = 0; i < iMin; i++) {
					Field field = fieldList.get(i);
					try {
						field.setAccessible(true);
						Object value = params[i];
						if (WritableValue.class.isAssignableFrom(field.getType())) {
							WritableValue property = (WritableValue) field.get(c);
							property.setValue(value);
						} else {
							field.set(c, value);
						}
					} catch (IllegalAccessException e) {
						throw new ControllerParameterException(String.format("在控制器'%s'中的字段 '%s'上设置参数值时发生错误", c, field.getName()), e);
					}
				}
			}
			initController(c, activity.getScene());
		}
		return activity;
	}

}
