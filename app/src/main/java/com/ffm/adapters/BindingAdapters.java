package com.ffm.adapters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import com.ffm.R;
import com.google.android.material.button.MaterialButton;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {
    @BindingAdapter("isGone")
    public static void bindIsGone(View view, boolean isGone) {
        view.setVisibility(isGone ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter("job_status")
    public static void configureButton(MaterialButton button, String status){
        if("Open".equals(status)){
            button.setText(R.string.start_job);
        }else if("InProgress".equals(status)){
            button.setText(R.string.complete_job);
        }
        if("Open".equals(status) || "InProgress".equals(status)){
            button.setVisibility(View.VISIBLE);
        }else{
            button.setVisibility(View.GONE);
        }
    }
    @BindingAdapter("image_big")
    public static void setImageBig(ImageView imageView, Bitmap bitmap) {
        if (bitmap == null) {
            //imageView.setImageResource(R.drawable.ic_userprofile_large);
            return;
        }
        Bitmap output;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        float r;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        imageView.setImageDrawable(new BitmapDrawable(imageView.getContext().getResources(), output));
    }
}
