package com.example.myapplication.Dda;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Ado.ReviewReport;
import com.example.myapplication.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class DdacompletedAdapter extends RecyclerView.Adapter<DdacompletedAdapter.ViewHolderCompletedDda> {
    private static final String TAG = "DdacompletedAdapter";
    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;
    Context mcontext;
    public boolean showcomletedshimmer = true;
    private int shimmer_item_count = 6;

    public DdacompletedAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
    }

    @NonNull
    @Override
    public DdacompletedAdapter.ViewHolderCompletedDda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.notassignedlist,parent,false);
        final ViewHolderCompletedDda viewHolderCompletedDda = new ViewHolderCompletedDda(view);
        viewHolderCompletedDda.cardcompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showcomletedshimmer) {
                    Intent intent = new Intent(mcontext, ReviewReport.class);
                    intent.putExtra("isDdo", true);
                    intent.putExtra("id", mtextview1.get(viewHolderCompletedDda.getAdapterPosition()));
                    intent.putExtra("isComplete", true);
                    mcontext.startActivity(intent);
                }
            }
        });
        return viewHolderCompletedDda;
    }

    @Override
    public void onBindViewHolder(@NonNull DdacompletedAdapter.ViewHolderCompletedDda holder, int position) {


        if(showcomletedshimmer){
            holder.shimmercomleted.startShimmer();
        }
        else {
            holder.shimmercomleted.stopShimmer();
            holder.shimmercomleted.setShimmer(null);
            holder.tv1.setBackground(null);
            holder.tv2.setBackground(null);
            holder.tv1.setText(mtextview1.get(position));
            holder.tv2.setText(mtextview2.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return showcomletedshimmer ? shimmer_item_count : mtextview1.size();
    }

    public class ViewHolderCompletedDda extends RecyclerView.ViewHolder{

        TextView tv1;
        TextView tv2;

        CardView cardcompleted;
        ShimmerFrameLayout shimmercomleted;

        public ViewHolderCompletedDda(@NonNull View itemView) {
            super(itemView);
            mcontext = itemView.getContext();
            cardcompleted = itemView.findViewById(R.id.card_unassigned);
            tv1 = itemView.findViewById(R.id.lid);
            tv2 = itemView.findViewById(R.id.address);
            shimmercomleted = itemView.findViewById(R.id.shimmer_unassigned);

        }

    }
}
