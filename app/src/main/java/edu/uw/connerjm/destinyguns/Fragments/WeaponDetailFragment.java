package edu.uw.connerjm.destinyguns.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.connerjm.destinyguns.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeaponDetailFragment extends Fragment
{


    public WeaponDetailFragment() {/* Required empty public constructor */}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_login, container, false);



        return v;
    }


}
