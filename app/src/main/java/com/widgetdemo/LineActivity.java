package com.widgetdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

public class LineActivity extends AppCompatActivity {

    SeekBar sbGauge;
    int myProgress = 0;
    float rotateDegree;
    private float degree = -225;
    private float sweepAngleControl = 0;
    private float sweepAngleFirstChart = 1;
    private float sweepAngleSecondChart = 1;
    private float sweepAngleThirdChart = 1;
    private CircleView circleView;
    private CircleView1 circleView1;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        circleView = (CircleView) findViewById(R.id.circleView);
        circleView1 = (CircleView1) findViewById(R.id.circleView1);

        sbGauge = (SeekBar) findViewById(R.id.sbGauge);
//        ObjectAnimator animation = ObjectAnimator.ofInt (sbGauge, "progress", 0, 100); // see this max value coming back here, we animale towards that value
//        animation.setDuration (5000); //in milliseconds
//        animation.setInterpolator (new DecelerateInterpolator());
//        animation.start ();

        //Handler for the progress

        handler = new Handler();

        //Initial 2 sec halt and then roatate with a degree of 2.7 degrees
        for (int j = 0; j < 1000; j++) {
            final int finalJ = j;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rotateDegree = (int) (finalJ * 0.27);
                                    degree = 0;
                                    sweepAngleControl++;
                                    circleView.setRotateDegree(rotateDegree);
                                }
                            });
                        }
                    }, 1 * (finalJ));
                }
            },2000);
        }


        sbGauge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                try {
                    rotateDegree = i * 27;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            degree = 0;
                            circleView.setRotateDegree(rotateDegree);
                            circleView1.setRotateDegree(rotateDegree);

                        }
                    });
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private Runnable myThread = new Runnable(){
        @Override
        public void run() {
            while (myProgress<100){
                try{
                    System.out.println("SSS");
                    sbGauge.setProgress(myProgress);
                    myHandle.sendMessage(myHandle.obtainMessage());
                    Thread.sleep(500);
                }
                catch(Throwable t){
                }
            }
        }

        Handler myHandle = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                myProgress++;
                sbGauge.setProgress(myProgress);
            }
        };
    };


}
