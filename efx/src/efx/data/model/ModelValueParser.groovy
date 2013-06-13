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
package efx.data.model

import javafx.beans.value.ObservableValue

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
class ModelValueParser {

	static parserMap = [(ObservableValue):{ it.value }]

	static registerParser(Class type,Closure parser){
		parserMap[type] = parser
	}

	static String parse(value){
		if(value){
			def parser = parserMap[value.getClass()]
			if(parser){
				return parser(value)
			}
			return value.toString()
		}
	}
}
