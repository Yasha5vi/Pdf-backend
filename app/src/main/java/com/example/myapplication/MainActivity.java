package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_pdf1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdf("pdf1.pdf");
            }
        });

        findViewById(R.id.button_pdf2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdf("pdf2.pdf");
            }
        });
    }

    private void openPdf(String pdfFileName) {
        Intent intent = new Intent(MainActivity.this, PdfViewerActivity.class);
        intent.putExtra("pdfFileName", pdfFileName);
        startActivity(intent);
    }
}
