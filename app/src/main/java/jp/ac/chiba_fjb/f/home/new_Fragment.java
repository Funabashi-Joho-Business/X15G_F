package jp.ac.chiba_fjb.f.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class new_Fragment extends Fragment {


    public new_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("新規追加");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.new_, container, false);
    }

}
