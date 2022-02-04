package com.meest.videoEditing.videocollage;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.meest.R;

public class SelectFrameActivity extends AppCompatActivity {
    static final boolean b = true;
    int[] a = {R.drawable.frame_2, R.drawable.frame_3, R.drawable.frame_4, R.drawable.frame_5, R.drawable.frame_6, R.drawable.frame_7, R.drawable.frame_8, R.drawable.frame_9, R.drawable.frame_10, R.drawable.frame_11, R.drawable.frame_12};


    @Override public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView( R.layout.selectvideocollagefragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText("Select Frame");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (b || supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(b);
            supportActionBar.setDisplayShowTitleEnabled(false);
            return;
        }
        throw new AssertionError();
    }

    @Override public void onStart() {
        super.onStart();
    }


    @Override public void onStop() {
        super.onStop();
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override public void onResume() {
        super.onResume();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return b;
    }

   @Override public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
            return b;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
