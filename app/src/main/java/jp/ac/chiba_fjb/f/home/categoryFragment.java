package jp.ac.chiba_fjb.f.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class categoryFragment extends Fragment {


    public categoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("編集");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.category, container, false);
    }

}
