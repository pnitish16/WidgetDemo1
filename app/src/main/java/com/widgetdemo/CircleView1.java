package com.widgetdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by esds001 on 3/23/2017.
 */

public class CircleView1 extends View {

    float presentIp = 70.0f;
    float minIp = 0.0f;
    float maxIp = 100.0f;
    float minOp = 135.0f;
    float maxOp = 45.0f;

    int totalSteps = 10;
    int totalSubSteps = 5;
    float stepWidth = 5.0f;
    float subStepWidth = stepWidth / 2.0f;

    private int scaleColor = Color.parseColor("#777777");
    private int filledColor = Color.RED;
    private int unfilledColor = Color.parseColor("#FFFFFF");
    int rotating_angle = 0;

    private float paddingMain;
    private float paddingInnerCircle;

    private float rotateDegree = 135; // for pointer line

    private float sweepAngleFirstChart = 0;
    private float sweepAngleSecondChart = 0;
    private float sweepAngleThirdChart = 0;
    private float strokePointerLineWidth = 4.5f;

    private float x;
    private float y;
    private float constantMeasure;
    private boolean isWidthBiggerThanHeight;

    private double internalArcStrokeWidthScale = 0.215;
    private double paddingInnerCircleScale = 0.27;
    private double pointerLineStrokeWidthScale = 0.006944;
    private float mainCircleScale = 5;

    private int colorCenterCircle = Color.parseColor("#434854");
    private int colorPointerLine = Color.parseColor("#434854");
    private int colorWhiteBlurred = Color.parseColor("#10FFFFFF");
    private int colorMainCenterCircle = Color.WHITE;
    private int colorWhiteBlurredStart = Color.parseColor("#90FFFFFF");
    private Context context;
    private float internalArcStrokeWidth;

/*    float radius;
    float guageWidth;
    float guageRadius
    float guageCircumference;
    float gap;*/

    public CircleView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        x = getWidth();
        y = getHeight();

        if (maxOp <= minOp) {
            maxOp += 360.0;
        }

        float radius = Math.min(x / 2, y / 2);
        float guageWidth = radius * 0.1f;
        float guageRadius = radius - (guageWidth / 2.0f);

        float arcLength = degToRad(maxOp - minOp) * guageRadius;
        RectF rectStep = new RectF(guageWidth / 2.0f, guageWidth / 2.0f, x - (guageWidth / 2.0f), y - (guageWidth / 2.0f));

        //Unfilled arc
        Paint unfilledPaint = new Paint();
        unfilledPaint.setColor(unfilledColor);
        unfilledPaint.setStyle(Paint.Style.STROKE);
        unfilledPaint.setStrokeWidth(guageWidth);

        canvas.drawArc(rectStep, minOp, maxOp - minOp, false, unfilledPaint);

        //Filled Arc
        Paint filledPaint = new Paint();
        filledPaint.setColor(filledColor);
        filledPaint.setStyle(Paint.Style.STROKE);
        filledPaint.setStrokeWidth(guageWidth);
        float shaderCx = 0;
        float shaderCy = 0;
        float shaderRadius = 90;
        int shaderColor0 = ContextCompat.getColor(context, R.color.login_btn_color);
        int shaderColor1 = ContextCompat.getColor(context, R.color.login_btn_color_back);
        filledPaint.setAntiAlias(true);
        Shader radialGradientShader;

        radialGradientShader = new RadialGradient(
                shaderCx, shaderCy, shaderRadius,
                shaderColor0, shaderColor1,
                Shader.TileMode.MIRROR);

//        filledPaint.setShader(radialGradientShader);

        float presentAngle = scale(presentIp, minIp, maxIp, minOp, maxOp);
        canvas.drawArc(rectStep, minOp, presentAngle - minOp, false, filledPaint);

        //SubStep Arc
        float subStepGap = (arcLength - (totalSteps * totalSubSteps * subStepWidth)) / (totalSteps * totalSubSteps);
        Paint subStepPaint = new Paint();
        subStepPaint.setColor(scaleColor);
        DashPathEffect subDashPath = new DashPathEffect(new float[]{(float) subStepWidth, subStepGap}, 0.0f);
        subStepPaint.setPathEffect(subDashPath);
        subStepPaint.setStyle(Paint.Style.STROKE);
        subStepPaint.setStrokeWidth(guageWidth / 2.0f);

