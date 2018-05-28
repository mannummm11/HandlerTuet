package com.navs.handlertuet;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    WorkerThread worker;
    UiThread uiThread=new UiThread(Looper.getMainLooper());
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.text_view);

        CustomHandler customHandler=new CustomHandler("my_handler");
        customHandler.start();
        worker=new WorkerThread(customHandler.getLooper());

        for(int i =0 ;i<10;i++)
            worker.sendEmptyMessage(i); // send data to worker thread...



    }

    public class WorkerThread extends Handler {

        int val=0;

        public WorkerThread(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            val=msg.what;
            val=val*2;

            uiThread.sendEmptyMessage(val);
            try{Thread.sleep(1000);}catch (Exception e){e.printStackTrace();}
        }
    }


    public class UiThread extends android.os.Handler {

        public UiThread(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            textView.setText(String.valueOf(msg.what));
        }
    }

    public class CustomHandler extends HandlerThread {

        MainActivity.WorkerThread worker;
        public CustomHandler(String name) {
            super(name);
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            worker=new MainActivity.WorkerThread(getLooper());
        }
    }
}
