package com.rrvq.listacompras;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_Premium extends AppCompatActivity implements PurchasesUpdatedListener {

    BillingClient billingClient;
    List<String> skuList = new ArrayList<>();

        String id = "purchase_premium";
//    String id = "android.test.purchased";

    Button btnAdquirir;
    TextView tvPrice;
    ImageButton ibx;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__premium);

        castingview();

        ibx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        btnAdquirir.setEnabled(false); // para que cuando compre se coloque el boton en falso



        Boolean b = getBoolFromPref(this, "myPref", id);
        if (b == true){

            btnAdquirir.setVisibility(View.INVISIBLE);
            Toast.makeText(this, getResources().getString(R.string.premiumBuscado), Toast.LENGTH_LONG).show();
            tvPrice.setText(getResources().getString(R.string.erespremium));

        }else {
            setupBillingClient();
        }



    }

    private void castingview(){

        tvPrice = findViewById(R.id.tvPrice);
        btnAdquirir = findViewById(R.id.btnAdquirir);

        ibx = findViewById(R.id.ibx);

        view = findViewById(R.id.activity_premium);

    }



    // este lo cree yo
    private void setupBillingClient() {

        // este es metodo de google play
//        Activity activity = this;
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // BillingClient est?? listo. Puedes consultar las compras aqu??.

                    //este lo cree yo
                    loadAllSkus();



                    /*Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
                    for (Purchase sourcePurchase : purchasesResult.getPurchasesList()) {
//                            if (sourcePurchase != null) {
                        if (sourcePurchase.getSku().equals(id)) {

                            handlePurchase(sourcePurchase);

                        } else {
                            loadAllSkus();
                        }
                    }*/



                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // se puede implementar un metodo recursivo para que vuelva a llamar porque
                //cuendo esta en stop la app se cancela para que retome cuando abra d enuevo
                // Intente reiniciar la conexi??n en la pr??xima solicitud para
                // Google Play llamando al m??todo startConnection ().
                Toast.makeText(MainActivity_Premium.this, getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // esto lo cree yo
    // esto es para verificar cuando ya halla comprado o si no muestre el precio
    private void loadAllSkus(){
        //lo hice yo
        if (billingClient.isReady()){
            // de google play
            skuList.add(id);
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP); // aqui uede ir compra completa o SUB (subcripcion)
            // aca hace llamado a internet solo asi puede comprar o ver preios
            billingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult,
                                                         List<SkuDetails> skuDetailsList) {
                            // Process the result.

                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                                    && skuDetailsList != null) {
                                for (final SkuDetails skuDetails : skuDetailsList) {
                                    String sku = skuDetails.getSku();
                                    String price = skuDetails.getPrice();
//                                    if ("premium_upgrade".equals(sku)) {
                                    if (id.equals(sku)) {

                                        // esta parte es para cuando no halla conprado aun
//                                        String precio = price;
//                                        tvPrice.setText("El precio es: " + price);
                                        tvPrice.setText(getResources().getString(R.string.eliminarAnuncios) + " " + price);
                                        btnAdquirir.setEnabled(true);



                                        //aqui puedo llamar a un listener del boton o hacer un objeto VIEW para el onclik
                                        btnAdquirir.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                        .setSkuDetails(skuDetails)
                                                        .build();
                                                billingClient.launchBillingFlow(MainActivity_Premium.this, flowParams);
                                            }
                                        });



                                    }
                                }
                            }


                        }
                    });
        }
    }


    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

        int responseCode = billingResult.getResponseCode();

        if (responseCode == BillingClient.BillingResponseCode.OK && purchases != null){

            //OK
            for (Purchase purchase : purchases){
                handlePurchase(purchase);
            }

        }else if (responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED){

            // ya esta pago
            setBoolInPref(this, "myPref", id, true);
            Toast.makeText(this, getResources().getString(R.string.usuarioPremium), Toast.LENGTH_LONG).show();
//            Snackbar.make(view, getResources().getString(R.string.usuarioPremium), Snackbar.LENGTH_SHORT).show();

            finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            // con la de arriba se elimian todas todas menos la que se llamo
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);  // con esta elimina solo las q estan delante de la que se llamo
            startActivity(intent);

        }else if (responseCode == BillingClient.BillingResponseCode.USER_CANCELED){

            Toast.makeText(this, getResources().getString(R.string.desconectadodelpago), Toast.LENGTH_LONG).show();
            // cancelo el pago
        }
    }

    private void handlePurchase(Purchase purchase) {


//        if (purchase.getSku().equals(id)){
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {

            // cambio a true el sharedP osea a premium
            setBoolInPref(this, "myPref", id, true);


            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();

                AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener =
                        new AcknowledgePurchaseResponseListener() {
                            @Override
                            public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                                setBoolInPref(MainActivity_Premium.this, "myPref", id, true);
                            }
                        };

                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }

            Toast.makeText(this, getResources().getString(R.string.erespremium), Toast.LENGTH_LONG).show();
//            Snackbar.make(view, getResources().getString(R.string.erespremium), Snackbar.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            // con la de arriba se elimian todas todas menos la que se llamo
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);  // con esta elimina solo las q estan delante de la que se llamo
            startActivity(intent);


        }
    }



    private Boolean getBoolFromPref(Context context, String prefName, String constantName){

        SharedPreferences pref = context.getSharedPreferences(prefName, 0);
        return pref.getBoolean(constantName, false);

    }

    private void setBoolInPref(Context context, String prefName, String constantName, Boolean val){

        SharedPreferences pref = context.getSharedPreferences(prefName,0);

        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(constantName, val);
        editor.commit();
    }




}