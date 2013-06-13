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
package test.efx.util

import javafx.collections.FXCollections
import efx.data.model.AbstractModel
import efx.data.model.annotation.FXBindable
import efx.util.MapUtil

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
class MapUtilTests extends GroovyTestCase{
	void testModelToMap(){
		def p1 = new Person(name:'john',age:5,date:new Date(),map:FXCollections.observableMap([1:'abc','g':567]),list:FXCollections.observableList([new Child(name:'test1'),new Child(name:'test2')]))
		println MapUtil.modelToMap(p1)
		
		println MapUtil.modelToMap(prefix1:p1,prefix2:p1,id:5)
	}
}

@FXBindable
class Person extends AbstractModel{
	String name
	int age
	Date date
	List list
	Map map
	Set set
}

@FXBindable
class Child extends AbstractModel{
	String name
}