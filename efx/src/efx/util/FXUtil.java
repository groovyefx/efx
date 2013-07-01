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

import java.lang.reflect.Field;

import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import com.sun.javafx.scene.control.skin.LabeledText;
import com.sun.javafx.scene.control.skin.TreeCellSkin;

import efx.app.Activity;
import efx.app.ControllerParameterException;
import efx.app.FxmlLoader;
import efx.stage.AnimateStage;
import efx.stage.StageConfig;
import groovy.lang.Closure;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public class FXUtil {  

	public static void makeNodeDraggable(final Node node) {
		DragEventHandler handler = new DragEventHandler(new Draggable() {

			@Override
			public void move(double offsetX, double offsetY) {
				node.setLayoutX(node.getLayoutX() + offsetX);
				node.setLayoutY(node.getLayoutY() + offsetY);
			}
		});
		node.setOnMousePressed(handler);
		node.setOnMouseDragged(handler);
		node.setOnMouseReleased(handler);
	}

	public static void makeWindowDraggable(final Window win) {
		DragEventHandler handler = new DragEventHandler(new Draggable() {

			@Override
			public void move(double offsetX, double offsetY) {
				win.setX(win.getX() + offsetX);
				win.setY(win.getY() + offsetY);
			}
		});
		win.addEventHandler(MouseEvent.MOUSE_PRESSED, handler);
		win.addEventHandler(MouseEvent.MOUSE_DRAGGED, handler);
		win.addEventHandler(MouseEvent.MOUSE_RELEASED, handler);
	}

	public static ScaleTransition buildScaleMaxAnimation(Node node) {
		ScaleTransition transition = ScaleTransitionBuilder.create().fromX(0).fromY(0).toX(1).toY(1).duration(Duration.millis(200)).node(node).build();
		return transition;
	}

	public static ScaleTransition buildScaleMiniAnimation(Node node) {
		ScaleTransition transition = ScaleTransitionBuilder.create().fromX(1).fromY(1).toX(0).toY(0).duration(Duration.millis(200)).node(node).build();
		return transition;
	}
	
	/**
	 * 创建淡出的效果--显示
	 * 
	 * @param node
	 * @return
	 */
	public static FadeTransition buildFadeOutTransition(Node node) {
		return FadeTransitionBuilder.create().duration(Duration.seconds(1)).fromValue(0).toValue(1).node(node).build();
	}

	/**
	 * 创建淡入的效果--消失
	 * 
	 * @param node
	 * @return
	 */
	public static FadeTransition buildFadeInTransition(Node node) {
		return FadeTransitionBuilder.create().duration(Duration.seconds(1)).fromValue(1).toValue(0).node(node).build();
	}
	
		
	public static void showMessage(Label messageLabel){
		FadeTransition ft = buildFadeOutTransition(messageLabel);
		ft.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				
			}
		});
		
	}

	public static Stage createStage(StageConfig sc, Scene scene) {
		Stage stage = null;
		if (sc.isAnimate()) {
			stage = new AnimateStage(scene);
		} else {
			stage = new Stage();			
			stage.setScene(scene);
			if (sc.isTransparent()) {
				stage.initStyle(StageStyle.TRANSPARENT);
				scene.setFill(Color.TRANSPARENT);
			}
		}		
		stage.initModality(sc.getModality());
		stage.initOwner(sc.getOwner());
		if(sc.getFullScreen()){
			setFullScreen(stage);
		}
		if (sc.isDraggable()) {
			FXUtil.makeWindowDraggable(stage);
		}

		return stage;
	}
	
	public static void setFullScreen(Stage stage){
		Rectangle2D rec = Screen.getPrimary().getBounds();			
		stage.setHeight(rec.getHeight());
		stage.setWidth(rec.getWidth());
		stage.setX(0);
		stage.setY(0);
	}

	public static Stage createStage(StageConfig sc) {
		Activity activity = null;
		if (sc.getParamsMap() != null) {
			activity = FxmlLoader.loadActivity(sc.getFxml(), sc.getStyleSheets(), sc.getBundle(), true, sc.getParamsMap());
		} else {
			activity = FxmlLoader.loadActivity(sc.getFxml(), sc.getStyleSheets(), sc.getBundle(), true, sc.getParams());
		}
		Stage stage = createStage(sc, activity.getScene());
		Object c = activity.getController();
		if (c != null) {
			Field f = null;
			try {
				f = c.getClass().getDeclaredField("_stage");
				if (f != null) {
					f.setAccessible(true);
					f.set(c, stage);
				}
			} catch (IllegalAccessException e) {
				throw new ControllerParameterException(String.format("在控制器'%s'中设置属性'_stage'时发生错误", c), e);
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		return stage;
	}

	public static void showStage(StageConfig sc) {
		Stage stage = createStage(sc);
		if (sc.isBlockShow()) {
			stage.showAndWait();
		} else {
			stage.show();
		}
	}

	public static boolean fromTreeItem(Object target) {
		if (target instanceof LabeledText) {
			if (StringUtil.isNotEmpty(((LabeledText) target).getText())) {
				return true;
			}
		} else if (target instanceof TreeCellSkin) {
			TreeCellSkin cell = (TreeCellSkin) target;
			if (StringUtil.isNotEmpty(((LabeledText) cell.getChildren().get(0)).getText())) {
				return true;
			}
		}
		return false;
	}
	
	public static void runOnUIThread(final Closure<?> callable){
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				callable.call();
			}
		});
	}

}
