package com.github.peonyking.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.github.peonyking.R;
import com.github.peonyking.mvp.model.TrendingLanguage;
import com.github.peonyking.ui.adapter.base.BaseAdapter;
import com.github.peonyking.ui.adapter.base.BaseViewHolder;
import com.github.peonyking.ui.fragment.base.BaseFragment;
import com.github.peonyking.util.LanguageColorsHelper;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/11/28 18:45:27
 */

public class LanguagesAdapter extends BaseAdapter<LanguagesAdapter.ViewHolder, TrendingLanguage> {

    @Inject
    public LanguagesAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.layout_item_language;
    }

    @Override
    protected ViewHolder getViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TrendingLanguage language = data.get(position);
        holder.languageName.setText(language.getName());
        holder.selectedFlag.setVisibility(language.isSelected() ? View.VISIBLE : View.INVISIBLE);
        int languageColor = LanguageColorsHelper.INSTANCE.getColor(context, language.getName());
        holder.languageColor.setImageTintList(ColorStateList.valueOf(languageColor));
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.language_name) TextView languageName;
        @BindView(R.id.selected_flag) AppCompatImageView selectedFlag;
        @BindView(R.id.language_color) AppCompatImageView languageColor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
