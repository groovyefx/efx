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
package efx.data.model;


/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public abstract class PropertyConversionExceptionHandler {

	private AbstractModel model;
	private String fieldName;
	private String message;

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setModel(AbstractModel model) {
		this.model = model;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void process() {
		process(model, fieldName, message);
	}
	
	protected abstract void process(AbstractModel model, String fieldName, String message);

}
