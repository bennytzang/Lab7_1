package com.example.penguin.lab7;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected Button start;
    protected SeekBar rabbit_seekBar , tortoise_seekBar;
    int rabbit_count = 0 , tortorise_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button)findViewById(R.id.start);
        rabbit_seekBar = (SeekBar)findViewById(R.id.rabbit_seekBar);
        tortoise_seekBar = (SeekBar)findViewById(R.id.tortoise_seekBar);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"開始",Toast.LENGTH_LONG).show();
                runThread();
                runAsyncTask();
            }
        });
    }
    private void runThread(){
        rabbit_count = 0;
        new Thread(){
            public  void run(){
                do{
                    try{
                        Thread.sleep(100);  ///延遲0.1秒
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    rabbit_count += (int)(Math.random()*3);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }while(rabbit_count <= 100);
            }
        }.start();
    }
    private Handler mHandler = new Handler(){

        public  void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    rabbit_seekBar.setProgress(rabbit_count); ///寫入計數器的值
                    break;
            }

            if (rabbit_count >= 100)
                if (tortorise_count < 100)
                    Toast.makeText(MainActivity.this,"兔子完成",Toast.LENGTH_LONG).show();
        }
    };
    private  void runAsyncTask(){
        new AsyncTask<Void, Integer, Boolean>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                tortorise_count = 0; ///初始化
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                do{
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    tortorise_count += (int)(Math.random()*3); ///隨機增加計數器0~2的值
                    publishProgress(tortorise_count); ///更新進度條，傳入烏龜計數器
                }while (tortorise_count <=100);

                return true;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                tortoise_seekBar.setProgress(values[0]); ///寫入計數器值到SeekBar
            }

            @Override
            protected void onPostExecute(Boolean status) {
                if (rabbit_count<100)
                    Toast.makeText(MainActivity.this,"烏龜獲勝",Toast.LENGTH_LONG);
            }
        }.execute();  ///啟動Async Task
    }
}
