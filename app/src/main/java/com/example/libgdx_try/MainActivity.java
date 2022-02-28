package com.example.libgdx_try;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	final PermissionManager permissionManager = new PermissionManager(this);
	final String[] PERMISSIONS = {
		Manifest.permission.ACCESS_FINE_LOCATION,
		Manifest.permission.ACCESS_COARSE_LOCATION,
		Manifest.permission.ACCESS_WIFI_STATE,
		Manifest.permission.CHANGE_WIFI_STATE,
		Manifest.permission.CHANGE_NETWORK_STATE,
		Manifest.permission.INTERNET,
		Manifest.permission.ACCESS_NETWORK_STATE
	};
	Game game;
	boolean gameActive = true;

	WifiP2pManager manager;
	WifiP2pManager.Channel channel;
	BroadcastReceiver receiver;

	IntentFilter intentFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Window window = getWindow();
		window.setFlags(
			WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN
		);

		setContentView(R.layout.activity_main);

		game = findViewById(R.id.game);

		findViewById(R.id.btn).setOnClickListener(view -> {
			if (gameActive) {
				game.pause();
				gameActive = false;
			} else {
				game.resume();
				gameActive = true;
			}
		});

		permissionManager.setPermissions(PERMISSIONS);
		permissionManager.requestAllPermissions();

		manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		channel = manager.initialize(this, getMainLooper(), null);
		receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
		intentFilter = new IntentFilter();
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		permissionManager.setPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();

		registerReceiver(receiver, intentFilter);
	}

	@Override
	protected void onPause() {
		game.pause();
		super.onPause();

		unregisterReceiver(receiver);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}