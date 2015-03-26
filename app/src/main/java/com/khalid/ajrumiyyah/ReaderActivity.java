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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.khalid.ajrumiyyah.adapter.ChapterLoader;
import com.khalid.ajrumiyyah.model.Chapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReaderActivity extends ActionBarActivity
        implements LoaderManager.LoaderCallbacks<List<Chapter>> {
    private Toolbar toolbar;
    private TextView tvActionBarTitle;
    private TextView tvContent;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mListView;
    private ArrayAdapter<Chapter> mAdapter;
    private List<Chapter> leftSliderData;
    StringBuilder mbuffer;
    String stringContent;
//    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/ScheherazadeRegOT.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        setContentView(R.layout.activity_main);

        initView();
        initDrawer();
        getSupportLoaderManager().initLoader(0, null, this);
    }

    /*@Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }*/

    private void initView() {
        tvActionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvContent.setText("Eat Me DEAD!");

        /*try {
            mbuffer = new StringBuilder();
            InputStream isContent;
            isContent = getAssets().open("book/cover.html");
            BufferedReader in=
                    new BufferedReader(new InputStreamReader(isContent, "UTF-8"));

            while ((stringContent = in.readLine()) != null) {
                mbuffer.append(stringContent);
            }

            tvContent.setText("Eat me Alive");
            tvContent.setText(Html.fromHtml("<b>Bold</b> Typeface.<br><i>Italic</i> Typeface."));

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            tvContent.setText("Caught IOException initView tvContent.setText()!");
        }*/

        try {
            InputStream is = getAssets().open("book/1.html");

            // We guarantee that the available method returns the total
            // size of the asset...  of course, this does mean that a single
            // asset can't be more than 2 gigs.
            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string.
            String text = new String(buffer);

            // Finally stick the string into the text view.
            tvContent.setText(Html.fromHtml(text));

        } catch (IOException e) {
            // Should never happen!
            tvContent.setText("Should not happen!");
            throw new RuntimeException(e);
        }

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (Build.VERSION.SDK_INT >= 17) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        mListView = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void initDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
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
            leftSliderData = data;
            mAdapter = new ArrayAdapter<>(ReaderActivity.this, R.layout.chapter_list_item, leftSliderData);
            mListView.setAdapter(mAdapter);
        } else {
            mListView.setAdapter(mAdapter);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String href = leftSliderData.get(position).getHref();
                Toast.makeText(ReaderActivity.this, "Position is: " + position, Toast.LENGTH_SHORT).show();
//                mWebView.loadUrl("file:///android_asset/sample_book/" + href);
                tvActionBarTitle.setText(leftSliderData.get(position).getChapterTitle());
                drawerLayout.closeDrawers();
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<Chapter>> loader) {
    }
}