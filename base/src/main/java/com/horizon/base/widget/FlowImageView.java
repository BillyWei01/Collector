
package com.horizon.base.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

/***
 * 根据宽度等比例调整高度的ImageView， 用于瀑布流
 */
public class FlowImageView extends AppCompatImageView {
    private static final int MAX_LENGTH = 4096;

    private int srcWidth;
    private int srcHeight;

    private Bitmap mSrcBitmap;
    private Rect mRect = new Rect();
    private Bitmap[] mPieces;
    private int m;
    private int n;
    int dw = 0;
    int dh = 0;

    public FlowImageView(Context context) {
        super(context);
    }

    public FlowImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSourceSize(int srcWidth, int srcHeight) {
        this.srcWidth = srcWidth;
        this.srcHeight = srcHeight;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);

        if(bm == mSrcBitmap){
            return;
        }

        if (bm == null) {
            mSrcBitmap = null;
            mPieces = null;
            return;
        }

        dw = bm.getWidth();
        dh = bm.getHeight();
        if (dw > MAX_LENGTH || dh > MAX_LENGTH) {
            Log.d("FlowImageView", "pieces");
            mSrcBitmap = bm;
            m = (dw >> 12) + ((dw & 0xFFF) == 0 ? 0 : 1);
            n = (dh >> 12) + ((dh & 0xFFF) == 0 ? 0 : 1);
            mPieces = new Bitmap[m * n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    int bw = i < m - 1 ? MAX_LENGTH : (dw & 0xFFF);
                    int bh = j < n - 1 ? MAX_LENGTH : (dh & 0xFFF);
                    mPieces[m * i + j] = Bitmap.createBitmap(bm, i << 12, j << 12, bw, bh);
                }
            }
        } else {
            mSrcBitmap = null;
            mPieces = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Bitmap srcBitmap = ((BitmapDrawable) drawable).getBitmap();
            if (srcBitmap != mSrcBitmap) {
                mSrcBitmap = null;
                mPieces = null;
            }
        } else {
            mSrcBitmap = null;
            mPieces = null;
        }

        if (mPieces != null) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    mRect.left = 0;
                    mRect.top = 0;
                    mRect.right = i < m - 1 ? MAX_LENGTH : (dw & 0xFFF);
                    mRect.bottom = j < n - 1 ? MAX_LENGTH : (dh & 0xFFF);
                    canvas.save();
                    canvas.translate(i << 12, j << 12);
                    canvas.drawBitmap(mPieces[m * i + j], null, mRect, null);
                    canvas.restore();
                }
            }
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (srcWidth > 0 && srcHeight > 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height;
            if (width > 0) {
                height = width * srcHeight / srcWidth;
            } else {
                height = MeasureSpec.getSize(heightMeasureSpec);
            }
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
