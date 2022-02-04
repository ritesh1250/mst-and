package com.meest.social;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import com.meest.R;

import hani.momanii.supernova_emoji_library.Helper.EmojiconHandler;

public class MyEdittext extends androidx.appcompat.widget.AppCompatEditText {

    private int mEmojiconSize;
    private int mEmojiconAlignment;
    private int mEmojiconTextSize;
    private boolean mUseSystemDefault = false;


    public MyEdittext(Context context) {
        super(context);
        mEmojiconSize = (int) getTextSize();
        mEmojiconTextSize = (int) getTextSize();
    }

    public MyEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public MyEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }



    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
        mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
        mEmojiconAlignment = a.getInt(R.styleable.Emojicon_emojiconAlignment, DynamicDrawableSpan.ALIGN_BASELINE);
        mUseSystemDefault = a.getBoolean(R.styleable.Emojicon_emojiconUseSystemDefault, false);
        a.recycle();
        mEmojiconTextSize = (int) getTextSize();
        setText(getText());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        updateText();
    }

    private void updateText() {
        EmojiconHandler.addEmojis(getContext(), getText(), mEmojiconSize, mEmojiconAlignment, mEmojiconTextSize, mUseSystemDefault);
    }


    /**
     * Set whether to use system default emojicon
     */
    public void setUseSystemDefault(boolean useSystemDefault) {
        mUseSystemDefault = useSystemDefault;
    }
}
