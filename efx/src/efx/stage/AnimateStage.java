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
package efx.stage;

import efx.util.FXUtil;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public class AnimateStage extends Stage{
	
	private Animation showingAnimation;
	
	private Animation closeAnimation;
	
	public AnimateStage(Scene scene) {
		this.initStyle(StageStyle.TRANSPARENT);		
		this.setScene(scene);
		scene.setFill(Color.TRANSPARENT);
		showingAnimation = FXUtil.buildScaleMaxAnimation(scene.getRoot());
		this.setOnShown(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				showingAnimation.play();
			}
		});
		
		closeAnimation = FXUtil.buildScaleMiniAnimation(scene.getRoot());
		
		closeAnimation.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				AnimateStage.super.hide();
			}
		});
	}
		
	@Override
	public void hide() {
		closeAnimation.play();
	}
	
}
