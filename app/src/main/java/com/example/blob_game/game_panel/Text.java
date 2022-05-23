package com.example.blob_game.game_panel;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import androidx.annotation.NonNull;

public class Text extends GamePanel {

	protected String text;
	protected Paint paint;

	public Text(PointF position, String text, Paint paint) {
		super(position);

		this.text = text;
		this.paint = paint;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawText(text, position.x, position.y, paint);
	}

	@Override
	public void update() {
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	@NonNull
	@Override
	public Object clone() throws CloneNotSupportedException {
		Text cloned = (Text) super.clone();
		if (cloned.paint != null) cloned.paint = new Paint(cloned.paint);
		return cloned;
	}

	public static class Options extends GamePanel.Options {
	}
}
