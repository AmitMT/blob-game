package com.example.libgdx_try.game_object;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import java.util.UUID;

public abstract class GameObject implements Cloneable {

	protected PointF position;
	protected PointF velocity = new PointF(0, 0);
	protected PointF acceleration = new PointF(0, 0);
	protected boolean active = true;

	protected String id = UUID.randomUUID().toString();

	public GameObject(PointF position) {
		this.position = position;
	}

	public GameObject(PointF position, Options options) {
		this.position = position;
		velocity = options.velocity;
		acceleration = options.acceleration;
		active = options.active;
		id = options.id;
	}

	public GameObject(GameObject that) {
		position = new PointF(that.position.x, that.position.y);
		velocity = new PointF(that.velocity.x, that.velocity.y);
		acceleration = new PointF(that.acceleration.x, that.acceleration.y);
		active = that.active;
		id = that.id;
	}

	public abstract void draw(Canvas canvas);

	public void update() {
		if (!active) return;
		velocity.offset(acceleration.x, acceleration.y);
		position.offset(velocity.x, velocity.y);
	}

	public PointF getPosition() {
		return position;
	}

	public void setPosition(PointF center) {
		this.position = center;
	}

	public PointF getVelocity() {
		return velocity;
	}

	public void setVelocity(PointF velocity) {
		this.velocity = velocity;
	}

	public PointF getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(PointF acceleration) {
		this.acceleration = acceleration;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean useTouch(MotionEvent event, int action, int index) {
		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@NonNull
	@Override
	public Object clone() throws CloneNotSupportedException {
		GameObject cloned = (GameObject) super.clone();
		cloned.position = new PointF(cloned.position.x, cloned.position.y);
		cloned.velocity = new PointF(cloned.velocity.x, cloned.velocity.y);
		cloned.acceleration = new PointF(cloned.acceleration.x, cloned.acceleration.y);
		return cloned;
	}

	public String getAttributes() {
		return " position=" + position +
			" velocity=" + velocity +
			" acceleration=" + acceleration +
			" active=" + active +
			" id='" + id;
	}

	@NonNull
	@Override
	public String toString() {
		return "GameObject {" + getAttributes() + " }";
	}

	public static class Options implements Cloneable {

		protected PointF velocity = new PointF(0, 0);
		protected PointF acceleration = new PointF(0, 0);
		protected boolean active = true;

		protected String id = UUID.randomUUID().toString();

		public Options() {
		}

		public PointF getVelocity() {
			return velocity;
		}

		public Options setVelocity(PointF velocity) {
			this.velocity = velocity;
			return this;
		}

		public PointF getAcceleration() {
			return acceleration;
		}

		public Options setAcceleration(PointF acceleration) {
			this.acceleration = acceleration;
			return this;
		}

		public boolean isActive() {
			return active;
		}

		public Options setActive(boolean active) {
			this.active = active;
			return this;
		}

		public String getId() {
			return id;
		}

		public Options setId(String id) {
			this.id = id;
			return this;
		}

		@NonNull
		@Override
		public Object clone() throws CloneNotSupportedException {
			Options cloned = (Options) super.clone();
			cloned.velocity = new PointF(cloned.velocity.x, cloned.velocity.y);
			cloned.acceleration = new PointF(cloned.acceleration.x, cloned.acceleration.y);
			return cloned;
		}
	}
}
