package com.meest.metme.MetMeUtils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.meest.R;

public class ToolTipWindow {

    private static final int MSG_DISMISS_TOOLTIP = 100;
    private Context ctx;
    private PopupWindow tipWindow;
    private View contentView;
    private LayoutInflater inflater;
    ImageView react_heart, react_laugh, react_cry, react_angry;
    public ToolTipWindow(Context ctx) {
        this.ctx = ctx;
        tipWindow = new PopupWindow(ctx);

        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.reactions_tooltip_layout, null);

        react_heart=contentView.findViewById(R.id.react_heart);
        react_laugh=contentView.findViewById(R.id.react_laugh);
        react_cry=contentView.findViewById(R.id.react_cry);
        react_angry=contentView.findViewById(R.id.react_angry);

        react_heart.setOnClickListener(v -> {
            Toast.makeText(ctx, "Heart", Toast.LENGTH_SHORT).show();
            dismissTooltip();
        });
        react_laugh.setOnClickListener(v -> {
            Toast.makeText(ctx, "Laugh", Toast.LENGTH_SHORT).show();
            dismissTooltip();
        });
        react_cry.setOnClickListener(v -> {
            Toast.makeText(ctx, "Cry", Toast.LENGTH_SHORT).show();
            dismissTooltip();
        });
        react_angry.setOnClickListener(v -> {
            Toast.makeText(ctx, "Angry", Toast.LENGTH_SHORT).show();
            dismissTooltip();
        });
    }

    public void showToolTip(View anchor) {

        tipWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        tipWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

        tipWindow.setOutsideTouchable(false);
        tipWindow.setTouchable(true);
        tipWindow.setFocusable(true);
        tipWindow.setBackgroundDrawable(new BitmapDrawable());

        tipWindow.setContentView(contentView);

        int screen_pos[] = new int[2];
        // Get location of anchor view on screen
        anchor.getLocationOnScreen(screen_pos);

        // Get rect for anchor view
        Rect anchor_rect = new Rect(screen_pos[0], screen_pos[1], screen_pos[0]
                + anchor.getWidth(), screen_pos[1] + anchor.getHeight());

        // Call view measure to calculate how big your view should be.
        contentView.measure(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        int contentViewHeight = contentView.getMeasuredHeight();
        int contentViewWidth = contentView.getMeasuredWidth();
        // In this case , i dont need much calculation for x and y position of
        // tooltip
        // For cases if anchor is near screen border, you need to take care of
        // direction as well
        // to show left, right, above or below of anchor view
        int position_x = anchor_rect.centerX() - (contentViewWidth / 2);
//        int position_y = anchor_rect.top - (anchor_rect.height() / 2);
        int position_y = anchor_rect.top - (anchor_rect.height()*2);

        tipWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, position_x, position_y);

        // send message to handler to dismiss tipWindow after X milliseconds
//        handler.sendEmptyMessageDelayed(MSG_DISMISS_TOOLTIP, 4000);
    }

    public boolean isTooltipShown() {
        if (tipWindow != null && tipWindow.isShowing())
            return true;
        return false;
    }

    public void dismissTooltip() {
        if (tipWindow != null && tipWindow.isShowing())
            tipWindow.dismiss();
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DISMISS_TOOLTIP:
                    if (tipWindow != null && tipWindow.isShowing())
                        tipWindow.dismiss();
                    break;
            }
        }
    };

}
