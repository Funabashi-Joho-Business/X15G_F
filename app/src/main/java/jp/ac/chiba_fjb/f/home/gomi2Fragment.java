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

import java.util.ArrayList;


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
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button hukugen = (Button)view.findViewById(R.id.button4);

        final ArrayList<Integer> id2 =  new ArrayList<Integer>();


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
            final CheckBox checkBox = (CheckBox) textlayout.findViewById(R.id.checkBox2);

            //0列目を取り出し
            textView2.append(res.getString(1));
            textView2.setId(res.getInt(0));
            checkBox.setId(res.getInt(0));
            layout.addView(textlayout);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkBox.isChecked() == true){
                    id2.add(v.getId());

                    }else if(checkBox.isChecked()  == false){
                        for(int x = 0;x <= id2.size();x++){
                            if(id2.get(x) == v.getId()) id2.remove(x);
                        }
                    }

                }
            });

        }

        hukugen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int i = 0;
                //データベースに接続
                TextDB db = new TextDB(getActivity());

                //クエリーの発行
                Cursor res = db.query("select id,name from GomiDB where id = (select max(id) from GomiDB);");
                res.moveToNext();
                int max = res.getInt(0);
                db.close();

                while (i < max){
                    int y = id2.get(i);
                    //データベースに接続
                    TextDB db2 = new TextDB(getActivity());
                    Cursor res2 = db2.query("select id,name from GomiDB where id = "+id2.get(i)+";");
                    res2.moveToNext();
                    String mText = res2.getString(1);
                    db2.close();

                    TextDB db3 = new TextDB(getActivity());
                    db3.exec("insert into TextDB(name) values('"+mText+"');");
                    db3.exec("delete from GomiDB where id="+id2.get(i)+";");
                    db3.close();
                    i++;
//                    CheckBox checkBox2 = (CheckBox) view.findViewById(id2.get(i));
//                    if(checkBox2.isChecked() == true){
//                        //データベースに接続
//                        TextDB db2 = new TextDB(getActivity());
//
//                        //データの取得
//                        Cursor res2 = db.query("select id,name from TextDB where id = "+i+";");
//                        res.moveToNext();
//
//                        String mGomi = res.getString(1);
//                        db.close();
//
//                        //データベースに接続
//                        TextDB db3 = new TextDB(getActivity());
//                        db3.exec("insert into TextDB(name) values('"+mGomi+"');");
//                        db3.exec("delete from GomiDB where id="+i+";");
//                        db3.close();
//                        i++;
//
//                    }
                }
                gomi2Fragment gomifra = new gomi2Fragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.faragment_area, gomifra);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}
