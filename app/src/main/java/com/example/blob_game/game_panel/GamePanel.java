package com.example.blob_game.game_panel;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

public abstract class GamePanel implements Cloneable {

	protected PointF position;

	public GamePanel(PointF position) {
		this.position = position;
	}

	public abstract void draw(Canvas canvas);

	public abstract void update();

	public PointF getPosition() {
		return position;
	}

	public void setPosition(PointF center) {
		this.position = center;
	}

	public boolean useTouch(MotionEvent event, int action, int index) {
		return false;
	}

	@NonNull
	@Override
	public Object clone() throws CloneNotSupportedException {
		GamePanel cloned = (GamePanel) super.clone();
		if (cloned.position != null)
			cloned.position = new PointF(cloned.position.x, cloned.position.y);
		return cloned;
	}


	public static class Options {
	}
}
