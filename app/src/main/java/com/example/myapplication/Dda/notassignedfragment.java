package com.example.myapplication.Dda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class notassignedfragment extends Fragment {

    private static final String TAG = "notassignedfragment";
    private ArrayList<String>Id;
    private ArrayList<String> Address;
    private DdapendingUnassignedAdapter ddapendingUnassignedAdapter;
    private String urlget = "http://13.235.100.235:8000/api/locations/dda/unassigned";
    private String villagename;
    private String blockname;
    private String district;
    private String state;
    private String token;
    private View view;
    private String nextUrl;
    private boolean isNextBusy = false;

    public notassignedfragment(){
        Id = new ArrayList<String>(3);
        Address = new ArrayList<String>(3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notassignedfragment, container, false);


        ddapendingUnassignedAdapter = new DdapendingUnassignedAdapter(getActivity(),Id, Address);
        RecyclerView notassignedreview = view.findViewById(R.id.recyclerViewnotassigned);
        notassignedreview.setAdapter(ddapendingUnassignedAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        notassignedreview.setLayoutManager(layoutManager);

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token","");
        Log.d(TAG, "onCreateView: "+token);


        final RequestQueue unassignedrequestqueue = Volley.newRequestQueue(getActivity());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlget, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try { ;
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    nextUrl = jsonObject.getString("next");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject c = jsonArray.getJSONObject(i);
                        villagename = c.getString("village_name");
                        blockname = c.getString("block_name");
                        district = c.getString("district");
                        state = c.getString("state");
                        Id.add(c.getString("id"));
                        Address.add(villagename+","+blockname+","+district+","+state);

                        ddapendingUnassignedAdapter.notifyDataSetChanged();
                        Log.d(TAG, "onResponse: error in this notassignedfragment"+response);
                    }
                }catch (JSONException e){
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error );
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        unassignedrequestqueue.add(jsonObjectRequest);
        notassignedreview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        Log.d(TAG, "onScrolled: " + nextUrl);
                        if (!nextUrl.equals("null") && !isNextBusy)
                            getNextLocations();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;


    }

    private void getNextLocations() {
        final RequestQueue unassignedrequestqueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, nextUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ;
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    nextUrl = jsonObject.getString("next");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        villagename = c.getString("village_name");
                        blockname = c.getString("block_name");
                        district = c.getString("district");
                        state = c.getString("state");
                        Id.add(c.getString("id"));
                        Address.add(villagename + "," + blockname + "," + district + "," + state);

                        ddapendingUnassignedAdapter.notifyDataSetChanged();
                        isNextBusy = false;
                        Log.d(TAG, "onResponse: error in this notassignedfragment" + response);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        unassignedrequestqueue.add(jsonObjectRequest);
    }

}
