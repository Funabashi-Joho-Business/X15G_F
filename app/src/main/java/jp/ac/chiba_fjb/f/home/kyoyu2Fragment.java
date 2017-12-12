package jp.ac.chiba_fjb.f.home;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.database.Cursor;
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

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class kyoyu2Fragment extends Fragment implements View.OnClickListener {

    private String strValue01;
    private String strValue02;


    public kyoyu2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton homebutton = (ImageButton) view.findViewById(R.id.homebutton);
        Button teikeibunbutton = (Button) view.findViewById(R.id.teikeibunbutton);
        ImageButton gomibakobutton = (ImageButton) view.findViewById(R.id.gomibakobutton);
        Button kyouyubutton = (Button)view.findViewById(R.id.kyouyubutton);
        strValue02 = new kyoyuFragment().getSqlstr();

        final kyoyu2Fragment fragment = new kyoyu2Fragment();


        //インスタンスの取得
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout4);

        //データベースに接続
        TextDB db = new TextDB(getActivity());

        //クエリーの発行
        Cursor res = db.query("select id,name,name2 from KyoyuDB where name = '" + strValue02 + "';");
        //データがなくなるまで次の行へ
        while (res.moveToNext()) {
            LinearLayout textlayout;
            textlayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.text3, null);
            final TextView textView = (TextView) textlayout.findViewById(R.id.textView);

            //0列目を取り出し
            textView.append(res.getString(2));
            textView.setId(res.getInt(0));

            layout.addView(textlayout);

            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    strValue01 = new MainActivity().getmId();
                    final int id = v.getId();
                    switch (strValue01) {
                        case "menu1":
                            TextView textview2 = (TextView) view.findViewById(id);
                            String cliptext = textview2.getText().toString();

                            //クリップボードに格納するItemを作成
                            ClipData.Item item = new ClipData.Item(cliptext);

                            //MIMETYPEの作成
                            String[] mimeType = new String[1];
                            mimeType[0] = ClipDescription.MIMETYPE_TEXT_URILIST;

                            //クリップボードに格納するClipDataオブジェクトの作成
                            ClipData cd = new ClipData(new ClipDescription("text_data", mimeType), item);

                            //クリップボードにデータを格納
                            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                            cm.setPrimaryClip(cd);

                            Toast.makeText(getActivity(), "「" + cliptext + "」をコピーしました", Toast.LENGTH_SHORT).show();
                            break;

                        case "menu2":
                            final EditText editView2 = new EditText(getActivity());
                            final LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout4);
                            //データベースに接続
                            TextDB db = new TextDB(getActivity());
                            //データの取得
                            Cursor res = db.query("select id,name,name2 from KyoyuDB where id = " + id + ";");
                            res.moveToNext();

                            editView2.setText(res.getString(2));
                            db.close();
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("編集")
                                    .setView(editView2)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            kyoyuFragment fragment = new kyoyuFragment();
                                            String str2 = editView2.getText().toString();

                                            //データベースに接続
                                            TextDB db = new TextDB(getActivity());
                                            db.exec("update kyoyuDB set name2 = '" + str2 + "' where id=" + id + ";");

                                            ft.replace(R.id.faragment_area, fragment);
                                            ft.addToBackStack(null);
                                            ft.commit();
                                            db.close();


                                        }
                                    })
                                    .setNegativeButton("キャンセル", null)
                                    .show();
                            break;


                        default:
                            Toast.makeText(getActivity(), "選択してください", Toast.LENGTH_SHORT).show();
                            break;
                    }

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.faragment_area, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

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

        kyouyubutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.faragment_area, new kyoyuFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
