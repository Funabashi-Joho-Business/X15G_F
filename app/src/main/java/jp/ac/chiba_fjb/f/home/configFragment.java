package jp.ac.chiba_fjb.f.home;


import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
        getActivity().setTitle("設定");
        super.onViewCreated(view, savedInstanceState);
        final CheckBox chkbox = (CheckBox) view.findViewById(R.id.checkBox);
        chkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
                builder.setSmallIcon(R.drawable.ic_launther);  // 左端に表示されるアイコン
                builder.setLargeIcon(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.lp_ic_alpha_only));
                builder.setColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                builder.setContentTitle("だいち"); // 通知のタイトル
                builder.setContentText("タップで表示");  // 通知メッセージ
                // 通知を通知バーから削除できなくする
                builder.setOngoing(true);
                NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                int notifyID = 1;
              if(chkbox.isChecked() == true){
                  // チェックされた状態の時の処理を記述
                  // NotificationManager.notify()に、通知IDとNotificationを渡すことで、通知が表示される
                  manager.notify(notifyID, builder.build());
                  Toast.makeText(getActivity(), "通知をONにしました", Toast.LENGTH_LONG).show();
                  // Inflate the layout for this fragment
              }else{
                  // チェックされていない状態の時の処理を記述
                  // 引数falseで呼び出せば削除できるようになる
                  manager.cancel(notifyID);
                  Toast.makeText(getActivity(), "通知をOFFにしました", Toast.LENGTH_LONG).show();
              }
            }

        });
    }

}
