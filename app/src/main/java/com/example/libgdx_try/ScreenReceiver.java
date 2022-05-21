package com.example.libgdx_try;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Context currentContext = ContextProvider.getInstance().getContext();
		if (currentContext instanceof GameActivity)
			((GameActivity) currentContext).game.pause();

	}
}
