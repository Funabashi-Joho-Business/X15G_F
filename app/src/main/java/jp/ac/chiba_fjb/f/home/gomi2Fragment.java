package jp.ac.chiba_fjb.f.home;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class gomi2Fragment extends Fragment {


    public gomi2Fragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view =  inflater.inflate(R.layout.gomi2, container, false);
            return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button hukugen = (Button)view.findViewById(R.id.button4);


        //インスタンスの取得
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.layout5);

        //データベースに接続
        TextDB db = new TextDB(getActivity());

        //クエリーの発行
        Cursor res = db.query("select id,name from GomiDB;");
        //データがなくなるまで次の行へ
        while(res.moveToNext()) {
            LinearLayout textlayout;
            textlayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.text2, null);
            final TextView textView2 = (TextView) textlayout.findViewById(R.id.textView2);
            CheckBox checkBox = (CheckBox) textlayout.findViewById(R.id.checkBox2);

            //0列目を取り出し
            textView2.append(res.getString(1));
            textView2.setId(res.getInt(0));
            checkBox.setId(res.getInt(0));
            layout.addView(textlayout);

        }

        //hukugen.setOnClickListener();
    }
}
