package com.example.libgdx_try.network;

import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.libgdx_try.ContextProvider;
import com.example.libgdx_try.R;
import com.example.libgdx_try.game_object.Tank;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TanksHandler {

	public static Tank player;
	static List<Tank> enemies = new ArrayList<>();

	public static String playerToJSON() throws JSONException {
		if (player == null) return "{}";
		try {
			return new Gson().toJson(player);
		} catch (Error error) {
			return "{}";
		}
	}

	public static void setTanks(String[] tankStrings) {
		Tank[] tanks = new Tank[tankStrings.length];
		for (int i = 0; i < tanks.length; i++)
			tanks[i] = new Gson().fromJson(tankStrings[i], Tank.class);

		Paint playerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		playerPaint.setColor(ContextCompat.getColor(ContextProvider.getInstance().getContext(), R.color.enemy));
		Paint playerBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		playerBorderPaint.setStyle(Paint.Style.STROKE);
		playerBorderPaint.setColor(ContextCompat.getColor(ContextProvider.getInstance().getContext(), R.color.player_border));
		playerBorderPaint.setStrokeWidth(3.5f);
		Paint barrelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		barrelPaint.setColor(ContextCompat.getColor(ContextProvider.getInstance().getContext(), R.color.barrel));
		Paint barrelBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		barrelBorderPaint.setStyle(Paint.Style.STROKE);
		barrelBorderPaint.setColor(ContextCompat.getColor(ContextProvider.getInstance().getContext(), R.color.barrel_border));
		barrelBorderPaint.setStrokeWidth(3.5f);

		Tank.Options playerOptions = (Tank.Options) new Tank.Options()
			.setBarrelOptions(new Tank.Barrel.Options()
				.setLength(18)
				.setThickness(22)
				.setPaint(barrelPaint)
			)
			.setPaint(playerPaint)
			.setBorderPaint(playerBorderPaint);
		synchronized (enemies) {
			enemies.clear();
			enemies.add(new Tank(new PointF(10, 100), 30, playerOptions));
		}
		// for (Tank tank : tanks) {
		// 	if (!tank.getId().equals(player.getId()))
		// enemies.add(tank);
		// }
		if (enemies.size() > 0) {
			Log.e("lollll", "" + player.getId() + "    " + enemies.get(0).getId());
		}
	}

	public static List<Tank> getEnemies() {
		return enemies;
	}

	public static void setEnemies(List<Tank> enemies) {
		TanksHandler.enemies = enemies;
	}
}
