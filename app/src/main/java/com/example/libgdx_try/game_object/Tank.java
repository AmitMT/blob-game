package com.example.libgdx_try.game_object;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.example.libgdx_try.camera.CameraShake;
import com.example.libgdx_try.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Tank extends Blob {

	Barrel barrel;
	Bullet.Options bulletOptions;

	List<Bullet> bullets = new ArrayList<>();
	int bulletRadius = 10;
	float bulletSpeed = 4;
	int reloadMill = 400;
	long prevShotTime;

	public Tank(PointF position, float radius) {
		super(position, radius);

		barrel = new Barrel();
	}

	public Tank(PointF position, float radius, Options options) {
		super(position, radius, options);

		barrel = new Barrel(options.barrelOptions);
	}

	@Override
	public void draw(Canvas canvas) {

		barrel.rotationMatrix.setRotate(barrel.angle);
		// 2d matrix of the barrel
		float[] points = new float[] {
			0, -barrel.thickness / 2f,
			barrel.length, -barrel.thickness / 2f,
			barrel.length, +barrel.thickness / 2f,
			0, +barrel.thickness / 2f,
		};
		barrel.rotationMatrix.mapPoints(points);

		Path barrelPath = new Path();
		barrelPath.moveTo(points[0] + position.x, points[1] + position.y);
		barrelPath.lineTo(points[2] + position.x, points[3] + position.y);
		barrelPath.lineTo(points[4] + position.x, points[5] + position.y);
		barrelPath.lineTo(points[6] + position.x, points[7] + position.y);

		bullets.forEach(bullet -> bullet.draw(canvas));

		canvas.drawPath(barrelPath, barrel.paint);

		super.draw(canvas);
	}

	@Override
	public void update() {
		if (!active) return;
		super.update();

		List<Bullet> needDeletion = new ArrayList<>();
		bullets.forEach(bullet -> {
			if (bullet.active) bullet.update();
			else needDeletion.add(bullet);
		});
		bullets.removeAll(needDeletion);
	}

	public float getAngle() {
		return barrel.angle;
	}

	public void setAngle(float angle) {
		barrel.angle = angle;
	}

	public void lerpToAngle(float angle, float speed) {
		if (angle - barrel.angle > 180)
			barrel.angle = Utils.lerp(barrel.angle + 360, angle, speed) % 360;
		else if (barrel.angle - angle > 180) {
			barrel.angle = Utils.lerp(barrel.angle, angle + 360, speed) % 360;
		} else {
			barrel.angle = Utils.lerp(barrel.angle, angle, speed);
		}
	}

	// TODO: wait what if the user changes the phone's time. I need a bypass.

	public void shoot(CameraShake cameraShake) {
		long currTime = System.currentTimeMillis();
		if (prevShotTime + reloadMill < currTime) {
			cameraShake.increaseTrauma(.5f);

			float[] positionAndVelocity = {
				barrel.length - bulletRadius, 0, // position (end of barrel)
				bulletSpeed, 0 // velocity
			};
			barrel.rotationMatrix.mapPoints(positionAndVelocity);

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.WHITE);

			Bullet.Options options = (Bullet.Options) new Bullet.Options()
				.setTimeToLive(1500)
				.setDisintegrationSpeed(0.05f)
				.setPaint(paint)
				.setVelocity(new PointF(velocity.x / 2 + positionAndVelocity[2], velocity.y / 2 + positionAndVelocity[3]));

			Bullet bullet = new Bullet(new PointF(
				position.x + positionAndVelocity[0],
				position.y + positionAndVelocity[1]),
				bulletRadius,
				options
			);
			bullets.add(bullet);
			prevShotTime = currTime;
		}
	}

	public static class Options extends Blob.Options {

		protected Barrel.Options barrelOptions = new Barrel.Options();
		protected Bullet.Options bulletOptions = new Bullet.Options();

		public Options() {
		}

		public Barrel.Options getBarrelOptions() {
			return barrelOptions;
		}

		public Options setBarrelOptions(Barrel.Options barrelOptions) {
			this.barrelOptions = barrelOptions;
			return this;
		}

		public Bullet.Options getBulletOptions() {
			return bulletOptions;
		}

		public Options setBulletOptions(Bullet.Options bulletOptions) {
			this.bulletOptions = bulletOptions;
			return this;
		}
	}

	public static class Barrel {

		int length = 80;
		int thickness = 30;
		float angle = 0; // 0 - 360
		Paint paint;
		Matrix rotationMatrix = new Matrix(); // For server optimisation. used instead of "canvas.rotate()"

		{
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.parseColor("#cccccc"));
		}

		public Barrel() {
		}

		public Barrel(Options options) {
			length = options.length;
			thickness = options.thickness;
			angle = options.angle;
			paint = options.paint;
		}

		public static class Options {

			protected int length = 80;
			protected int thickness = 30;
			protected float angle = 0;
			protected Paint paint;

			{
				paint = new Paint(Paint.ANTI_ALIAS_FLAG);
				paint.setColor(Color.parseColor("#cccccc"));
			}

			public Options() {
			}

			public int getLength() {
				return length;
			}

			public Options setLength(int length) {
				this.length = length;
				return this;
			}

			public int getThickness() {
				return thickness;
			}

			public Options setThickness(int thickness) {
				this.thickness = thickness;
				return this;
			}

			public float getAngle() {
				return angle;
			}

			public Options setAngle(float angle) {
				this.angle = angle;
				return this;
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
}
