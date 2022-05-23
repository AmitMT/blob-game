package com.example.blob_game.camera;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.example.blob_game.utils.PerlinNoise;

public class CameraShake {

	final PerlinNoise perlinNoise = new PerlinNoise();
	final double seed = perlinNoise.getSeed();
	final double randomSeedAdder = 1;
	final PointF currentShake = new PointF();
	final PointF lastFrameShake = new PointF();
	final float maxDisplacement = 50;
	final float maxRotation = 10;
	final float maxFrequency = 3f; // per frame
	double currentAngleShake = 0;
	int currentFrame = 0;
	double lastFrameAngleShake = 0;
	double trauma = 0;
	double shake = 0;
	float damping = 0.01f;
	float currentNoiseX = 0;
	float traumaExponent = 1f;

	public CameraShake() {
	}

	public void update() {
		currentFrame++;

		if (trauma != 0)
			shake = Math.pow(trauma, traumaExponent);
		else shake = 0;
		if (shake > 0) {
			currentNoiseX += maxFrequency * trauma;
			double currentSeed = seed;

			currentShake.x = (float) ((float) perlinNoise.noise(currentNoiseX) * maxDisplacement * shake);

			currentSeed += randomSeedAdder;
			perlinNoise.setSeed(currentSeed);
			currentShake.y = (float) ((float) perlinNoise.noise(currentNoiseX) * maxDisplacement * shake);

			currentSeed += randomSeedAdder;
			perlinNoise.setSeed(currentSeed);
			currentAngleShake = perlinNoise.noise(currentNoiseX) * maxRotation * shake;

			perlinNoise.setSeed(seed);
		}
		if (trauma > damping) trauma -= damping;
		else trauma = 0;
	}

	public void transformCanvas(Canvas canvas, PointF cameraPosition) {
		lastFrameShake.set(currentShake);
		lastFrameAngleShake = currentAngleShake;
		canvas.translate(lastFrameShake.x, lastFrameShake.y);
		canvas.rotate((float) lastFrameAngleShake, cameraPosition.x, cameraPosition.y);
	}

	public void revertCanvas(Canvas canvas, PointF cameraPosition) {
		canvas.translate(-lastFrameShake.x, -lastFrameShake.y);
		canvas.rotate((float) -lastFrameAngleShake, cameraPosition.x, cameraPosition.y);
	}

	public void increaseTrauma(float trauma) {
		this.trauma += trauma;
		if (this.trauma > 1) this.trauma = 1;
	}
}
