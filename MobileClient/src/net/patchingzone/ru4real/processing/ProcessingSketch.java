package net.patchingzone.ru4real.processing;

import processing.core.PApplet;
import processing.core.PFont;
import android.os.Bundle;
import android.os.Looper;

public class ProcessingSketch extends PApplet {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	PFont t;

	float count1 = 0;
	float count2 = 1;
	float count3 = 2;
	float count4 = 3;
	float count5 = 4; 
	
	float speed1 = 0.001f; 
	float speed2 = 0.001f; 
	float speed3 = 0.001f; 
	float speed4 = 0.001f; 
	float speed5 = 0.001f; 

	float q = 0.0f;
	float q2 = 0.0f;

	public void setup() {
		frameRate(28);
		smooth();
		t = createFont("AdventPro-SemiBold", 48);

	}

	public void draw() { // background(255);

		if (frameCount == 1) {
			Looper.prepare();
		}

		background(0);
		// q = map(mouseX, 0, width, 0, 10);

		textFont(t, 30);
		fill(221, 41, 224, 225);

		text("Are you \nfor Real?", width / 2 - 50 + random(q), height / 2 - 20 + random(q2));
		fill(41, 210, 224, 225);

		text("Are you \nfor Real?", width / 2 - 50 + random(q), height / 2 - 20 + random(q2));

		noStroke();
		fill(41, 210, 224);
		pushMatrix();
		translate(width / 2, height / 2);
		rotate(count1);
		Utils.corona(this, (float) (80 + 20 * Math.sin(frameCount * 0.1)), (float) (90 + 20 * Math.sin(frameCount * 0.1 + 5)), 0, 100, 100);
		popMatrix();

		pushMatrix();
		translate(width / 2, height / 2);
		rotate(count2);
		Utils.corona(this, 100, 110, 0, 100, 100);
		popMatrix();

		pushMatrix();
		translate(width / 2, height / 2);
		rotate(count3);
		Utils.corona(this, 120, 130, 0, 100, 100);
		popMatrix();

		pushMatrix();
		translate(width / 2, height / 2);
		rotate(count4);
		noStroke();
		Utils.corona(this, 140, 150, 0, 100, 100);
		popMatrix();

		pushMatrix();
		translate(width / 2, height / 2);
		rotate(count5);
		Utils.corona(this, 160, 170, 0, 100, 100);
		popMatrix();

		count1 += speed1;
		count2 += speed2;
		count3 += speed3;
		count4 += speed4;
		count5 += speed5;

	}

	/*
	 * public int sketchWidth() { return 500; }
	 * 
	 * public int sketchHeight() { return 500; }
	 */

	void randomSpeeds() {
		speed1 = random(0.01f, 0.5f);
		speed2 = random(0.01f, 0.5f);
		speed3 = random(0.01f, 0.5f);
		speed4 = random(0.01f, 0.5f);
		speed5 = random(0.01f, 0.5f);
	}

	public void mouseReleased() {
		randomSpeeds();
		q = map(mouseX, 0, width, 0, 10);
		q2 = map(mouseY, 0, height, 0, 10);

	}

}