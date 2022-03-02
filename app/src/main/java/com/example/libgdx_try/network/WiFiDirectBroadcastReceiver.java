package com.example.libgdx_try.network;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import com.example.libgdx_try.MainActivity;
import com.example.libgdx_try.PermissionManager;

import java.util.ArrayList;
import java.util.List;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

	WifiP2pManager manager;
	WifiP2pManager.Channel channel;
	MainActivity activity;

	boolean isDiscoveringPeers;
	List<WifiP2pDevice> wifiPeers = new ArrayList<>();

	List<WifiP2pManager.PeerListListener> peerListListeners = new ArrayList<>();

	public WiFiDirectBroadcastReceiver(@NonNull WifiP2pManager manager, @NonNull WifiP2pManager.Channel channel, @NonNull MainActivity activity) {
		super();
		this.manager = manager;
		this.channel = channel;
		this.activity = activity;
	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
	@RequiresPermission(value = Manifest.permission.ACCESS_FINE_LOCATION)
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
				Log.e("Yesss", "Connected to wifi");
			else
				Log.e("Nooo", "Disconnected from wifi");

		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			manager.requestConnectionInfo(channel, networkInfo -> Log.i("Info-requestConnectionInfo", networkInfo.toString()));

			manager.requestPeers(channel, wifiP2pDeviceList -> {
				wifiPeers = new ArrayList<>(wifiP2pDeviceList.getDeviceList());
				Log.d("Current peers: ", wifiPeers.toString());
				if (wifiPeers.size() > 0) {
					// connectToPeer(context, this.wifiPeers.get(0));
				}

				for (WifiP2pManager.PeerListListener peerListListener : peerListListeners)
					if (peerListListener != null)
						peerListListener.onPeersAvailable(wifiP2pDeviceList);

			});
		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			// Respond to new connection or disconnections
			Log.e("Yesss", "New!");
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			// Respond to this device's wifi state changing
		}
	}

	public void connectToPeer(Context context, WifiP2pDevice device) {
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = device.deviceAddress;
		if (PermissionManager.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
			manager.connect(channel, config, new WifiP2pManager.ActionListener() {
				@Override
				public void onSuccess() {
					Log.e("Yesss: ", "Connected to " + device.deviceName);
				}

				@Override
				public void onFailure(int i) {
					Log.e("Problem: ", "Could not connect to " + device.deviceName + ". Error code " + i);
				}
			});
		}
	}

	public boolean isDiscoveringPeers() {
		return isDiscoveringPeers;
	}

	public void setDiscoveringPeers(Context context, boolean isDiscoveringPeers) {
		final WiFiDirectBroadcastReceiver that = this;

		if (isDiscoveringPeers)
			if (PermissionManager.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
				manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
					@Override
					public void onSuccess() {
						that.isDiscoveringPeers = true;
					}

					@Override
					public void onFailure(int i) {
						Log.e("Problem: ", "Discovering peers didn't start. Error code " + i);
					}
				});
			} else if (PermissionManager.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
				manager.stopPeerDiscovery(channel, new WifiP2pManager.ActionListener() {

					@Override
					public void onSuccess() {
						that.isDiscoveringPeers = false;
					}

					@Override
					public void onFailure(int i) {
						Log.e("Problem: ", "Discovering peers didn't stop. Error code " + i);
					}
				});
			}
	}

	public List<WifiP2pDevice> getWifiPeers() {
		return wifiPeers;
	}

	public boolean addPeerListListener(WifiP2pManager.PeerListListener peerListListener) {
		return peerListListeners.add(peerListListener);
	}

	public boolean removePeerListListener(WifiP2pManager.PeerListListener peerListListener) {
		return peerListListeners.remove(peerListListener);
	}
}

