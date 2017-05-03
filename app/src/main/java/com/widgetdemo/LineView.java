package com.widgetdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.text.DecimalFormat;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by mucahit on 22/09/15.
 */
public class LineView extends View {
    private float internalArcStrokeWidth;

    private int colorFirstItem = Color.parseColor("#59F859");
    private int colorSecondItem = Color.parseColor("#F8AE59");
    private int colorThirdItem = Color.parseColor("#F85959");
    private int colorMainCenterCircle = Color.WHITE;
    private int colorCenterCircle = Color.parseColor("#434854");
    private int colorPointerLine = Color.parseColor("#434854");

    private int color_unfilled = Color.parseColor("#35C2F7");
    private int color_filled = Color.parseColor("#DDDDDD");

    private float paddingMain;
    private float paddingInnerCircle;

    private float rotateDegree = 0;
    private float prevrotateDegree = 0;// for pointer line

    private float sweepAngleFirstChart = 0;
    private float sweepAngleSecondChart = 0;
    private float sweepAngleThirdChart = 0;
    private float strokePointerLineWidth = 4.5f;

    private float x;
    private float y;
    private float constantMeasure;
    private boolean isWidthBiggerThanHeight;

    private double internalArcStrokeWidthScale = 0.0515;
    private double paddingInnerCircleScale = 0;
    private double pointerLineStrokeWidthScale = 0.006944;
    private float mainCircleScale = 5;
    private float percentvalue = 0;
    private Context context;

    //Scale parameters
    private int max_value_scale, min_value_scale, current_value_scale;
    private int max_angle, min_angle, startingAngle,endingAngle;
    private int current_angle;

    //getting from the view property
    int noOfSteps = 10 ,substeps = 5;
    float stepWidth = 10.0f;
    float substepWidth = 5.0f;

    float radius;
    float guageWidth;
    float guageRadius, guageCircumference;
    double currentCircumference;
    double gap;
    TypedArray typedArray;

