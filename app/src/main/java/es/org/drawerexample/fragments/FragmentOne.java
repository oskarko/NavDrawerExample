package es.org.drawerexample.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import es.org.drawerexample.R;

public class FragmentOne extends SXFragment {

    public static String TAGNAME = "FIRST_FRAG";
    private RelativeLayout mainLayout;


    public FragmentOne() {
        setTagName(TAGNAME + generateId());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        // Layout principal
        mainLayout = (RelativeLayout) view.findViewById(R.id.Relative_main_layout);

        // si queremos cerrar el teclado en un momento dado, con presionar en el layout superior, nos basta
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        return view;
    }


    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    public boolean onBackStack() {
        return super.onBackStack();
    }

}
