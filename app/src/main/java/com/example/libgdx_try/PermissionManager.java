package com.example.libgdx_try;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

public class PermissionManager {

	final Map<String, PermissionState> permissions = new HashMap<>();
	final int REQUEST_CODE = 69420;
	Activity context;

	public PermissionManager(Activity context) {
		this.context = context;
	}

	public PermissionManager(Activity context, String[] permissions) {
		for (String permission : permissions) {
			checkPermission(permission);
		}
	}

	public static boolean checkPermission(Context context, String permission) {
		return PermissionState.fromInteger(ContextCompat.checkSelfPermission(context, permission)) == PermissionState.GRANTED;
	}

	public static boolean checkPermissions(Context context, String[] permissions) {
		for (String permission : permissions)
			if (PermissionState.fromInteger(ContextCompat.checkSelfPermission(context, permission)) == PermissionState.DENIED)
				return false;

		return true;
	}

	public boolean checkPermission(String permission) {
		PermissionState granted = PermissionState.fromInteger(ContextCompat.checkSelfPermission(context, permission));
		permissions.put(permission, granted);
		return granted == PermissionState.GRANTED;
	}

	public void requestPermission(String permission) {
		ActivityCompat.requestPermissions(context, new String[] { permission }, REQUEST_CODE);
	}

	public boolean checkAllPermissions() {
		for (Map.Entry<String, PermissionState> permission : permissions.entrySet()) {
			if (!checkPermission(permission.getKey())) return false;
		}
		return true;
	}

	public void requestAllPermissions() {
		ActivityCompat.requestPermissions(context, permissions.keySet().toArray(new String[0]), REQUEST_CODE);
	}

	public Activity getContext() {
		return context;
	}

	public void setContext(Activity context) {
		this.context = context;
	}

	public Map<String, PermissionState> getPermissions() {
		return permissions;
	}

	public void setPermissions(String[] permissions) {
		this.permissions.clear();

		for (String permission : permissions) {
			checkPermission(permission);
		}
	}

	public void setPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == REQUEST_CODE)
			for (int i = 0; i < permissions.length; i++)
				this.permissions.put(permissions[i], PermissionState.fromInteger(grantResults[i]));

	}

	@NonNull
	@Override
	public String toString() {
		return "PermissionManager: " + permissions;
	}

	enum PermissionState {
		GRANTED(0),
		DENIED(-1);

		private final int i;

		PermissionState(int i) {
			this.i = i;
		}

		public static PermissionState fromInteger(int x) {
			switch (x) {
				case 0:
					return GRANTED;
				case -1:
					return DENIED;
			}
			return null;
		}

		public int toInteger() {
			return this.i;
		}
	}
}
