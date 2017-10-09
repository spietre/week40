package fi.jamk.l3329.mapsjsonexample;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.Inflater;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private JSONArray jsonCourses;
    private List<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        courses = new ArrayList<>();
        FetchDataTask task = new FetchDataTask();
        task.execute("http://ptm.fi/materials/golfcourses/golf_courses.json");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //add one marker
        LatLng ICT = new LatLng(62.2416223, 25.7597309);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ICT, 5));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.detail, null);
                String imageName = null;
                for (Course c:courses){
                    if (c.getCourse().equals(marker.getTitle())){
                        imageName = c.getImage();
//                        Log.i("_____", imageName);
                        break;
                    }
                }
                ImageView imageView =  v.findViewById(R.id.image_golf);

                Picasso.with(getApplicationContext())
                        .load("http://ptm.fi/materials/golfcourses/" + imageName)
                        .into(imageView);
                TextView title = v.findViewById(R.id.title);
                TextView snippet = v.findViewById(R.id.snippet);

                title.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());
                return v;

            }

        });

    }



    class FetchDataTask extends AsyncTask<String, Void, JSONObject>
    {
        Course c;

        @Override
        protected JSONObject doInBackground(String... urls) {

            HttpURLConnection urlConnection = null;
            JSONObject json = null;

            try {
                String line;
                URL url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));

                StringBuilder stringBuilder = new StringBuilder();


                while ( (line = reader.readLine()) != null){
                    stringBuilder.append(line).append("\n");
                }

                reader.close();
                json = new JSONObject(stringBuilder.toString());

            }
            catch (IOException | JSONException e){
                e.printStackTrace();
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return json;
        }



        @Override
        protected void onPostExecute(JSONObject json){

//            StringBuffer text = new StringBuffer("");

            try {
                jsonCourses = json.getJSONArray("courses");
                for (int i = 0; i < jsonCourses.length(); i++){
                    JSONObject cs = jsonCourses.getJSONObject(i);
                    c = new Course.CourseBuilder()
                                    .type(cs.getString("type"))
                                    .latLng(new LatLng(cs.getDouble("lat"), cs.getDouble("lng")))
                                    .course(cs.getString("course"))
                                    .address(cs.getString("address"))
                                    .phone(cs.getString("phone"))
                                    .email(cs.getString("email"))
                                    .web(cs.getString("web"))
                                    .image(cs.getString("image"))
                                    .text(cs.getString("text"))
                                    .build();

                    courses.add(c);

                    MarkerOptions m = new MarkerOptions()
                            .position(c.getLatLng())
                            .title(c.getCourse())
                            .snippet("Address: " + c.getAddress() + "\n"
                                    + "Phone: " + c.getPhone() + "\n"
                                    + "email: " + c.getEmail() + "\n"
                                    + "web: " + c.getWeb() + "\n"
                                    + "text: " + c.getText()
                            );
                    switch (c.getType()){
                        case "Kulta":
                            m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                            break;
                        case "Kulta/Etu":
                            m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            break;
                        case "Etu":
                            m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                            break;
                        default:

                    }

                    mMap.addMarker(m);


                }


            }
            catch (JSONException e){
                Log.e("JSON", "Error getting data.");
            }


        }
    }
}
