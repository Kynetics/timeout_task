package com.kynetics.android.timeout;

import android.os.Handler;
import android.os.SystemClock;

/**
 * Created by andrea on 19/12/16.
 */

public final class TimeoutTask {

    public static TimeoutTask newInstance(Handler handler, long timeout, Runnable task) {
        return new TimeoutTask(handler, timeout, task);
    }

    public static TimeoutTask newInstance(long timeout, Runnable task) {
        return newInstance(new Handler(), timeout, task);
    }

    public final void start() {
        if(!isRunning()){
            this.lastTouch = SystemClock.uptimeMillis();
            this.handler.postAtTime(task, this.lastTouch + this.timeout);
        }
    }

    public final void stop() {
        if(isRunning()) {
            this.handler.removeCallbacks(task);
            this.lastTouch = -1;
        }
    }

    public final void delay() {
        if(isRunning()) {
            this.lastTouch = SystemClock.uptimeMillis();
        }
    }

    private TimeoutTask(Handler _handler, long _timeout, final Runnable _task) {
        this.handler = _handler;
        this.timeout = _timeout;
        this.task = new Runnable() {
            @Override
            public final void run() {
                final long now = SystemClock.uptimeMillis();
                if(now - lastTouch >= timeout) {
                    _task.run();
                    lastTouch = -1L;
                } else {
                    handler.postAtTime(task, lastTouch + timeout);
                }
            }
        };
    }

    public boolean isRunning() {
        return lastTouch >= 0;
    }

    private final Handler handler;

    private final long timeout;

    private final Runnable task;

    private long lastTouch = -1L;

}