    public LineView(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineView, 0, 0);
        guageWidth = ta.getFloat(R.styleable.LineView_gaugewidth,0.0f);
        noOfSteps = ta.getInteger(R.styleable.LineView_steps, 0);
        stepWidth = ta.getInteger(R.styleable.LineView_stepwidth, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        x = getWidth();
        y = getHeight();

        if (x >= y) {
            constantMeasure = y;
            isWidthBiggerThanHeight = true;
        } else {
            constantMeasure = x;
            isWidthBiggerThanHeight = false;
        }



        internalArcStrokeWidth = (float) (constantMeasure * internalArcStrokeWidthScale);

//        paddingInnerCircle = (float) (constantMeasure * paddingInnerCircleScale);
//        paddingInnerCircle = 0;

        strokePointerLineWidth = (float) (constantMeasure * pointerLineStrokeWidthScale);
        int mainCircleStroke = (int) (mainCircleScale * constantMeasure / 60);

        final TextPaint textPaint = new TextPaint();
        textPaint.setTextSize((int) ((constantMeasure / 3) * 0.6));
        textPaint.setColor(Color.parseColor("#000000"));
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("490", constantMeasure / 2, constantMeasure / 2, textPaint);

        // middle arcs START
        Paint paintInnerArc1 = new Paint();
        paintInnerArc1.setStyle(Paint.Style.STROKE);
        paintInnerArc1.setStrokeWidth(internalArcStrokeWidth);
        paintInnerArc1.setColor(color_unfilled);
        paintInnerArc1.setAntiAlias(true);

        Paint paintInnerArc2 = new Paint();
        paintInnerArc2.setStyle(Paint.Style.STROKE);
        paintInnerArc2.setStrokeWidth(internalArcStrokeWidth);
        paintInnerArc2.setColor(color_filled);
        paintInnerArc2.setAntiAlias(true);

        Paint paintInnerArc3 = new Paint();
        paintInnerArc3.setStyle(Paint.Style.STROKE);
        paintInnerArc3.setStrokeWidth(internalArcStrokeWidth);
        paintInnerArc3.setColor(colorThirdItem);
        paintInnerArc3.setAntiAlias(true);

        RectF rectfInner;
        if (isWidthBiggerThanHeight) {
            rectfInner = new RectF((x - constantMeasure) / 2 + internalArcStrokeWidth / 2, internalArcStrokeWidth / 2, (x - constantMeasure) / 2 + constantMeasure - internalArcStrokeWidth / 2, constantMeasure - internalArcStrokeWidth / 2);
        } else {
            rectfInner = new RectF(paddingInnerCircle, (y - constantMeasure) / 2 + paddingInnerCircle, constantMeasure - paddingInnerCircle, (y - constantMeasure) / 2
                    + constantMeasure - paddingInnerCircle);
        }

        //setting the gauge values
        current_angle = sample_data();

//        rotateDegree = mapDegrees(470, 0, 512, 135, 405);

        //filled arc

//        canvas.drawArc(rectfInner, startingAngle, (int)(endingAngle - min_angle + (0.5 * stepWidth)) , false, paintInnerArc3);
        canvas.drawArc(rectfInner, startingAngle , (int)(endingAngle - min_angle) , false, paintInnerArc3);
//        canvas.drawArc(rectfInner, (int)(startingAngle - ( 0.5 * stepWidth)) , (int)(endingAngle - min_angle  + ( 0.5 * stepWidth)) , false, paintInnerArc3);


        //value (infilled arc)
        canvas.drawArc(rectfInner, startingAngle, (int)(current_angle - min_angle), false, paintInnerArc2);
//        canvas.drawArc(rectfInner, (int)(startingAngle -(0.5 * stepWidth)), (int)(current_angle - min_angle + (0.5 * stepWidth)), false, paintInnerArc2);


        //Drawing the gauge

        radius = Math.min(x / 2, y / 2);
//        radius = Math.min((rectfInner.right - rectfInner.left)/2,(rectfInner.bottom - rectfInner.top));
        guageWidth = radius * 0.0856f;
        guageRadius = radius - (guageWidth / 2.0f);

        // for whole circle
        /*guageCircumference = 2.0f * (float) Math.PI * guageRadius;
        gap = (guageCircumference - ((noOfSteps - 1) * stepWidth)) / (float) (noOfSteps - 1);*/

//        currentCircumference = ((degreesToRad(max_angle - min_angle)) * guageRadius);
        currentCircumference = ((degreesToRad(max_angle - min_angle)) * guageRadius);
        gap = (currentCircumference - ((noOfSteps * (dpToPx((int) stepWidth))))) / noOfSteps;

        int totalsteps = noOfSteps * substeps;
//        gap = gap - (stepWidth/(noOfSteps +1));

        DecimalFormat decimalFormat = new DecimalFormat("#");
        System.out.println(decimalFormat.format(gap));
//        float gap1 = (float)gap;
        float gap1 = Float.parseFloat(decimalFormat.format(gap));
        gap1 = dpToPx((int) gap1);

        float gap2 = Float.parseFloat(decimalFormat.format((currentCircumference - ((totalsteps + 1) * dpToPx((int)substepWidth))) /totalsteps));
//        double gap1 = gap/4;

//        drawGauge(gap, guageWidth, startingAngle, endingAngle , canvas);

        DashPathEffect dashPath = new DashPathEffect(new float[]{stepWidth, (float)gap}, 0.0f);
        Paint p = new Paint();
        p.setPathEffect(dashPath);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(guageWidth);

//        int gap2 = (int) Math.ceil(gap1);// + (0.8 * stepWidth));
        Log.d("ratio" , "" + gap1);
        DashPathEffect dashPath1 = new DashPathEffect(new float[]{stepWidth, (int)(Math.ceil(gap1))}, 0.0f);
        Paint p1 = new Paint();
        p1.setPathEffect(dashPath1);
        p1.setStyle(Paint.Style.STROKE);

        p1.setStrokeWidth(guageWidth);

        DashPathEffect dashPath2 = new DashPathEffect(new float[]{substepWidth, (int)(Math.ceil(gap2))}, 0.0f);
        Paint p2 = new Paint();
        p2.setPathEffect(dashPath2);
        p2.setStyle(Paint.Style.STROKE);

        p2.setStrokeWidth(guageWidth/2);
//        canvas.drawArc(rectfInner, (int)(startingAngle - (0.5 * stepWidth)), (int)(endingAngle - min_angle  +  (1.5 * stepWidth)), false, p);

//        canvas.drawArc(rectfInner, (int)(startingAngle - (0.5 * stepWidth)), (int)(endingAngle - min_angle  +  (1.5 * stepWidth)), false, p1);
//        canvas.drawArc(rectfInner, (int)(startingAngle - (0.5 * stepWidth)), (int)(endingAngle - min_angle  ), false, p1);
//        canvas.drawArc(rectfInner, (int)(startingAngle - (0.5 * stepWidth)), (int)(endingAngle - min_angle + (1.5 * stepWidth)), false, p);

//        canvas.drawArc(rectfInner, (int)(startingAngle - (0.5 * stepWidth)), (int)(endingAngle - min_angle + (1.5 * stepWidth)), false, p1);
        canvas.drawArc(rectfInner, (int)(startingAngle), (int)(endingAngle - min_angle), false, p1);
//        canvas.drawArc(rectfInner, (int)(startingAngle), (int)(endingAngle - min_angle), false, p2);
    }

