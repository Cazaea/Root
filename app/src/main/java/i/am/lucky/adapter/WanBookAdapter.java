package i.am.lucky.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import i.am.lucky.R;
import i.am.lucky.base.baseadapter.BaseRecyclerViewAdapter;
import i.am.lucky.base.baseadapter.BaseRecyclerViewHolder;
import i.am.lucky.bean.book.BooksBean;
import i.am.lucky.databinding.ItemBookBinding;
import i.am.lucky.ui.wan.child.BookDetailActivity;
import i.am.lucky.utils.PerfectClickListener;

/**
 * Created by Cazaea on 2016/11/25.
 */

public class WanBookAdapter extends BaseRecyclerViewAdapter<BooksBean> {

    private Activity activity;

    public WanBookAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_book);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<BooksBean, ItemBookBinding> {

        ViewHolder(ViewGroup context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindViewHolder(final BooksBean book, final int position) {
            if (book != null) {
                binding.setBean(book);
                binding.executePendingBindings();

                binding.llItemTop.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        BookDetailActivity.start(activity,book,binding.ivTopPhoto);
                    }
                });
            }
        }
    }
}
