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
package efx.util

import java.text.SimpleDateFormat
import java.util.List;
import java.util.Map;


import javafx.beans.property.ListProperty
import javafx.beans.property.MapProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SetProperty
import javafx.collections.FXCollections;
import efx.data.model.AbstractModel
import efx.data.model.annotation.FXBindable;


/**
 * 
 * 将{@link efx.data.model.AbstractModel}类型对象转换为{@link java.util.Map}的工具类
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
class MapUtil {

	static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd")

	static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

	static Map modelToMap(AbstractModel model,String prefix = "",Map map = [:]){
		model.fields.each {fk,fv->
			def value = fv.get(model)?.get()
			if(value != null){
				switch(fv.type){
					case MapProperty:
						value.each {mk,mv->
							process("${solvePath(prefix)}${fk}[${mk}]",mv,map)
						}
						break
					case ListProperty:
					case SetProperty:
						value.eachWithIndex {val,i->
							process("${solvePath(prefix)}${fk}[${i}]",val,map)
						}
						break
					default:
						process("${solvePath(prefix)}${fk}",value,map)
				}
			}
		}
		return map
	}

	/**
	 * 
	 * @param modelMap a map of [prefix:{@link efx.data.model.AbstractModel}],
	 * @return
	 */
	static Map modelToMap(Map modelMap){
		def map = [:]
		modelMap.each {k,v->
			if(v instanceof AbstractModel){
				map<<modelToMap(v,k)
			}else{
				map<<[(k):v]
			}
		}
		map
	}

	static process(prefix,value,map){
		if(value instanceof AbstractModel){
			modelToMap(value,prefix,map)
		}else{
			map["${prefix}"] = toString(value)
		}
	}

	static String solvePath(String prefix){
		if(prefix){
			return "${prefix}."
		}
		return prefix
	}



	/*
	 * excludeList:所有的map中要排除的key的List
	 * (客户端的Model基本上是跟服务端一一对应的，但是服务端还会传送一个key为class的键值对，
	 * 这是每个Model中都会传送的，因此设置该字段，就是要统一排除那些不要的字段)
	 * ***
	 * 功能：
	 * 1.过滤掉map中多余的键值对，目前只有class这个键值对需要过滤掉
	 * 2.如果value本身也是一个Model，则要调用该方法
	 * 服务端传递过来的数据：newAgreement:[id:5, class:RoomPriceAgreement]
	 * 要转换成的数据：newAgreement:[id:5]
	 * 3.格式化时间，服务器传递过来的数据格式如下：
	 * yyyy-MM-ddTHH:mm:ssZ
	 * 这种格式的字符串直接传递给Model的构造函数是不能够初始化的
	 * 服务端传递过来的数据：dateCreated:1999-11-29T16:00:00Z
	 * 要转换成的数据：dateCreated:Mon Nov 29 16:00:00 CST 1999
	 * 4.处理枚举类型的数据，枚举类型的class是空，如下：
	 * 服务器传递过来的数据：defaultOffDay:[name:C, enumType:aspic.hotel.domain.config.DefaultOffDay]
	 * 要转换成如下格式的数据：defaultOffDay:C
	 *
	 */
	public static Map getFieldValueMap(Map map,List<String> excludeList){
		def classMap = [:]
		map.each {key,value->
			//如果excludeList中存在key，则不添加进classMap
			boolean flag = true
			//判断excludeList中是否包含key
			excludeList.each {
				if(it == key){
					flag = false
				}
			}
			if(flag && value){
				if(value.class instanceof String){
					classMap.put(key, getFieldValueMap(value,excludeList))
				}else if(value.class == null){
					if(value.containsKey("enumType") && value.containsKey('name')){
						classMap.put(key, "${value['name']}")
					}
					classMap.put(key, getFieldValueMap(value,excludeList))
				}else{
					if(value ==~ /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}Z$/){
						classMap.put(key,DateUtils.parse(value,"yyyy-MM-dd'T'HH:mm:ss'Z'"))
					}else{
						classMap.put(key, value)
					}
				}
			}
		}
		return classMap
	}

	/**
	 * getFieldValueMap(Map map,List<String> excludeList)方法的简化版
	 */
	public static Map getFieldValueMap(Map map){
		return getFieldValueMap(map,['class'])
	}



	static String toString(java.util.Date date){
		return DATE_TIME_FORMAT.format(date)
	}

	static String toString(java.sql.Date date){
		return DATE_FORMAT.format(date)
	}

	static String toString(Object obj){
		return "$obj"
	}

}

