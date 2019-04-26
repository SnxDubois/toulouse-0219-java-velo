package fr.wildcodeschool.metro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


public class SettingsFragment extends Fragment {
    private Singleton settings;
    public static Settings mSettings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        settings = Singleton.getInstance();
        mSettings = settings.getSettings();
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Switch switchTheme = view.findViewById(R.id.switch1);
        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setTheme(isChecked);
            }
        });
        return  view;

    }

    private void switchTheme(){

    }

}
