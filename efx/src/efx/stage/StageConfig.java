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
	private Window owner;
	/**
	 * 模态模式
	 */
	private Modality modality;
	/**
	 * 窗口是否可拖动
	 */
	private boolean draggable;
	private URL fxml;
	private String styleSheets;
	private ResourceBundle bundle;
	/**
	 * 是否开启动画效果
	 */
	private boolean animate;
	/**
	 * HashMap<参数名,参数值>
	 */
	private HashMap<String, Object> paramsMap;	
	/**
	 * 参数值数组
	 */
	private Object[] params;
	/**
	 * 是否以模态窗口模式展现
	 */
	private boolean blockShow;
	/**
	 * 是否透明
	 */
	private boolean transparent;
	public Window getOwner() {
		return owner;
	}
	public void setOwner(Window owner) {
		this.owner = owner;
	}
	public Modality getModality() {
		return modality;
	}
	public void setModality(Modality modality) {
		this.modality = modality;
	}
	public boolean isDraggable() {
		return draggable;
	}
	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}
	public URL getFxml() {
		return fxml;
	}
	public void setFxml(URL fxml) {
		this.fxml = fxml;
	}
	public String getStyleSheets() {
		return styleSheets;
	}
	public void setStyleSheets(String styleSheets) {
		this.styleSheets = styleSheets;
	}
	public ResourceBundle getBundle() {
		return bundle;
	}
	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}		
	public boolean isAnimate() {
		return animate;
	}
	public void setAnimate(boolean animate) {
		this.animate = animate;
	}
	public HashMap<String, Object> getParamsMap() {
		return paramsMap;
	}
	public void setParamsMap(HashMap<String, Object> paramsMap) {
		this.paramsMap = paramsMap;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	public boolean isBlockShow() {
		return blockShow;
	}
	public void setBlockShow(boolean blockShow) {
		this.blockShow = blockShow;
	}
	public boolean isTransparent() {
		return transparent;
	}
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}
	
}
