package jp.ac.chiba_fjb.f.home;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import jp.ac.chiba_fjb.f.home.google.GoogleAccount;
import jp.ac.chiba_fjb.f.home.google.SpreadSheet;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class configFragment extends Fragment {
    private SpreadSheet mSheet;
    private static int radioId = 2;


    public configFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.config, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final CheckBox chkbox = (CheckBox) view.findViewById(R.id.checkBox);
        final LayerService layerService = new LayerService();
        RadioGroup radioGroup =(RadioGroup)view.findViewById(R.id.radiogroup);
        final RadioButton radioButton =(RadioButton)view.findViewById(R.id.radioButton);
        RadioButton radioButton2 =(RadioButton)view.findViewById(R.id.radioButton2);
        RadioButton radioButton3 =(RadioButton)view.findViewById(R.id.radioButton3);
        Button btn = (Button)view.findViewById(R.id.button3);

        //チェック状態確認
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        chkbox.setChecked(sp.getBoolean("Check", Boolean.parseBoolean(null)));

        chkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent notificationIntent = new Intent(getActivity(),LayerService.class);
                PendingIntent contentIntent = PendingIntent.getService(getActivity(),0,notificationIntent,0);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
                // 左端に表示されるアイコン
                builder.setSmallIcon(R.drawable.ic_launther);
                //タスクバー表示アイコン
                builder.setLargeIcon(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.lp_ic_alpha_only));
                //背景カラー
                builder.setColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                // 通知のタイトル
                builder.setContentTitle("だいち");
                // 通知メッセージ
                builder.setContentText("タップで表示");
                // 通知を通知バーから削除できなくする
                builder.setOngoing(true);
                builder.setContentIntent(contentIntent);
                NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                int notifyID = 1;
              if(chkbox.isChecked() == true){
                  // チェックされた状態の時の処理を記述
                  // NotificationManager.notify()に、通知IDとNotificationを渡すことで、通知が表示される
                  manager.notify(notifyID, builder.build());
                  Toast.makeText(getActivity(), "通知をONにしました", Toast.LENGTH_LONG).show();
                  //チェック状態保存
                  Boolean Check = chkbox.isChecked();
                  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                  sp.edit().putBoolean("Check", Check).commit();
                  // Inflate the layout for this fragment
              }else{
                  // チェックされていない状態の時の処理を記述
                  manager.cancel(notifyID);
                  Toast.makeText(getActivity(), "通知をOFFにしました", Toast.LENGTH_LONG).show();
                  //チェック状態保存
                  Boolean Check = chkbox.isChecked();
                  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                  sp.edit().putBoolean("Check", Check).commit();
              }
            }

        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                switch (checkedId){
                    default:
                        break;
                }
            }
            });
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //スプレットシートの生成
                mSheet = new SpreadSheet(getActivity());
                mSheet.resetAccount();
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

            }
        });
    }

}
