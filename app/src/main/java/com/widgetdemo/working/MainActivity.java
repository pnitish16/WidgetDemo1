package com.widgetdemo.working;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.widgetdemo.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Random RAND = new Random(100);
    private float degree = -225;
    private float sweepAngleControl = 0;
    private float sweepAngleFirstChart = 1;
    private float sweepAngleSecondChart = 1;
    private float sweepAngleThirdChart = 1;
    private boolean isInProgress = false;
    private boolean resetMode = false;
    private boolean canReset = false;
    private GaugeView1 gaugeView;
    private SeekBar sbGauge;
    float rotateDegree;
    private Handler handler;
    private int i = 0;
    private TextView tvText;
    private int prevProgress = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gaugeView = (GaugeView1) findViewById(R.id.gaugeView);
        gaugeView.setRotateDegree(degree);
        sbGauge = (SeekBar) findViewById(R.id.sbGauge);
        tvText= (TextView) findViewById(R.id.tvText);

        handler = new Handler();

//        gaugeView.setRotateDegree(180);


        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 60; i++) {
                    gaugeView.setValueText(i);
                }
            }
        }, 1);*/
       final String[] myarray = new String[]{"hi","how","are","you","doing"};

        /*for (int count = 0; count < myarray.length; count++){
            final int finalCount = count;
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    tvText.setText(myarray[finalCount]);
                }
            }, 700 * (count));
        }*/

        /*for (int count = 0; count < 5; count++){
            final int finalCount = count;
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    int rotateDeg = finalCount * 36;
                    gaugeView.setRotateDegree(rotateDeg);
//                    gaugeView.invalidate();
                }
            }, 1000 * (count));
        }*/

        /*for (int j = 0; j < 60; j++) {
            final int finalJ = j;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            prevProgress = finalJ;
                            sbGauge.setProgress(prevProgress + finalJ);
//                            gaugeView.setRotateDegree(finalJ * 36);
//                            gaugeView.setSweepAngleSecondChart(rotateDegree);
                            handler.postDelayed(this,1000);
                        }
                    });
                }
            }, 1000 * (j));
        }*/

//        for (int count = 0; count < 5; count++){
//            final int finalCount = count;
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            int rotateDeg = (finalCount + 2) * 36;
//                            gaugeView.setRotateDegree(rotateDeg);
//                        }
//                    });
//                }
//            }, 1000 * (count));
//        }

        /*for (int count = 0; count < 5; count++){
            final int finalCount = count;
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    tvText.setText(myarray[finalCount]);
                    gaugeView.setValueText((int)(finalCount * 10));
                    gaugeView.invalidate();
                }
            }, 700 * (count));
        }*/

        /*Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();*/


        /*final Handler handler = new Handler();
        for (int i = 0; i < 60; i++) {

        }
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //Do something after 100ms
                i++;
                if(i< 60)
                gaugeView.setValueText(i);
//                handler.postDelayed(this, 60);
            }
        }, 1);*/

        sbGauge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                try {
                    rotateDegree = i * 36;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            degree = 0;
                            sweepAngleControl++;
                            if (degree < 45) {
                                gaugeView.setRotateDegree(rotateDegree);
                                gaugeView.setSweepAngleSecondChart(rotateDegree);
                            }

                            if (sweepAngleControl <= 90) {
                                sweepAngleFirstChart++;
                                gaugeView.setSweepAngleFirstChart(sweepAngleFirstChart);
                            } else if (sweepAngleControl <= 180) {
                                sweepAngleSecondChart++;
                                gaugeView.setSweepAngleSecondChart(sweepAngleSecondChart);
                            } else if (sweepAngleControl <= 270) {
                                sweepAngleThirdChart++;
                                gaugeView.setSweepAngleThirdChart(sweepAngleThirdChart);
                            }

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
}
