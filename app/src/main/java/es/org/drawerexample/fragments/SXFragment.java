package es.org.drawerexample.fragments;

import android.support.v4.app.Fragment;
import java.util.Date;

/**
 * @author Óscar Garrucho <oscar.garrucho@gmail.com>
 * @since 07/04/2016 17:23
 */
public class SXFragment extends Fragment {

    private String tagName;


    public  boolean onBackStack () {
        return true;
    }


    public long generateId(){
        return new Date().getTime();
    }


    public String getTagName() {
        return tagName;
    }


    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
