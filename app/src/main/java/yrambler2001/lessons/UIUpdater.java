package yrambler2001.lessons;

import android.os.Handler;
import android.os.Looper;


public class UIUpdater {
    // Create a Handler that uses the Main Looper to run in
    public Handler mHandler = new Handler(Looper.getMainLooper());

    public Runnable mStatusChecker;
    private int UPDATE_INTERVAL = 1000;
    Runnable runnable;

    public UIUpdater() {
        mStatusChecker = new Runnable() {
            @Override
            public void run() {
                runnable.run();
                mHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
    }

    public synchronized void startUpdates() {
        mStatusChecker.run();
    }

    public UIUpdater setRunnable(final Runnable runnable) {
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