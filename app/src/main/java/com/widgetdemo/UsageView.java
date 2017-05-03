package com.widgetdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mucahit on 22/09/15.
 */
public class UsageView extends View {
    private float internalArcStrokeWidth;

    private int colorFirstItem = Color.parseColor("#59F859");
    private int colorSecondItem = Color.parseColor("#F8AE59");
    private int colorThirdItem = Color.parseColor("#F85959");
    private int colorMainCenterCircle = Color.WHITE;
    private int colorCenterCircle = Color.parseColor("#434854");
    private int colorPointerLine = Color.parseColor("#434854");

    private int color_unfilled =  Color.parseColor("#35C2F7");
    private int color_filled =  Color.parseColor("#DDDDDD");

    private int start_color = Color.parseColor("#0892c4");
    private int end_color = Color.parseColor("#066d93");
    private int black_color = Color.parseColor("#000000");
    private int white_color = Color.parseColor("#FFFFFF");

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
    private double paddingInnerCircleScale = 0.1;
    private double pointerLineStrokeWidthScale = 0.006944;
    private float mainCircleScale = 5;
    private float percentvalue = 0;
    private Context context;
    private String unitTextValue;

    public UsageView(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;
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

        paddingInnerCircle = (float) (constantMeasure * paddingInnerCircleScale);

        strokePointerLineWidth = (float) (constantMeasure * pointerLineStrokeWidthScale);
        int mainCircleStroke = (int) (mainCircleScale * constantMeasure / 60);

        final TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(40);
        textPaint.setColor(Color.parseColor("#000000"));
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(TextUtils.isEmpty(unitTextValue)?"None":unitTextValue,constantMeasure/2, constantMeasure/2, textPaint);

        // middle arcs START
        Paint paintInnerArc1 = new Paint();
        paintInnerArc1.setStyle(Paint.Style.STROKE);
        paintInnerArc1.setStrokeWidth(internalArcStrokeWidth);
        paintInnerArc1.setColor(color_unfilled);
        paintInnerArc1.setAntiAlias(true);

        Paint paintInnerArc2 = new Paint();
        paintInnerArc2.setStyle(Paint.Style.FILL);
        paintInnerArc2.setStrokeWidth(internalArcStrokeWidth);
        paintInnerArc2.setColor(start_color);
//        paintInnerArc2.setShader(new LinearGradient(0, getHeight()/2, getWidth(), getHeight()/2, start_color, end_color, Shader.TileMode.MIRROR));
        paintInnerArc2.setAntiAlias(true);

        Paint paintInnerArc2_1 = new Paint();
        paintInnerArc2_1.setStyle(Paint.Style.FILL);
        paintInnerArc2_1.setStrokeWidth(internalArcStrokeWidth);
        paintInnerArc2_1.setColor(start_color);
        paintInnerArc2_1.setAntiAlias(true);

        Paint paintInnerArc2_2 = new Paint();
        paintInnerArc2_2.setStyle(Paint.Style.FILL);
        paintInnerArc2_2.setStrokeWidth(internalArcStrokeWidth);
//        paintInnerArc2_2.setColor(end_color);
        paintInnerArc2_2.setShader(new LinearGradient(0, getHeight()/2, getWidth(), getHeight()/2, start_color, end_color, Shader.TileMode.MIRROR));
        paintInnerArc2_2.setAntiAlias(true);

        Paint paintInnerArc3 = new Paint();
        paintInnerArc3.setStyle(Paint.Style.STROKE);
        paintInnerArc3.setStrokeWidth(internalArcStrokeWidth);
        paintInnerArc3.setColor(colorThirdItem);
        paintInnerArc3.setAntiAlias(true);

        Paint paintInnerArc4 = new Paint();
        paintInnerArc4.setStyle(Paint.Style.STROKE);
        paintInnerArc4.setStrokeWidth(internalArcStrokeWidth);
        paintInnerArc4.setColor(color_filled);
        paintInnerArc4.setAntiAlias(true);

        /** with padding
        /*RectF rectfInner,rectfOuter;
        if (isWidthBiggerThanHeight) {
            rectfInner = new RectF((x - constantMeasure) / 2 + paddingInnerCircle, paddingInnerCircle, (x - constantMeasure) / 2 + paddingInnerCircle + constantMeasure - 2
                    * paddingInnerCircle, constantMeasure - paddingInnerCircle);

            rectfOuter = new RectF(0, (y - constantMeasure) / 2 , constantMeasure, (y - constantMeasure) / 2
                    + constantMeasure);

        } else {
            rectfInner = new RectF(paddingInnerCircle, (y - constantMeasure) / 2 + paddingInnerCircle, constantMeasure - paddingInnerCircle, (y - constantMeasure) / 2
                    + constantMeasure - paddingInnerCircle);
            rectfOuter = new RectF(0, (y - constantMeasure) / 2 , constantMeasure, (y - constantMeasure) / 2
                    + constantMeasure);
        }*/

        RectF rectfInner,rectfOuter,rectfInnerLeft, rectfInnerRight;
        if (isWidthBiggerThanHeight) {
            rectfOuter = new RectF(paddingInnerCircle, (y - constantMeasure) / 2 + paddingInnerCircle, constantMeasure - paddingInnerCircle , (y - constantMeasure) / 2
                    + constantMeasure - paddingInnerCircle);
//            paddingInnerCircle = 2 * paddingInnerCircle;
//            rectfInner = new RectF(paddingInnerCircle, (y - constantMeasure) / 2 + paddingInnerCircle, constantMeasure - paddingInnerCircle , (y - constantMeasure) / 2
//                    + constantMeasure - paddingInnerCircle);
            /*rectfInnerLeft = new RectF(paddingInnerCircle, (y - constantMeasure) / 2 + paddingInnerCircle, constantMeasure/2 - paddingInnerCircle , (y - constantMeasure) / 2
                    + constantMeasure - paddingInnerCircle);*/

            rectfInnerLeft = new RectF(paddingInnerCircle,paddingInnerCircle,constantMeasure - paddingInnerCircle,constantMeasure - paddingInnerCircle);

            rectfInner = new RectF(20,20,180,180);

            rectfInnerRight = new RectF((x - constantMeasure) / 2 + paddingInnerCircle, (y - constantMeasure) / 2 + paddingInnerCircle, constantMeasure - paddingInnerCircle , (y - constantMeasure) / 2
                    + constantMeasure - paddingInnerCircle);
//            rectfInner = new RectF(paddingInnerCircle, (y - constantMeasure) / 2 + paddingInnerCircle, constantMeasure - paddingInnerCircle , (y - constantMeasure) / 2
//                    + constantMeasure - paddingInnerCircle);
            /*rectfInner = new RectF((x - constantMeasure) / 2 + paddingInnerCircle, paddingInnerCircle, (x - constantMeasure) / 2 + paddingInnerCircle + constantMeasure - 2
                    * paddingInnerCircle, constantMeasure - paddingInnerCircle);

            rectfOuter = new RectF(0, (y - constantMeasure) / 2 , constantMeasure, (y - constantMeasure) / 2
                    + constantMeasure);*/

        } else {
            rectfOuter = new RectF(paddingInnerCircle, (y - constantMeasure) / 2 + paddingInnerCircle, constantMeasure - paddingInnerCircle , (y - constantMeasure) / 2
                    + constantMeasure - paddingInnerCircle);
            rectfInner = new RectF(2 * paddingInnerCircle, (y - constantMeasure) / 2 + (2*  paddingInnerCircle), constantMeasure -(2* paddingInnerCircle), (y - constantMeasure) / 2
                    + constantMeasure  -(2* paddingInnerCircle));

            /*rectfInnerLeft = new RectF(paddingInnerCircle, (y - constantMeasure) / 2 + paddingInnerCircle, constantMeasure/2 - paddingInnerCircle , (y - constantMeasure) / 2
                    + constantMeasure - paddingInnerCircle);*/

            rectfInnerLeft = new RectF(20,20,100,180);

            rectfInnerRight = new RectF((x - constantMeasure) / 2 + paddingInnerCircle, (y - constantMeasure) / 2 + paddingInnerCircle, constantMeasure - paddingInnerCircle , (y - constantMeasure) / 2
                    + constantMeasure - paddingInnerCircle);
        }

        int sweepingangle = (int)(360 * (percentvalue/100));
//        canvas.drawArc(rectfInner, 0, 90 , false, paintInnerArc2_1);
//        canvas.drawArc(rectfInnerLeft, 0 , 360 , false, paintInnerArc2_1);
//        canvas.drawArc(rectfInner, 270 , 360, false, paintInnerArc2_1);
//        canvas.drawArc(rectfInnerRight, 0 ,90 , false, paintInnerArc2_2);
        canvas.drawArc(rectfInnerLeft, 0 , 360 , false, paintInnerArc2_2);
//        canvas.drawArc(rectfInner, 90 , 180 , true, paintInnerArc2_1);

//        canvas.drawArc(rectfInnerRight,  ,90 , false, paintInnerArc2_2);
        canvas.drawArc(rectfOuter, 0, 360, false, paintInnerArc4);
//        canvas.drawArc(rectfInner, 90, 270 , false, paintInnerArc2);


        // pointer line START
//        Paint p = new Paint();
//        p.setAntiAlias(true);
//        p.setColor(colorPointerLine);
//        p.setStrokeWidth(strokePointerLineWidth);
//        canvas.rotate(rotateDegree, x / 2, y / 2);

    }


