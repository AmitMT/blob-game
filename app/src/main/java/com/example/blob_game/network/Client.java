package com.example.blob_game.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client extends Thread {

	String hostAddress;
	InputStream inputStream;
	OutputStream outputStream;

	Socket socket;

	public Client(InetAddress hostAddress) {
		this.hostAddress = hostAddress.getHostAddress();

		socket = new Socket();
	}

	@Override
	public void run() {
		try {
			socket.connect(new InetSocketAddress(hostAddress, 8888), 500);
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Handler handler = new Handler(Looper.getMainLooper());

		executorService.execute(() -> {
			byte[] buffer = new byte[1024];
			int bytes;

			while (socket != null) {
				try {
					bytes = inputStream.read(buffer);
					if (bytes > 0) {
						int finalBytes = bytes;
						handler.post(() -> {
							String tempMessage = new String(buffer, 0, finalBytes);
							Log.e("Socket Data: ", tempMessage);
						});
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
