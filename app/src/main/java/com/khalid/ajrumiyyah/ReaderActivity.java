package com.khalid.ajrumiyyah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.khalid.ajrumiyyah.adapter.ChapterAdapter;
import com.khalid.ajrumiyyah.loader.ChapterLoader;
import com.khalid.ajrumiyyah.model.Chapter;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReaderActivity extends ActionBarActivity
        implements LoaderManager.LoaderCallbacks<List<Chapter>> {
    private static final String SI_LAST_CHAPTER = "SI_LAST_CHAPTER";
    private static final String SI_ACTION_BAR_TITLE = "SI_ACTION_BAR_TITLE";

    private Toolbar toolbar;
    private TextView tvActionBarTitle;
    private TextView tvContent;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mListView;
    private List<Chapter> mDrawerData;
    private ChapterAdapter mAdapter;
    private SharedPreferences sharedPreferences;
    private int pref_FontSize;
    private String mLastHref;
    private String mActionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);
        setContentView(R.layout.activity_reader);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        initView(savedInstanceState);
        initDrawer();
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        pref_FontSize =
                sharedPreferences.getInt(
                        getResources().getString(R.string.pref_font_size),
                        getResources().getInteger(R.integer.pref_font_size_default));
        tvContent.setTextSize(pref_FontSize);
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SI_LAST_CHAPTER, mLastHref);
        outState.putString(SI_ACTION_BAR_TITLE, mActionBarTitle);
        super.onSaveInstanceState(outState);
    }

    public void setTextViewWithContent(String href) {
        InputStream is;
        try {
            is = getAssets().open("book/" + href);
            String text = readFromString(is);
            tvContent.setText(Html.fromHtml(text));
            int textGravity;
            int textAlignment;
            textGravity = href.equals("cover.html") ? Gravity.CENTER : Gravity.NO_GRAVITY;
            textAlignment = href.equals("cover.html") ? View.TEXT_ALIGNMENT_CENTER : View.TEXT_ALIGNMENT_TEXT_START;
            tvContent.setTextAlignment(textAlignment);
            tvContent.setGravity(textGravity);
            mLastHref = href;
        } catch (IOException e) {
            tvContent.setText("Should not happen!");
            throw new RuntimeException(e);
        }
    }

    private void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    // taken from http://stackoverflow.com/questions/309424
    private String readFromString(InputStream is) throws IOException {
        final char[] buffer = new char[1024];
        final StringBuilder out = new StringBuilder();

        Reader in = null;
        try {
            in = new InputStreamReader(is, "UTF-8");
            while (true) {
                int read = in.read(buffer, 0, buffer.length);
                if (read < 0) {
                    break;
                }
                out.append(buffer, 0, read);
            }
        } finally {
            closeSilently(in);
        }

        return out.toString();
    }

    private void initView(Bundle savedInstanceState) {
        tvActionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvContent.setTextSize(pref_FontSize);

        View mToolbarShadow = findViewById(R.id.view_toolbar_shadow);

        mListView = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mLastHref = "cover.html";
        if (savedInstanceState != null) {
            mLastHref = savedInstanceState.getString(SI_LAST_CHAPTER, mLastHref);
            tvActionBarTitle.setText(savedInstanceState.getString(SI_ACTION_BAR_TITLE, mActionBarTitle));
        }
        setTextViewWithContent(mLastHref);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        if (Build.VERSION.SDK_INT >= 21) {
            mToolbarShadow.setVisibility(View.GONE);
        }
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            this.startActivity(intent);
        }
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
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
            mAdapter = new ChapterAdapter(ReaderActivity.this, R.layout.chapter_list_item, mDrawerData);
            mListView.setAdapter(mAdapter);
        } else {
            mListView.setAdapter(mAdapter);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String href = mDrawerData.get(position).getHref();
                setTextViewWithContent(href);
                mActionBarTitle = mDrawerData.get(position).getChapterTitle();
                tvActionBarTitle.setText(mActionBarTitle);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<Chapter>> loader) {
    }
}