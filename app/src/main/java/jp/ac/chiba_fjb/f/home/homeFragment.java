package jp.ac.chiba_fjb.f.home;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment  {


    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button kyouyubutton = (Button)view.findViewById(R.id.kyouyubutton);
        Button teikeibunbutton = (Button)view.findViewById(R.id.teikeibunbutton);
        ImageButton gomibakobutton = (ImageButton)view.findViewById(R.id.gomibakobutton);
        //インスタンスの取得
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.layout5);

        //データベースに接続
        TextDB db = new TextDB(getActivity());

        //クエリーの発行
        Cursor res = db.query("select * from TextDB;");
        //データがなくなるまで次の行へ
        while(res.moveToNext())
        {
            //0列目を取り出し
            TextView textView = new TextView(getActivity());
            textView.append(res.getString(0)+"\n");
            layout.addView(textView);
        }
        //カーソルを閉じる
        res.close();
        //データベースを閉じる
        db.close();


        kyouyubutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.faragment_area, new kyoyuFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        teikeibunbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.faragment_area, new teikeibunFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        gomibakobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.faragment_area, new gomi2Fragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }
}
