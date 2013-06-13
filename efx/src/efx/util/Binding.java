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
package efx.util;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import efx.form.BindDirection;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Binding {
	private ReadOnlyProperty sourceProperty;
	private ReadOnlyProperty targetProperty;
	private ChangeListener listener;
	private BindDirection dirction;

	public Binding(ReadOnlyProperty sourceProperty, ReadOnlyProperty targetProperty, ChangeListener listener, BindDirection dirction) {
		this.sourceProperty = sourceProperty;
		this.targetProperty = targetProperty;
		this.listener = listener;
		this.dirction = dirction;
	}

	public ReadOnlyProperty getSourceProperty() {
		return sourceProperty;
	}

	public void setSourceProperty(ReadOnlyProperty sourceProperty) {
		this.sourceProperty = sourceProperty;
	}

	public ReadOnlyProperty getTargetProperty() {
		return targetProperty;
	}

	public void setTargetProperty(ReadOnlyProperty targetProperty) {
		this.targetProperty = targetProperty;
	}

	public ChangeListener getListener() {
		return listener;
	}

	public void setListener(ChangeListener listener) {
		this.listener = listener;
	}

	public BindDirection getDirction() {
		return dirction;
	}

	public void setDirction(BindDirection dirction) {
		this.dirction = dirction;
	}

	public boolean unBind(ReadOnlyProperty property) {
		if (sourceProperty.equals(property) || targetProperty.equals(property)) {
			if (sourceProperty.equals(property) && dirction == BindDirection.Unidirectional) {
				targetProperty.removeListener(getListener());
			} else if (targetProperty.equals(property) && dirction == BindDirection.Reverse) {
				sourceProperty.removeListener(getListener());
			}
			return true;
		}
		return false;
	}

	public boolean unbindBidirectional(ReadOnlyProperty property) {
		if (sourceProperty.equals(property) || targetProperty.equals(property)) {
			ChangeListener c = getListener();
			sourceProperty.removeListener(c);
			targetProperty.removeListener(c);
			return true;
		}
		return false;
	}

	public boolean isBind(ReadOnlyProperty property) {
		if (sourceProperty.equals(property) && dirction == BindDirection.Unidirectional) {
			return true;
		} else if (targetProperty.equals(property) && dirction == BindDirection.Reverse) {
			return true;
		}
		return false;
	}
	

}
