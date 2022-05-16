package com.example.libgdx_try.network;

import android.graphics.Paint;
import android.graphics.PointF;

import androidx.core.content.ContextCompat;

import com.example.libgdx_try.ContextProvider;
import com.example.libgdx_try.R;
import com.example.libgdx_try.game_object.Tank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TanksHandler {

	public static Tank player;
	static List<Tank> enemies = new ArrayList<>();

	static Paint playerPaint;
	static Paint playerBorderPaint;
	static Paint barrelPaint;
	static Paint barrelBorderPaint;

	static {
		playerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		playerPaint.setColor(ContextCompat.getColor(ContextProvider.getInstance().getContext(), R.color.enemy));
		playerBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		playerBorderPaint.setStyle(Paint.Style.STROKE);
		playerBorderPaint.setColor(ContextCompat.getColor(ContextProvider.getInstance().getContext(), R.color.enemy_border));
		playerBorderPaint.setStrokeWidth(3.5f);
		barrelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		barrelPaint.setColor(ContextCompat.getColor(ContextProvider.getInstance().getContext(), R.color.barrel));
		barrelBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		barrelBorderPaint.setStyle(Paint.Style.STROKE);
		barrelBorderPaint.setColor(ContextCompat.getColor(ContextProvider.getInstance().getContext(), R.color.barrel_border));
		barrelBorderPaint.setStrokeWidth(3.5f);
	}

	public static String getPlayerStatus() {
		return "id: " + player.getId() +
			"\nx: " + player.getPosition().x +
			"\ny: " + player.getPosition().y +
			"\nradius: " + player.getRadius() +
			"\nvelocityX: " + player.getVelocity().x +
			"\nvelocityY: " + player.getVelocity().y +
			"\naccelerationX: " + player.getAcceleration().x +
			"\naccelerationY: " + player.getAcceleration().y;
	}

	public static void setTanks(String[] tankStrings) {
		Tank.Options playerOptions = (Tank.Options) new Tank.Options()
			.setBarrelOptions(new Tank.Barrel.Options()
				.setLength(18)
				.setThickness(22)
				.setPaint(barrelPaint)
			)
			.setPaint(playerPaint)
			.setBorderPaint(playerBorderPaint);

		ArrayList<HashMap<String, String>> tanksProperties = new ArrayList<>(tankStrings.length);
		for (String tankString : tankStrings)
			tanksProperties.add(getPropertiesFromStatusString(tankString));

		boolean[] enemyUpdated = new boolean[enemies.size()];
		for (HashMap<String, String> tankProperties : tanksProperties) {
			if (!player.getId().equals(tankProperties.get("id"))) {
				boolean foundExistingTank = false;
				int index = 0;
				for (Tank enemy : enemies) {
					if (enemy.getId().equals(tankProperties.get("id"))) {
						enemy.setPosition(
							new PointF(
								Float.parseFloat(Objects.requireNonNull(tankProperties.get("x"))),
								Float.parseFloat(Objects.requireNonNull(tankProperties.get("y")))
							)
						);
						enemy.setRadius(Float.parseFloat(Objects.requireNonNull(tankProperties.get("radius"))));
						enemy.setVelocity(
							new PointF(
								Float.parseFloat(Objects.requireNonNull(tankProperties.get("velocityX"))),
								Float.parseFloat(Objects.requireNonNull(tankProperties.get("velocityY")))
							)
						);
						enemy.setAcceleration(
							new PointF(
								Float.parseFloat(Objects.requireNonNull(tankProperties.get("accelerationX"))),
								Float.parseFloat(Objects.requireNonNull(tankProperties.get("accelerationY")))
							)
						);
						foundExistingTank = true;
						enemyUpdated[index] = true;
						break;
					}
					index++;
				}
				if (foundExistingTank) continue;
				Tank newEnemy = new Tank(
					new PointF(
						Float.parseFloat(Objects.requireNonNull(tankProperties.get("x"))),
						Float.parseFloat(Objects.requireNonNull(tankProperties.get("y")))
					),
					Float.parseFloat(Objects.requireNonNull(tankProperties.get("radius"))),
					playerOptions
				);
				newEnemy.setId(tankProperties.get("id"));
				newEnemy.setVelocity(
					new PointF(
						Float.parseFloat(Objects.requireNonNull(tankProperties.get("velocityX"))),
						Float.parseFloat(Objects.requireNonNull(tankProperties.get("velocityY")))
					)
				);
				newEnemy.setAcceleration(
					new PointF(
						Float.parseFloat(Objects.requireNonNull(tankProperties.get("accelerationX"))),
						Float.parseFloat(Objects.requireNonNull(tankProperties.get("accelerationY")))
					)
				);
				enemies.add(newEnemy);
			}
		}
		int removed = 0;
		for (int i = 0; i < enemyUpdated.length; i++)
			if (!enemyUpdated[i]) {
				enemies.remove(i - removed);
				removed++;
			}
	}

	public static List<Tank> getEnemies() {
		return enemies;
	}

	public static HashMap<String, String> getPropertiesFromStatusString(String status) {
		HashMap<String, String> tankProperties = new HashMap<>();
		String[] properties = status.split("\n");
		for (String property : properties) {
			String[] keyAndValue = property.split(": ");
			tankProperties.put(keyAndValue[0], keyAndValue[1]);
		}
		return tankProperties;
	}
}
