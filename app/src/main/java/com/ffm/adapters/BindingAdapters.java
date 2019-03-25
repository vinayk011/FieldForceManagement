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
import com.ffm.util.IssueStatus;
import com.google.android.material.button.MaterialButton;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {
    @BindingAdapter("isGone")
    public static void bindIsGone(View view, boolean isGone) {
        view.setVisibility(isGone ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter("job_status")
    public static void configureButton(MaterialButton button, String status) {
        button.setVisibility(View.VISIBLE);
        switch (IssueStatus.getIssueStatus(status)) {
            case NEW:
            case ACCEPTED:
            case ASSIGNED:
                button.setText(R.string.start_job);
                break;
            case STARTED:
            case RE_OPENED:
            case PAUSED:
                button.setText(R.string.complete_job);
                break;
            case COMPLETED:
                button.setVisibility(View.GONE);
                break;
        }
    }

    @BindingAdapter("layout_job_status")
    public static void configureIssueDetails(View view, String status) {
        int id = view.getId();
        view.setVisibility(View.GONE);
        switch (IssueStatus.getIssueStatus(status)) {
            case NEW:
            case ACCEPTED:
            case ASSIGNED:
            case RE_OPENED:
                if (R.id.rl_customer_call_response == id) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                break;
            case STARTED:
            case COMPLETED:
                if (R.id.rl_customer_call_response == id) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.VISIBLE);
                }
                break;
            case PAUSED:
                if (R.id.rl_completed == id) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                break;
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
