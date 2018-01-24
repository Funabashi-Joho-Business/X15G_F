package jp.ac.chiba_fjb.f.home;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class LayerService extends Service implements View.OnTouchListener {
    private View mView;
    private View textlayout;
    private LinearLayout droplayer;
    private WindowManager.LayoutParams promisu;
    private int LAYOUT_FLAG;
    private int oldx;
    private int oldy;
    private Point desplaysize;
    private  MainActivity main = new MainActivity();
    private int count;
    public LayerService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    void removeLayer(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.removeView(mView);
    }
    void showLayer(){
        // Viewからインフレータを作成する
        final LayoutInflater layoutInflater = LayoutInflater.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        // 重ね合わせするViewの設定を行う
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
              WindowManager.LayoutParams.WRAP_CONTENT,
              WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                  WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED|
                  WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|
                  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                      | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
              PixelFormat.TRANSPARENT);

        promisu =params;


        // WindowManagerを取得する
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // レイアウトファイルから重ね合わせするViewを作成する
        mView = layoutInflater.inflate(R.layout.layer, null);
        droplayer = mView.findViewById(R.id.layerlayout);
//        droplayer.getLayoutParams().width = 750;
//        droplayer.getLayoutParams().height = 1000;
        droplayer.getLayoutParams().width = 300;
        droplayer.getLayoutParams().height = 400;
        droplayer.requestLayout();
        droplayer.setOnTouchListener(this);
        //インスタンスの取得
        LinearLayout layout = (LinearLayout)mView.findViewById(R.id.layout8);

        //データベースに接続
        TextDB db = new TextDB(LayerService.this);

        //クエリーの発行
        Cursor res = db.query("select id,name from TextDB;");
        while(res.moveToNext()) {
            textlayout = layoutInflater.inflate(R.layout.text5, null);
            final TextView textView = (TextView) textlayout.findViewById(R.id.textView);

            //0列目を取り出し
            textView.append(res.getString(1));
            textView.setId(res.getInt(0));
            layout.addView(textlayout);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int id = v.getId();
                    TextView textview2 = (TextView) mView.findViewById(id);
                    String cliptext = textview2.getText().toString();

                    //クリップボードに格納するItemを作成
                    ClipData.Item item = new ClipData.Item(cliptext);

                    //MIMETYPEの作成
                    String[] mimeType = new String[1];
                    mimeType[0] = ClipDescription.MIMETYPE_TEXT_URILIST;

                    //クリップボードに格納するClipDataオブジェクトの作成
                    ClipData cd = new ClipData(new ClipDescription("text_data", mimeType), item);

                    //クリップボードにデータを格納
                    ClipboardManager cm = (ClipboardManager) LayerService.this.getSystemService(CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(cd);

                    Toast.makeText(LayerService.this, "「" + cliptext + "」をコピーしました", Toast.LENGTH_SHORT).show();
                }
            });

            db.close();
        }
        //イベントサンプル
        mView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = mView.findViewById(R.id.textView);
                //textView.setText("ボタンが押されました");
                count = 0;
                removeLayer();



            }
        });

        //レイアウトのサイズを確定させる
        mView.measure(-1,-1);
        // Viewを画面上に重ね合わせする
        wm.addView(mView, params);

        //レイアウトサイズの取得
        int width = mView.getMeasuredWidth();
        int height = mView.getMeasuredHeight();

        //スクリーンサイズの取得
        Point screenSize = new Point();
        wm.getDefaultDisplay().getSize(screenSize);

        desplaysize = screenSize;

        //0,0が中心位置の設定なので、左上を0,0に補正する為の値
        int baseX = -(screenSize.x-width)/2;
        int baseY = -(screenSize.y-height)/2;

        params.x = baseX + screenSize.x - width;
        params.y = baseY + screenSize.y - height*2;
        wm.updateViewLayout(mView, params);



    }
    @Override
    public void onCreate() {
        if(count != 1) {
            super.onCreate();
            System.out.println("サービス開始");
        }



    }

    @Override
    public void onDestroy() {
        System.out.println("サービス停止");

        removeLayer();

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(count != 1) {
            count = 1;
            showLayer();
//            if (intent != null) {
//                if (intent.getAction() == null)
//                    return super.onStartCommand(intent, flags, startId);
//                switch (intent.getAction()) {
//                    case "START":
//                        count = 1;
//                        break;
//                    case "STOP":
//                        break;
//                }
//            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        // WindowManagerを取得する
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        // タッチしている位置取得
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int left = x -(desplaysize.x / 2);
                int top = y -(desplaysize.y / 2);

                promisu.x = left;
                promisu.y = top+260;

                break;
        }
        wm.updateViewLayout(mView, promisu);

        // 今回のタッチ位置を保持
        oldx = x;
        oldy = y;
        // イベント処理完了
        return true;
    }
}
