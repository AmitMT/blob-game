package com.example.libgdx_try.network;

import android.os.Build;
import android.util.Log;

import java.net.URISyntaxException;
import java.util.Collections;

import io.socket.client.IO;

public class Socket {

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
			socketHandler = new Socket(!Build.FINGERPRINT.contains("generic") ? "http://192.168.0.111:8000" : "http://10.0.2.2:8000");
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

			while (connectionActive) {
				if (dataSending) socket.emit("update-tank-data", TanksHandler.getPlayerStatus());
				if (dataSending) Log.i("aaa", TanksHandler.getPlayerStatus());
				try {
					sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
