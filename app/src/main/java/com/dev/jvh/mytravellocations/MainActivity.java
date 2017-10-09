package com.dev.jvh.mytravellocations;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private List<TravelLocation> travelLocations;
    private JSONArray locations;
    private GoogleMap mMap;
    private CountDownLatch latch = new CountDownLatch(1);
    private FetchDataTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        travelLocations = new ArrayList<>();
        fetchData();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void fetchData() {
        task = new FetchDataTask();
        task.execute("http://student.labranet.jamk.fi/~L3333/travellocations.json");

    }

    @Override
    public  void onMapReady(GoogleMap googleMap) {
        // store map object to member variable
        mMap = googleMap;
        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    }

    private class FetchDataTask extends AsyncTask<String,Void,JSONObject> {

        private boolean ready = false;
        @Override
        protected JSONObject doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            JSONObject json = null;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                json = new JSONObject(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                locations = jsonObject.getJSONArray("locations");
                for (int i=0;i < locations.length();i++) {
                    JSONObject hs = locations.getJSONObject(i);
                    travelLocations.add(
                            new TravelLocation(
                                    hs.getString("title"),
                                    hs.getLong("latitude"),
                                    hs.getLong("longitude"))
                    );
                }
                for (TravelLocation location : travelLocations) {
                    LatLng ICT = new LatLng(location.getLatitude(), location.getLongitude());
                    final Marker ict = mMap.addMarker(new MarkerOptions()
                            .position(ICT)
                            .title(location.getTitle()));
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(final Marker marker) {
                                Toast.makeText(getApplicationContext(), "Marker = " + marker.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;
                        }
                    });
                }
            } catch (JSONException e) {
                Log.e("JSON", "Error getting data.");
            }

        }
    }
}
