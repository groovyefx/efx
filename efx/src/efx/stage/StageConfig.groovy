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
package efx.stage;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.stage.Modality;
import javafx.stage.Window;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 * 
 */
public class StageConfig {
	Window owner;
	/**
	 * 模态模式
	 */
	Modality modality;
	/**
	 * 窗口是否可拖动
	 */
	boolean draggable;
	URL fxml;
	String styleSheets;
	ResourceBundle bundle;
	/**
	 * 是否开启动画效果
	 */
	boolean animate;
	/**
	 * HashMap<参数名,参数值>
	 */
	HashMap<String, Object> paramsMap;
	/**
	 * 参数值数组
	 */
	Object[] params;
	/**
	 * 是否以模态窗口模式展现
	 */
	boolean blockShow;
	/**
	 * 是否透明
	 */
	boolean transparent;
	
	/**
	 * 是否全屏
	 */
	boolean fullScreen;

}
