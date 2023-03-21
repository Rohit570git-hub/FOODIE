package com.android.foodorderapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.foodorderapp.adapters.RestaurantListAdapter;
import com.android.foodorderapp.model.RestaurantModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RestaurantListAdapter.RestaurantListClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recycle = findViewById(R.id.recyclerView);
        recycle.addItemDecoration(new DividerItemDecoration(this,0));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Restaurant List");

        List<RestaurantModel> restaurantModelList =  getRestaurantData();

        initRecyclerView(restaurantModelList);
    }

    /**
     * its will show restlist on recycler view
     * @param restaurantModelList
     */
    private void initRecyclerView(List<RestaurantModel> restaurantModelList ) {
        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RestaurantListAdapter adapter = new RestaurantListAdapter(restaurantModelList, this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * function to get the json file convert ino list
     * @return List<RestaurantModel>
     */
    private List<RestaurantModel> getRestaurantData() {
        InputStream is = getResources().openRawResource(R.raw.restaurent);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try{
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while(( n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0,n);
            }
        }catch (Exception e) {

        }

        String jsonStr = writer.toString();
        Gson gson = new Gson();
        RestaurantModel[] restaurantModels =  gson.fromJson(jsonStr, RestaurantModel[].class);
        List<RestaurantModel> restList = Arrays.asList(restaurantModels);

        return  restList;

    }

    @Override
    public void onItemClick(RestaurantModel restaurantModel) {
        int hrsLeft= restaurantModel.getHours().getHrsLeft();
        Log.i("gethours", "onItemClick: "+restaurantModel.getHours()+restaurantModel.getHours().getTodaysHours());
        Log.i("Checking HrsLeft when", "onItemClick: "+hrsLeft);

        if(hrsLeft==0){
            Toast.makeText(this, "This isn't taking order",Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(MainActivity.this, RestaurantMenuActivity.class);
            intent.putExtra("RestaurantModel", restaurantModel);
            startActivity(intent);
      }

    }
}