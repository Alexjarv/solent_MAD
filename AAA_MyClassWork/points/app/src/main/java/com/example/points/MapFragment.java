package com.example.points;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MapFragment extends Fragment {

    private static final Double DEFAULT_LAT = 50.9035;
    private static final Double DEFAULT_LON = -1.4042;
    public static final Integer DEFAULT_ZOOM = 16;

    MapView mv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.map_fragment, parent);

        mv = (MapView) view.findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(DEFAULT_ZOOM);
        mv.getController().setCenter(new GeoPoint(DEFAULT_LAT, DEFAULT_LON));

        return view;
    }

    public void setLocation(Double lat, Double lon) {
        mv = (MapView) getView().findViewById(R.id.map1);
        mv.getController().setCenter(new GeoPoint(lat, lon));
    }
    public void setOverlays(org.osmdroid.views.overlay.Overlay items){
        mv = (MapView) getView().findViewById(R.id.map1);
        mv.getOverlays().add(items);
    }
}
