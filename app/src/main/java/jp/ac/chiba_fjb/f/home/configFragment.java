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
import android.widget.CheckBox;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class configFragment extends Fragment {


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

        //チェック状態確認
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        chkbox.setChecked(sp.getBoolean("Check", Boolean.parseBoolean(null)));

        chkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(getActivity(),0,notificationIntent,0);
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
    }

}
