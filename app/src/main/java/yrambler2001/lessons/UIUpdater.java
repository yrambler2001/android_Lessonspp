package yrambler2001.lessons;

import android.os.Handler;
import android.os.Looper;


public class UIUpdater {
    // Create a Handler that uses the Main Looper to run in
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Runnable mStatusChecker;
    private int UPDATE_INTERVAL = 1000;
    private Runnable runnable;

    UIUpdater() {
        mStatusChecker = new Runnable() {
            @Override
            public void run() {
                runnable.run();
                mHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
    }

    synchronized void startUpdates() {
        mStatusChecker.run();
    }

    UIUpdater setRunnable(final Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public UIUpdater setDelay(int delay) {
        this.UPDATE_INTERVAL = delay;
        return this;
    }

    public synchronized void reUpdate() {
        mHandler.post(this.runnable);
    }

    public synchronized void stopUpdates() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}