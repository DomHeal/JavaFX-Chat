package com.messages.bubble;

import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;

public class Bubble extends Path{
	
	/*Copyright {2015} {Terah Laweh}

	   Licensed under the Apache License, Version 2.0 (the "License");
	   you may not use this file except in compliance with the License.
	   You may obtain a copy of the License at

	       http://www.apache.org/licenses/LICENSE-2.0

	   Unless required by applicable law or agreed to in writing, software
	   distributed under the License is distributed on an "AS IS" BASIS,
	   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	   See the License for the specific language governing permissions and
	   limitations under the License.
	*/

	public Bubble(BubbleSpec bubbleSpec) {
		super();
		switch (bubbleSpec) {
		case FACE_BOTTOM:
			break;
		case FACE_LEFT_BOTTOM:
			drawRectBubbleLeftBaselineIndicator();
			break;
		case FACE_LEFT_CENTER:
			drawRectBubbleLeftCenterIndicator();
			break;
		case FACE_RIGHT_BOTTOM:
			drawRectBubbleRightBaselineIndicator();
			break;
		case FACE_RIGHT_CENTER:
			drawRectBubbleRightCenterIndicator();
			break;
		case FACE_TOP:
			drawRectBubbleToplineIndicator();
			break;

		default:
			break;
		}

	}

	private void drawRectBubbleToplineIndicator() {
		getElements().addAll(new MoveTo(1.0f, 1.2f),
				new HLineTo(2.5f),
				new LineTo(2.7f, 1.0f),
				new LineTo(2.9f, 1.2f),
				new HLineTo(4.4f),
				new VLineTo(4f),
				new HLineTo(1.0f),
				new VLineTo(1.2f)
				);
	}

	private void drawRectBubbleRightBaselineIndicator() {
		getElements().addAll(new MoveTo(3.0f, 1.0f),
				new HLineTo(0f),
				new VLineTo(4f),
				new HLineTo(3.0f),
				new LineTo(2.8f, 3.8f),
				new VLineTo(1f)
				);
	}

	private void drawRectBubbleLeftBaselineIndicator() {
		getElements().addAll(new MoveTo(1.2f, 1.0f),
				new HLineTo(3f),
				new VLineTo(4f),
				new HLineTo(1.0f),
				new LineTo(1.2f, 3.8f),
				new VLineTo(1f)
				);
	}

	private void drawRectBubbleRightCenterIndicator() {
		getElements().addAll(new MoveTo(3.0f, 2.5f),
				new LineTo(2.8f, 2.4f),
				new VLineTo(1f),
				new HLineTo(0f),
				new VLineTo(4f),
				new HLineTo(2.8f),
				new VLineTo(2.7f),
				new LineTo(3.0f, 2.5f)
				);
	}
	
	protected double drawRectBubbleIndicatorRule = 0.2;

	private void drawRectBubbleLeftCenterIndicator() {
		getElements().addAll(new MoveTo(1.0f, 2.5f),
				new LineTo(1.2f, 2.4f),
				new VLineTo(1f),
				new HLineTo(2.9f),
				new VLineTo(4f),
				new HLineTo(1.2f),
				new VLineTo(2.7f),
				new LineTo(1.0f, 2.5f)
				);
	}


}