    public void setInternalArcStrokeWidth(float internalArcStrokeWidth) {
        this.internalArcStrokeWidth = internalArcStrokeWidth;
        invalidate();
    }
    public void setColorFirstItem(int colorFirstItem) {
        this.colorFirstItem = colorFirstItem;
        invalidate();
    }

    public void setColorSecondItem(int colorSecondItem) {
        this.colorSecondItem = colorSecondItem;
        invalidate();
    }

    public void setColorThirdItem(int colorThirdItem) {
        this.colorThirdItem = colorThirdItem;
        invalidate();
    }

    public void setColorCenterCircle(int colorCenterCircle) {
        this.colorCenterCircle = colorCenterCircle;
        invalidate();
    }

    public void setColorMainCenterCircle(int colorMainCenterCircle) {
        this.colorMainCenterCircle = colorMainCenterCircle;
        invalidate();
    }

    public void setColorPointerLine(int colorPointerLine) {
        this.colorPointerLine = colorPointerLine;
        invalidate();
    }

    public void setPaddingMain(float paddingMain) {
        this.paddingMain = paddingMain;
        invalidate();
    }

    public void setPaddingInnerCircle(float paddingInnerCircle) {
        this.paddingInnerCircle = paddingInnerCircle;
        invalidate();
    }

