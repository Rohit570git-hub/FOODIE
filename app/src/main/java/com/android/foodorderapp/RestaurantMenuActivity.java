package com.android.foodorderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.foodorderapp.adapters.MenuListAdapter;
import com.android.foodorderapp.model.Hours;
import com.android.foodorderapp.model.Menu;
import com.android.foodorderapp.model.RestaurantModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMenuActivity extends AppCompatActivity implements MenuListAdapter.MenuListClickListener {
    private List<Menu> menuList = null;
    private MenuListAdapter menuListAdapter;
    private List<Menu> itemsInCartList;
    private int totalItemInCart = 0;
    private TextView itemsCount;
    private TextView cartPrice;
    private float totalItemInCartPrice=0;
    private RelativeLayout buttonCheckout;
    private ScrollView scrollView;
    String monday;
    String tuesday;
    String wednesday;
    String thursday;
    String friday;
    String saturday;
    String sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        RestaurantModel restaurantModel = getIntent().getParcelableExtra("RestaurantModel");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Items available here");
//        actionBar.setSubtitle(restaurantModel.getAddress());
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView restrauntName = findViewById(R.id.restaurantName);
        TextView restrauntAddress = findViewById(R.id.restaurantAddress);
        ImageView restrauntImage = findViewById(R.id.restaurantImage);
        TextView restrauntTimeUpdate = findViewById(R.id.restaurantTimeUpdate);

        restrauntName.setText(restaurantModel.getName());
        restrauntAddress.setText(restaurantModel.getAddress());

         monday=restaurantModel.getHours().getMonday();
         tuesday=restaurantModel.getHours().getTuesday();
        wednesday=restaurantModel.getHours().getWednesday();
        thursday=restaurantModel.getHours().getThursday();
         friday=restaurantModel.getHours().getFriday();
         saturday=restaurantModel.getHours().getSaturday();
         sunday=restaurantModel.getHours().getSunday();
        Log.i("in menupage ", "restraunt: "+restaurantModel.getAddress()+","+restaurantModel.getImage()+","+restaurantModel.getHours().getTodaysHours()+",,"+restaurantModel.getMenus());
        Glide.with(restrauntImage).load(restaurantModel.getImage()).into(restrauntImage);
        int hrsLeft= restaurantModel.getHours().getHrsLeft();
        Log.i("in menu page ", "onCreate: "+restaurantModel.getHours()+restaurantModel.getDelivery_charge());
        if(hrsLeft==0){
            restrauntTimeUpdate.setText("Closed");
            restrauntTimeUpdate.setTextColor(Color.parseColor("#F63E3E"));
            //  holder.card.setCardBackgroundColor(Color.parseColor("#60000000"));

        }else if(hrsLeft==1) {
            restrauntTimeUpdate.setText("Closing Soon");
            restrauntTimeUpdate.setTextColor(Color.parseColor("#F63E3E"));
        }
        else{
            restrauntTimeUpdate.setText("Open Now");
            restrauntTimeUpdate.setTextColor(Color.parseColor("#32A638"));
        }

        menuList = restaurantModel.getMenus();
        initRecyclerView();


         itemsCount = findViewById(R.id.itemsCount);
         cartPrice =findViewById(R.id.cartPrice);
         buttonCheckout=findViewById(R.id.buttonCheckout);
        scrollView=findViewById(R.id.scrollView);
        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemsInCartList != null && itemsInCartList.size() <= 0) {
//                    Toast.makeText(RestaurantMenuActivity.this, "Please add some items in cart.", Toast.LENGTH_SHORT).show();
                    return;
                }
                restaurantModel.setMenus(itemsInCartList);
                Intent i = new Intent(RestaurantMenuActivity.this, PlaceYourOrderActivity.class);
                i.putExtra("RestaurantModel", restaurantModel);
                startActivityForResult(i, 1000);
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuListAdapter = new MenuListAdapter(this,menuList, this);
        recyclerView.setAdapter(menuListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
    @Override
    public void onAddToCartClick(Menu menu) {
        if(itemsInCartList == null) {
            itemsInCartList = new ArrayList<>();
        }
        itemsInCartList.add(menu);
        totalItemInCart = 0;
        totalItemInCartPrice=0;

        for(Menu m : itemsInCartList) {
            totalItemInCart = totalItemInCart + m.getTotalInCart();
            totalItemInCartPrice=totalItemInCartPrice+(m.getTotalInCart()*m.getPrice());
        }
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) scrollView.getLayoutParams();
        marginParams.setMargins(0, 0, 0, (int) (350/(getResources().getDisplayMetrics().density)));
        itemsCount.setText(" "+ totalItemInCart);
        cartPrice.setText("$"+totalItemInCartPrice);
        buttonCheckout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUpdateCartClick(Menu menu) {
        if(itemsInCartList.contains(menu)) {
            int index = itemsInCartList.indexOf(menu);
            itemsInCartList.remove(index);
            itemsInCartList.add(index, menu);

            totalItemInCart = 0;
            totalItemInCartPrice=0;

            for(Menu m : itemsInCartList) {
                totalItemInCart = totalItemInCart + m.getTotalInCart();
                Log.i("Check total item value", "onUpdateCartClick: "+m.getTotalInCart());
                totalItemInCartPrice=totalItemInCartPrice+(m.getTotalInCart()*m.getPrice());
            }
            itemsCount.setText(" "+ totalItemInCart);
            cartPrice.setText("$"+totalItemInCartPrice);
        }
    }

    @Override
    public void onRemoveFromCartClick(Menu menu) {
        if(itemsInCartList.contains(menu)) {
            itemsInCartList.remove(menu);
            totalItemInCart = 0;
            totalItemInCartPrice=0;
            for(Menu m : itemsInCartList) {
                totalItemInCart = totalItemInCart + m.getTotalInCart();
                totalItemInCartPrice=totalItemInCartPrice+(m.getTotalInCart()*m.getPrice());
            }
            itemsCount.setText(" "+ totalItemInCart);
            cartPrice.setText("$"+totalItemInCartPrice);
        }
        if(itemsInCartList.size() <= 0){
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) scrollView.getLayoutParams();
            marginParams.setMargins(0, 0, 0, (int) (50/(getResources().getDisplayMetrics().density)));
            buttonCheckout.setVisibility(View.GONE);
            Toast.makeText(RestaurantMenuActivity.this, "Your Food Cart is Empty😢", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
            default:
                //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            //
            finish();
        }
    }

    public void onOpeninghrsPopupWindowClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        //this is custom dialog
        //custom_popup_dialog contains textview only
        View customView = layoutInflater.inflate(R.layout.popup_window_openinghrs, null);
        //myDialog.setContentView(R.layout.popup_window_openinghrs);
        TextView mondayT=customView.findViewById(R.id.textViewMonday);
        TextView tuesdayT=customView.findViewById(R.id.textViewTuesday);
        TextView wednesdayT=customView.findViewById(R.id.textViewWednesday);
        TextView thrusdayT=customView.findViewById(R.id.textViewThursday);
        TextView fridayT=customView.findViewById(R.id.textViewFriday);
        TextView saturdayT=customView.findViewById(R.id.textViewSaturday);
        TextView sundayT=customView.findViewById(R.id.textViewSunday);

        mondayT.setText(monday);
        tuesdayT.setText(tuesday);
        wednesdayT.setText(wednesday);
        thrusdayT.setText(thursday);
        fridayT.setText(friday);
        saturdayT.setText(saturday);
        sundayT.setText(sunday);

        builder.setView(customView);
        builder.create();
        builder.show();

    }
}