        canvas.drawArc(rectStep, minOp, maxOp - minOp, false, subStepPaint);

        //Step Arc
        float stepGap = (arcLength - (totalSteps * stepWidth)) / totalSteps;
        Paint stepPaint = new Paint();
        stepPaint.setColor(scaleColor);
        DashPathEffect dashPath = new DashPathEffect(new float[]{(float) stepWidth, stepGap}, 0.0f);
        stepPaint.setPathEffect(dashPath);
        stepPaint.setStyle(Paint.Style.STROKE);
        stepPaint.setStrokeWidth(guageWidth);

        canvas.drawArc(rectStep, minOp, maxOp - minOp, false, stepPaint);

        if ((maxOp - minOp) < 360.0f) {
            canvas.drawArc(rectStep, maxOp, (maxOp + stepWidth + 1.0f) - maxOp, false, stepPaint);
        }

        float cx = x / 2 - guageWidth;
        float cy = y / 2 + guageWidth / 2;
        float unitvalue = maxIp / totalSteps;
        float textradius = (int) (radius - (guageWidth * 2.5));

        for (int i = 0; i < totalSteps + 1; i++) {

            float scaleAngle = scale((((maxIp - minIp) / totalSteps) * i), minIp, maxIp, minOp, maxOp);
            float px = (float) (cx + (textradius * Math.cos(degreesToRad(scaleAngle))));
            float py = (float) (cy + (textradius * Math.sin(degreesToRad(scaleAngle))));

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            String rotatedtext = String.valueOf(i * unitvalue);

            Rect rect = new Rect();
            paint.getTextBounds(rotatedtext, 0, rotatedtext.length(), rect);
            canvas.translate(px, py);
            paint.setStyle(Paint.Style.FILL);

            canvas.translate(-px, -py);

            paint.setColor(Color.RED);

            canvas.rotate((scaleAngle + 90), px + rect.exactCenterX(), py + rect.exactCenterY());
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText(rotatedtext, px, py, paint);
            canvas.rotate(-(scaleAngle + 90), px + rect.exactCenterX(), py + rect.exactCenterY());
            Log.d("text coordinates", px + "," + py);
        }

        //Drawing Needles in the view

        // pointer line START
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(ContextCompat.getColor(context, R.color.login_btn_color));
        p.setStrokeWidth(strokePointerLineWidth);
//        p.setShadowLayer(12, 0, 0, Color.LTGRAY);
        canvas.rotate(rotateDegree, x / 2, y / 2);

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

        paddingInnerCircle = (float) (constantMeasure * paddingInnerCircleScale);

        strokePointerLineWidth = (float) (constantMeasure * pointerLineStrokeWidthScale);
        int mainCircleStroke = (int) (mainCircleScale * constantMeasure / 60);
        int mainCircleStrokeHalf = mainCircleStroke/2;

        int a = 10;//10
        if (isWidthBiggerThanHeight) {

            // ***** speedtest like needle

            /*float stopX = (x - constantMeasure) / 2 + paddingInnerCircle + constantMeasure - 2 * paddingInnerCircle + mainCircleStroke;
            float stopY = y / 2;
            Path path = new Path();
            path.setFillType(Path.FillType.EVEN_ODD);
            *//*path.moveTo(x / 2 + mainCircleStroke / a, y / 2 - mainCircleStroke);
            path.lineTo(x / 2 + mainCircleStroke / a, y / 2 + mainCircleStroke);*//*
            path.moveTo(x / 2 + mainCircleStroke / a, y / 2 - mainCircleStroke);
            path.lineTo(x / 2 + mainCircleStroke / a, (int) (y / 2 + (mainCircleStroke * 0.6)));
            path.lineTo(stopX, y / 2 + mainCircleStroke / (a / 5));
            path.lineTo(stopX, y / 2 - mainCircleStroke / (a / 5));
            path.lineTo(x / 2 + mainCircleStroke / a, y / 2 - mainCircleStroke);
            path.close();

            Shader shader = new LinearGradient(x / 2 + mainCircleStroke / a, y / 2 - mainCircleStroke, stopX, y / 2 + mainCircleStroke / (a / 5), colorWhiteBlurred, colorWhiteBlurredStart, Shader.TileMode.CLAMP);
            p.setShader(shader);

            canvas.drawPath(path, p);*/


            float stopX = constantMeasure - mainCircleStrokeHalf;
            float stopY = y / 2;

            Path path = new Path();
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(x / 2 + mainCircleStrokeHalf / a, y / 2 - mainCircleStrokeHalf);
            path.lineTo(x / 2 + mainCircleStrokeHalf / a, y / 2 + mainCircleStrokeHalf);
            path.lineTo(stopX, stopY);
            path.close();
            canvas.drawPath(path, p);
        } else {
            //needle like speedtest
            /*float stopX = constantMeasure - paddingInnerCircle + mainCircleStroke;
            float stopY = y / 2;

            Path path = new Path();
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(x / 2 + mainCircleStroke / a, y / 2 - mainCircleStroke);
            path.lineTo(x / 2 + mainCircleStroke / a + 10, y / 2 - mainCircleStroke + 10);
            path.moveTo(x / 2 + mainCircleStroke / a + 10, y / 2 - mainCircleStroke + 10);
            path.lineTo(x / 2 + mainCircleStroke / a, y / 2 + mainCircleStroke);
            path.lineTo(stopX, stopY);
            path.close();
            canvas.drawPath(path, p);*/

            float stopX = constantMeasure - paddingInnerCircle + mainCircleStroke;
            float stopY = y / 2;

            Path path = new Path();
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(x / 2 + mainCircleStroke / a, y / 2 - mainCircleStroke);
            path.lineTo(x / 2 + mainCircleStroke / a, y / 2 + mainCircleStroke);
            path.lineTo(stopX, stopY);
            path.close();
            canvas.drawPath(path, p);
        }

