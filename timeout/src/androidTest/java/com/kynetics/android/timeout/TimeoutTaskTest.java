package com.kynetics.android.timeout;

import android.content.Context;
import android.os.Handler;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by andrea on 20/12/16.
 */
@RunWith(AndroidJUnit4.class)
public class TimeoutTaskTest {

    private final static long TIMEOUT_MILLIS = 1000L;
    private final static long TIMEOUT_CHECK_MILLIS = 1010L;

    private final static AtomicBoolean TASK_EXECUTED = new AtomicBoolean(false);

    private static boolean taskHasBeenExecuted() { return TASK_EXECUTED.get();}

    private static void resetTaskExecution() { TASK_EXECUTED.set(false);}

    private static void executeTask() { TASK_EXECUTED.set(true);}

    private static TimeoutTask task = null;

    @BeforeClass
    public static void beforeAllTests() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        final Handler handler = new Handler(appContext.getMainLooper());
        task = TimeoutTask.newInstance(handler, TIMEOUT_MILLIS, new Runnable() {
            @Override
            public void run() {
                executeTask();
            }
        });
    }

    @Test
    public void testTimeoutTask_testStart() throws InterruptedException {
        resetTaskExecution();
        task.start();
        assertTrue(task.isRunning());
        assertFalse(taskHasBeenExecuted());
        Thread.sleep(TIMEOUT_CHECK_MILLIS);
        assertTrue(taskHasBeenExecuted());
        assertFalse(task.isRunning());
    }

    @Test
    public void testTimeoutTask_testDelay() throws InterruptedException {
        resetTaskExecution();
        task.start();
        Thread.sleep(TIMEOUT_CHECK_MILLIS/2);
        assertTrue(task.isRunning());
        assertFalse(taskHasBeenExecuted());
        task.delay();
        Thread.sleep(TIMEOUT_CHECK_MILLIS/2);
        assertTrue(task.isRunning());
        assertFalse(taskHasBeenExecuted());
        task.delay();
        Thread.sleep(TIMEOUT_CHECK_MILLIS);
        assertTrue(taskHasBeenExecuted());
        assertFalse(task.isRunning());
    }

    @Test
    public void testTimeoutTask_testStop() throws InterruptedException {
        resetTaskExecution();
        task.start();
        Thread.sleep(TIMEOUT_CHECK_MILLIS/2);
        assertTrue(task.isRunning());
        assertFalse(taskHasBeenExecuted());
        task.stop();
        assertFalse(task.isRunning());
        Thread.sleep(TIMEOUT_CHECK_MILLIS);
        assertFalse(taskHasBeenExecuted());
    }

}
