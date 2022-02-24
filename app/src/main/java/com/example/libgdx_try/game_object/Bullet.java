package com.example.libgdx_try.game_object;

import android.graphics.Canvas;
import android.graphics.PointF;

public class Bullet extends CircleObject {

	int timeToLive = 3000;
	float disintegrationSpeed = 0.05f; // slow - 0 < x < 1 - fast
	long timeSinceCreation;
	boolean dying;

	public Bullet(PointF position, float radius) {
		super(position, radius);

		timeSinceCreation = System.currentTimeMillis();
	}

	public Bullet(PointF position, float radius, Options options) {
		super(position, radius, options);

		timeSinceCreation = System.currentTimeMillis();
		velocity = options.velocity;
		timeToLive = options.timeToLive;
		disintegrationSpeed = options.disintegrationSpeed;
	}

	@Override
	public void update() {
		if (!active) return;
		super.update();

		if (System.currentTimeMillis() > timeSinceCreation + timeToLive) dying = true;
	}

	@Override
	public void draw(Canvas canvas) {
		if (dying) {
			if (paint.getAlpha() <= 255 * disintegrationSpeed) active = false;
			else paint.setAlpha(paint.getAlpha() - (int) (255 * disintegrationSpeed));
		}

		super.draw(canvas);
	}

	public boolean collision(CircleObject circleObject) {
		return Math.pow(position.x - circleObject.position.x, 2) + Math.pow(position.x - circleObject.position.x, 2) < Math.pow(radius + circleObject.radius, 2);
	}

	public static class Options extends CircleObject.Options {

		protected int timeToLive = 3000;
		protected float disintegrationSpeed = 0.05f;

		public Options() {
		}

		public int getTimeToLive() {
			return timeToLive;
		}

		public Options setTimeToLive(int timeToLive) {
			this.timeToLive = timeToLive;
			return this;
		}

		public float getDisintegrationSpeed() {
			return disintegrationSpeed;
		}

		public Options setDisintegrationSpeed(float disintegrationSpeed) {
			this.disintegrationSpeed = disintegrationSpeed;
			return this;
		}
	}
}