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

import javafx.scene.Parent;
import javafx.scene.Scene;
/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
@SuppressWarnings("unchecked")
public class Activity{
	private Object controller;
	private Parent page;
	private Scene scene;
	
	public <T> T getController() {
		return (T)controller;
	}
	public void setController(Object controller) {
		this.controller = controller;
	}	
	public Parent getPage() {
		return page;
	}
	public void setPage(Parent page) {
		this.page = page;
	}
	public Scene getScene() {
		return scene;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
}
