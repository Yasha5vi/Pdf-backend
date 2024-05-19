package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PdfViewerActivity extends AppCompatActivity {

    private ImageView pdfImageView;
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor fileDescriptor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pdfImageView = findViewById(R.id.pdfImageView);

        String pdfFileName = getIntent().getStringExtra("pdfFileName");

        try {
            openRenderer(pdfFileName);
            showPage(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openRenderer(String fileName) throws IOException {
        File file = new File(getCacheDir(), fileName);
        if (!file.exists()) {
            InputStream asset = getAssets().open(fileName);
            FileOutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = asset.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.close();
            asset.close();
        }
        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        pdfRenderer = new PdfRenderer(fileDescriptor);
    }

    private void showPage(int index) {
        if (pdfRenderer.getPageCount() <= index) {
            return;
        }
        if (currentPage != null) {
            currentPage.close();
        }
        currentPage = pdfRenderer.openPage(index);
        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        pdfImageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onDestroy() {
        try {
            if (currentPage != null) {
                currentPage.close();
            }
            pdfRenderer.close();
            fileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
