package email.crappy.ssao.ruoka.ui.list;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import email.crappy.ssao.ruoka.data.model.Week;

/**
 * @author Santeri 'iffa'
 */
public interface ListView extends MvpView {
    void showLoading();

    void showError(Throwable throwable);

    void showContent(List<Week> weeks, int currentPosition);
}
