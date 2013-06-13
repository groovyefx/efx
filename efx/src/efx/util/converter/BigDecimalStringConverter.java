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

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
/**
 * 实现BigDecimal与String类型相互间转换
 * @author 麦俊进
 *
 */
public class BigDecimalStringConverter extends BaseConverter{
	
	@Override
	protected Object cast(Object obj){
		if(obj == null){
			//下面请直接返回null,不要返回空字符串，不如界面会报“不合法的值”
			return null;
		}
		if(obj instanceof BigDecimal){
			return obj.toString();
		}else if(obj instanceof String){
			String strValue = String.valueOf(obj);
			if(StringUtils.isNotEmpty(strValue)){
				return new BigDecimal(strValue);
			}		
		}
		return super.cast(obj);
	}
	
}
