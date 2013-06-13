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

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import efx.util.DateUtils;

/**
 * 实现Date与String类型相互间转换 只是提供给DatePicker控件和模型绑定的时候使用
 * 
 * @author 麦俊进
 * 
 */
public class DateStringConverter extends BaseConverter {

	@Override
	protected Object cast(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Date) {
			try {
				return DateUtils.format((Date) obj, "yyyy/MM/dd");
			} catch (Exception e) {
				return null;
			}
		} else if (obj instanceof String) {
			String strValue = String.valueOf(obj);
			if (StringUtils.isNotEmpty(strValue)) {
				try {
					return DateUtils.parse(strValue, "yyyy/MM/dd");
				} catch (Exception e) {
					return null;
				}
			}else{
				return null;
			}
		}
		return super.cast(obj);
	}

}
