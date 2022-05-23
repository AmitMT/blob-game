package com.example.blob_game.game_panel;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.blob_game.ContextProvider;
import com.example.blob_game.MainActivity;
import com.example.blob_game.utils.Utils;

public class HealthBar extends GamePanel {

	float maxHealth;
	float health;

	PointF size = new PointF(60, 4);

	float healthFraction;
	float healthFractionAnimated;

	Paint backgroundPaint = new Paint();
	Paint healthPaint = new Paint();

	{
		backgroundPaint.setColor(Color.parseColor("#727272"));
		backgroundPaint.setAlpha(180);
		healthPaint.setColor(Color.parseColor("#39B63F"));
		healthPaint.setAlpha(180);
	}

	public HealthBar(PointF position, float maxHealth) {
		super(position);

		this.maxHealth = maxHealth;
		health = maxHealth;
		healthFraction = 1;
		healthFractionAnimated = healthFraction;
	}

	public HealthBar(PointF position, float maxHealth, Options options) {
		super(position);

		this.maxHealth = maxHealth;
		health = options.health;
		size = options.size;
		healthFraction = health / maxHealth;
		healthFractionAnimated = healthFraction;
	}

	@Override
	public void draw(Canvas canvas) {
		Path path = new Path();
		path.addRoundRect(position.x - size.x / 2, position.y - size.y / 2, position.x + size.x / 2, position.y + size.y / 2, 2, 2, Path.Direction.CW);
		canvas.drawPath(path, backgroundPaint);
		if (healthFractionAnimated > 0) {
			path.reset();
			path.addRoundRect(position.x - size.x / 2, position.y - size.y / 2, position.x + (healthFractionAnimated - 0.5f) * size.x, position.y + size.y / 2, 2, 2, Path.Direction.CW);
			canvas.drawPath(path, healthPaint);
		}
	}

	@Override
	public void update() {
		healthFractionAnimated = Utils.lerp(healthFraction, healthFractionAnimated, 0.9f);
	}

	public void changeHealth(float health) {
		this.health += health;
		this.health = Math.min(Math.max(0, this.health), maxHealth);
		setHealth(this.health);
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
		healthFraction = maxHealth / health;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
		healthFraction = health / maxHealth;
	}

	public float getHealthFractionAnimated() {
		return healthFractionAnimated;
	}

	public void setHealthFractionAnimated(float healthFractionAnimated) {
		this.healthFractionAnimated = healthFractionAnimated;
	}

	@NonNull
	@Override
	public Object clone() throws CloneNotSupportedException {
		HealthBar cloned = (HealthBar) super.clone();
		if (cloned.size != null) size = new PointF(size.x, size.y);
		if (cloned.backgroundPaint != null) backgroundPaint = new Paint(backgroundPaint);
		if (cloned.healthPaint != null) healthPaint = new Paint(healthPaint);
		return cloned;
	}

	public static class Options extends GamePanel.Options {

		protected PointF size = new PointF(60, 4);
		protected float health;
		protected Paint healthPaint = new Paint();
		protected Paint backgroundPaint = new Paint();

		{
			backgroundPaint.setColor(Color.parseColor("#727272"));
			backgroundPaint.setAlpha(180);
			healthPaint.setColor(Color.parseColor("#39B63F"));
			healthPaint.setAlpha(180);
		}

		public Options() {
		}

		public float getHealth() {
			return health;
		}

		public Options setHealth(float health) {
			this.health = health;
			return this;
		}

		public PointF getSize() {
			return size;
		}

		public Options setSize(PointF size) {
			this.size = size;
			return this;
		}

		public Paint getHealthPaint() {
			return healthPaint;
		}

		public Options setHealthPaint(Paint healthPaint) {
			this.healthPaint = healthPaint;
			return this;
		}

		public Paint getBackgroundPaint() {
			return backgroundPaint;
		}

		public Options setBackgroundPaint(Paint backgroundPaint) {
			this.backgroundPaint = backgroundPaint;
			return this;
		}
	}
}
