package com.bk2rl.pokevoice.view;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bk2rl.pokevoice.entity.SoundItem;
import com.bk2rl.pokevoice.R;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements SoundItemFragment.OnListFragmentInteractionListener,
       /* AddMusicFragment.OnFragmentInteractionListener ,*/
        SoundItemFragment.OnScreenLoading {
    private HashMap<Class,Fragment> fragments = new HashMap<>();
    private FloatingActionButton fab;
    private MediaPlayer mediaPlayer;

    public HashMap<Class, Fragment> getFragments() {
        return fragments;
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        fragments.put(SoundItemFragment.class,SoundItemFragment.newInstance(3));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragments.get(SoundItemFragment.class)).commit();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mediaPlayer = new MediaPlayer();
    }

    @Override
    protected void onPause() {
//        ((MainApplication) getApplication()).saveList();
        super.onPause();
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(SoundItem mItem) {
        ((SoundItemFragment) getFragments().get(SoundItemFragment.class)).updatePockemonInfo(mItem);
    }

//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }

    @Override
    public void onImageLoaded() {
        ((SoundItemFragment) getFragments().get(SoundItemFragment.class)).onImageLoaded();
    }
}
