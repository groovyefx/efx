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
package efx.control;

import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import efx.util.FXUtil;

/**
 * 
 * @author Franklyn Donald <lovefree103@gmail.com>
 *
 */
public class AnimateInfoLabel extends Label {

	private Timer timer;

	private FadeTransition lastFadeTransition;
	
	private int delay = 5000; 
		
	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
	}

	public void setLabelText(String text){
		setOpacity(1);
		setText(text);	
	}

	public void animateMessage(final String message) {
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer(true);
		if (lastFadeTransition != null) {
			lastFadeTransition.stop();
		}
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				AnimateInfoLabel.this.setLabelText(message);		
			}
		});		
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				lastFadeTransition = FXUtil.buildFadeInTransition(AnimateInfoLabel.this);				
				lastFadeTransition.play();
			}
		}, delay);
	}
}
