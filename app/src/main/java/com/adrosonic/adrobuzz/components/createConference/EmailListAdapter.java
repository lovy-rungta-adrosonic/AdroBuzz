package com.adrosonic.adrobuzz.components.createConference;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.databinding.CellEmailBinding;

import java.util.List;

/**
 * Created by Lovy on 08-05-2018.
 */

public class EmailListAdapter extends
        RecyclerView.Adapter<EmailListAdapter.ViewHolder> {

    private List<String> emailList;
    private Context context;

    public EmailListAdapter(List<String> flsLst, Context ctx) {
        emailList = flsLst;
        context = ctx;
    }

    @Override
    public EmailListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        CellEmailBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.cell_email, parent, false);

        ViewHolder viewHolder = new ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String email = emailList.get(position);
        holder.cellEmailBinding.setEmail(email);
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CellEmailBinding cellEmailBinding;

        public ViewHolder(CellEmailBinding cellEmailLayoutBinding) {
            super(cellEmailLayoutBinding.getRoot());
            cellEmailBinding = cellEmailLayoutBinding;
        }
    }
}
