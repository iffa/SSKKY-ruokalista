package email.crappy.ssao.ruoka.ui.view;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Santeri Elo
 */
public class DashedItemDecorator extends RecyclerView.ItemDecoration {
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;

            Paint dotPaint = new Paint();
            dotPaint.setPathEffect(new DashPathEffect(new float[]{15f, 15f}, 0));
            dotPaint.setStyle(Paint.Style.STROKE); //Important!
            Path path = new Path();
            path.moveTo(left, top);
            path.lineTo(right, top);
            c.drawPath(path, dotPaint);

        }
    }

}
