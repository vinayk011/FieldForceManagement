package com.ffm.adapters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ffm.R;
import com.ffm.util.DateUtil;
import com.ffm.util.IssueStatus;
import com.google.android.material.button.MaterialButton;

import androidx.databinding.BindingAdapter;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

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
            case IN_PROGRESS:
                button.setText(R.string.complete_job);
                break;
            case PAUSED:
                button.setText(R.string.pause_job);
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
            case IN_PROGRESS:
            case PAUSED:
                if (R.id.rl_customer_call_response == id) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.VISIBLE);
                }
                break;
            case COMPLETED:
                if (R.id.rl_completed == id) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                break;
        }
    }

    @BindingAdapter("layout_job_status_cl")
    public static void configureCLIssueDetails(View view, String status) {
        int id = view.getId();
        view.setVisibility(View.GONE);
        switch (IssueStatus.getIssueStatus(status)) {
            case COMPLETED:
                if (R.id.rl_completed == id) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                break;
        }
    }


    @BindingAdapter({"status", "updated_time"})
    public static void setStatus(TextView textView, String status, String updatedTime) {
        String text = textView.getResources().getString(R.string.job) + " " + status.toLowerCase();
        SpannableString span1 = new SpannableString(text);
        span1.setSpan(new AbsoluteSizeSpan((int) textView.getContext().getResources().getDimension(R.dimen._10sdp))
                , 0, text.length(), SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString span2 = new SpannableString(updatedTime);
        span2.setSpan(new AbsoluteSizeSpan((int) textView.getContext().getResources().getDimension(R.dimen._10sdp))
                , 0, updatedTime.length(), SPAN_INCLUSIVE_INCLUSIVE);

        CharSequence finalText = TextUtils.concat(span1, " - ", span2);
        textView.setText(finalText);

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
