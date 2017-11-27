package jp.ac.chiba_fjb.f.home;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static jp.ac.chiba_fjb.f.home.R.id.TextView;
import static jp.ac.chiba_fjb.f.home.R.id.menu1;
import static jp.ac.chiba_fjb.f.home.R.id.menu2;
import static jp.ac.chiba_fjb.f.home.R.id.menu3;
import static jp.ac.chiba_fjb.f.home.R.id.menu4;
import static jp.ac.chiba_fjb.f.home.R.id.menu5;


public class MainActivity extends AppCompatActivity{
    private static String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_input_add);

        homeFragment fragment = new homeFragment();

        mId = "";








        //フラグメント表示
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.faragment_area, fragment);
        ft.commit();

    }

    //メニュー機能
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.humbergur, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //メニュー機能(右)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        homeFragment fragment = new homeFragment();


        switch (item.getItemId()) {
            case menu1:
                mId = "menu1";
                setTitle("だいちのはさみ");
                Toast.makeText(MainActivity.this, "だいちのはさみモード", Toast.LENGTH_SHORT).show();
                return true;

            case menu2:
                setTitle("編集");
                mId = "menu2";
                Toast.makeText(MainActivity.this, "だいち編集モード", Toast.LENGTH_SHORT).show();
                return true;

            case menu3:
                setTitle("痴漢");
                mId = "menu3";
                return true;

            case menu4:
                setTitle("同期");
                return true;

            case menu5:
                ft.replace(R.id.faragment_area, new configFragment());
                ft.addToBackStack(null);
                ft.commit();
                return true;
        }

        //メニュー機能(左)
        if (android.R.id.home == item.getItemId()) {
            final EditText editView = new EditText(MainActivity.this);
            final LinearLayout layout = (LinearLayout)findViewById(R.id.layout4);
            new AlertDialog.Builder(this)
                    .setTitle("新規テキスト入力")
                    .setView(editView)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String str = editView.getText().toString();
                            //データベースに接続
                            TextDB db = new TextDB(MainActivity.this);
                            //データの挿入
                            db.exec("insert into TextDB(name) values('"+str+"');");

                            //クエリーの発行
                            Cursor res = db.query("select id,name from TextDB where id = (select max(id) from TextDB);");

                            //データがなくなるまで次の行へ
                            if (res.moveToNext()) {
                                //0列目を取り出し
                                final LinearLayout textlayout;
                                textlayout = (LinearLayout)getLayoutInflater().inflate(R.layout.text, null);
                                TextView textView = (TextView)textlayout.findViewById(R.id.textView);
                                ImageButton imageButton = (ImageButton)textlayout.findViewById(R.id.sakuzyo);
                                textView.append(res.getString(1));
                                textView.setId(res.getInt(0));
                                imageButton.setId(res.getInt(0));
                                layout.addView(textlayout);



                                imageButton.setOnClickListener(new View.OnClickListener() {
                                    homeFragment fragment = new homeFragment();

                                    @Override
                                    public void onClick(View v) {
                                        int id = v.getId();
                                        TextDB db = new TextDB(MainActivity.this);
                                        db.exec("delete from TextDB where id="+id+";");
                                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                        ft.replace(R.id.faragment_area, fragment);
                                        ft.addToBackStack(null);
                                        ft.commit();
                                        db.close();


                                    }
                                });

                                textView.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(final View v) {
                                        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                        String strValue01 = mId;
                                        final int id = v.getId();

                                        switch (strValue01) {
                                            case "menu1":
                                                TextView textview2 = (TextView)findViewById(id);
                                                String cliptext = textview2.getText().toString();

                                                //クリップボードに格納するItemを作成
                                                ClipData.Item item = new ClipData.Item(cliptext);

                                                //MIMETYPEの作成
                                                String[] mimeType = new String[1];
                                                mimeType[0] = ClipDescription.MIMETYPE_TEXT_URILIST;

                                                //クリップボードに格納するClipDataオブジェクトの作成
                                                ClipData cd = new ClipData(new ClipDescription("text_data", mimeType), item);

                                                //クリップボードにデータを格納
                                                ClipboardManager cm = (ClipboardManager) MainActivity.this.getSystemService(CLIPBOARD_SERVICE);
                                                cm.setPrimaryClip(cd);

                                                Toast.makeText(MainActivity.this, "「" + cliptext + "」をコピーしました", Toast.LENGTH_SHORT).show();
                                                break;

                                            case "menu2":
                                                final EditText editView2 = new EditText(MainActivity.this);
                                                final LinearLayout layout = (LinearLayout)findViewById(R.id.layout4);
                                                //データベースに接続
                                                TextDB db = new TextDB(MainActivity.this);
                                                //データの取得
                                                Cursor res = db.query("select id,name from TextDB where id = "+id+";");
                                                res.moveToNext();

                                                editView2.setText(res.getString(1));
                                                db.close();
                                                new AlertDialog.Builder(MainActivity.this)
                                                        .setTitle("編集")
                                                        .setView(editView2)
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                homeFragment fragment = new homeFragment();
                                                                String str2 = editView2.getText().toString();

                                                                //データベースに接続
                                                                TextDB db = new TextDB(MainActivity.this);
                                                                db.exec("update TextDB set name = '"+str2+"' where id="+id+";");

                                                                ft.replace(R.id.faragment_area, fragment);
                                                                ft.addToBackStack(null);
                                                                ft.commit();
                                                                db.close();


                                                            }
                                                        })
                                                        .setNegativeButton("キャンセル",null)
                                                        .show();
                                                break;

                                            default:
                                                Toast.makeText(MainActivity.this, "選択してください", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                });


                            }

                            //カーソルを閉じる
                            res.close();

                            //データベースを閉じる
                            db.close();

                        }
                    })

                    .setNegativeButton("キャンセル",null)
                    .show();
        }


        return super.onOptionsItemSelected(item);
    }

    //バックボタン処理
    @Override
    public void onBackPressed() {
        int backStackCnt = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCnt != 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public String getmId(){
        return mId;
    }

}