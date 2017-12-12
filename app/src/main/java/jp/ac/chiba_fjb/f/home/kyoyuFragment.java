package jp.ac.chiba_fjb.f.home;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class kyoyuFragment extends Fragment {

    private  String strValue01;
    private  static  String sqlstr;


    public kyoyuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.kyoyu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton homebutton = (ImageButton) view.findViewById(R.id.homebutton);
        Button teikeibunbutton = (Button)view.findViewById(R.id.teikeibunbutton);
        ImageButton gomibakobutton = (ImageButton)view.findViewById(R.id.gomibakobutton);

        final kyoyuFragment fragment = new kyoyuFragment();

        ArrayList<String> a = new ArrayList<String>();
        ArrayList<Integer> b = new ArrayList<Integer>();

        //インスタンスの取得
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.layout4);

        //データベースに接続
        TextDB db = new TextDB(getActivity());

        //クエリーの発行
        Cursor res = db.query("select id,name,name2 from KyoyuDB;");
        //データがなくなるまで次の行へ
        while(res.moveToNext()) {

           if(!a.contains(res.getString(1))) {
               a.add(res.getString(1));
                LinearLayout textlayout;
                textlayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.text4, null);
                final TextView textView = (TextView) textlayout.findViewById(R.id.textView);
               textView.setTypeface(Typeface.DEFAULT_BOLD);



                //0列目を取り出し
                textView.append(res.getString(1));
                textView.setId(res.getInt(0));

                layout.addView(textlayout);


                textView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String str;
                        final int id = v.getId();

                        //インスタンスの取得
                        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout4);

                        //データベースに接続
                        TextDB db = new TextDB(getActivity());

                        //クエリーの発行
                        Cursor res = db.query("select id,name,name2 from KyoyuDB where id = " + id + ";");
                        res.moveToNext();
                        sqlstr = res.getString(1);
                        System.out.println(sqlstr);
                        db.close();

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.faragment_area, new kyoyu2Fragment());
                        ft.addToBackStack(null);
                        ft.commit();

                        //カーソルを閉じる
                        res.close();
                        //データベースを閉じる
                        db.close();
                    }
                });

            }

        }
        //カーソルを閉じる
        res.close();
        //データベースを閉じる
        db.close();


        homebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                homeFragment fragment = new homeFragment();
                ft.replace(R.id.faragment_area, fragment);
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

    public String getSqlstr(){
        return sqlstr;
    }
}
