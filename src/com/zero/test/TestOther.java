package com.zero.test;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/*class LooperThread extends Thread {
    public Handler mHandler;

    public void run() {
        Looper.prepare();

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case MSG_QUIT:
                    Looper.myLooper().quit();
                    break;

                default:
                    break;
                }
            }
        };

        Looper.loop();
    }
}*/

public class TestOther extends Activity{

    /*Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            //TODO
            default:
                break;
            }
            super.handleMessage(msg);
        }
        
    };
    
    
    public class Looper {

        final MessageQueue mQueue;
        final Thread mThread;
        volatile boolean mRun;

        private Printer mLogging = null;

         *//** Initialize the current thread as a looper.
          * This gives you a chance to create handlers that then reference
          * this looper, before actually starting the loop. Be sure to call
          * {@link #loop()} after calling this method, and end it by calling
          * {@link #quit()}.
          *//*
        public static void prepare() {
            if (sThreadLocal.get() != null) {
                throw new RuntimeException("Only one Looper may be created per thread");
            }
            sThreadLocal.set(new Looper());
        }


        private synchronized static void setMainLooper(Looper looper) {
            mMainLooper = looper;
        }

        public static void loop() {
            Looper me = myLooper();
            if (me == null) {
                throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
            }
            MessageQueue queue = me.mQueue;
            
            // Make sure the identity of this thread is that of the local process,
            // and keep track of what that identity token actually is.
            Binder.clearCallingIdentity();
            final long ident = Binder.clearCallingIdentity();
            
            while (true) {
                Message msg = queue.next(); // might block
                if (msg != null) {
                    
                    ......
                    
                    msg.target.dispatchMessage(msg);
                    
                    ......
                    
                    msg.recycle();
                }
            }
        }
    }*/
}
