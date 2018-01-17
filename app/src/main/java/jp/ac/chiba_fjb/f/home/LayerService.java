package jp.ac.chiba_fjb.f.home;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


public class LayerService extends Service implements View.OnTouchListener {
    private View mView;
    private LinearLayout droplayer;
    private WindowManager.LayoutParams promisu;
    private int LAYOUT_FLAG;
    private int oldx;
    private int oldy;
    private Point desplaysize;
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
        droplayer.setOnTouchListener(this);
        //イベントサンプル
        mView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = mView.findViewById(R.id.textView);
                //textView.setText("ボタンが押されました");
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
        super.onCreate();
        System.out.println("サービス開始");



    }

    @Override
    public void onDestroy() {
        System.out.println("サービス停止");
        removeLayer();

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showLayer();
        if(intent!=null) {
            if (intent.getAction() == null)
                return super.onStartCommand(intent, flags, startId);
            ;
            switch (intent.getAction()) {
                case "START":
                    break;
                case "STOP":
                    break;
            }
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
