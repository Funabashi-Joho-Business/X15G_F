package jp.ac.chiba_fjb.f.home;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
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
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;


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
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("だいちのはさみ");
        super.onViewCreated(view, savedInstanceState);
        Button kyouyubutton = (Button)view.findViewById(R.id.kyouyubutton);
        Button teikeibunbutton = (Button)view.findViewById(R.id.teikeibunbutton);
        ImageButton gomibakobutton = (ImageButton)view.findViewById(R.id.gomibakobutton);
        //インスタンスの取得
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.layout4);

        //データベースに接続
        TextDB db = new TextDB(getActivity());

        //クエリーの発行
        Cursor res = db.query("select id,name from TextDB;");
        //データがなくなるまで次の行へ
        while(res.moveToNext())
        {
            LinearLayout textlayout;
            textlayout = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.text, null);
            final TextView textView = (TextView)textlayout.findViewById(R.id.textView);
            ImageButton imageButton = (ImageButton)textlayout.findViewById(R.id.sakuzyo);

            //0列目を取り出し
            textView.append(res.getString(1));
            textView.setId(res.getInt(0));
            imageButton.setId(res.getInt(0));
            layout.addView(textlayout);

            imageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    TextDB db = new TextDB(getActivity());
                    db.exec("delete from TextDB where id="+id+";");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.faragment_area, new homeFragment());
                    ft.addToBackStack(null);
                    ft.commit();
                    db.close();


                    }
            });

            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    TextView textview2 = (TextView)view.findViewById(v.getId());
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

                        Toast.makeText(getActivity(), "「"+cliptext+"」をコピーしました", Toast.LENGTH_LONG).show();
                }
            });

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
