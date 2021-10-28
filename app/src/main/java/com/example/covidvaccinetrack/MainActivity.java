package com.example.covidvaccinetrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView tvCases, tvRecovered, tvCritical, tvActive, tvTodayCases, tvTotalDeaths, tvTodDeath, tvAffCount;
    SimpleArcLoader loader;
    ScrollView scrollView;
    PieChart pieChart;
    Button btnAffCoun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCases = findViewById(R.id.text_cases);
        tvRecovered = findViewById(R.id.text_recovered);
        tvCritical = findViewById(R.id.text_critical);
        tvActive = findViewById(R.id.text_active);
        tvTodayCases = findViewById(R.id.text_todaycases);
        tvTotalDeaths = findViewById(R.id.text_death);
        tvTodDeath = findViewById(R.id.text_todaydeath);
        tvAffCount = findViewById(R.id.text_countryAff);
        btnAffCoun = findViewById(R.id.track_btn);

        loader = findViewById(R.id.loader);
        scrollView = findViewById(R.id.scroll_details);
        pieChart = findViewById(R.id.piechart);

        fetchData();

        btnAffCoun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AffecCountry.class));
            }
        });


    }

    private void fetchData() {

        String url = "https://disease.sh/v3/covid-19/all";

        loader.start();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response.toString());

                    tvCases.setText(object.getString("cases"));
                    tvRecovered.setText(object.getString("recovered"));
                    tvCritical.setText(object.getString("critical"));
                    tvTodayCases.setText(object.getString("todayCases"));
                    tvActive.setText(object.getString("active"));
                    tvTotalDeaths.setText(object.getString("deaths"));
                    tvTodDeath.setText(object.getString("todayDeaths"));
                    tvAffCount.setText(object.getString("affectedCountries"));

                    pieChart.addPieSlice(new PieModel("Cases", Integer.parseInt(tvCases.getText().toString()), Color.parseColor("#FFA726")));
                    pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29B6F6")));
                    pieChart.addPieSlice(new PieModel("Total Deaths", Integer.parseInt(tvTotalDeaths.getText().toString()), Color.parseColor("#EF5350")));
                    pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));

                    pieChart.startAnimation();

                    loader.stop();
                    loader.setVisibility(View.INVISIBLE);
                    scrollView.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                    loader.stop();
                    loader.setVisibility(View.INVISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                loader.stop();
                loader.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}