package jp.ac.chiba_fjb.f.home;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_input_add);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.faragment_area,new teikeibunFragment());
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.humbergur, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {
            new AlertDialog.Builder(this)
                .setTitle("メッセージ")
                .setMessage("アイコンタップ")
                .setPositiveButton("OK", null)
                .show();
        }
        return super.onOptionsItemSelected(item);
    }


}
