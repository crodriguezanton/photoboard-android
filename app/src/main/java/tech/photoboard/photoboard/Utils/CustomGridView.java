package tech.photoboard.photoboard.Utils;

import android.content.Context;
import android.widget.GridView;

import tech.photoboard.photoboard.Activities.MainPageActivity;

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
