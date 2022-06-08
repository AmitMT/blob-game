package com.example.blob_game.network;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.blob_game.ContextProvider;
import com.example.blob_game.Game;
import com.example.blob_game.MainActivity;
import com.example.blob_game.game_panel.UpgradesPanel;

import java.net.URISyntaxException;
import java.util.Collections;

import io.socket.client.IO;

public class Socket {

	static final boolean serverPublic = false;

	static Socket socketHandler;
	io.socket.client.Socket socket;
	boolean dataSending = false;
	boolean connectionActive = false;

	Socket(String uri) {
		try {
			IO.Options options = IO.Options.builder()
				.setAuth(Collections.singletonMap("tankId", TanksHandler.player.getId()))
				.build();
			socket = IO.socket(uri, options);

			socket.on("connect_error", args -> {
				Log.e("Error", "Error: " + args[0]);
			});

			socket.on("connected", args -> new TankDataSender().start());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static Socket getInstance() {
		if (socketHandler == null)
			// run ipconfig in cmd and set your IPv4 in the first url below
			socketHandler = new Socket(
				serverPublic ?
					"https://blob-game-server.herokuapp.com/" :
					(!Build.FINGERPRINT.contains("generic") ? "http://192.168.0.112:8000" : "http://10.0.2.2:8000")
			);

		return socketHandler;
	}

	public io.socket.client.Socket getSocket() {
		return socket;
	}

	public void setDataSending(boolean dataSending) {
		this.dataSending = dataSending;
	}

	public void startSocket() {
		socket.connect();
	}

	public void stopSocket() {
		socket.disconnect();
		connectionActive = false;
	}

	class TankDataSender extends Thread {

		@Override
		public void run() {
			Log.i("Info", "Connected");
			connectionActive = true;

			socket.on("tanks-data", args -> {
				String[] tankStrings = new String[args.length];
				for (int i = 0; i < tankStrings.length; i++)
					tankStrings[i] = (String) args[i];

				TanksHandler.setTanks(tankStrings);
			});

			socket.on("you-got-hit", args -> {
				float damage = Float.parseFloat((String) args[0]);
				String enemyId = (String) args[1];
				TanksHandler.player.getHealthBar().changeHealth(-damage);
				if (TanksHandler.player.getHealthBar().getHealth() <= 0) {
					Socket.getInstance().getSocket().emit("killed", enemyId);
					ContextProvider.getInstance().getContext().startActivity(new Intent(ContextProvider.getInstance().getContext(), MainActivity.class));
				}
				Game.cameraShake.increaseTrauma(damage / TanksHandler.player.getHealthBar().getMaxHealth() * 5);
			});

			socket.on("you-killed", args -> {
				UpgradesPanel.upgradesPanel.addUpgradePoint();
			});

			while (connectionActive) {
				if (dataSending) socket.emit("update-tank-data", TanksHandler.getPlayerStatus());
				try {
					sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
