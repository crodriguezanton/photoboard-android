package tech.photoboard.photoboard.CustomViews;

import android.content.Context;
import android.widget.GridView;

/**
 * Created by pc1 on 23/10/2016.
 */

public class CustomGridView extends GridView {
    public CustomGridView(Context context) {
        super(context);
    }

    @Override

    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {

    }
}
