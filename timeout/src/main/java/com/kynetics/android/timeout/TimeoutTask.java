/**
 * Copyright (c) 2017-2018 Kynetics, LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
