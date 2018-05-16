package com.adrosonic.adrobuzz.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Lovy on 15-05-2018.
 */

public class PrefixEditText extends android.support.v7.widget.AppCompatEditText {

    private String mPrefix = "AB-";
    private Rect mPrefixRect = new Rect();

    public PrefixEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getPaint().getTextBounds(mPrefix, 0, mPrefix.length(), mPrefixRect);
        mPrefixRect.right += getPaint().measureText(" ");

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(mPrefix, super.getCompoundPaddingLeft(), getBaseline(), getPaint());
    }

    @Override
    public int getCompoundPaddingLeft() {
        return super.getCompoundPaddingLeft() + mPrefixRect.width();
    }
}