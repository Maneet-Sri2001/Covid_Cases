 package com.example.covidvaccinetrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

 public class Detail extends AppCompatActivity {

     private int countPosi;
     TextView tvdCases, tvdRecovered, tvdCritical, tvdActive, tvdTodayCases, tvdTotalDeaths, tvdTodDeath, tvdAffCount;

     @Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         if (item.getItemId() == android.R.id.home)
             finish();
         return super.onOptionsItemSelected(item);
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        countPosi = getIntent().getIntExtra("position", 0);

        getSupportActionBar().setTitle("Details : ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvdCases = findViewById(R.id.text_cases);
        tvdRecovered = findViewById(R.id.text_recovered);
        tvdCritical = findViewById(R.id.text_critical);
        tvdActive = findViewById(R.id.text_active);
        tvdTodayCases = findViewById(R.id.text_todaycases);
        tvdTotalDeaths = findViewById(R.id.text_death);
        tvdTodDeath = findViewById(R.id.text_todaydeath);
        tvdAffCount = findViewById(R.id.text_countryAff);

        tvdCases.setText(AffecCountry.modalList.get(countPosi).getCases());
        tvdRecovered.setText(AffecCountry.modalList.get(countPosi).getRecovered());
        tvdCritical.setText(AffecCountry.modalList.get(countPosi).getCritical());
        tvdActive.setText(AffecCountry.modalList.get(countPosi).getActive());
        tvdTodayCases.setText(AffecCountry.modalList.get(countPosi).getTodayCases());
        tvdTotalDeaths.setText(AffecCountry.modalList.get(countPosi).getDeath());
        tvdTodDeath.setText(AffecCountry.modalList.get(countPosi).getTodDeaths());
    }
}