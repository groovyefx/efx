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
package efx.data.store;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 * @param <T>
 */
@SuppressWarnings({"rawtypes","unchecked"})
public abstract class AbstractStore<T>{
	
	protected ObservableList<Filter<T>> filters = FXCollections.observableArrayList();
	
	protected ObservableList<T> data = FXCollections.observableArrayList();
	
	public void addFilter(Filter<T> filter){
		if(!filters.contains(filter)){
			filters.add(filter);
		}		
	}
	
	public void removeFilter(Filter<T> filter){
		filters.remove(filter);
	}
	
	public void removeAllFilters(){
		filters.clear();
	}
		
	public ObservableList<T> load(){
		ObservableList<T> d = FXCollections.observableArrayList();
		if(filters.size() == 0){
			return data;
		}
		for(T t:data){
			for(Filter f:filters){
				if(f.doFilter(t)){
					d.add(t);
					break;
				}
			}
		}
		return d;
	}
	
	public ObservableList<T> getData(){
		return data;
	}
	
	public void setData(ObservableList<T> data){
		this.data.setAll(data);
	}
	
			
}
