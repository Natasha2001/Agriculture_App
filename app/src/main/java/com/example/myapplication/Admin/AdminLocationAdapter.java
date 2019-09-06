

package com.example.myapplication.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class AdminLocationAdapter extends RecyclerView.Adapter<AdminLocationAdapter.ViewHolder>{
    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;
    ArrayList<String> mtextview3;
    ArrayList<String> mtextview4;
    boolean mShowShimmer = true;
    private int SHIMMER_ITEM_NO = 5;
    Context mcontext;

    public AdminLocationAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2,ArrayList<String> mtextview3,ArrayList<String> mtextview4) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mtextview3 = mtextview3;
        this.mtextview4 = mtextview4;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.adminlocationlist,parent,false);
        ViewHolder viewHolderDda = new ViewHolder(view);
        return viewHolderDda;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mShowShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);
            holder.tv1.setBackground(null);
            holder.tv2.setBackground(null);
            holder.tv3.setBackground(null);
            holder.tv4.setBackground(null);
            holder.tv1.setText(mtextview1.get(position));
            holder.tv2.setText(mtextview2.get(position));
            holder.tv3.setText(mtextview3.get(position));
            holder.tv4.setText(mtextview4.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mShowShimmer ? SHIMMER_ITEM_NO : mtextview1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        RelativeLayout parentnotassigned;
        ShimmerFrameLayout shimmerFrameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentnotassigned = itemView.findViewById(R.id.parentnotassigned);
            tv1 = itemView.findViewById(R.id.title);
            tv2 = itemView.findViewById(R.id.date1);
            tv3 = itemView.findViewById(R.id.Time1);
            tv4 = itemView.findViewById(R.id.address1);
            shimmerFrameLayout = itemView.findViewById(R.id.locations_shimmer);
        }


    }

}