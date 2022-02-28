package com.example.libgdx_try;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.Arrays;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

	WifiP2pManager manager;
	WifiP2pManager.Channel channel;
	MainActivity activity;

	public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, MainActivity activity) {
		super();
		this.manager = manager;
		this.channel = channel;
		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				// Wifi P2P is enabled
			} else {
				// Wi-Fi P2P is not enabled
			}
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			// Call WifiP2pManager.requestPeers() to get a list of current peers
			if (PermissionManager.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
				manager.requestPeers(channel, (wifiP2pDeviceList) -> {
					Log.e("hii", "lololol");
					Log.e("hii", Arrays.toString(wifiP2pDeviceList.getDeviceList().toArray()));
				});
			}
		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			// Respond to new connection or disconnections
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			// Respond to this device's wifi state changing
		}
	}
}
