package com.example.bfmemiao.myapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bfmemiao.myapplication.R;
import com.example.bfmemiao.myapplication.adapter.CategoryLeftRecycleViewAdapter;
import com.example.bfmemiao.myapplication.adapter.CategoryRightRecycleViewAdapter;
import com.example.bfmemiao.myapplication.bean.CategoryAllBean;
import com.example.bfmemiao.myapplication.listener.OnItemCoustomClickListener;
import com.example.bfmemiao.myapplication.mvp.presenter.HomePresenter;
import com.example.bfmemiao.myapplication.mvp.view.HomeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BFMe.miao on 2018/1/23.
 */

public class CategoryFragment extends BaseFragment implements HomeView {
    public static int CURRENT_POSITION = 0;
    private RecyclerView mLeftRecycleView;
    private RecyclerView mRightRecycleView;
    private List<CategoryAllBean.CategoryBean> category = new ArrayList<>();
    private CategoryLeftRecycleViewAdapter categoryLeftRecycleViewAdapter;
    private CategoryRightRecycleViewAdapter categoryRightRecycleViewAdapter;
    private LinearLayoutManager linearLayoutManagerright;
    private boolean isClick;
    private Handler handler = new Handler();

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_category, null);
        mLeftRecycleView = (RecyclerView) view.findViewById(R.id.recycleviewleft);
        mRightRecycleView = (RecyclerView) view.findViewById(R.id.recycleviewright);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager linearLayoutManagerleft = new LinearLayoutManager(mActivity);
        mLeftRecycleView.setLayoutManager(linearLayoutManagerleft);

        linearLayoutManagerright = new LinearLayoutManager(mActivity);
        mRightRecycleView.setLayoutManager(linearLayoutManagerright);

        categoryLeftRecycleViewAdapter = new CategoryLeftRecycleViewAdapter(mActivity, category);
        categoryRightRecycleViewAdapter = new CategoryRightRecycleViewAdapter(mActivity, category);

        mLeftRecycleView.setAdapter(categoryLeftRecycleViewAdapter);
        mRightRecycleView.setAdapter(categoryRightRecycleViewAdapter);

        categoryLeftRecycleViewAdapter.setOnItemClickListener(new OnItemCoustomClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                CURRENT_POSITION = postion;
                categoryLeftRecycleViewAdapter.notifyDataSetChanged();
                isClick = true;
                mRightRecycleView.scrollToPosition(postion);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      isClick = false;
                    }
                },500);
            }
        });

        mRightRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!isClick){
                    int firstVisibleItemPosition = linearLayoutManagerright.findFirstVisibleItemPosition();
                    CURRENT_POSITION = firstVisibleItemPosition;
                    categoryLeftRecycleViewAdapter.notifyDataSetChanged();
                }
//

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isClick){
                    int firstVisibleItemPosition = linearLayoutManagerright.findFirstVisibleItemPosition();
                    CURRENT_POSITION = firstVisibleItemPosition;
                    categoryLeftRecycleViewAdapter.notifyDataSetChanged();
                }

            }
        });
        HomePresenter homePresenter = new HomePresenter(this);
        homePresenter.requestGet("1");

    }

    @Override
    public void showProcress() {
        showLoadingDialog();
    }

    @Override
    public void parserDate(Object data) {
        if (data instanceof CategoryAllBean) {
            CategoryAllBean data1 = (CategoryAllBean) data;
            List<CategoryAllBean.CategoryBean> category = data1.getCategory();
            this.category.addAll(category);
            categoryLeftRecycleViewAdapter.notifyDataSetChanged();
            categoryRightRecycleViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void hintProcress() {
        dismissLoadingDialog();
    }

    @Override
    public void errorHandling(Throwable e) {

    }
}
