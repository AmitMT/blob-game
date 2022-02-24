package com.example.libgdx_try.game_object;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

public class CircleObject extends GameObject {

	protected float radius;
	protected Paint paint;

	{
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.parseColor("#ffffff"));
	}

	public CircleObject(PointF position, float radius) {
		super(position);

		this.radius = radius;
	}

	public CircleObject(PointF position, float radius, Options options) {
		super(position, options);

		this.radius = radius;
		paint = options.paint;
	}

	@Override
	public void draw(Canvas canvas) {
		if (paint != null) canvas.drawCircle(position.x, position.y, radius, paint);
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public int getColor() {
		return paint.getColor();
	}

	public void setColor(int color) {
		paint.setColor(color);
	}

	public static class Options extends GameObject.Options {

		protected Paint paint;

		public Options() {
		}

		public Paint getPaint() {
			return paint;
		}

		public Options setPaint(Paint paint) {
			this.paint = paint;
			return this;
		}
	}
}
