package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CountryArrayAdapter countryArrayAdapter;

    // local storage
    private SharedPreferences sharedPreferences;

    /*swipe layout*/
    private SwipeRefreshLayout pullToRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener swipeRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
        fetchData();
    }

    // setup
    private void setup() {
        pullToRefreshLayout = findViewById(R.id.pullToRefresh);
        swipeRefreshListener = this::fetchData;
        pullToRefreshLayout.setOnRefreshListener(swipeRefreshListener);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    // fetch countries data
    private void fetchData() {
        //loading
        pullToRefreshLayout.setRefreshing(true);

        // sanity check
        if(!Utils.isNetworkAvailable(this)) {
            // load latest offline data
            String strJson = sharedPreferences.getString("countries", "0");
            try {
                JSONArray data = new JSONArray(strJson);
                fill_adapter(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://restcountries.com/v3.1/all";

        // Request a string response from the provided URL.
        JsonArrayRequest request = new JsonArrayRequest (Request.Method.GET, url,  null, this::fill_adapter, Throwable::printStackTrace);

        // Add the request to the RequestQueue.
        queue.add(request);
    }

    // fill recycler view's adapter with the country data
    private void fill_adapter(JSONArray response) {
        // save the data in shared preferences for offline mode
        sharedPreferences.edit().putString("countries", response.toString()).apply();

        countryArrayAdapter = new CountryArrayAdapter(this,response);
        recyclerView.setAdapter(countryArrayAdapter);

        pullToRefreshLayout.setRefreshing(false);
    }
}