    private int sample_data() {
        min_value_scale = 0;
        max_value_scale = 512;
        current_value_scale = 256;

        min_angle = 135;
        max_angle = 405;

        startingAngle = (int)mapDegrees(min_value_scale,min_value_scale,max_value_scale,min_angle,max_angle);
        endingAngle = (int)mapDegrees(max_value_scale,min_value_scale,max_value_scale,min_angle,max_angle);

        current_angle = (int)mapDegrees(current_value_scale, min_value_scale, max_value_scale, startingAngle, endingAngle);
        return current_angle;
    }


    /**
     * @param gap        : gap between tow values in the gauge
     * @param guageWidth : width of the gauge
     * @param canvas     : view canvas object
     */
    private void drawGauge(float gap, float guageWidth, int startingAngle, int endingAngle,Canvas canvas) {

        DashPathEffect dashPath = new DashPathEffect(new float[]{(float) stepWidth, gap}, 0.0f);
        Paint p = new Paint();
        p.setPathEffect(dashPath);
        p.setStyle(Paint.Style.STROKE);

        p.setStrokeWidth(guageWidth);
//        canvas.drawPaint(p);
//        canvas.drawCircle(x / 2, y / 2, guageRadius, p);
//        canvas.drawArc(rectfInner, startingAngle, endingAngle - min_angle, false, paintInnerArc3);

    }


    //deprecated
    public void drawMarkers(int markercount, Canvas canvas) {

        int singlerotation = (405 - 135) / markercount;
        double degreerotation;
        float xinner, yinner, xouter, youter;
        int innerradius = (int) ((x - internalArcStrokeWidth) / 2);
        int outerradius = (int) (x / 2);
        double radvalue;

        Paint linepaint1 = new Paint();
        linepaint1.setAntiAlias(true);
        linepaint1.setColor(Color.BLACK);
        linepaint1.setStyle(Paint.Style.STROKE);
        linepaint1.setStrokeWidth(10);

        //finding point on a circle
        /*newAngle = Angle+rot;
        xbutton = center.x+cos(newAngle)*radius;
        ybutton = center.y+sin(newAngle)*radius;*/

        for (int i = 1; i < markercount; i++) {
//            degreerotation = mapDegrees(singlerotation * i,0,512,135,405);
            degreerotation = mapDegrees((512 / markercount) * i, 0, 512, 135, 405);
//            degreerotation = 135 + degreerotation;
            Log.d("Angle for " + i + "th", "" + degreerotation);
            radvalue = degreesToRad(degreerotation);
            xinner = (float) (x / 2 + ((x - internalArcStrokeWidth) / 2) * cos(radvalue));
            yinner = (float) (y / 2 + ((x - internalArcStrokeWidth) / 2) * sin(radvalue));
            /*xinner = (float)(x/2 + ((x-internalArcStrokeWidth)/ 2) * cos(radvalue)));
            yinner = (float)(y/2 + ((x-internalArcStrokeWidth)/ 2) * sin(radvalue)));*/
            xouter = (float) (x / 2 + (xinner) * cos(radvalue));
            youter = (float) (y / 2 + (xinner + internalArcStrokeWidth) * sin(radvalue));
            canvas.drawLine(xinner, yinner, xouter, youter, linepaint1);
        }
    }


    /**
     * @param x       : present value in mb
     * @param in_min  : minimum value of the scale(0MB)(value range 0-512mb)
     * @param in_max  : max value of the scale(512MB)
     * @param out_min : 135 (starting angle of the arc)
     * @param out_max : 405 (ending angle of the arc)
     * @return : degree of rotation
     */
    public float mapDegrees(long x, long in_min, long in_max, long out_min, long out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public double radToDegrees(double radians) {
        return (radians * Math.PI / 180);
    }

    public double degreesToRad(double degrees) {
        return (degrees * Math.PI / 180);
    }


    public void setX(float x) {
        this.x = x;
        invalidate();
    }

    public void setY(float y) {
        this.y = y;
        invalidate();
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setUnitText(String unit) {

    }

    public void setValueText(float value) {
        percentvalue = value;
        invalidate();
    }

    public int dpToPx(int dp) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        int px = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_PX, dp, displaymetrics );
        return px;

    }


    public int pxToDp(int px) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        int dp = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, px, displaymetrics );
        return dp;
    }
}
