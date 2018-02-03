package erris.pulsesensor.cardio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class CardiographView extends View {
    protected Paint mPaint;
    protected int mLineColor = Color.parseColor("#76f112");
    protected int mGridColor = Color.parseColor("#1b4200");

    protected int mSGridColor = Color.parseColor("#092100");
    protected int mBackgroundColor = Color.BLACK;
    protected int mWidth,mHeight;

    protected int mGridWidth = 50;
    protected int mSGridWidth = 10;

    protected Path mPath ;

    public CardiographView(Context context) {
        this(context,null);
    }

    public CardiographView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CardiographView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initBackground(canvas);
    }

    private void initBackground(Canvas canvas) {

        canvas.drawColor(mBackgroundColor);

        int vSNum = mWidth /mSGridWidth;

        int hSNum = mHeight/mSGridWidth;
        mPaint.setColor(mSGridColor);
        mPaint.setStrokeWidth(2);

        for(int i = 0;i<vSNum+1;i++){
            canvas.drawLine(i*mSGridWidth,0,i*mSGridWidth,mHeight,mPaint);
        }

        for(int i = 0;i<hSNum+1;i++){
            canvas.drawLine(0,i*mSGridWidth,mWidth,i*mSGridWidth,mPaint);
        }

        int vNum = mWidth / mGridWidth;
        int hNum = mHeight / mGridWidth;
        mPaint.setColor(mGridColor);
        mPaint.setStrokeWidth(2);

        for(int i = 0;i<vNum+1;i++){
            canvas.drawLine(i*mGridWidth,0,i*mGridWidth,mHeight,mPaint);
        }

        for(int i = 0;i<hNum+1;i++){
            canvas.drawLine(0,i*mGridWidth,mWidth,i*mGridWidth,mPaint);
        }
    }
}
