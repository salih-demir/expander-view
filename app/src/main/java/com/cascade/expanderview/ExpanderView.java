package com.cascade.expanderview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ExpanderView extends LinearLayout {
    private static int animationDuration = 300;
    private static int animationHeight = 20;

    private boolean contentExpanded = true;
    private RelativeLayout layoutExpandIndicator;
    private LinearLayout layoutExpandViewContent;
    private LinearLayout layoutExpandViewFooter;
    private TextView textViewExpanderTitle;
    private ToggleButton toggleButtonExpandIndicator;

    public ExpanderView(Context context) {
        super(context);
        initialize(context, null);
    }

    public ExpanderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public ExpanderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child.getId() != R.id.layout_expander_view) {
            addChildView(child);
        }
    }

    private void showContentView() {
        layoutExpandViewContent.setVisibility(View.VISIBLE);
        layoutExpandViewContent.setAlpha(0.0f);
        layoutExpandViewContent.setTranslationY(-animationHeight);
        layoutExpandViewContent.animate()
                .alpha(1.0f)
                .setDuration(animationDuration)
                .translationY(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layoutExpandViewContent.setAlpha(1.0f);
                        layoutExpandViewContent.setTranslationY(0.0f);
                    }
                });
    }

    private void hideContentView() {
        layoutExpandViewContent.animate()
                .translationY(-animationHeight)
                .alpha(0.0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        if (!contentExpanded)
                            layoutExpandViewContent.setVisibility(View.GONE);
                    }
                });
    }

    public void initialize(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.view_expander, this);

        layoutExpandViewContent = rootView.findViewById(R.id.ll_expand_view_content);
        layoutExpandViewFooter = rootView.findViewById(R.id.ll_expand_view_footer_line);
        textViewExpanderTitle = rootView.findViewById(R.id.tv_expander_title);
        layoutExpandIndicator = rootView.findViewById(R.id.rl_expand_indicator);
        toggleButtonExpandIndicator = rootView.findViewById(R.id.tb_expand_indicator);

        layoutExpandIndicator.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButtonExpandIndicator.toggle();
            }
        });

        toggleButtonExpandIndicator.setChecked(isContentExpanded());
        toggleButtonExpandIndicator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                contentExpanded = b;

                if (b)
                    showContentView();
                else
                    hideContentView();
            }
        });

        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExpanderView, 0, 0);
            try {
                boolean footerLineEnabled = typedArray.getBoolean(R.styleable.ExpanderView_footerLineEnabled, true);
                if (!footerLineEnabled)
                    layoutExpandViewFooter.setVisibility(GONE);
                else
                    layoutExpandViewFooter.setVisibility(VISIBLE);

                boolean titleMarginEnabled = typedArray.getBoolean(R.styleable.ExpanderView_titleMarginEnabled, false);
                setTitleMarginEnabled(titleMarginEnabled);

                boolean contentMarginEnabled = typedArray.getBoolean(R.styleable.ExpanderView_contentMarginEnabled, false);
                setContentMarginEnabled(contentMarginEnabled);

                boolean footerMarginEnabled = typedArray.getBoolean(R.styleable.ExpanderView_footerMarginEnabled, false);
                setFooterMarginEnabled(footerMarginEnabled);

                String listTitle = typedArray.getString(R.styleable.ExpanderView_expanderTitle);
                if (listTitle != null)
                    setTitle(listTitle);
            } finally {
                typedArray.recycle();
            }
        }
    }

    public void setTitle(String title) {
        textViewExpanderTitle.setText(Html.fromHtml(title));
    }

    public void setTitle(String title, String superScript) {
        SpannableStringBuilder cs = new SpannableStringBuilder(title + superScript);
        cs.setSpan(new SuperscriptSpan(), title.length(), title.length() + superScript.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        cs.setSpan(new ForegroundColorSpan(Color.RED), title.length(), title.length() + superScript.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewExpanderTitle.setText(cs);
    }

    public void setTitleMarginEnabled(boolean enabled) {
        if (enabled) {
            int dpInPx = Math.round(getResources().getDimensionPixelSize(R.dimen.default_expander_margin));
            layoutExpandIndicator.setPadding(dpInPx, 0, dpInPx, 0);
        } else {
            layoutExpandIndicator.setPadding(0, 0, 0, 0);
        }
    }

    public void setContentMarginEnabled(boolean enabled) {
        if (enabled) {
            int dpInPx = Math.round(getResources().getDimensionPixelSize(R.dimen.default_expander_margin));
            layoutExpandViewContent.setPadding(dpInPx, 0, dpInPx, 0);

            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(dpInPx, 0, dpInPx, 0);
            layoutExpandViewFooter.setLayoutParams(layoutParams);
        } else {
            layoutExpandViewContent.setPadding(0, 0, 0, 0);
        }
    }

    public void setFooterMarginEnabled(boolean enabled) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        if (enabled) {
            int dpInPx = Math.round(getResources().getDimensionPixelSize(R.dimen.default_expander_margin));
            layoutParams.setMargins(dpInPx, 0, dpInPx, 0);
        } else {
            layoutParams.setMargins(0, 0, 0, 0);
        }

        layoutExpandViewFooter.setLayoutParams(layoutParams);
    }

    public void showFooterLine(boolean enabled) {
        if (enabled)
            layoutExpandViewFooter.setVisibility(View.VISIBLE);
        else
            layoutExpandViewFooter.setVisibility(View.GONE);
    }

    public void addChildView(View child) {
        if (child.getParent() != null)
            removeView(child);

        layoutExpandViewContent.addView(child);
    }

    public void show() {
        if (!contentExpanded)
            toggleButtonExpandIndicator.toggle();
    }

    public void hide() {
        if (contentExpanded)
            toggleButtonExpandIndicator.toggle();
    }

    public boolean isContentExpanded() {
        return contentExpanded;
    }

    public LinearLayout getContentView() {
        return layoutExpandViewContent;
    }
}