    public void setRotateDegree(float rotateDegree) {
        this.rotateDegree = rotateDegree;
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

    public void setStrokePointerLineWidth(float strokePointerLineWidth) {
        this.strokePointerLineWidth = strokePointerLineWidth;
        invalidate();
    }

    public void setX(float x) {
        this.x = x;
        invalidate();
    }

    public void setY(float y) {
        this.y = y;
        invalidate();
    }

    public void setConstantMeasure(float constantMeasure) {
        this.constantMeasure = constantMeasure;
        invalidate();
    }

    public void setWidthBiggerThanHeight(boolean isWidthBiggerThanHeight) {
        this.isWidthBiggerThanHeight = isWidthBiggerThanHeight;
        invalidate();
    }

    public void setInternalArcStrokeWidthScale(double internalArcStrokeWidthScale) {
        this.internalArcStrokeWidthScale = internalArcStrokeWidthScale;
        invalidate();
    }

    public void setPaddingInnerCircleScale(double paddingInnerCircleScale) {
        this.paddingInnerCircleScale = paddingInnerCircleScale;
        invalidate();
    }

    public void setPointerLineStrokeWidthScale(double pointerLineStrokeWidthScale) {
        this.pointerLineStrokeWidthScale = pointerLineStrokeWidthScale;
        invalidate();
    }

    public float getInternalArcStrokeWidth() {
        return internalArcStrokeWidth;
    }

    public int getColorFirstItem() {
        return colorFirstItem;
    }

    public int getColorSecondItem() {
        return colorSecondItem;
    }

    public int getColorThirdItem() {
        return colorThirdItem;
    }

    public int getColorCenterCircle() {
        return colorCenterCircle;
    }

    public int getColorMainCenterCircle() {
        return colorMainCenterCircle;
    }

    public int getColorPointerLine() {
        return colorPointerLine;
    }

    public float getPaddingMain() {
        return paddingMain;
    }

    public float getPaddingInnerCircle() {
        return paddingInnerCircle;
    }

    public float getRotateDegree() {
        return rotateDegree;
    }

    public float getSweepAngleFirstChart() {
        return sweepAngleFirstChart;
    }

    public float getSweepAngleSecondChart() {
        return sweepAngleSecondChart;
    }

    public float getSweepAngleThirdChart() {
        return sweepAngleThirdChart;
    }

    public float getStrokePointerLineWidth() {
        return strokePointerLineWidth;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getConstantMeasure() {
        return constantMeasure;
    }

    public boolean isWidthBiggerThanHeight() {
        return isWidthBiggerThanHeight;
    }

    public double getInternalArcStrokeWidthScale() {
        return internalArcStrokeWidthScale;
    }

    public double getPaddingInnerCircleScale() {
        return paddingInnerCircleScale;
    }

    public double getPointerLineStrokeWidthScale() {
        return pointerLineStrokeWidthScale;
    }

    public void setUnitText(String unit){
        unitTextValue = unit;
        invalidate();
    }

    public void setValueText(float value){
        percentvalue = value;
        invalidate();
    }
}