package com.ffm.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.ffm.R;
import com.ffm.binding.ViewHolderBinding;
import com.ffm.databinding.ListItemReportBinding;
import com.ffm.db.room.entity.Complaint;
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

import static com.ffm.constants.IntentConstants.ISSUE_ID;


public class ComplaintsAdapter extends RecyclerView.Adapter {

    private ArrayList<Complaint> complaints = new ArrayList<>();
    private Context mContext;
    private ListItemListener listItemListener;

    public ComplaintsAdapter(ArrayList<Complaint> complaints, Context mContext, ListItemListener listItemListener) {
        setReports(complaints, false);
        this.mContext = mContext;
        this.listItemListener = listItemListener;
    }

    public void setReports(ArrayList<Complaint> newComplaints, boolean callBackRequired) {
        if (callBackRequired) {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DeviceDiffUtilCallback(newComplaints, complaints));

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
            complaints.clear();
            this.complaints.addAll(newComplaints);
        } else {
            complaints.clear();
            this.complaints.addAll(newComplaints);
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
        final Complaint complaint = complaints.get(holder.getAdapterPosition());
        final ListItemReportBinding itemDeviceBinding = (ListItemReportBinding) ((ViewHolderBinding) holder).binding;
        itemDeviceBinding.layoutHandler.setOnClickListener(v -> listItemListener.onItemClick(position));
        itemDeviceBinding.setComplaintInfo(complaint);
        itemDeviceBinding.executePendingBindings();

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle o = (Bundle) payloads.get(0);
            for (String key : o.keySet()) {
                if (key.equals(ISSUE_ID)) {
                    final ListItemReportBinding itemDeviceBinding = (ListItemReportBinding) ((ViewHolderBinding) holder).binding;
                    itemDeviceBinding.layoutHandler.setOnClickListener(v -> listItemListener.onItemClick(position));
                    itemDeviceBinding.setComplaintInfo((Complaint) o.getSerializable(ISSUE_ID));
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
        return complaints.size();
    }

    public void removeItem(int position) {
        complaints.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Complaint deletedModel, int deletedPosition) {
        complaints.add(deletedPosition, deletedModel);
        notifyItemInserted(deletedPosition);
    }
}
