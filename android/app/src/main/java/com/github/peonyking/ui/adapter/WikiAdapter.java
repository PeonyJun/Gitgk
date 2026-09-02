package com.github.peonyking.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.github.peonyking.R;
import com.github.peonyking.mvp.model.WikiModel;
import com.github.peonyking.ui.adapter.base.BaseAdapter;
import com.github.peonyking.ui.adapter.base.BaseViewHolder;
import com.github.peonyking.ui.fragment.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/12/6 16:22:59
 */

public class WikiAdapter extends BaseAdapter<WikiAdapter.ViewHolder, WikiModel> {

    @Inject
    public WikiAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_wiki;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        WikiModel model = data.get(position);
        holder.wikiTitle.setText(model.getName());
        String textContent = model.getSimpleTextContent();
        holder.wikiDesc.setText(textContent);
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.wiki_title) TextView wikiTitle;
        @BindView(R.id.wiki_desc) TextView wikiDesc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
