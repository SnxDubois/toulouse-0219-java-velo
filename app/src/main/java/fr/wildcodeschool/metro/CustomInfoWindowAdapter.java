package fr.wildcodeschool.metro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    private int bikeAvailable;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.info_windows_map, null);
    }

    private void rendowWindowText(Marker marker, View view) {

        //récupération de l'objet marker
        Station recupMarker = (Station) marker.getTag();

        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        if (!title.equals("")) {
            tvTitle.setText(title);
        }

        int bikeAvailable = recupMarker.getAvailableBikes();
        TextView tvBikeAvailable = (TextView) view.findViewById(R.id.tvAvailableBike);

        if (bikeAvailable != 0) {
            tvBikeAvailable.setText(String.valueOf(bikeAvailable) + " " + mContext.getString(R.string.velos_disponibles));
        }

        int standsAvailable = recupMarker.getAvailableStands();
        TextView tvStandsAvailable = (TextView) view.findViewById(R.id.tvAvailableStands);

        if (standsAvailable != 0) {
            tvStandsAvailable.setText(String.valueOf(standsAvailable) + " " + mContext.getString(R.string.places_disponibles));
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }
}
