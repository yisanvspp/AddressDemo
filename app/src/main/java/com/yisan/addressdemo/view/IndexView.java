package com.yisan.addressdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.yisan.addressdemo.R;

/**
 * @author：wzh
 * @description: 联系人快速索引控件
 * 1、把26个字母放入数组
 * 2、在onMeasure计算每个item的高和宽
 * 3、在onDraw计算字的宽高、和字的x，y坐标
 * 注意：字默认以左下角那个点为x，y
 * <p>
 * 手指按下或者滑动字母变灰、手指离开字母回复原本颜色
 * 1、计算当前点击的位置 currentIndex
 * 2、绘制当前位置的字母、修改颜色、强制刷新绘制
 * 3、手指离开，强制刷新绘制
 * @packageName: com.yisan.dingdingaddressdemo.view
 * @date：2020/4/13 0013 下午 10:16
 */
public class IndexView extends View {

    private String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int itemWidth;
    private int itemHeight;
    private Paint paint;
    private int currentIndex = -1;
    private OnSelectChangeListener mListener;
    private int defaultTextColor;
    private int chooseTextColor;
    private int defaultTextSize;
    private int chooseTextSize;

    public void setOnSelectChangeListener(OnSelectChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnSelectChangeListener {
        void onSelectChange(String word);
    }

    public IndexView(Context context) {
        this(context, null);

    }

    public IndexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public IndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndexView);
            defaultTextColor = typedArray.getColor(R.styleable.IndexView_textDefaultColor, Color.GRAY);
            chooseTextColor = typedArray.getColor(R.styleable.IndexView_textChooseColor, Color.BLACK);
            defaultTextSize = typedArray.getDimensionPixelOffset(R.styleable.IndexView_textDefaultSize, 22);
            chooseTextSize = typedArray.getDimensionPixelOffset(R.styleable.IndexView_textChooseSize, 40);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(defaultTextColor);
        paint.setAntiAlias(true);
        paint.setTextSize(defaultTextSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        itemWidth = getMeasuredWidth();
        itemHeight = getMeasuredHeight() / words.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (currentIndex != -1) {
                if (word.equals(words[currentIndex])) {
                    paint.setColor(chooseTextColor);
                    paint.setTextSize(chooseTextSize);
                } else {
                    paint.setColor(defaultTextColor);
                    paint.setTextSize(defaultTextSize);
                }
            } else {
                paint.setColor(defaultTextColor);
                paint.setTextSize(defaultTextSize);
            }
            //矩形框用来计算字的宽高
            Rect rect = new Rect();
            paint.getTextBounds(word, 0, 1, rect);
            int wordWidth = rect.width();
            int wordHeight = rect.height();
            float wordX = itemWidth / 2 - wordWidth / 2;
            float wordY = itemHeight / 2 + wordHeight / 2 + i * itemHeight;
            canvas.drawText(word, wordX, wordY, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                //触摸点相对于View左上角为原点坐标系的Y坐标
                float y = event.getY();
                int index = (int) (y / itemHeight);
                if (currentIndex != index && index < words.length && index > -1) {
                    currentIndex = index;
                    if (mListener != null) {
                        mListener.onSelectChange(words[index]);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                currentIndex = -1;
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }
}
