package com.example.admin.swisknife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PdfViewerFragment pdfViewerFragment = new PdfViewerFragment();
        getSupportFragmentManager().beginTransaction().replace(
                R.id.flFrame1, pdfViewerFragment, "MainActivity"
        ).commit();
    }
}
