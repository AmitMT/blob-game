package com.example.blob_game.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread {

	ServerSocket serverSocket;
	InputStream inputStream;
	OutputStream outputStream;

	Socket socket;

	public Server() {
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(8888);
			socket = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Handler handler = new Handler(Looper.getMainLooper());

		executorService.execute(()->{
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

	public void write(byte[] bytes) {
		try {
			outputStream.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
