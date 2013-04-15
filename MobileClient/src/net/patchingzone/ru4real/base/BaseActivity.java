package net.patchingzone.ru4real.base;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("NewApi")
public class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (AppSettings.fullscreen) {
			// activity in full screen
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			// requestWindowFeature(Window.FEATURE_ACTION_BAR);
			getWindow()
					.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		if (AppSettings.hideHomeBar) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}

		if (AppSettings.screenAlwaysOn) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}

		setVolume(100);
		//Utils.playSound("http://outside.mediawerf.net/8-Light_2.mp3");
		//playSound("http://outside.mediawerf.net/music.ogg");

	}

	public void setVolume(int value) {

		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		value = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) * value / 100;

		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

	}

	

	public void setWakeLock(boolean b) {

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");

		if (b) {
			wl.acquire();
		} else {
			wl.release();
		}

	}

	// override home buttons
	@Override
	public void onAttachedToWindow() {
		if (AppSettings.overrideHomeButtons) {
			this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
			super.onAttachedToWindow();
		}
	}

	// override volume buttons
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (AppSettings.overrideVolumeButtons
				&& (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}