package com.cunnj.activitylauncher;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AllTasksListFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                new LibsBuilder()
                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                        .withActivityTitle(getResources().getString(R.string.action_about))
                        .withAboutIconShown(true)
                        .withAboutDescription(getResources().getString(R.string.app_description))
                        .withAboutVersionShown(true)
                        .withAboutAppName(getResources().getString(R.string.app_name))
                        .withAutoDetect(true)
                        .withLicenseShown(true)
                        .start(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
