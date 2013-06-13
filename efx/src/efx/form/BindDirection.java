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
package efx.form;

/**
 * 
 * 绑定方向
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public enum BindDirection {
	/**
	 * 单向，source property监听target property，当target property的值变化时更新source property的值
	 */
	Unidirectional,
	/**
	 * 双向
	 */
	Bidirectional,
	/**
	 * 反向，target property监听source property，当source property的值变化时更新target property的值
	 */
	Reverse
}	
