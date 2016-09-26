package es.org.drawerexample.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import es.org.drawerexample.R;


public class FragmentTwo extends SXFragment  {

    public static String TAGNAME = "SECOND_FRAG";


    public FragmentTwo() {
        setTagName(TAGNAME + generateId());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        return view;
    }


    @Override
    public boolean onBackStack() {
        return super.onBackStack();
    }


}
