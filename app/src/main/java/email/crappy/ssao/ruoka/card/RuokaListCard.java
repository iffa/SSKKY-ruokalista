package email.crappy.ssao.ruoka.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.pojo.Item;
import email.crappy.ssao.ruoka.pojo.Ruoka;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;

/**
 * TODO: Complete background code work on this class (so this can be used to display the menu items)
 *
 * @author Santeri 'iffa'
 */
public class RuokaListCard extends CardWithList {
    private Ruoka ruoka;

    public RuokaListCard(Context context, Ruoka ruoka) {
        super(context);
        this.ruoka = ruoka;
    }

    @Override
    protected CardHeader initCardHeader() {
        CardHeader header = new CardHeader(getContext());
        header.setTitle(getContext().getResources().getString(R.string.hello_world));
        return header;
    }

    @Override
    protected void initCard() {
        setSwipeable(false);
    }

    @Override
    protected List<ListObject> initChildren() {
        List<ListObject> mObjects = new ArrayList<ListObject>();

        for(Item item : ruoka.getItems()) {
            mObjects.add(item);
        }
        
        return mObjects;
    }

    @Override
    public View setupChildView(int i, ListObject listObject, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getChildLayoutId() {
        return 0;
    }
}
