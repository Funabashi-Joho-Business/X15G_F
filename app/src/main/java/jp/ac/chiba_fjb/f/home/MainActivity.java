package jp.ac.chiba_fjb.f.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

import static jp.ac.chiba_fjb.f.home.R.id.menu1;
import static jp.ac.chiba_fjb.f.home.R.id.menu2;
import static jp.ac.chiba_fjb.f.home.R.id.menu3;
import static jp.ac.chiba_fjb.f.home.R.id.menu4;
import static jp.ac.chiba_fjb.f.home.R.id.menu5;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_input_add);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.faragment_area, new homeFragment());
        ft.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.humbergur, menu);
        return super.onCreateOptionsMenu(menu);
    }


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

        if (android.R.id.home == item.getItemId()) {
            final EditText editView = new EditText(MainActivity.this);
            new AlertDialog.Builder(this)
                    .setTitle("新規テキスト入力")
                    .setView(editView)
                    .setPositiveButton("OK", null)
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