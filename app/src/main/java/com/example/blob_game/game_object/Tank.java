package com.example.blob_game.game_object;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import androidx.annotation.NonNull;

import com.example.blob_game.ContextProvider;
import com.example.blob_game.Game;
import com.example.blob_game.MainActivity;
import com.example.blob_game.R;
import com.example.blob_game.game_panel.HealthBar;
import com.example.blob_game.game_panel.Text;
import com.example.blob_game.network.TanksHandler;
import com.example.blob_game.utils.Collision;
import com.example.blob_game.utils.Utils;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tank extends Blob {

	Barrel barrel;
	Bullet.Options bulletOptions;

	ArrayList<Bullet> bullets = new ArrayList<>();
	int bulletRadius = 10;
	float bulletSpeed = 4;
	int reloadMilli = 400;
	long prevShotTime;

	float recoil = 1;
	float barrelRecoilMultiplier = 4;
	boolean hasRecoil = false;
	int maxRecoilAnimationTime = 400;
	int recoiledBarrelLength;

	float bodyDamage = 10;
	float regeneration = 1;
	float speed = 1;
	float damage = 50;

	Text name;

	HealthBar healthBar;

	public Tank(PointF position, float radius) {
		super(position, radius);

		barrel = new Barrel();
	}

	public Tank(PointF position, float radius, Options options) {
		super(position, radius, options);

		barrel = new Barrel(options.barrelOptions);
		recoiledBarrelLength = barrel.length;
		bulletOptions = options.bulletOptions;
		reloadMilli = options.reloadMilli;
		recoil = options.recoil;
		barrelRecoilMultiplier = options.barrelRecoilMultiplier;

		Paint namePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		namePaint.setColor(Color.parseColor("#00b2e1"));
		namePaint.setTextSize(12);
		namePaint.setTextAlign(Paint.Align.CENTER);
		namePaint.setTypeface(ContextProvider.getInstance().getContext().getResources().getFont(R.font.fjalla_one));
		namePaint.setAlpha(180);
		name = new Text(new PointF(position.x, position.y - radius * 1.6f), (options.name != null ? options.name : new Faker().name().fullName()), namePaint);

		healthBar = new HealthBar(new PointF(position.x, position.y - radius * 1.6f + 7), 1000);

		id = UUID.randomUUID().toString();
	}

	@Override
	public void draw(Canvas canvas) {

		bullets.forEach(bullet -> bullet.draw(canvas));

		drawBarrel(canvas);

		super.draw(canvas);

		name.draw(canvas);
		healthBar.draw(canvas);
	}

	public void drawBarrel(Canvas canvas) {
		barrel.rotationMatrix.setRotate(barrel.angle);
		// 2d matrix of the barrel
		float[] points = new float[] {
			// border
			0, -barrel.thickness / 2f,
			recoiledBarrelLength + radius, -barrel.thickness / 2f,
			recoiledBarrelLength + radius, barrel.thickness / 2f,
			0, barrel.thickness / 2f,

			// fill
			0, -barrel.thickness / 2f + barrel.borderPaint.getStrokeWidth(),
			recoiledBarrelLength + radius - barrel.borderPaint.getStrokeWidth(), -barrel.thickness / 2f + barrel.borderPaint.getStrokeWidth(),
			recoiledBarrelLength + radius - barrel.borderPaint.getStrokeWidth(), barrel.thickness / 2f - barrel.borderPaint.getStrokeWidth(),
			0, barrel.thickness / 2f - barrel.borderPaint.getStrokeWidth()
		};
		barrel.rotationMatrix.mapPoints(points);

		Path barrelBorderPath = new Path();
		barrelBorderPath.moveTo(points[0] + position.x, points[1] + position.y);
		barrelBorderPath.lineTo(points[2] + position.x, points[3] + position.y);
		barrelBorderPath.lineTo(points[4] + position.x, points[5] + position.y);
		barrelBorderPath.lineTo(points[6] + position.x, points[7] + position.y);

		Path barrelPath = new Path();
		barrelPath.moveTo(points[8] + position.x, points[9] + position.y);
		barrelPath.lineTo(points[10] + position.x, points[11] + position.y);
		barrelPath.lineTo(points[12] + position.x, points[13] + position.y);
		barrelPath.lineTo(points[14] + position.x, points[15] + position.y);

		canvas.drawPath(barrelBorderPath, barrel.borderPaint);
		canvas.drawPath(barrelPath, barrel.paint);
	}

	@Override
	public void update() {
		if (!active) return;
		acceleration.x = acceleration.x * speed;
		acceleration.y = acceleration.y * speed;
		super.update();

		List<Bullet> needDeletion = new ArrayList<>();
		bullets.forEach(bullet -> {
			if (bullet.active) bullet.update();
			else needDeletion.add(bullet);
		});
		bullets.removeAll(needDeletion);

		name.setPosition(new PointF(position.x, position.y - radius * 1.6f));
		healthBar.setPosition(new PointF(position.x, position.y - radius * 1.6f + 7));

		healthBar.changeHealth(regeneration);

		healthBar.update();

		animateRecoil();

		if (TanksHandler.player == this) {
			List<Tank> enemies = TanksHandler.getEnemies();
			for (Tank enemy : enemies)
				if (Collision.circleCircle(this, enemy)) {
					getHealthBar().changeHealth(-enemy.getBodyDamage());
					if (getHealthBar().getHealth() <= 0) {
						ContextProvider.getInstance().getContext().startActivity(new Intent(ContextProvider.getInstance().getContext(), MainActivity.class));
					}
					Game.cameraShake.increaseTrauma(enemy.getBodyDamage() / TanksHandler.player.getHealthBar().getMaxHealth() * 5);
				}
		}
	}

	public float getAngle() {
		return barrel.angle;
	}

	public void setAngle(float angle) {
		barrel.angle = angle;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(ArrayList<Bullet> bullets) {
		this.bullets = bullets;
	}

	public String getName() {
		return name.getText();
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	public HealthBar getHealthBar() {
		return healthBar;
	}

	public void setHealthBar(HealthBar healthBar) {
		this.healthBar = healthBar;
	}

	public float getBodyDamage() {
		return bodyDamage;
	}

	public void setBodyDamage(float bodyDamage) {
		this.bodyDamage = bodyDamage;
	}

	public float getRegeneration() {
		return regeneration;
	}

	public void setRegeneration(float regeneration) {
		this.regeneration = regeneration;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public float getBulletSpeed() {
		return bulletSpeed;
	}

	public void setBulletSpeed(float bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	public int getReloadMilli() {
		return reloadMilli;
	}

	public void setReloadMilli(int reloadMilli) {
		this.reloadMilli = reloadMilli;
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

	public void shoot() {
		long currentTime = System.currentTimeMillis();
		if (prevShotTime + reloadMilli < currentTime) {
			float[] positionAndVelocity = {
				barrel.length + radius - bulletRadius, 0, // position (end of barrel)
				bulletSpeed, 0 // velocity
			};
			barrel.rotationMatrix.mapPoints(positionAndVelocity);

			Paint bulletBorderPaint = new Paint(borderPaint);
			bulletBorderPaint.setStrokeWidth(3);
			Bullet.Options bulletOptions = (Bullet.Options) new Bullet.Options()
				.setDamage(damage)
				.setPaint(new Paint(paint))
				.setBorderPaint(bulletBorderPaint)
				.setVelocity(new PointF(velocity.x / 2 + positionAndVelocity[2], velocity.y / 2 + positionAndVelocity[3]));

			Bullet bullet = new Bullet(new PointF(
				position.x + positionAndVelocity[0],
				position.y + positionAndVelocity[1]),
				bulletRadius,
				bulletOptions
			);
			bullets.add(bullet);
			prevShotTime = currentTime;

			// Recoil
			hasRecoil = true;
			float[] points = new float[] {
				-recoil, 0,
			};
			barrel.rotationMatrix.mapPoints(points);
			velocity.offset(points[0], points[1]);
		}
	}

	public void animateRecoil() {
		if (!hasRecoil) return;
		long currentTime = System.currentTimeMillis();
		long timeDiff = currentTime - prevShotTime;
		int animationTime = (int) Math.min(reloadMilli * 0.7f, maxRecoilAnimationTime);
		if (timeDiff > animationTime) hasRecoil = false;
		else
			recoiledBarrelLength = Math.max((int) (barrel.length - (Math.pow(timeDiff / (float) animationTime - 0.5f, 2) * -4 + 1) * recoil * barrelRecoilMultiplier), 0);
	}

	@NonNull
	@Override
	public Object clone() throws CloneNotSupportedException {
		Tank cloned = (Tank) super.clone();
		if (cloned.barrel != null) cloned.barrel = (Barrel) barrel.clone();
		if (cloned.bulletOptions != null)
			cloned.bulletOptions = (Bullet.Options) cloned.bulletOptions.clone();
		if (cloned.bullets != null) {
			ArrayList<Bullet> clonedBullets = new ArrayList<>();
			for (Bullet bullet : cloned.bullets)
				clonedBullets.add((Bullet) bullet.clone());
			cloned.bullets = clonedBullets;
		}
		if (cloned.name != null) cloned.name = (Text) cloned.name.clone();
		if (cloned.healthBar != null) cloned.healthBar = (HealthBar) cloned.healthBar.clone();
		return cloned;
	}

	@Override
	public String getAttributes() {
		return super.getAttributes() +
			" barrel=" + barrel +
			" bulletOptions=" + bulletOptions +
			" bullets=" + bullets +
			" bulletRadius=" + bulletRadius +
			" bulletSpeed=" + bulletSpeed +
			" reloadMilli=" + reloadMilli +
			" prevShotTime=" + prevShotTime +
			" recoil=" + recoil +
			" barrelRecoilMultiplier=" + barrelRecoilMultiplier +
			" hasRecoil=" + hasRecoil +
			" maxRecoilAnimationTime=" + maxRecoilAnimationTime +
			" recoiledBarrelLength=" + recoiledBarrelLength +
			" name=" + name +
			" healthBar=" + healthBar;
	}

	@NonNull
	@Override
	public String toString() {
		return "Tank {" + getAttributes() + " }";
	}

	public static class Options extends Blob.Options {

		protected Barrel.Options barrelOptions = new Barrel.Options();
		protected Bullet.Options bulletOptions = new Bullet.Options();
		protected float recoil = 1;
		protected int reloadMilli = 400;
		protected float barrelRecoilMultiplier = 4;
		protected String name;

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

		public int getReloadMilli() {
			return reloadMilli;
		}

		public Options setReloadMilli(int reloadMilli) {
			this.reloadMilli = reloadMilli;
			return this;
		}

		public float getRecoil() {
			return recoil;
		}

		public Options setRecoil(float recoil) {
			this.recoil = recoil;
			return this;
		}

		public float getBarrelRecoilMultiplier() {
			return barrelRecoilMultiplier;
		}

		public Options setBarrelRecoilMultiplier(float barrelRecoilMultiplier) {
			this.barrelRecoilMultiplier = barrelRecoilMultiplier;
			return this;
		}

		public String getName() {
			return name;
		}

		public Options setName(String name) {
			this.name = name;
			return this;
		}
	}

	public static class Barrel implements Cloneable {

		int length = 18;
		int thickness = 23;
		float angle = 0; // 0 - 360
		Paint paint;
		Paint borderPaint;
		Matrix rotationMatrix = new Matrix(); // For server optimisation. used instead of "canvas.rotate()"

		public Barrel() {
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.parseColor("#cccccc"));
			borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			borderPaint.setColor(Color.parseColor("#727272"));
			borderPaint.setStrokeWidth(3);
		}

		public Barrel(Options options) {
			length = options.length;
			thickness = options.thickness;
			angle = options.angle;
			paint = options.paint;
			borderPaint = options.borderPaint;
		}

		@NonNull
		@Override
		public Object clone() throws CloneNotSupportedException {
			Barrel cloned = (Barrel) super.clone();
			if (cloned.paint != null) cloned.paint = new Paint(cloned.paint);
			if (cloned.borderPaint != null) cloned.paint = new Paint(cloned.borderPaint);
			return cloned;
		}

		public static class Options {

			protected int length = 18;
			protected int thickness = 22;
			protected float angle = 0;
			protected Paint paint;
			protected Paint borderPaint;

			{
				paint = new Paint(Paint.ANTI_ALIAS_FLAG);
				paint.setColor(Color.parseColor("#999999"));
				borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
				borderPaint.setColor(Color.parseColor("#727272"));
				borderPaint.setStrokeWidth(3);
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

			public Paint getBorderPaint() {
				return borderPaint;
			}

			public Options setBorderPaint(Paint borderPaint) {
				this.borderPaint = borderPaint;
				return this;
			}
		}
	}
}
