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

import org.apache.commons.lang3.StringUtils;


/**
 * 实现Double与String类型相互间转换
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public class DoubleStringConverter extends BaseConverter{
	
	@Override
	protected Object cast(Object obj){
		if(obj == null){
			return null;
		}
		if(obj instanceof Double){
			return obj.toString();
		}else if(obj instanceof String){
			String strValue = String.valueOf(obj);
			if(StringUtils.isNotEmpty(strValue)){
				return Double.parseDouble(strValue);
			}			
		}
		return super.cast(obj);
	}
	

}
