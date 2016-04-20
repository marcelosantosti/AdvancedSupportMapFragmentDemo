package com.marcelosantosti.advancedsupportmapfragment;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marcelosantosti.advancedsupportmapfragment.interfaces.OnMarkerClicked;
import com.marcelosantosti.advancedsupportmapfragment.utils.UnitUtil;

import java.util.AbstractMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by mlsantos on 20/04/2016.
 */
public class AdvancedSupportMapFragment extends SupportMapFragment {

    private Map<Marker, Object> dictionaryMarker;
    private GoogleMap googleMap;

    private Location currentLocation;

    private static final int DEFAULT_ZOOM_LEVEL = 14;
    private static final int DEFAULT_ZOOM_PADDING = 50;
    private static final int DEFAULT_ZOOM_HEIGHT = 160;
    private static final int DEFAULT_ZOOM_WIDTH = 160;

    //Listeners
    private OnMapReadyCallback onMapReadyCallback;
    private OnMarkerClicked onMarkerClicked;

    private boolean isMapReady;

    public AdvancedSupportMapFragment() {

        super();
    }

    public static AdvancedSupportMapFragment newInstance() {

        AdvancedSupportMapFragment advancedSupportMapFragment = new AdvancedSupportMapFragment();
        return advancedSupportMapFragment;
    }

    public void init(OnMapReadyCallback onMapReadyCallback) {

        this.onMapReadyCallback = onMapReadyCallback;

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mapReady(googleMap);
            }
        });
    }

    private void mapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        if (onMapReadyCallback != null)
            onMapReadyCallback.onMapReady(googleMap);

        isMapReady = true;

        if (onMarkerClicked != null) {

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    onMarkerClicked(marker);
                    return true;
                }
            });
        }
    }

    private void onMarkerClicked(Marker marker) {

        onMarkerClicked.onMarkerClicked(marker, dictionaryMarker.get(marker));
    }

    public void centerMap() {

        if (getDictionaryMarker().keySet().size() == 1) {

            Marker firstMarker = dictionaryMarker.keySet().iterator().next();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstMarker.getPosition(), DEFAULT_ZOOM_LEVEL));
        } else {

            if (getDictionaryMarker().keySet().size() > 1) {

                //center map in latlng bounds with all establishments found
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : dictionaryMarker.keySet())
                    builder.include(marker.getPosition());

                LatLngBounds bounds = builder.build();
                int padding = UnitUtil.convertToDp(getActivity(), DEFAULT_ZOOM_PADDING);

                CameraUpdate cameraUpdate;

                try {
                    cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    googleMap.animateCamera(cameraUpdate);
                } catch (IllegalStateException e) {
                    cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, DEFAULT_ZOOM_WIDTH, DEFAULT_ZOOM_HEIGHT, 0);
                    googleMap.animateCamera(cameraUpdate);
                }
            }
        }
    }

    public void addMarker(MarkerOptions markerOptions, Object object) {

        Marker marker = googleMap.addMarker(markerOptions);
        getDictionaryMarker().put(marker, object);
    }

    public void removeAllMarkers() {

        for (Marker marker : getDictionaryMarker().keySet())
            marker.remove();
    }

    public Map<Marker, Object> getDictionaryMarker() {

        if (dictionaryMarker == null)
            dictionaryMarker = new Hashtable<>();

        return dictionaryMarker;
    }

    public GoogleMap getGoogleMap() {

        return googleMap;
    }

    public void setOnMarkerClicked(OnMarkerClicked onMarkerClicked) {
        this.onMarkerClicked = onMarkerClicked;
    }

    public boolean isMapReady() {
        return isMapReady;
    }
}
