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
package test.efx.data.model.annotation;

import static org.junit.Assert.*
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import efx.data.model.AbstractModel
import efx.data.model.Option;
import efx.data.model.annotation.FXBindable;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
class FXBindableTests extends GroovyTestCase{  

	void testFXBindable(){
		def now = new Date()      
		def p = new Person()  
		assert p.getBirth() == null     
		
		def sp = new SimpleStringProperty('john')
		def ip = new SimpleIntegerProperty(26)
		def op = new SimpleObjectProperty(now)   	
		println 'okay..'	
		
		// Person p的所有属性（例如name）是懒加载的，必须通过p.nameProperty()来初始化
		assert p.@name == null
		assert p.name() != null  
		p.nameProperty().bindBidirectional(sp)
		p.ageProperty().bindBidirectional(ip)   
		p.birthProperty().bindBidirectional(op)	
		
		assert p.nameProperty()==p.getNameProperty()
		assert p.nameProperty()==p.@name
		assert p.@name==p.name()

		assert p.name == 'john'
		assert p.name == p.getName()			
		assert p.@name instanceof StringProperty
		assert p.@name instanceof SimpleStringProperty
		assert p.name instanceof String
		
		assert p.age == 26
		assert p.@age instanceof SimpleIntegerProperty
		assert p.age == p.getAge()
		  
		assert p.birth == now
		assert p.getBirth() == now
		assert p.birth().value == now
		assert p.birth().get() == now
		assert p.birthProperty().value == now
		
		assert p.@birth instanceof SimpleObjectProperty
		p.age = 27
		
		assert ip.value == 27
		assert ip.get() == 27
		
		p.name = 'steven'
		assert sp.value == 'steven'
		
		p.setSet([5,6,7] as Set)
		println p.getSet()
		
	} 
	
}

@FXBindable
class Person extends AbstractModel{
	
	String name
	int age = 5
	
	boolean old

	Date birth
	
	Map map
	List list
	Set set
}