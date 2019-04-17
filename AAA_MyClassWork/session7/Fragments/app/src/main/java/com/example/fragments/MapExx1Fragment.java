package com.example.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MapExx1Fragment extends Fragment {

    // my house burnett close
    // http://www.informationfreeway.org/
    public static final Double DEFAULT_LAT = 50.9246;
    public static final Double DEFAULT_LON = -1.3719;
    public static final Integer DEFAULT_ZOOM = 16;

    MapView mv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mapex1frag, parent);

        // this does allow the default values to work
        mv = (MapView) view.findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(DEFAULT_ZOOM);
        mv.getController().setCenter(new GeoPoint(DEFAULT_LAT, DEFAULT_LON));

        return view;
    }

    public void setLocation(Double lat, Double lon) {
        MapView mv = (MapView) getView().findViewById(R.id.map1);
        mv.getController().setZoom(DEFAULT_ZOOM);
        mv.getController().setCenter(new GeoPoint(lat, lon));
    }
}