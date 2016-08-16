package email.crappy.ssao.ruoka.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.data.util.DateUtil;

/**
 * @author Santeri Elo
 */
public class DateBoxView extends LinearLayout {
    public DateBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupView(attrs);
    }

    private void setupView(AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DateBoxView, 0, 0);
        boolean small = a.getBoolean(R.styleable.DateBoxView_small, false);
        a.recycle();

        if (small) {
            LayoutInflater.from(getContext()).inflate(R.layout.view_date_small, this, true);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.view_date, this, true);
        }
    }

    public void setDate(Date date) {
        TextView dateView = (TextView) findViewById(R.id.date);

        Calendar calendar = DateUtil.getCurrentCalendar();
        calendar.setTime(date);

        dateView.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
    }

    public void setDay(Date date) {
        TextView dayView = (TextView) findViewById(R.id.day);

        Calendar calendar = DateUtil.getCurrentCalendar();
        calendar.setTime(date);

        dayView.setText(getResources().getString(DateUtil.getDayResource(calendar)));
    }
}
