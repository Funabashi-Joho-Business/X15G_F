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
    final ArrayList<Integer> id2 =  new ArrayList<Integer>();
    ArrayList<CheckBox> id3 =  new ArrayList<CheckBox>();


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
        Button sakuzyo = (Button)view.findViewById(R.id.button5);
        Button ikkatu = (Button)view.findViewById(R.id.button7);


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
            id3.add(checkBox);


            layout.addView(textlayout);


            ikkatu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = 0;
                    int y = 0;
                    //データベースに接続
                    TextDB db = new TextDB(getActivity());

                    //クエリーの発行
                    Cursor res = db.query("select id,name from GomiDB where id = (select max(id) from GomiDB);");
                    res.moveToNext();
                    int max = res.getInt(0);
                    db.close();

                    while (i < max) {
                        if(id3.get(i).isChecked() == false) {
                            id3.get(i).setChecked(true);
                            i++;
                            id2.add(i);
                        }else{
                               for(int x = 0;x < max;x++){
                                   if(id3.get(x).isChecked() == true) {
                                       y++;
                                   }
                               }
                            if(y == max) {
                                for (int a = 0; a < max; a++) {
                                    id3.get(a).setChecked(false);
                                }
                                id2.clear();
                                break;
                            }
                            i++;
                        }
                    }
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkBox.isChecked() == true){
                    id2.add(v.getId());

                    }else if(checkBox.isChecked()  == false){
                        for(int x = 0;x < id2.size();x++){
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
                Cursor res = db.query("select id,name from GomiDB where id = "+id2.size()+";");
                res.moveToNext();
                int max = res.getInt(0);
                db.close();

                while (i < max){
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

                    id3.remove(id2.get(i));
                    i++;
                }
                id2.clear();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.faragment_area, new gomi2Fragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        sakuzyo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i = 0;
                //データベースに接続
                TextDB db = new TextDB(getActivity());

                //クエリーの発行
                Cursor res = db.query("select id,name from GomiDB where id = "+id2.size()+";");
                res.moveToNext();
                int max = res.getInt(0);
                db.close();

                while (i < max){
                    TextDB db2 = new TextDB(getActivity());
                    db2.exec("delete from GomiDB where id="+id2.get(i)+";");
                    db2.close();

                    id3.remove(id2.get(i));
                    i++;
                }
                id2.clear();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.faragment_area, new gomi2Fragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}
