package app.added.kannauj.Utils;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ChatScrollListener extends RecyclerView.OnScrollListener {

    int visibleItemCount,totalItemCount,pastVisibleItems;
    LinearLayoutManager linearLayoutManager;

    public ChatScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy < 0) {

            // Visible Item count is the number of items visible on screen.
            // Total item count is the number of total items in adapter.
            // Past visible items is the number of items scrolled out of screen.

            visibleItemCount = linearLayoutManager.getChildCount();
            totalItemCount = linearLayoutManager.getItemCount();
            pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
//            Log.e("scroll","currentVisible "+visibleItemCount);
//            Log.e("scroll","pastVisible "+pastVisibleItems);
//            Log.e("scroll","total "+totalItemCount);

            if (!isLoading()) {
                Log.e("scroll",""+visibleItemCount);
                if (pastVisibleItems==0) {
                    Log.e("scroll","load");
                    loadMoreItems();
                }
            }
        }
    }


    public abstract boolean isLoading();

    protected abstract void loadMoreItems();


}
