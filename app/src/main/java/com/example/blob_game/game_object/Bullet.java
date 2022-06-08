package com.example.blob_game.game_object;

import android.graphics.Canvas;
import android.graphics.PointF;

import androidx.annotation.NonNull;

import com.example.blob_game.network.Socket;
import com.example.blob_game.network.TanksHandler;
import com.example.blob_game.utils.Collision;

import java.util.List;

public class Bullet extends CircleObject {

	int timeToLive = 3000;
	float disintegrationSpeed = 0.05f; // slow - 0 < x < 1 - fast
	long timeSinceCreation;
	float damage = 100;
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
		paint = options.paint;
		borderPaint = options.borderPaint;
	}

	@Override
	public void update() {
		if (!active) return;
		super.update();

		if (System.currentTimeMillis() > timeSinceCreation + timeToLive) dying = true;

		if (!dying) {
			List<Tank> enemies = TanksHandler.getEnemies();
			for (Tank enemy : enemies)
				if (Collision.circleCircle(this, enemy)) {
					Socket.getInstance().getSocket().emit("hit", enemy.id, String.valueOf(damage));
					enemy.getHealthBar().changeHealth(-damage);
					dying = true;
				}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (dying) {
			if (paint.getAlpha() <= 255 * disintegrationSpeed && borderPaint.getAlpha() <= 255 * disintegrationSpeed)
				active = false;
			else {
				paint.setAlpha(paint.getAlpha() - (int) (255 * disintegrationSpeed));
				borderPaint.setAlpha(borderPaint.getAlpha() - (int) (255 * disintegrationSpeed));
				// radius *= 1 - disintegrationSpeed;
			}
		}

		super.draw(canvas);
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	@NonNull
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String getAttributes() {
		return super.getAttributes() +
			" timeToLive=" + timeToLive +
			" disintegrationSpeed=" + disintegrationSpeed +
			" timeSinceCreation=" + timeSinceCreation +
			" dying=" + dying +
			" damage=" + damage;

	}

	@NonNull
	@Override
	public String toString() {
		return "Bullet {" + getAttributes() + " }";
	}

	public static class Options extends CircleObject.Options {

		protected int timeToLive = 1500;
		protected float disintegrationSpeed = 0.05f;
		protected float damage = 100;

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

		public float getDamage() {
			return damage;
		}

		public Options setDamage(float damage) {
			this.damage = damage;
			return this;
		}

		@NonNull
		@Override
		public Object clone() throws CloneNotSupportedException {
			return (Options) super.clone();
		}
	}
}