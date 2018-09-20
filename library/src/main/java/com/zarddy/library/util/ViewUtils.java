package com.zarddy.library.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zarddy.library.widget.TipsClickableSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewUtils {

    /**
     * 显示或隐藏密码输入框内容
     * @param inputView 密码输入框
     * @param state 状态，是否显示密码内容
     */
    public static void changeEditTextPasswordVisibility(EditText inputView, boolean state) {
        if (state) {
            // 可见密码
            inputView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            // 隐藏密码内容
            inputView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    // EditText输入框，过滤特殊符号
    public static InputFilter InputFilterCommon = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String speChat="[\u4e00-\u9fa5a-zA-Z0-9,.，。_+@!！? ？\\-]+";
            Pattern pattern = Pattern.compile(speChat);
            Matcher matcher = pattern.matcher(source.toString());
            if(matcher.find())
                return null;
            else
                return "";
        }
    };

    // email输入框，过滤特殊符号
    public static InputFilter InputFilterEmail = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String speChat="[\u4e00-\u9fa5a-zA-Z0-9._@\\-]+";
            Pattern pattern = Pattern.compile(speChat);
            Matcher matcher = pattern.matcher(source.toString());
            if(matcher.find())
                return null;
            else
                return "";
        }
    };

    // 仅限中文
    public static InputFilter InputFilterChinese = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String chat = "[\u4e00-\u9fa5]+";
            Pattern pattern = Pattern.compile(chat);
            Matcher matcher = pattern.matcher(source.toString());
            if (matcher.find())
                return null;
            else
                return "";
        }
    };

    // 仅限英文字母和数字
    public static InputFilter InputFilterEnglish = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String chat = "[a-zA-Z]+";
            Pattern pattern = Pattern.compile(chat);
            Matcher matcher = pattern.matcher(source.toString());
            if (matcher.find())
                return null;
            else
                return "";
        }
    };

    // 仅限英文字母和数字
    public static InputFilter InputFilterEnglishNumber = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String chat = "[a-zA-Z0-9]+";
            Pattern pattern = Pattern.compile(chat);
            Matcher matcher = pattern.matcher(source.toString());
            if (matcher.find())
                return null;
            else
                return "";
        }
    };

    /**
     * 创建一个横向LinearLayout
     */
    public static LinearLayout newHorizontalLinearLayout(Context context) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        return linearLayout;
    }

    /**
     * 创建一个ListView空头部横向LinearLayout
     */
    public static LinearLayout newEmptyListViewHeader(Context context) {
        return newEmptyListViewHeader(context, AbsListView.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 创建一个ListView空头部横向LinearLayout
     * @param height 占位符高度
     * @return 空白Linearlayout
     */
    public static LinearLayout newEmptyListViewHeader(Context context, int height) {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        return linearLayout;
    }

    /**
     * 解决SwipeRefreshLayout与内部ScrollView滑动冲突，只有滑动到顶部时，SwipeRefreshLayout才能下拉刷新
     * @param swipeRefreshLayout 下拉刷新控件
     * @param scrollView SwipeRefreshLayout内部ScrollView
     */
    public static void solveSwipeRefreshLayoutScrollViewConflicts(final SwipeRefreshLayout swipeRefreshLayout, final ScrollView scrollView) {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
                }
            }
        });
    }

    /**
     * 文本内嵌可点击文本
     * @param contentView 显示完整文本内容的控件
     * @param full 完整的文本内容
     * @param part 可点击的文本内容
     * @param embedTextColorResId 内嵌文字颜色id
     * @param tipsClickableSpan 点击事件
     */
    public static void embedClickableText(Context context, TextView contentView,
                                          CharSequence full, CharSequence part,
                                          int embedTextColorResId, TipsClickableSpan tipsClickableSpan) {

        int startHotLine = String.valueOf(full).indexOf(String.valueOf(part));
        int endHotLine = startHotLine + part.length();

        SpannableString tipsString = new SpannableString(full);
        tipsString.setSpan(tipsClickableSpan, startHotLine, endHotLine, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tipsString.setSpan(new ForegroundColorSpan(context.getResources().getColor(embedTextColorResId)),
                startHotLine, endHotLine, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        contentView.setText(tipsString);
        contentView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * window设置为全屏
     * @param on 是否全屏
     */
    public static void setWindowFullscreen(Activity activity, boolean on) {
        Window win = activity.getWindow();
        if (win == null) {
            return;
        }

        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (on) {
            winParams.flags |=  bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * TextView添加中划线
     */
    public static void addTextViewMiddleLine(TextView textView) {
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); // 设置中划线
    }

    /**
     * TextView添加下划线
     */
    public static void addTextViewUnderLine(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 测量View的宽高
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }
}
