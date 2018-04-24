package com.prd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrderConfirmationActivity extends AppCompatActivity {
    private Button oCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        oCB = findViewById(R.id.o_c_btn);

        oCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent confirmIntent = new Intent(OrderConfirmationActivity.this, MainActivity.class);
                startActivity(confirmIntent);
                finish();

            }
        });
    }
}
