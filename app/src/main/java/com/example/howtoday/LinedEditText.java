package com.example.howtoday;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Toast;

//글을 적지 않아도 밑줄이 있는 상태에서 시작할 수 있게 해주는 소스입니다.


public class LinedEditText extends androidx.appcompat.widget.AppCompatEditText {
    private Paint mPaint = new Paint();

    /**
     * Max lines to be present in editable text field
     */
    private int maxLines = 30;

    /**
     * Max characters to be present in editable text field
     */
    private int maxCharacters = 1000;
    /**
     * application context;
     */
    private Context context;

    public int getMaxCharacters() {
        return maxCharacters;
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    @Override
    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public LinedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initPaint();
    }

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initPaint();
    }

    public LinedEditText(Context context) {
        super(context);
        this.context = context;
        initPaint();
    }

    private void initPaint() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xFFFFFFFF);
    }

    @Override protected void onDraw(Canvas canvas) {
        int left = getLeft();
        int right = getRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int height = getHeight();
        int lineHeight = getLineHeight();
        int count = (height-paddingTop-paddingBottom) / lineHeight;

        for (int i = 0; i < count+20; i++) {
            int baseline = lineHeight * (i+1) + paddingTop;
            canvas.drawLine(left-20, baseline-15, right, baseline-15, mPaint);
        }

        super.onDraw(canvas);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        TextWatcher watcher = new TextWatcher() {

            private String text;
            private int beforeCursorPosition = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //TODO sth
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                text = s.toString();
                beforeCursorPosition = start;
            }

            @Override
            public void afterTextChanged(Editable s) {

                /* turning off listener */
                removeTextChangedListener(this);

                /* handling lines limit exceed */
                if (LinedEditText.this.getLineCount() > maxLines) {
                    LinedEditText.this.setText(text);
                    LinedEditText.this.setSelection(beforeCursorPosition);
                    Toast.makeText(context, "최대 30줄 까지 입니다.", Toast.LENGTH_SHORT)
                            .show();
                }

                /* handling character limit exceed */
                if (s.toString().length() > maxCharacters) {
                    LinedEditText.this.setText(text);
                    LinedEditText.this.setSelection(beforeCursorPosition);
                    Toast.makeText(context, "text too long", Toast.LENGTH_SHORT)
                            .show();
                }

                /* turning on listener */
                addTextChangedListener(this);

            }
        };

        this.addTextChangedListener(watcher);
    }
}
