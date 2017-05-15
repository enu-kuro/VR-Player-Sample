package jp.puddingheart.hd.nkvrplayer;

import android.os.Bundle;
import android.view.MotionEvent;

import org.gearvrf.GVRActivity;

public class MainActivity extends GVRActivity {
    private Main mMain = null;
    private long lastDownTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMain = new Main(this);
        setMain(mMain);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMain.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        mMain.onTouch();
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            lastDownTime = event.getDownTime();
        }

        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (event.getEventTime() - lastDownTime < 200) {
                mMain.onTap();
            }
        }
        return true;
    }
}
