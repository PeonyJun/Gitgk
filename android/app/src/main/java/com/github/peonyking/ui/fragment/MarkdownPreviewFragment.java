package com.github.peonyking.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.github.peonyking.R;
import com.github.peonyking.inject.component.AppComponent;
import com.github.peonyking.ui.activity.MarkdownEditorCallback;
import com.github.peonyking.ui.fragment.base.BaseFragment;
import com.github.peonyking.util.StringUtils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import butterknife.BindView;

import io.noties.markwon.Markwon;

/**
 * Created by ThirtyDegreesRay on 2017/9/29 11:52:42
 */
public class MarkdownPreviewFragment extends BaseFragment{

    private Markwon markwon;

    public static MarkdownPreviewFragment create(){
        MarkdownPreviewFragment fragment = new MarkdownPreviewFragment();
        return fragment;
    }

    @BindView(R.id.preview_text) TextView previewText;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_markdown_preview;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {

    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        previewText.setText(R.string.nothing_to_preview);
    }

    private MarkdownEditorCallback getMarkdownEditorCallback(){
        return (MarkdownEditorCallback) getActivity();
    }

    private Markwon getMarkwon() {
        if (markwon == null) {
            markwon = Markwon.builder(getContext())
                    .build();
        }
        return markwon;
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(getMarkdownEditorCallback().isTextChanged()){
            if(StringUtils.isBlank(getMarkdownEditorCallback().getText())){
                previewText.setText(R.string.nothing_to_preview);
            }else{
                getMarkwon().setMarkdown(previewText, getMarkdownEditorCallback().getText());
            }
        }
    }

}