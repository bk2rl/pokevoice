package com.bk2rl.pokevoice.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bk2rl.pokevoice.R;
import com.bk2rl.pokevoice.core.MainApplication;
import com.bk2rl.pokevoice.entity.SoundItem;

import java.util.List;
import java.util.Locale;

public class SoundItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;
    private SoundItemAdapter mAdapter;
    private MainActivity mActivity;
    private TextView pokemonInfoView;
    private static List<SoundItem> soundItems;
    private RecyclerView recyclerView;
    private FrameLayout loadScreen;
    private ProgressBar progressBar;

    private int visibleItems;
    private int p_start;
    private int p_end;

    private int loadCount;
    private OnScreenLoading mImageLoadingListener;
    private View rootView;
    private TextView currentPokemonIds;

    public SoundItemFragment() {
    }

    public static SoundItemFragment newInstance(int columnCount) {
        SoundItemFragment fragment = new SoundItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundItems = MainApplication.getSoundItems();
        mActivity = ((MainActivity) getActivity());
        mImageLoadingListener = mActivity;
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sound_item_list, container, false);
        recyclerView = ((RecyclerView) rootView.findViewById(R.id.list));
        if (recyclerView != null) {
            Context context = rootView.getContext();
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//
//            }
//            mAdapter = new SoundItemAdapter(soundItems, mListener, mActivity, mImageLoadingListener);
//            recyclerView.setAdapter(mAdapter);
        }

        pokemonInfoView = (TextView) rootView.findViewById(R.id.pokemonInfo);

        //Set the action on fab
//        mActivity.getFab().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mActivity.getFragments().containsKey(AddMusicFragment.class)) {
//                    mActivity.getFragments().remove(AddMusicFragment.class);
//                }
//                mActivity.getFragments().put(AddMusicFragment.class, AddMusicFragment.newInstance(null));
//                mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mActivity.getFragments().get(AddMusicFragment.class)).addToBackStack("").commit();
//            }
//        });

        currentPokemonIds = (TextView) rootView.findViewById(R.id.currentPokemonIds);

        setRecyclerViewLayoutParams();

        rootView.findViewById(R.id.button_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCount = 0;
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                loadScreen.setVisibility(View.INVISIBLE);
                onLeftClick();
            }
        });

        rootView.findViewById(R.id.button_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCount = 0;
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                loadScreen.setVisibility(View.INVISIBLE);
                onRightClick();
            }
        });

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        loadScreen = (FrameLayout) rootView.findViewById(R.id.loadingScreen);

        return rootView;
    }

    private void setRecyclerViewLayoutParams() {
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                final int width = recyclerView.getMeasuredWidth();
                final int height = recyclerView.getMeasuredHeight();

                final View itemView = rootView.findViewById(R.id.img);
                itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int itemWidth = itemView.getWidth();
                        int itemHeight = itemView.getHeight();
                        int column = width / itemWidth;
                        int rows = height / itemHeight;
                        int addWidthMargin = (width - column * itemWidth) / 2;
                        int addHeightMargin = (height - rows * itemHeight) / 2;

                        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
                        lp.setMargins(lp.leftMargin + addWidthMargin, lp.topMargin + addHeightMargin,
                                lp.rightMargin + addWidthMargin, lp.bottomMargin + addHeightMargin);
                        recyclerView.setLayoutParams(lp);
                        loadScreen.setLayoutParams(lp);

                        GridLayoutManager layout = new GridLayoutManager(SoundItemFragment.this.getActivity(), column) {
                            @Override
                            public boolean canScrollVertically() {
                                super.canScrollVertically();
                                return false;
                            }
                        };

                        //TODO set gravity
                        recyclerView.setLayoutManager(layout);
                        visibleItems = column * rows;
                        soundItems = soundItems.subList(p_start = 0, p_end = visibleItems);
                        mAdapter = new SoundItemAdapter(soundItems, mListener, mActivity, mImageLoadingListener);
                        recyclerView.setAdapter(mAdapter);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            itemView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }

                        currentPokemonIds.setText(
                                p_start + 1 != p_end ? String.format(Locale.getDefault(), "%d...%d", p_start + 1, p_end) :
                                        String.format(Locale.getDefault(), "%d", p_end));
                    }
                });

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updatePockemonInfo(SoundItem mItem) {
        pokemonInfoView.setText(mItem.getLabel());
    }

    private void onLeftClick() {
        if (p_start - visibleItems < 0) {
            p_end = MainApplication.getSoundItems().size();
            int index = p_end;
            while ((index % visibleItems) != 0) {
                index--;
            }
            p_start = index;
        } else {
            p_end = p_start;
            p_start = p_end - visibleItems;
        }
        soundItems = MainApplication.getSoundItems().subList(p_start, p_end);
        mAdapter = new SoundItemAdapter(soundItems, mListener, mActivity, mImageLoadingListener);
        currentPokemonIds.setText(
                p_start + 1 != p_end ? String.format(Locale.getDefault(), "%d...%d", p_start + 1, p_end) :
                        String.format(Locale.getDefault(), "%d", p_end));
        recyclerView.setAdapter(mAdapter);
    }

    private void onRightClick() {
        if (p_end > MainApplication.getSoundItems().size() - 1) {
            p_start = 0;
            p_end = visibleItems;
        } else if (p_end + visibleItems > MainApplication.getSoundItems().size() - 1) {
            p_start += visibleItems;
            p_end = MainApplication.getSoundItems().size();
        } else {
            p_start += visibleItems;
            p_end += visibleItems;
        }
        soundItems = MainApplication.getSoundItems().subList(p_start, p_end);
        mAdapter = new SoundItemAdapter(soundItems, mListener, mActivity, mImageLoadingListener);
        currentPokemonIds.setText(
                p_start + 1 != p_end ? String.format(Locale.getDefault(), "%d...%d", p_start + 1, p_end) :
                        String.format(Locale.getDefault(), "%d", p_end));
        recyclerView.setAdapter(mAdapter);
    }

    public void onImageLoaded() {
        loadCount++;
        progressBar.setProgress(progressBar.getProgress() + 100 / (soundItems.size() * 2));
        if (loadCount == (soundItems.size() * 2 - 1)) {
            progressBar.setVisibility(View.GONE);
            loadScreen.setVisibility(View.GONE);
        }
    }

//    private void nextList(){
//            int visibleItemCount = recyclerView.getLayoutManager().getChildCount();//смотрим сколько элементов на экране
//            int totalItemCount = recyclerView.getLayoutManager().getItemCount();//сколько всего элементов
//            int firstVisibleItems = ((GridLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();//какая позиция первого элемента
//
//            if (!isLoading) {//проверяем, грузим мы что-то или нет, эта переменная должна быть вне класса  OnScrollListener
//                if ((visibleItemCount + firstVisibleItems) >= totalItemCount) {
//                    isLoading = true;//ставим флаг что мы попросили еще элемены
//                    if (loadMoreItemsListener != null) {
//                        loadMoreItemsListener.loadMoreItems(currentElement + RECIEVE_ELEMENTS_COUNTS, );//тут я использовал калбэк который просто говорит наружу что нужно еще элементов и с какой позиции начинать загрузку
//                        mAdapter.notifyDataSetChanged();
//                        currentElement += RECIEVE_ELEMENTS_COUNTS;
//                    }
//                }
//            }
//    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(SoundItem mItem);
    }

    public interface OnScreenLoading {
        void onImageLoaded();
    }
}
