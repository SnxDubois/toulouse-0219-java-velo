package fr.wildcodeschool.metro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AccountFragment extends Fragment {
    private View accountView;
    private FloatingActionButton returnFloat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        accountView = inflater.inflate(R.layout.fragment_account, container, false);
        accountView = inflater.inflate(R.layout.fragment_send, container, false);
        returnFloat = accountView.findViewById(R.id.fbReturn);
        returnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goListStationActivity = new Intent(getActivity(), ListStation.class);
                startActivity(goListStationActivity);
            }
        });
        return  accountView;
    }


}
