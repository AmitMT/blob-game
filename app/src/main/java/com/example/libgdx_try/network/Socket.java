package com.example.libgdx_try.network;

import android.os.Build;
import android.util.Log;

import org.json.JSONException;

import java.net.URISyntaxException;

import io.socket.client.IO;

public class Socket {

	static Socket socketHandler;
	io.socket.client.Socket socket;

	boolean dataSending = false;

	Socket(String uri) {
		try {
			socket = IO.socket(uri);
			socket.connect();

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

	class TankDataSender extends Thread {

		@Override
		public void run() {
			Log.i("Info", "Connected");

			socket.on("tanks-data", args -> {
				String[] tankStrings = new String[args.length];
				for (int i = 0; i < tankStrings.length; i++)
					tankStrings[i] = (String) args[i];

				TanksHandler.setTanks(tankStrings);
			});

			while (true) {
				try {
					if (dataSending) socket.emit("player-data", TanksHandler.playerToJSON());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
