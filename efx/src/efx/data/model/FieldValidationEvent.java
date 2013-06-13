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

import javafx.event.Event;
import javafx.event.EventType;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public class FieldValidationEvent extends Event {

	public static final EventType<FieldValidationEvent> ANY = new EventType<FieldValidationEvent>(Event.ANY, "FIELD_ERROR");

	private final FieldValidationResult fieldValidationResult;

	public FieldValidationEvent(FieldValidationResult fieldValidationResult) {
		super(ANY);
		this.fieldValidationResult = fieldValidationResult;
	}

	public final FieldValidationResult getFieldValidationResult() {
		return this.fieldValidationResult;
	}
}
