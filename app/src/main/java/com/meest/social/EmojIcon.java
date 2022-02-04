package com.meest.social;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.meest.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconGridView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconsPopup;
import hani.momanii.supernova_emoji_library.emoji.Emojicon;


public class EmojIcon implements View.OnFocusChangeListener {

    private boolean useSystemEmoji = false;
    private EmojiconsPopup popup;
    private Context context;
    private View rootView;
    private ImageView emojiButton;
    private int KeyBoardIcon = R.drawable.ic_action_keyboard;
    private int SmileyIcons = R.drawable.smiley;
    private hani.momanii.supernova_emoji_library.Actions.EmojIconActions.KeyboardListener keyboardListener;
    private List<MyEdittext> emojiconEditTextList = new ArrayList<>();
    private MyEdittext emojiconEditText;


    /**
     * Constructor
     *
     * @param ctx              The context of current activity.
     * @param rootView         The top most layout in your view hierarchy. The difference of this
     *                         view and the screen height will be used to calculate the keyboard
     *                         height.
     * @param emojiconEditText The Id of EditText.
     * @param emojiButton      The Id of ImageButton used to open Emoji
     */
    public EmojIcon(Context ctx, View rootView, MyEdittext emojiconEditText,
                           ImageView emojiButton) {
        this.emojiButton = emojiButton;
        this.context = ctx;
        this.rootView = rootView;
        addEmojiconEditTextList(emojiconEditText);
        this.popup = new EmojiconsPopup(rootView, ctx, useSystemEmoji);
    }

    public void addEmojiconEditTextList(MyEdittext... emojiconEditText) {
        Collections.addAll(emojiconEditTextList, emojiconEditText);
        for (MyEdittext editText : emojiconEditText) {
            editText.setOnFocusChangeListener(this);
        }
    }


    /**
     * Constructor
     *
     * @param ctx              The context of current activity.
     * @param rootView         The top most layout in your view hierarchy. The difference of this
     *                         view and the screen height will be used to calculate the keyboard
     *                         height.
     * @param emojiconEditText The Id of EditText.
     * @param emojiButton      The Id of ImageButton used to open Emoji
     * @param iconPressedColor The color of icons on tab
     * @param tabsColor        The color of tabs background
     * @param backgroundColor  The color of emoji background
     */
    public EmojIcon(Context ctx, View rootView, MyEdittext emojiconEditText,
                           ImageView emojiButton, String iconPressedColor, String tabsColor,
                           String backgroundColor) {
        addEmojiconEditTextList(emojiconEditText);
        this.emojiButton = emojiButton;
        this.context = ctx;
        this.rootView = rootView;
        this.popup = new EmojiconsPopup(rootView, ctx, useSystemEmoji, iconPressedColor,
                tabsColor, backgroundColor);
    }

    public void setIconsIds(int keyboardIcon, int smileyIcon) {
        this.KeyBoardIcon = keyboardIcon;
        this.SmileyIcons = smileyIcon;
    }

    public void setUseSystemEmoji(boolean useSystemEmoji) {
        this.useSystemEmoji = useSystemEmoji;
        for (MyEdittext editText : emojiconEditTextList) {
            editText.setUseSystemDefault(useSystemEmoji);
        }
        refresh();
    }


    private void refresh() {
        popup.updateUseSystemDefault(useSystemEmoji);
    }

    public void ShowEmojIcon() {
        if (emojiconEditText == null)
            emojiconEditText = emojiconEditTextList.get(0);
        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();

        //If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                changeEmojiKeyboardIcon(emojiButton, SmileyIcons);
            }
        });

        //If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup
                .OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {
                if (keyboardListener != null)
                    keyboardListener.onKeyboardOpen();
            }

            @Override
            public void onKeyboardClose() {
                if (keyboardListener != null)
                    keyboardListener.onKeyboardClose();
                if (popup.isShowing())
                    popup.dismiss();
            }
        });

        //On emoji clicked, add it to edittext
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (emojicon == null) {
                    return;
                }

                int start = emojiconEditText.getSelectionStart();
                int end = emojiconEditText.getSelectionEnd();
                if (start < 0) {
                    emojiconEditText.append(emojicon.getEmoji());
                } else {
                    emojiconEditText.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });

        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup
                .OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                emojiconEditText.dispatchKeyEvent(event);
            }
        });

        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        showForEditText();
    }

    private void showForEditText() {

        emojiButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (emojiconEditText == null)
                    emojiconEditText = emojiconEditTextList.get(0);
                //If popup is not showing => emoji keyboard is not visible, we need to show it
                if (!popup.isShowing()) {

                    //If keyboard is visible, simply show the emoji popup
                    if (popup.isKeyBoardOpen()) {
                        popup.showAtBottom();
                        changeEmojiKeyboardIcon(emojiButton, KeyBoardIcon);
                    }

                    //else, open the text keyboard first and immediately after that show the
                    // emoji popup
                    else {
                        emojiconEditText.setFocusableInTouchMode(true);
                        emojiconEditText.requestFocus();
                        final InputMethodManager inputMethodManager = (InputMethodManager)
                                context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(emojiconEditText, InputMethodManager
                                .SHOW_IMPLICIT);
                        popup.showAtBottomPending();
                        changeEmojiKeyboardIcon(emojiButton, KeyBoardIcon);
                    }
                }

                //If popup is showing, simply dismiss it to show the undelying text keyboard
                else {
                    popup.dismiss();
                }


            }
        });
    }


    public void closeEmojIcon() {
        if (popup != null && popup.isShowing())
            popup.dismiss();

    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId) {
        iconToBeChanged.setImageResource(drawableResourceId);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            if (view instanceof MyEdittext) {
                emojiconEditText = (MyEdittext) view;
            }
        }
    }


    public interface KeyboardListener {
        void onKeyboardOpen();

        void onKeyboardClose();
    }

    public void setKeyboardListener(hani.momanii.supernova_emoji_library.Actions.EmojIconActions.KeyboardListener listener) {
        this.keyboardListener = listener;
    }

}

