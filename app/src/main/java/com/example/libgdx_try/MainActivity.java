package com.example.libgdx_try;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.libgdx_try.network.Client;
import com.example.libgdx_try.network.Server;
import com.example.libgdx_try.network.WiFiDirectBroadcastReceiver;

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
	WiFiDirectBroadcastReceiver wifiReceiver;
	IntentFilter intentFilter;

	ListView wifiListView;

	Server server;
	Client client;

	boolean isHost;

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

				wifiReceiver.setDiscoveringPeers(this, false);
			} else {
				game.resume();
				gameActive = true;

				wifiReceiver.setDiscoveringPeers(this, true);
			}
		});

		permissionManager.setPermissions(PERMISSIONS);
		permissionManager.requestAllPermissions();

		manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		channel = manager.initialize(this, getMainLooper(), null);
		wifiReceiver = new WiFiDirectBroadcastReceiver(manager, channel, this);

		intentFilter = new IntentFilter();
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		wifiReceiver.setDiscoveringPeers(this, true);

		wifiListView = findViewById(R.id.wifi_list);
		wifiReceiver.addPeerListListener(wifiP2pDeviceList -> {
			WifiP2pDevice[] devices = wifiP2pDeviceList.getDeviceList().toArray(new WifiP2pDevice[0]);
			String[] deviceList = new String[devices.length];
			for (int i = 0; i < deviceList.length; i++)
				deviceList[i] = devices[i].deviceName;

			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, deviceList);
			wifiListView.setAdapter(arrayAdapter);
		});
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

		registerReceiver(wifiReceiver, intentFilter);
	}

	@Override
	protected void onPause() {
		game.pause();
		super.onPause();

		unregisterReceiver(wifiReceiver);
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