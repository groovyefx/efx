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

import java.awt.Point;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
@SuppressWarnings("rawtypes")
public class DragEventHandler implements EventHandler<MouseEvent> {

    private Point currentPoint;

    private Draggable draggable;

    public DragEventHandler(Draggable draggable) {
        this.draggable = draggable;
    }
    
	@Override
    public void handle(MouseEvent e) {
        EventType type = e.getEventType();
        if (type == MouseEvent.MOUSE_PRESSED) {
        	currentPoint = new Point();
            currentPoint.setLocation(e.getScreenX(), e.getScreenY());
        } else if (currentPoint!=null && type == MouseEvent.MOUSE_DRAGGED) {        	
            draggable.move(e.getScreenX()-currentPoint.getX(),e.getScreenY()-currentPoint.getY());
            currentPoint.setLocation(e.getScreenX(), e.getScreenY());
        } else if (type == MouseEvent.MOUSE_RELEASED){
        	currentPoint = null;
        }
    }
}
