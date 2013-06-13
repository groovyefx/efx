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
package efx.util.converter;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
@SuppressWarnings("rawtypes")
public abstract class BaseConverter implements Converter {

	@Override
	final public Object toSource(Object obj) {		
		return cast(obj);
	}

	@Override
	final public Object toTarget(Object obj) {
		return cast(obj);
	}
	
	protected Object cast(Object obj){
		throw new UnsupportCastTypeException(String.format("不支持的转换类型：'%s'",obj.getClass().getName()));
	}

}
