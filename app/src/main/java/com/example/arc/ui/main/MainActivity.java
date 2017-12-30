package com.example.arc.ui.main;

import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.arc.R;
import com.example.arc.adapter.NewsAdapter;
import com.example.arc.databinding.ActivityMainBinding;
import com.example.arc.model.Article;
import com.example.arc.ui.BaseBindingActivity;
import com.example.arc.ui.detail.DetailActivity;
import com.example.arc.ui.source.SourcesActivity;

import java.util.ArrayList;

/**
 * @author ihsan on 12/19/17.
 */

public class MainActivity extends BaseBindingActivity<MainViewModel, ActivityMainBinding> implements Toolbar.OnMenuItemClickListener, NewsAdapter.ItemSelectedListener {

    private static final String KEY_ARTICLES = "key:articles";
    private NewsAdapter mAdapter;
    private MainViewModel viewModel;

    @Override
    protected Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle instance, MainViewModel viewModel, ActivityMainBinding binding) {
        mAdapter = new NewsAdapter();
        mAdapter.setOnItemClickListener(this);
        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        binding.recyclerView.setAdapter(mAdapter);
        binding.toolbar.setTitle(R.string.str_feed);
        binding.toolbar.inflateMenu(R.menu.main_menu);
        binding.toolbar.setOnMenuItemClickListener(this);
        this.viewModel = viewModel;
        if (instance != null) {
            ArrayList<Article> articles = instance.getParcelableArrayList(KEY_ARTICLES);
            mAdapter.setData(articles);
        } else {
            viewModel.getArticleList().observe(this, articles -> mAdapter.setData((ArrayList<Article>) articles));
            viewModel.getSourceList().observe(this, t -> callNews());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_ARTICLES, mAdapter.getData());
    }

    private void callNews() {
        viewModel.getArticleLiveList().observe(this, articles -> mAdapter.setData((ArrayList<Article>) articles));
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        SourcesActivity.start(this);
        return false;
    }

    @Override
    public void onItemSelected(View view, Article item) {
        View viewAnimation = view.findViewById(R.id.imageView);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, viewAnimation, getString(R.string.trans_shared_image));
        DetailActivity.start(this, item.getId(), options);
    }
}