        // center circles START
//        Paint paintInnerCircle = new Paint();
//        paintInnerCircle.setStyle(Paint.Style.FILL);
//        paintInnerCircle.setColor(ContextCompat.getColor(context, R.color.login_btn_color_back));
//        paintInnerCircle.setAntiAlias(true);
//        canvas.drawCircle(x / 2, y / 2, mainCircleStroke, paintInnerCircle);

        Paint paintCenterCircle = new Paint();
        paintCenterCircle.setStyle(Paint.Style.FILL);
        paintCenterCircle.setColor(ContextCompat.getColor(context, R.color.login_btn_color));

        // center circles START

        //inner cirlce
        Paint paintInnerCircle = new Paint();
        paintInnerCircle.setStyle(Paint.Style.FILL);
        paintInnerCircle.setColor(colorCenterCircle);
        paintInnerCircle.setAntiAlias(true);
        canvas.drawCircle(x / 2, y / 2, mainCircleStroke, paintInnerCircle);

        //outer cirlce
        Paint paintCenterCircleOuter = new Paint();
        paintCenterCircleOuter.setStyle(Paint.Style.FILL);
        paintCenterCircleOuter.setColor(colorMainCenterCircle);
        canvas.drawCircle(x / 2, y / 2, mainCircleStroke / 2, paintCenterCircleOuter);
    }

    public double degreesToRad(double degrees) {
        return (degrees * Math.PI / 180);
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            Log.d("touch point", "" + event.getX() + "," + event.getY());
        }
        return super.onTouchEvent(event);
    }*/

    /**
     * @param present : present value in mbl
     * @param minIp   : minimum value of the scale(0MB)(value range 0-512mb)
     * @param maxIp   : max value of the scale(512MB)
     * @param minOp   : 135 (starting angle of the arc)
     * @param maxOp   : 405 (ending angle of the arc)
     * @return : degree of rotation
     */
    public float scale(float present, float minIp, float maxIp, float minOp, float maxOp) {
        return (((present - minIp) * (maxOp - minOp)) / (maxIp - minIp)) + minOp;
    }

    public float radToDeg(float radians) {
        return (radians * (float) (Math.PI / 180.0));
    }

    public float degToRad(float degrees) {
        return (degrees * (float) (Math.PI / 180));
    }

    public void setRotateDegree(float rotateDegree) {
        this.rotateDegree = minOp + rotateDegree;
        invalidate();
    }

    public void setSweepAngleFirstChart(float sweepAngleFirstChart) {
        this.sweepAngleFirstChart = sweepAngleFirstChart;
        invalidate();
    }

    public void setSweepAngleSecondChart(float sweepAngleSecondChart) {
        this.sweepAngleSecondChart = sweepAngleSecondChart;
        invalidate();
    }

    public void setSweepAngleThirdChart(float sweepAngleThirdChart) {
        this.sweepAngleThirdChart = sweepAngleThirdChart;
        invalidate();
    }
}
