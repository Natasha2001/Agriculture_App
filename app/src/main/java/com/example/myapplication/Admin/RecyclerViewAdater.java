package com.example.myapplication.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Admin.AdoDdoActivity.AdoDdoActivity;
import com.example.myapplication.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewAdater extends RecyclerView.Adapter<RecyclerViewAdater.ViewHolder> {

    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;
    public boolean mShowShimmer = true;
    Context mcontext;
    private boolean isDdoFragment;
    ArrayList<String> mUserId;
    private ArrayList<String> mPkList;
    private int SHIMMER_ITEM_COUNT = 6;
    private String TAG = "RecyclerViewAdapter";

    public RecyclerViewAdater(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2, boolean isDdoFragment) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.isDdoFragment = isDdoFragment;

    }

    public RecyclerViewAdater(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2,
                              ArrayList<String> mUserId, boolean isDdoFragment, ArrayList<String> pkList) {
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mUserId = mUserId;
        this.mcontext = mcontext;
        this.isDdoFragment = isDdoFragment;
        mPkList = pkList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.listusers, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mShowShimmer) {
                    Intent intent = new Intent(mcontext, AdoDdoActivity.class);
                    intent.putExtra("Id", mUserId.get(viewHolder.getAdapterPosition()));
                    if (isDdoFragment)
                        intent.putExtra("isDdo", true);
                    else
                        intent.putExtra("isDdo", false);
                    intent.putExtra("name", mtextview1.get(viewHolder.getAdapterPosition()));
                    mcontext.startActivity(intent);
                }
            }
            });

        viewHolder.editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mShowShimmer) {
                    Intent intent = new Intent(mcontext, EditActivity.class);
                    int pos = viewHolder.getAdapterPosition();
                    intent.putExtra("id", mPkList.get(pos));
                    if (isDdoFragment)
                        intent.putExtra("isDdo", true);
                    else
                        intent.putExtra("isDdo", false);
                    intent.putExtra("place", mtextview2.get(pos));
                    mcontext.startActivity(intent);
                }
            }
        });

        viewHolder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mShowShimmer) {
                    showdialogbox("Delete " + mtextview1.get(viewHolder.getAdapterPosition()),
                            "Are you sure?", "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(mcontext, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
                                    mtextview1.remove(0);
                                    mtextview2.remove(0);
                                    deleteRecord(viewHolder.getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                            }, "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }, true);
                }
            }
        });

        return viewHolder;
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
            holder.tv1.setText(mtextview1.get(position));
            holder.tv2.setText(mtextview2.get(position));
        }

    }


    @Override
    public int getItemCount() {
        return mShowShimmer ? SHIMMER_ITEM_COUNT : mtextview1.size();
    }

    private AlertDialog showdialogbox(String title, String msg, String positiveLabel, DialogInterface.OnClickListener positiveOnclick,
                                      String negativeLabel, DialogInterface.OnClickListener negativeOnclick,
                                      boolean isCancelable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnclick);
        builder.setNegativeButton(negativeLabel, negativeOnclick);
        builder.setCancelable(isCancelable);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    private void deleteRecord(int pos) {
        String id = mUserId.get(pos);
        final String url = "http://13.235.100.235:8000/api/user/" + id + "/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences preferences = mcontext.getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = preferences.getString("token", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
        requestQueue.add(jsonObjectRequest);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1;
        TextView tv2;
        RelativeLayout parentlayout;
        RelativeLayout relativeLayout;
        ShimmerFrameLayout shimmerFrameLayout;
        ImageView editImage;
        ImageView deleteImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentlayout= itemView.findViewById(R.id.parent);
            tv1= itemView.findViewById(R.id.tvuser);
            tv2= itemView.findViewById(R.id.tvinfo);
            relativeLayout = itemView.findViewById(R.id.ddo_profile_cardview);
            shimmerFrameLayout = itemView.findViewById(R.id.ado_ddo_shimmer);
            editImage = itemView.findViewById(R.id.edit_id);
            deleteImage = itemView.findViewById(R.id.delete_id);
        }
    }
}
