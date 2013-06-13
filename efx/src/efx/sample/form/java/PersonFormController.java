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
package efx.sample.form.java;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import efx.app.FormController;
import efx.data.model.annotation.Model;
import efx.form.Bind;
import efx.util.converter.IntegerStringConverter;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
@SuppressWarnings("rawtypes")
public class PersonFormController extends FormController {

	@Model
	private Person person;

	@Bind(value="person.age",converter=IntegerStringConverter.class)
	@FXML
	// fx:id="age"
	private TextField age; // Value injected by FXMLLoader

	@Bind("person.gender")
	@FXML
	// fx:id="gender"
	private ComboBox gender; // Value injected by FXMLLoader
	
	/**
	 * property 绑定model中的property
	 */
	@Bind(value="person.name")
	private StringProperty bindNameProperty = new SimpleStringProperty();

	/**
	 * 指定控件的绑定属性
	 */
	@Bind(value="person.name",property="text")
	@FXML
	// fx:id="personName"
	private TextField personName; // Value injected by FXMLLoader
	
	@Override
	protected void init() {
		person = new Person();
	}
		
	public void commit(){
		person.checkValid();
	}
	
	public void reset(){
		person.clearErrors();
		System.out.println(bindNameProperty.get());
	}

}
