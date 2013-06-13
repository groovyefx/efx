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
public class FieldValidationResult {	

	private final String message;
	private final ValidationResultType type;
	private final String fieldName;

	public FieldValidationResult(ValidationResultType type, String fieldName) {
		this(null, type, fieldName);
	}

	public FieldValidationResult(String message, ValidationResultType type, String fieldName) {
		this.message = message;
		this.type = type;
		this.fieldName = fieldName;
	}

	public final String getMessage() {
		return message;
	}

	public final ValidationResultType getType() {
		return type;
	}

	public final String getFieldName() {
		return fieldName;
	}

}
