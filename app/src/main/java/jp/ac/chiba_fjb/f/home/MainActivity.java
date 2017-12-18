package jp.ac.chiba_fjb.f.home;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.ac.chiba_fjb.f.home.google.GoogleAccount;
import jp.ac.chiba_fjb.f.home.google.SpreadSheet;

import static com.google.common.collect.ComparisonChain.start;
import static jp.ac.chiba_fjb.f.home.R.id.TextView;
import static jp.ac.chiba_fjb.f.home.R.id.menu1;
import static jp.ac.chiba_fjb.f.home.R.id.menu2;
import static jp.ac.chiba_fjb.f.home.R.id.menu3;
import static jp.ac.chiba_fjb.f.home.R.id.menu4;
import static jp.ac.chiba_fjb.f.home.R.id.menu5;


public class MainActivity extends AppCompatActivity{
    private static String mId;
    private SpreadSheet mSheet;
    Handler mHandler = new Handler();
    final static int OVERLAY_PERMISSION_REQ_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_input_add);

        homeFragment fragment = new homeFragment();

        mId = "menu1";

        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }
        else
            startService(new Intent(this, LayerService.class).setAction("START"));

        //スプレットシートの生成
        mSheet = new SpreadSheet(this);
//        mSheet.resetAccount();
        mSheet.execute(new GoogleAccount.GoogleRunnable() {
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void run() throws IOException {
                //スプレッドシートの作成
                String id = mSheet.create("/だいち共有用/SpreadSheet");

                if(id != null){
                    //データの書き込み
                    Object[][] values = {{"カテゴリー↓","テキスト↓","※1カテゴリー未入力の場合は「その他」に入ります。\n"+"※2上から順番に入力してください。"}};
                    mSheet.setRange(id,values);

                    //全データの取得
                    List<List<Object>> data = mSheet.getRange(id);
                    System.out.println(data);
                }
            }
        });



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
        final homeFragment fragment = new homeFragment();


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
                setTitle("置換");
                mId = "menu3";
                Toast.makeText(MainActivity.this, "だいち置換モード", Toast.LENGTH_SHORT).show();
                return true;

            case menu4:
                mSheet.execute(new GoogleAccount.GoogleRunnable() {
                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void run() throws IOException {
                        //スプレッドシートの作成
                        String id = mSheet.create("/だいち共有用/SpreadSheet");



                        if(id != null){
                            //全データの取得
                            final List<List<Object>> data = mSheet.getRange(id);
                            System.out.println(data);
                            System.out.println(data.size());
                            System.out.println(data.get(0).size());
                            //データベースに接続
                            TextDB db = new TextDB(MainActivity.this);
                            Cursor res = db.query("select max(id) from KyoyuDB;");
                            System.out.println(res);
                            if(res != null) {
                                res.moveToNext();
                                int max = res.getInt(0);
                                db.close();
                                TextDB db2 = new TextDB(MainActivity.this);
                                for (int i = 1; i <= max; i++) {
                                    db2.exec("delete from KyoyuDB where id=" +i+ ";");
                                }
                                db2.close();
                            }
                            TextDB db3 = new TextDB(MainActivity.this);
                            for(int i=1;i<data.size();i++){
                                String str3 = (String) data.get(i).get(0);
                                if(str3.isEmpty()){
                                    db3.exec("insert into KyoyuDB(name,name2) values('その他','"+data.get(i).get(1)+"');");
                                }else {
                                    //データの挿入
                                    db3.exec("insert into KyoyuDB(name,name2) values('" + data.get(i).get(0) + "','" + data.get(i).get(1) + "');");
                                }

                            }
                            db3.close();

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    String str = (String)data.get(0).get(0);
                                    String str1 = (String)data.get(1).get(1);
                                    System.out.println(str);
                                    ft.replace(R.id.faragment_area, new kyoyuFragment());
                                    ft.addToBackStack(null);
                                    ft.commit();
                                    Toast.makeText(MainActivity.this,"同期に成功しました", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                });
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
            final LinearLayout layout = (LinearLayout)findViewById(R.id.layout6);
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
                                try{
                                    layout.addView(textlayout);
                                }catch (NullPointerException e){
                                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.faragment_area, new homeFragment());
                                    ft.addToBackStack(null);
                                    ft.commit();
                                }




                                imageButton.setOnClickListener(new View.OnClickListener() {
                                    homeFragment fragment = new homeFragment();

                                    @Override
                                    public void onClick(View v) {
                                        int id = v.getId();
                                        //データベースに接続
                                        TextDB db = new TextDB(MainActivity.this);
                                        Cursor res = db.query("select id,name from TextDB where id = "+id+";");
                                        res.moveToNext();
                                        String mGomi = res.getString(1);
                                        db.close();

                                        //データベースに接続
                                        TextDB db2 = new TextDB(MainActivity.this);
                                        db2.exec("insert into GomiDB(name) values('"+mGomi+"');");
                                        db2.exec("delete from TextDB where id="+id+";");
                                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                        ft.replace(R.id.faragment_area, fragment);
                                        ft.addToBackStack(null);
                                        ft.commit();
                                        db2.close();


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
                                                final LinearLayout layout = (LinearLayout)findViewById(R.id.layout6);
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

//                                            case "menu3":
//                                                final EditText editView3 = new EditText(MainActivity.this);
//                                                final EditText editView4 = new EditText(MainActivity.this);
//                                                final LinearLayout layout2 = (LinearLayout)findViewById(R.id.layout4);
//                                                //データベースに接続
//                                                TextDB db2 = new TextDB(MainActivity.this);
//                                                //データの取得
//                                                Cursor res2 = db2.query("select id,name from TextDB where id = "+id+";");
//                                                res2.moveToNext();
//
//                                                String str3 = (res2.getString(1));
//                                                db2.close();
//                                                new AlertDialog.Builder(MainActivity.this)
//                                                        .setTitle("置換")
//                                                        //.setMessage(str3)
//                                                        .setView(editView3)
//                                                        //.setMessage("")
//                                                        .setView(editView4)
//                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                homeFragment fragment = new homeFragment();
//                                                                String str4 = editView3.getText().toString();
//                                                                String str5 = editView4.getText().toString();
//
//
//                                                                //データベースに接続
//                                                                TextDB db = new TextDB(MainActivity.this);
//                                                                db.exec("select replace(name,'"+str4+"','"+str5+"') from TextDB where id="+id+";");
//                                                                //SELECT REPLACE(カラム名,'置換対象','置換後の文字') FROM テーブル名;
//
//                                                                ft.replace(R.id.faragment_area, fragment);
//                                                                ft.addToBackStack(null);
//                                                                ft.commit();
//                                                                db.close();
//
//
//                                                            }
//                                                        })
//                                                        .setNegativeButton("キャンセル",null)
//                                                        .show();
//                                                break;

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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.faragment_area, new homeFragment());
        ft.addToBackStack(null);
        ft.commit();
//        int backStackCnt = getSupportFragmentManager().getBackStackEntryCount();
//        if (backStackCnt != 0) {
//            getSupportFragmentManager().popBackStack();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                startService(new Intent(this, LayerService.class).setAction("START"));

            }
        }

        //認証許可情報を設定
        mSheet.onActivityResult(requestCode, resultCode, data);

    }


    public String getmId(){
        return mId;
    }


}