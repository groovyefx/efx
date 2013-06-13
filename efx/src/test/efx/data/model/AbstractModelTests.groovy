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
package test.efx.data.model

import efx.data.model.AbstractModel;
import efx.data.model.annotation.FXBindable;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
class AbstractModelTests extends GroovyTestCase{

	void testCopyTo(){
		def p = new Person(age:5,name:'john')
		def p2 = new Person()
		p.copyTo(p2)
		assert p2.age == 5
		assert p2.name == 'john'
	}

}   

@FXBindable
class Person extends AbstractModel{
	int age
	String name
}
