package com.dev4lazy.pricecollector.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.csv2pojo.Converter;

public class MainActivity extends AppCompatActivity {
    // todo to usunąć, bo służy tylko do wygenerowania mocka bazy remote
    Converter converter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        // todo na potrzeby zaincjowania mocka bazy zdalnej
        // converter = new Converter(this);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {
        if ((grantResults.length > 0) && (grantResults[0] ==
                PackageManager.PERMISSION_GRANTED)) {
            // Dostęp nadany
            switch (requestCode) {
                // todo to usunąć, bo służy tylko do wygenerowania mocka bazy remote
                case Converter.MY_PERMISSIONS_REQUEST_STORAGE: {
                    converter.prepare();
                    converter.convert();
                    break;
                }
            }
        } else {
            // Dostęp nie udany. Wyświetlamy Toasta
            Toast.makeText(getApplicationContext(), getString(R.string.access_denied),
                    Toast.LENGTH_LONG).show();
        }
    }
}
