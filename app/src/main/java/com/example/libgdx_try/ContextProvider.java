package com.example.libgdx_try;

import android.content.Context;

public class ContextProvider {

	static ContextProvider contextProvider;

	Context context;

	ContextProvider() {
	}

	ContextProvider(Context context) {
		this.context = context;
	}

	public static ContextProvider getInstance() {
		return contextProvider;
	}

	public static ContextProvider getInstance(Context context) {
		return contextProvider = new ContextProvider(context);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
