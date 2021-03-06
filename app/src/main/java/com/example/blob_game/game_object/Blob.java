package com.example.blob_game.game_object;

import android.graphics.Canvas;
import android.graphics.PointF;

import androidx.annotation.NonNull;

import com.example.blob_game.GameLoop;
import com.example.blob_game.graphics.Sprite;

public class Blob extends CircleObject {

	public static final float MAX_ACCELERATION_IN_SECONDS = 18;
	public static final float MAX_ACCELERATION = MAX_ACCELERATION_IN_SECONDS / GameLoop.MAX_UPS;
	public static final float FRICTION = .05f;
	// The blob's max velocity is ( (MAX_ACCELERATION_IN_SECONDS / FRICTION) + MAX_ACCELERATION_IN_SECONDS )

	protected Sprite sprite;

	public Blob(PointF position, float radius) {
		super(position, radius);
	}

	public Blob(PointF position, float radius, Options options) {
		super(position, radius, options);

		sprite = options.sprite;
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

	@Override
	public void update() {
		if (!active) return;
		super.update();

		velocity.set(velocity.x * (1 - FRICTION), velocity.y * (1 - FRICTION));
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	@NonNull
	@Override
	public Object clone() throws CloneNotSupportedException {
		Blob cloned = (Blob) super.clone();
		if (cloned.sprite != null) cloned.sprite = (Sprite) cloned.sprite.clone();
		return cloned;
	}

	@Override
	public String getAttributes() {
		return super.getAttributes() +
			" sprite=" + sprite;
	}

	@NonNull
	@Override
	public String toString() {
		return "Blob {" + getAttributes() + " }";
	}

	public static class Options extends CircleObject.Options {

		protected Sprite sprite;

		public Options() {
		}

		public Sprite getSprite() {
			return sprite;
		}

		public Options setSprite(Sprite sprite) {
			this.sprite = sprite;
			return this;
		}

		@NonNull
		@Override
		public Object clone() throws CloneNotSupportedException {
			Options cloned = (Options) super.clone();
			if (cloned.sprite != null) cloned.sprite = (Sprite) cloned.sprite.clone();
			return cloned;
		}
	}
}
