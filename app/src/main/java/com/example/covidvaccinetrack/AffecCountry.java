package com.example.covidvaccinetrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AffecCountry extends AppCompatActivity {

    EditText edtSearch;
    ListView listView;
    SimpleArcLoader loader;

    public static List<Modal> modalList = new ArrayList<>();
    Modal modal;
    Adaptor adaptor;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affec_country);

        getSupportActionBar().setTitle("Affected Country");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtSearch = findViewById(R.id.edt_search);
        listView = findViewById(R.id.country_list);
        loader =  findViewById(R.id.loader);

        fetchData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), Detail.class).putExtra("position", position));
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptor.getFilter().filter(s);
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void fetchData() {

        String url = "https://disease.sh/v3/covid-19/countries";

        loader.start();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for(int i=0; i<array.length(); i++) {
                       JSONObject object = array.getJSONObject(i);
                       String countName = object.get("country").toString();
                       String flag = object.getJSONObject("countryInfo").get("flag").toString();
                        String cases = object.get("cases").toString(),
                                todayCases = object.get("todayCases").toString(),
                                death = object.get("deaths").toString(),
                                todDeaths = object.get("todayDeaths").toString(),
                                recovered = object.get("recovered").toString(),
                                active = object.get("active").toString(),
                                critical = object.get("critical").toString();

                        modal = new Modal(flag, countName, cases, todayCases, death, todDeaths, recovered, active, critical);
                        modalList.add(modal);
                    }

                    adaptor =new Adaptor(AffecCountry.this, modalList);
                    listView.setAdapter(adaptor);
                    loader.stop();
                    loader.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    loader.stop();
                    loader.setVisibility(View.INVISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AffecCountry.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                loader.stop();
                loader.setVisibility(View.INVISIBLE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}