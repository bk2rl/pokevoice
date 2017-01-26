package com.bk2rl.pokevoice.view;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bk2rl.pokevoice.R;
import com.bk2rl.pokevoice.entity.SoundItem;
import com.bk2rl.pokevoice.view.SoundItemFragment.OnListFragmentInteractionListener;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class SoundItemAdapter extends RecyclerView.Adapter<SoundItemAdapter.ViewHolder> {

    private final List<SoundItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final MainActivity mActivity;
    private static ViewHolder currentItem;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private SoundItemFragment.OnScreenLoading mImageLoadingListener;


    public SoundItemAdapter(List<SoundItem> items, OnListFragmentInteractionListener listener, MainActivity activity, SoundItemFragment.OnScreenLoading mImageLoadingListener) {
        mValues = items;
        mListener = listener;
        mActivity = activity;
        this.mImageLoadingListener = mImageLoadingListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_sound_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.imgUri = Glide.with(mActivity).load(Uri.parse("file:///android_asset/" + holder.mItem.getImgSrc()));
        setStaticGif(holder, holder.mImgView);
        setAnimatedGif(holder, holder.mAnimView);
        holder.mLabelView.setText(holder.mItem.getLabel());
        holder.mView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                onListItemClick(holder);
                                            }
                                        }

        );
    }

    private void onListItemClick(final ViewHolder holder) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            try {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    currentItem.mAnimView.setVisibility(View.INVISIBLE);
                    currentItem.mImgView.setVisibility(View.VISIBLE);
                }

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(mActivity.getAssets().openFd(holder.mItem.getAudioSrc()).getFileDescriptor(),
                        mActivity.getAssets().openFd(holder.mItem.getAudioSrc()).getStartOffset(),
                        mActivity.getAssets().openFd(holder.mItem.getAudioSrc()).getLength());
                mediaPlayer.prepare();
                mediaPlayer.start();
                currentItem = holder;
                currentItem.mAnimView.setVisibility(View.VISIBLE);
                currentItem.mImgView.setVisibility(View.INVISIBLE);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        holder.mAnimView.setVisibility(View.INVISIBLE);
                        holder.mImgView.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {

            }
            mListener.onListFragmentInteraction(holder.mItem);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImgView;
        public final ImageView mAnimView;
        public final TextView mLabelView;
        public DrawableTypeRequest<Uri> imgUri;
        public SoundItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImgView = (ImageView) view.findViewById(R.id.img);
            mAnimView = (ImageView) view.findViewById(R.id.animation);
            mLabelView = (TextView) view.findViewById(R.id.label);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLabelView.getText() + "'";
        }

    }

    private void setStaticGif(ViewHolder holder, final ImageView imageView) {
        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                imageView.setImageBitmap(bitmap);
                mImageLoadingListener.onImageLoaded();
            }
        };
        Glide.with(mActivity).load(Uri.parse("file:///android_asset/" + holder.mItem.getImgSrc())).asBitmap().into(target);
    }

    private void setAnimatedGif(ViewHolder holder, final ImageView imageView) {
        GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(imageView);
        Glide.with(mActivity).load(Uri.parse("file:///android_asset/" + holder.mItem.getImgSrc())).asGif().listener(new RequestListener<Uri, GifDrawable>() {
            @Override
            public boolean onException(Exception e, Uri model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Uri model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                mImageLoadingListener.onImageLoaded();
                return false;
            }
        }).into(imageView);
//        new Thread(new Runnable() {
//            public void run() {
//                InputStream bitmapStream = null;
//                BitmapDrawable mDrawable = null;
//                try {
////                    bitmapStream = mActivity.getAssets().open(path);
////                    Bitmap userImage = BitmapFactory.decodeStream(bitmapStream);
////                    mDrawable = new BitmapDrawable(mActivity.getResources(), userImage);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (bitmapStream != null) {
//                            bitmapStream.close();
//                        }
//                    } catch (IOException ignored) {
//                    }
//                }
////                final BitmapDrawable finalMDrawable = mDrawable;
//                mActivity.runOnUiThread(new Runnable() {
//                    public void run() {
//
////     imageView.setImageDrawable(finalMDrawable);
//                    }
//                });
//            }
//        }).start();
//    }
    }
}
