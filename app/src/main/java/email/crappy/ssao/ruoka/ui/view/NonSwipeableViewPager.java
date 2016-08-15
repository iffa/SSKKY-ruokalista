package email.crappy.ssao.ruoka.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * ViewPager that doesn't scroll.
 *
 * @author Santeri Elo
 */
public class NonSwipeableViewPager extends VerticalViewPager {
    public NonSwipeableViewPager(Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}