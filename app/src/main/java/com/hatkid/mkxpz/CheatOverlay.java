package com.hatkid.mkxpz;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

final class CheatOverlay
{
    private View mToggleButton;
    private View mPanel;

    void attachTo(Context context, ViewGroup parent)
    {
        if (!(parent instanceof RelativeLayout) || mToggleButton != null) {
            return;
        }

        RelativeLayout root = (RelativeLayout) parent;
        int margin = dp(context, 12);
        int sectionSpacing = dp(context, 10);

        TextView toggle = new TextView(context);
        toggle.setId(View.generateViewId());
        toggle.setText("修改");
        toggle.setTextColor(Color.WHITE);
        toggle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        toggle.setTypeface(Typeface.DEFAULT_BOLD);
        toggle.setGravity(Gravity.CENTER);
        toggle.setPadding(dp(context, 14), dp(context, 8), dp(context, 14), dp(context, 8));
        toggle.setBackground(makeRoundedBackground(Color.argb(212, 94, 53, 168), dp(context, 18), Color.argb(230, 245, 242, 255)));
        toggle.setClickable(true);
        toggle.setFocusable(false);

        RelativeLayout.LayoutParams toggleParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        toggleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        toggleParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        toggleParams.setMargins(margin, margin, margin, margin);
        root.addView(toggle, toggleParams);

        LinearLayout panel = new LinearLayout(context);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setVisibility(View.GONE);
        panel.setPadding(dp(context, 14), dp(context, 12), dp(context, 14), dp(context, 14));
        panel.setBackground(makeRoundedBackground(Color.argb(224, 20, 20, 28), dp(context, 16), Color.argb(176, 255, 255, 255)));
        panel.setClickable(true);

        TextView title = new TextView(context);
        title.setText("悬浮修改器");
        title.setTextColor(Color.WHITE);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        panel.addView(title);

        TextView subtitle = new TextView(context);
        subtitle.setText("支持无敌、无限MP、无限搜索、秒杀、金币自定义添加");
        subtitle.setTextColor(Color.argb(210, 225, 225, 235));
        subtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        LinearLayout.LayoutParams subtitleParams = wrapContent();
        subtitleParams.topMargin = dp(context, 4);
        panel.addView(subtitle, subtitleParams);

        CheckBox invincibleBox = createCheckBox(context, "无限生命（无敌）");
        invincibleBox.setChecked(MainActivity.cheatInvincibleEnabled());
        invincibleBox.setOnCheckedChangeListener((buttonView, isChecked) -> MainActivity.setCheatInvincibleEnabled(isChecked));
        LinearLayout.LayoutParams invincibleParams = wrapContent();
        invincibleParams.topMargin = sectionSpacing;
        panel.addView(invincibleBox, invincibleParams);

        CheckBox infiniteMpBox = createCheckBox(context, "无限MP（无限魔法值）");
        infiniteMpBox.setChecked(MainActivity.cheatInfiniteMpEnabled());
        infiniteMpBox.setOnCheckedChangeListener((buttonView, isChecked) -> MainActivity.setCheatInfiniteMpEnabled(isChecked));
        LinearLayout.LayoutParams infiniteMpParams = wrapContent();
        infiniteMpParams.topMargin = dp(context, 6);
        panel.addView(infiniteMpBox, infiniteMpParams);

        CheckBox infiniteSearchBox = createCheckBox(context, "无限搜索次数");
        infiniteSearchBox.setChecked(MainActivity.cheatInfiniteSearchEnabled());
        infiniteSearchBox.setOnCheckedChangeListener((buttonView, isChecked) -> MainActivity.setCheatInfiniteSearchEnabled(isChecked));
        LinearLayout.LayoutParams infiniteSearchParams = wrapContent();
        infiniteSearchParams.topMargin = dp(context, 6);
        panel.addView(infiniteSearchBox, infiniteSearchParams);

        CheckBox oneHitKillBox = createCheckBox(context, "秒杀");
        oneHitKillBox.setChecked(MainActivity.cheatOneHitKillEnabled());
        oneHitKillBox.setOnCheckedChangeListener((buttonView, isChecked) -> MainActivity.setCheatOneHitKillEnabled(isChecked));
        LinearLayout.LayoutParams oneHitKillParams = wrapContent();
        oneHitKillParams.topMargin = dp(context, 6);
        panel.addView(oneHitKillBox, oneHitKillParams);

        TextView goldLabel = new TextView(context);
        goldLabel.setText("自定义金币");
        goldLabel.setTextColor(Color.WHITE);
        goldLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        LinearLayout.LayoutParams goldLabelParams = wrapContent();
        goldLabelParams.topMargin = sectionSpacing;
        panel.addView(goldLabel, goldLabelParams);

        LinearLayout goldRow = new LinearLayout(context);
        goldRow.setOrientation(LinearLayout.HORIZONTAL);
        goldRow.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams goldRowParams = matchWidth();
        goldRowParams.topMargin = dp(context, 6);

        EditText goldInput = new EditText(context);
        goldInput.setHint("输入数量");
        goldInput.setHintTextColor(Color.argb(160, 255, 255, 255));
        goldInput.setTextColor(Color.WHITE);
        goldInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        goldInput.setSingleLine(true);
        goldInput.setPadding(dp(context, 10), dp(context, 8), dp(context, 10), dp(context, 8));
        goldInput.setBackground(makeRoundedBackground(Color.argb(184, 42, 42, 52), dp(context, 12), Color.argb(120, 255, 255, 255)));
        LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        goldRow.addView(goldInput, inputParams);

        Button addGoldButton = new Button(context);
        addGoldButton.setText("添加");
        addGoldButton.setTextColor(Color.WHITE);
        addGoldButton.setAllCaps(false);
        addGoldButton.setBackground(makeRoundedBackground(Color.argb(214, 94, 53, 168), dp(context, 12), Color.TRANSPARENT));
        LinearLayout.LayoutParams buttonParams = wrapContent();
        buttonParams.leftMargin = dp(context, 8);
        goldRow.addView(addGoldButton, buttonParams);

        addGoldButton.setOnClickListener(v -> {
            String raw = goldInput.getText().toString().trim();
            if (raw.isEmpty()) {
                Toast.makeText(context, "请输入金币数量", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int amount = Integer.parseInt(raw);
                if (amount == 0) {
                    Toast.makeText(context, "金币数量不能为 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                MainActivity.queueCheatGold(amount);
                goldInput.setText("");
                Toast.makeText(context, "金币修改已加入队列", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException ex) {
                Toast.makeText(context, "金币数量格式不正确", Toast.LENGTH_SHORT).show();
            }
        });

        panel.addView(goldRow, goldRowParams);

        TextView tip = new TextView(context);
        tip.setText("修改会在当前游戏场景内即时生效");
        tip.setTextColor(Color.argb(180, 220, 220, 230));
        tip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        LinearLayout.LayoutParams tipParams = wrapContent();
        tipParams.topMargin = dp(context, 10);
        panel.addView(tip, tipParams);

        RelativeLayout.LayoutParams panelParams = new RelativeLayout.LayoutParams(dp(context, 250), RelativeLayout.LayoutParams.WRAP_CONTENT);
        panelParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        panelParams.addRule(RelativeLayout.BELOW, toggle.getId());
        panelParams.setMargins(margin, 0, margin, margin);
        root.addView(panel, panelParams);

        toggle.setOnClickListener(v -> panel.setVisibility(panel.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));

        mToggleButton = toggle;
        mPanel = panel;
    }

    private static CheckBox createCheckBox(Context context, String text)
    {
        CheckBox checkBox = new CheckBox(context);
        checkBox.setText(text);
        checkBox.setTextColor(Color.WHITE);
        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        return checkBox;
    }

    private static GradientDrawable makeRoundedBackground(int fillColor, int radius, int strokeColor)
    {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(fillColor);
        drawable.setCornerRadius(radius);
        if (strokeColor != Color.TRANSPARENT) {
            drawable.setStroke(1, strokeColor);
        }
        return drawable;
    }

    private static LinearLayout.LayoutParams wrapContent()
    {
        return new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    private static LinearLayout.LayoutParams matchWidth()
    {
        return new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    private static int dp(Context context, int value)
    {
        return Math.round(TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            context.getResources().getDisplayMetrics()
        ));
    }
}
