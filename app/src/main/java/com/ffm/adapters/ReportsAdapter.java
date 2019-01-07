package com.ffm.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.ffm.R;
import com.ffm.binding.ViewHolderBinding;
import com.ffm.databinding.ListItemReportBinding;
import com.ffm.db.room.entity.Report;
import com.ffm.listener.DeviceDiffUtilCallback;
import com.ffm.listener.ListItemListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import static com.ffm.constants.IntentConstants.REPORT_ID;


public class ReportsAdapter extends RecyclerView.Adapter {

    private ArrayList<Report> reports = new ArrayList<>();
    private Context mContext;
    private ListItemListener listItemListener;

    public ReportsAdapter(ArrayList<Report> reports, Context mContext, ListItemListener listItemListener) {
        setReports(reports, false);
        this.mContext = mContext;
        this.listItemListener = listItemListener;
    }

    public void setReports(ArrayList<Report> newReports, boolean callBackRequired) {
        if (callBackRequired) {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DeviceDiffUtilCallback(newReports, reports));

            diffResult.dispatchUpdatesTo(new ListUpdateCallback() {
                @Override
                public void onInserted(int position, int count) {
                    Log.i("onInserted", "position" + position + "--" + count);
                    notifyItemInserted(position);
                    listItemListener.onChanged(true);
                }

                @Override
                public void onRemoved(int position, int count) {
                    Log.i("onRemoved", "position");
                    notifyItemRemoved(position);
                    listItemListener.onChanged(true);
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    Log.i("onMoved", "position");
                }

                @Override
                public void onChanged(int position, int count, Object payload) {
                    Log.i("onChanged", "position");
                    notifyItemChanged(position, payload);
                    listItemListener.onChanged(true);
                }
            });
            reports.clear();
            this.reports.addAll(newReports);
        } else {
            reports.clear();
            this.reports.addAll(newReports);
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.list_item_report, parent, false);
        return new ViewHolderBinding(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Report report = reports.get(holder.getAdapterPosition());
        final ListItemReportBinding itemDeviceBinding = (ListItemReportBinding) ((ViewHolderBinding) holder).binding;
        itemDeviceBinding.layoutHandler.setOnClickListener(v -> listItemListener.onItemClick(position));
        itemDeviceBinding.setReportInfo(report);
        itemDeviceBinding.executePendingBindings();

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle o = (Bundle) payloads.get(0);
            for (String key : o.keySet()) {
                if (key.equals(REPORT_ID)) {
                    final ListItemReportBinding itemDeviceBinding = (ListItemReportBinding) ((ViewHolderBinding) holder).binding;
                    itemDeviceBinding.layoutHandler.setOnClickListener(v -> listItemListener.onItemClick(position));
                    itemDeviceBinding.setReportInfo((Report) o.getSerializable(REPORT_ID));
                    itemDeviceBinding.executePendingBindings();
                    //holder.mPrice.setTextColor(Color.GREEN);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public void removeItem(int position) {
        reports.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Report deletedModel, int deletedPosition) {
        reports.add(deletedPosition, deletedModel);
        notifyItemInserted(deletedPosition);
    }
}
