package xujf.demo.example.customview.circle_menu;

import android.content.Context;

/**
 * Created by xujf on 2018/9/3.
 */

public class BaseUtil {

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }
}
