package email.crappy.ssao.ruoka.ui.base;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;

/**
 * @author Santeri 'iffa'
 */
public class ColorUtil {
    public static int getColor(Context context, int res) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(res, typedValue, true);
        TypedArray arr = context.obtainStyledAttributes(typedValue.data, new int[]{res});
        int color = arr.getColor(0, -1);
        arr.recycle();
        return color;
    }
}
