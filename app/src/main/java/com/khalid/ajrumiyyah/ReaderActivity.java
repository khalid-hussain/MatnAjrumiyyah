package com.khalid.ajrumiyyah;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.khalid.ajrumiyyah.adapter.ChapterAdapter;
import com.khalid.ajrumiyyah.loader.ChapterLoader;
import com.khalid.ajrumiyyah.model.Chapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReaderActivity extends ActionBarActivity
        implements LoaderManager.LoaderCallbacks<List<Chapter>> {
    private Toolbar toolbar;
    private TextView tvActionBarTitle;
    private TextView tvContent;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mListView;
    private ArrayAdapter<Chapter> mAdapter;
    private List<Chapter> mDrawerData;
    private ChapterAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/ScheherazadeRegOT.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);
        setContentView(R.layout.activity_main);

        initView();
        initDrawer();
        getSupportLoaderManager().initLoader(0, null, this);
    }

    public void setTextViewWithContent(String href) {
        try {
            InputStream is = getAssets().open("book/" + href);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);
            tvContent.setText(Html.fromHtml(text));
            int textGravity;
            textGravity = (href == "cover.html") ? Gravity.CENTER : Gravity.NO_GRAVITY;
            tvContent.setGravity(textGravity);
        } catch (IOException e) {
            tvContent.setText("Should not happen!");
            throw new RuntimeException(e);
        }
    }

    private void initView() {
        tvActionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        tvContent = (TextView) findViewById(R.id.tvContent);
        setTextViewWithContent("cover.html");

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (Build.VERSION.SDK_INT >= 17) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        mListView = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void initDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public Loader<List<Chapter>> onCreateLoader(int id, Bundle bundle) {
        return new ChapterLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Chapter>> loader, List<Chapter> data) {
        if (mAdapter == null) {
            mDrawerData = data;
            customAdapter = new ChapterAdapter(ReaderActivity.this, R.layout.chapter_list_item, mDrawerData);
            //mAdapter = new ArrayAdapter<>(ReaderActivity.this, R.layout.chapter_list_item, mDrawerData);
            //mListView.setAdapter(mAdapter);
            mListView.setAdapter(customAdapter);
        } else {
            mListView.setAdapter(customAdapter);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String href = mDrawerData.get(position).getHref();
                setTextViewWithContent(href);
                Toast.makeText(ReaderActivity.this, "Position is: " + (position + 1), Toast.LENGTH_SHORT).show();
                tvActionBarTitle.setText(mDrawerData.get(position).getChapterTitle());
                mDrawerLayout.closeDrawers();
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<Chapter>> loader) {
    }
}