package net.patchingzone.ru4real.fragments;

import java.util.HashMap;

import net.patchingzone.ru4real.L;
import net.patchingzone.ru4real.MainActivityPhone;
import net.patchingzone.ru4real.Network;
import net.patchingzone.ru4real.NetworkListener;
import net.patchingzone.ru4real.R;
import net.patchingzone.ru4real.game.Player;

import org.json.JSONArray;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapCustomFragment extends SupportMapFragment {

	protected static final String TAG = "GMAP";

	static final LatLng ROTTERDAM_HOME = new LatLng(51.908815, 4.503193);
	static final LatLng ROTTERDAM_DAVID = new LatLng(51.934496, 4.465889);
	static final LatLng ROTTERDAM_CENTRAAL = new LatLng(51.925405, 4.469494);
	static final LatLng ROTTERDAM_ZADKINE = new LatLng(51.885088, 4.496005);
	static final LatLng ROTTERDAM_LIBRARY = new LatLng(51.921138, 4.488409);
	static final LatLng ROTTERDAM_RAAF = new LatLng(51.9046454, 4.497884);
	UiSettings settings;

	View v;
	private GoogleMap map;
	public TouchableWrapper mTouchView;
	private Marker touchPosition;
	private Marker currentPosition;
	Network network;
	public MapView mapView;

	final long duration = 400;
	final Handler handler = new Handler();
	final long start = SystemClock.uptimeMillis();

	HashMap<String, Player> playerList = new HashMap<String, Player>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.fragment_map, container, false);
		// L.d("qq", "" + getChildFragmentManager() + " " +
		// getFragmentManager().findFragmentById(R.id.map));
		mapView = (MapView) v.findViewById(R.id.mapq);
		mapView.onCreate(savedInstanceState);

		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO handle this situation
		}

		map = mapView.getMap();
		map.setInfoWindowAdapter(new CustomInfoWindowAdapter(getLayoutInflater(savedInstanceState)));

		// map = ((SupportMapFragment)
		// getChildFragmentManager().findFragmentById(R.id.map)).getMap();

		ToggleButton lock = (ToggleButton) v.findViewById(R.id.toggleLock);
		lock.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				L.d("qq", "" + isChecked);
				settings.setAllGesturesEnabled(isChecked);
				((MainActivityPhone) getActivity()).gpsOn = isChecked;

			}
		});

		network = ((MainActivityPhone) getActivity()).network;

		network.addGameListener(new NetworkListener() {

			@Override
			public void onUpdatedLocation(String event, JSONArray arguments) {
			}

			@Override
			public void onPlayerJoined(final Player player) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (player == null) {
							Log.d("qq", player.nickname + " " + player.latLng.latitude + " " + player.latLng.longitude);
						} else {
							Log.d("qq", "not null ---> " + player.nickname + " " + player.latLng.latitude + " "
									+ player.latLng.longitude);

						}
						Marker marker = map.addMarker(new MarkerOptions().position(player.latLng)
								.title(player.nickname).snippet("connected users")
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
						player.addMarker(marker);
						playerList.put(player.id, player);
					}
				});

			}

			@Override
			public void onPlayerDisconnected(String event, JSONArray arguments) {
			}

			@Override
			public void onMessageReceived(String event, JSONArray arguments) {
			}

			@Override
			public void onListTrips(String event, JSONArray arguments) {
			}

			@Override
			public void onListTargets(String event, JSONArray arguments) {
			}

			@Override
			public void onUpdateLocation(final Player player) {
				L.d("player", "--> update location");
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Player p = (Player) playerList.get(player.id);
						if (p != null) {
							L.d("player", "player update " + p.latLng.latitude + " " + p.latLng.longitude);
							p.marker.setPosition(player.latLng);
						}
					}
				});
			}

			@Override
			public void onTargetInRange(double latitude, double longitude, String value, float distance, int range) {
			}

			@Override
			public void onTextMessage(String text) {
			}

			@Override
			public void onPoke() {
			}

			@Override
			public void onPlayerInRange(String nickname, String sound, float distance) {
			}

			@Override
			public void onRefresh() {
			}

			@Override
			public void onPlayerScored(Player player, int targetIndex, int totalTargets) {
			}

		
		});

		// map = getMap();
		mTouchView = new TouchableWrapper(this, getActivity());
		mTouchView.addView(v);

		settings = mapView.getMap().getUiSettings();
		settings.setAllGesturesEnabled(false);
		settings.setMyLocationButtonEnabled(false);
		settings.setZoomControlsEnabled(true);

		/*
		 * Marker rCentraal = map.addMarker(new
		 * MarkerOptions().position(ROTTERDAM_CENTRAAL
		 * ).title("Rotterdam Centraal"));
		 * 
		 * Marker rZadkine = map.addMarker(new
		 * MarkerOptions().position(ROTTERDAM_ZADKINE).title("Zadkine")
		 * .snippet(
		 * "Zadkine lalala").icon(BitmapDescriptorFactory.fromResource(R
		 * .drawable.ic_launcher)));
		 * 
		 * Marker rLibrary = map.addMarker(new
		 * MarkerOptions().position(ROTTERDAM_LIBRARY).title("Library")
		 * .snippet(
		 * "Zadkine lalala").icon(BitmapDescriptorFactory.fromResource(R
		 * .drawable.ic_launcher)));
		 * 
		 * Marker rHome = map.addMarker(new
		 * MarkerOptions().position(ROTTERDAM_HOME).title("Home")
		 * .snippet("Zadkine lalala"
		 * ).icon(BitmapDescriptorFactory.fromResource(R
		 * .drawable.ic_launcher)));
		 * 
		 * Marker rDavid = map.addMarker(new
		 * MarkerOptions().position(ROTTERDAM_DAVID).title("David")
		 * .snippet("Zadkine lalala"
		 * ).icon(BitmapDescriptorFactory.fromResource(R
		 * .drawable.ic_launcher)));
		 */

		touchPosition = map.addMarker(new MarkerOptions().position(ROTTERDAM_DAVID).title("Touch").snippet("Touching")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.finger)));

		currentPosition = map.addMarker(new MarkerOptions().position(ROTTERDAM_DAVID).title("Touch")
				.snippet("Touching").icon(BitmapDescriptorFactory.fromResource(R.drawable.current)));

		// Move the camera instantly
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(ROTTERDAM_RAAF, 11));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

		/*
		 * map.animateCamera(CameraUpdateFactory.newCameraPosition( new
		 * CameraPosition
		 * .Builder().target(ROTTERDAM_LIBRARY).zoom(17).bearing(90
		 * ).tilt(30).build()), 2000, null);
		 */

		/*
		 * final Interpolator interpolator = new LinearInterpolator();
		 * handler.post(new Runnable() {
		 * 
		 * @Override public void run() { long elapsed =
		 * SystemClock.uptimeMillis() - start; float t =
		 * interpolator.getInterpolation((float) elapsed / duration); double
		 * lng_ = t * lon + (1 - t) * lon; double lat_ = t * lat + (1 - t) *
		 * lat; currentPosition.setPosition(new LatLng(lat_, lng_)); if (t <
		 * 1.0) { // Post again 10ms later. handler.postDelayed(this, 10); }
		 * else { // animation ended } } });
		 */

		// map.add
		return mTouchView;

	}

	public void setTouch(LatLng latLng) {
		touchPosition.setPosition(latLng);
		network.sendLocation(latLng.latitude, latLng.longitude, 0);
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onDestroy() {
		mapView.onDestroy();
		super.onDestroy();
	}

}
