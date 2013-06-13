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

import java.lang.reflect.Field


import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.PropertyValueFactory
import efx.data.model.annotation.FXBindable;
import efx.util.Binding
/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public abstract class Controller implements Initializable {

	private ResourceBundle resource;

	private URL url;

	Scene scene;

	/**
	 * 数据加载状态
	 */
	@FXBindable
	Object loadingState

	////////////////////////////////
	def ResourceBundle getResource() {
		return resource;
	}

	def URL getUrl() {
		return url;
	}

	protected final List<Binding> bindingList = new ArrayList<Binding>();
	
	def getBindingList(){
		bindingList
	}

	def unbindBidirectional(ReadOnlyProperty property) {
		for (Binding b : bindingList) {
			if (b.unbindBidirectional(property)) {
				break;
			}
		}
	}

	def unBind(ReadOnlyProperty property) {
		for (Binding b : bindingList) {
			if (b.unBind(property)) {
				break;
			}
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		this.url = url;
		this.resource = resource;
	}

	protected void beforeInit() {
		if (getScene() != null) {
			addStyleSheets("style.css");
		}

		Field[] fields = getClass().getDeclaredFields();
		for(field in fields){
			if(field.getAnnotation(FXML.class) && field.getType() == TableColumn.class){
				field.setAccessible(true)
				// 初始表格列绑定的属性值名称为表格列变量名
				field.get(this).setCellValueFactory(new PropertyValueFactory(field.getName()))
			}
		}
	}

	private void addStyleSheets(String... name) {
		ObservableList<String> sheets = getScene().getStylesheets();
		for (int i = 0; i < name.length; i++) {
			sheets.add(getClass().getResource("/efx/resource/${name[i]}").toExternalForm());
		}
	}

	protected void init(){
	}

	protected void afterInit(){
	}

	void loadContent(){
	}
}
