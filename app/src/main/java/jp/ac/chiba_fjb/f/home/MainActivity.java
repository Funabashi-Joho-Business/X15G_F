package jp.ac.chiba_fjb.f.home;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static jp.ac.chiba_fjb.f.home.R.id.TextView;
import static jp.ac.chiba_fjb.f.home.R.id.menu1;
import static jp.ac.chiba_fjb.f.home.R.id.menu2;
import static jp.ac.chiba_fjb.f.home.R.id.menu3;
import static jp.ac.chiba_fjb.f.home.R.id.menu4;
import static jp.ac.chiba_fjb.f.home.R.id.menu5;


public class MainActivity extends AppCompatActivity {
    public String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_input_add);



    //フラグメント表示
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.faragment_area, new homeFragment());
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

        switch (item.getItemId()) {
            case menu1:
                setTitle("だいちのはさみ");
                return true;

            case menu2:
                setTitle("編集");
                return true;

            case menu3:
                setTitle("痴漢");
                return true;

            case menu4:
                setTitle("同期");
                return true;

            case menu5:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.faragment_area, new configFragment());
                ft.addToBackStack(null);
                ft.commit();
                return true;
        }

        //メニュー機能(左)
        if (android.R.id.home == item.getItemId()) {
            final EditText editView = new EditText(MainActivity.this);
            final TextView textView = (TextView) findViewById(R.id.edittext);
            new AlertDialog.Builder(this)
                    .setTitle("新規テキスト入力")
                    .setView(editView)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            str = editView.getText().toString();
                            //データベースに接続
                            TextDB db = new TextDB(this);
                            //データの挿入
                            db.exec("insert into test values('" + str + "');");

                            //クエリーの発行
                            Cursor res = db.query("select * from TextDB;");

                            //データがなくなるまで次の行へ
                            while (res.moveToNext()) {
                                //0列目を取り出し
                                textView.append(res.getString(0) + "\n");
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
}