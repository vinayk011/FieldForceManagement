package com.ffm.fragment;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ffm.R;
import com.ffm.activity.HomeActivity;
import com.ffm.constants.IntentConstants;
import com.ffm.databinding.FragmentImageCropBinding;
import com.ffm.db.paper.PaperDB;
import com.ffm.preference.AppPrefConstants;
import com.ffm.preference.AppPreference;
import com.ffm.util.Trace;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;

import java.io.File;
import java.io.FileOutputStream;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

public class ImageCropFragment extends BaseFragment<FragmentImageCropBinding> {
    private String imagePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_crop, container, false);
        if (getArguments() != null) {
            imagePath =  getArguments().getString(IntentConstants.EXTRA, null);
            initCropper();
        } else {
            close();
        }
        return binding.getRoot();
    }

    private void close() {
        snackBarView(getString(R.string.failed_try_again));
        ((HomeActivity) context).onBackPressed();
    }

    private void initCropper() {
        binding.fab.setImageDrawable(context.getDrawable(R.drawable.ic_check));
        binding.cropImageView.startLoad(Uri.parse("file://" + imagePath),
                new LoadCallback() {
                    @Override
                    public void onError(Throwable e) {
                        Trace.e(e.getMessage());
                        close();
                    }

                    @Override
                    public void onSuccess() {
                        ready();
                    }
                });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.cropImageView.startCrop(

                        Uri.parse(imagePath),

                        new CropCallback() {
                            @Override
                            public void onError(Throwable e) {
                                Trace.e(e.getMessage());
                                close();
                            }

                            @Override
                            public void onSuccess(Bitmap bitmap) {
                                if (bitmap != null) {
                                    if (bitmap.getWidth() < 250 && bitmap.getHeight() < 250) {
                                        snackBarView(getString(R.string.picture_crop_size_too_small));
                                    } else {
                                        new SetThumbnail(bitmap, new File(imagePath)).execute();
                                    }
                                } else {
                                    close();
                                }
                            }
                        },

                        new SaveCallback() {
                            @Override
                            public void onSuccess(Uri outputUri) {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Trace.e(e.getMessage());
                            }
                        }
                );
            }
        });
    }

    private class SetThumbnail extends AsyncTask<Void, Void, Boolean> {
        Bitmap bitmap;
        File thumbPath;

        SetThumbnail(Bitmap bitmap, File thumbPath) {
            this.bitmap = bitmap;
            this.thumbPath = thumbPath;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                FileOutputStream fos = new FileOutputStream(thumbPath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if (status) {
                /*UploadProfilePictureModel uploadProfilePictureModel = new UploadProfilePictureModel();
                uploadProfilePictureModel.run(context, thumbPath.getAbsolutePath()).getData().observe(ImageCropFragment.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if (aBoolean != null && aBoolean) {
                            PaperDB.getInstance().saveImageBitmap(bitmap);
                            AppPreference.getInstance().putBoolean(AppPrefConstants.PROFILE_PIC_UPDATE, true);
                            snackBarView(getString(R.string.profile_picture_uploaded));
                            ((DashboardActivity) context).onBackPressed();
                        }
                    }
                });*/
                //Todo handle image upload to server
                PaperDB.getInstance().saveImageBitmap(bitmap);
                AppPreference.getInstance().putBoolean(AppPrefConstants.JOB_PIC_UPDATE, true);
                snackBarView(getString(R.string.picture_uploaded));
                ((HomeActivity) context).onBackPressed();
            } else {
                close();
            }
        }
    }

    private void ready() {
        try {
            binding.cropImageView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
            binding.cropImageView.setOverlayColor(ContextCompat.getColor(context, R.color.black_80));
            binding.cropImageView.setFrameColor(ContextCompat.getColor(context, R.color.colorPrimary));
            binding.cropImageView.setHandleColor(ContextCompat.getColor(context, R.color.colorPrimary));
            binding.cropImageView.setGuideColor(ContextCompat.getColor(context, R.color.colorPrimary));
            binding.cropImageView.setFrameStrokeWeightInDp(2);
            binding.cropImageView.setGuideStrokeWeightInDp(1);
            binding.cropImageView.setHandleSizeInDp(5);
            binding.cropImageView.setTouchPaddingInDp(20);
            binding.cropImageView.setHandleShowMode(CropImageView.ShowMode.SHOW_ALWAYS);
            binding.cropImageView.setGuideShowMode(CropImageView.ShowMode.SHOW_ON_TOUCH);
            binding.cropImageView.setCropMode(CropImageView.CropMode.SQUARE);
            binding.cropImageView.setOutputMaxSize(250, 250);
            binding.cropImageView.setMinFrameSizeInDp(62);
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
    }

    public void resume() {
        if (getUserVisibleHint()) {
            ((HomeActivity) context).setTitle(getString(R.string.customer_location_picture));
        }
    }
}