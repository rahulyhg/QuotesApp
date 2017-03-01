package sourabh.quotes;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import sourabh.quotes.adaptor.AuthorQuotesDataAdapter;
import sourabh.quotes.app.Const;
import sourabh.quotes.app.CustomRequest;
import sourabh.quotes.data.AuthorsItem;
import sourabh.quotes.data.AuthorQuotesItem;
import sourabh.quotes.data.QuotesItem;
import sourabh.quotes.helper.JsonSeparator;
import sourabh.quotes.helper.Util;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static sourabh.quotes.R.id.circleImage;
import static sourabh.quotes.R.id.imageView;
import static sourabh.quotes.R.id.upper_imageview_author;
import static sourabh.quotes.helper.Util.getRandomColor;


public class AuthorQuotesActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE = 0.8f;
    private static final float PERCENTAGE_TO_HIDE_SECOND_TITTLE = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    @Bind(R.id.ll_title)
    LinearLayout titleContainer;

    @Bind(R.id.tv_author_name)
    TextView tv_author_name;


    @Bind(R.id.tv_author_description)
    TextView tv_author_description;

    @Bind(R.id.tv_author_title)
    TextView title;

    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.circleImage)
    de.hdodenhof.circleimageview.CircleImageView circleImageView;

    @Bind(R.id.upper_imageview_author)
    ImageView upper_imageview_author;

    @Bind(R.id.framelayout_author)
    FrameLayout framelayout_author;
    private AdView mAdView;
    int color_common;

    private ArrayList<String> quotesArray = new ArrayList<>();
    ;
    String author = "";
    Context context;
    String author_image_url;
    String id_author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_quotes);
        id_author=getIntent().getStringExtra("id_author");
        ButterKnife.bind(this);

        toolbar.setTitle("");
        appBarLayout.addOnOffsetChangedListener(this);
        color_common = Util.getRandomColor(getApplicationContext());
        toolbar.setBackground(new ColorDrawable(color_common));
        framelayout_author.setBackground(new ColorDrawable(color_common));
        //  upper_imageview_author.setBackgroundColor(color_common);

        setSupportActionBar(toolbar);
        startAlphaAnimation(title, 0, View.INVISIBLE);


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Toast.makeText(getApplicationContext(), "Ad is loaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                Toast.makeText(getApplicationContext(), "Ad is opened!", Toast.LENGTH_SHORT).show();
            }
        });

        context = AuthorQuotesActivity.this;

        getQuotesByAuther();

    }

    void getQuotesByAuther() {

        HashMap params = new HashMap();
        HashMap headers = new HashMap();
        headers.put("Authorization", Const.GUEST_API_KEY);
        String url = Const.URL_GET_QUOTES_BY_AUTHOR + "all/"+id_author+"/null";

        System.out.println(url);
        Volley.newRequestQueue(this).add(new CustomRequest(getApplicationContext(), this,
                false, 0, url, params, headers,


                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {
                        JSONObject jsonObject = (JSONObject) response;
                        JsonSeparator js = new JsonSeparator(context, jsonObject);

                        try {
                            if (js.isError()) {

                                Toast.makeText(context, js.getMessage().toString(), Toast.LENGTH_LONG).show();
                            } else {

                                JSONArray quotes = js.getData().getJSONArray(Const.KEY_QUOTES);

                                //Toast.makeText(context,quotes.toString(),Toast.LENGTH_LONG).show();
                                JsonToQuotes(quotes);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();

            }
        }));

    }

    void JsonToQuotes(JSONArray quotes) throws JSONException {


        ArrayList<QuotesItem> quotesItemArrayList = new ArrayList<>();
        QuotesItem quotesItem = new QuotesItem();
        ArrayList<AuthorsItem> authorsItemArray = new ArrayList<>();


        for (int i = 0; i < quotes.length(); i++) {
            JSONObject single_quote = quotes.getJSONObject(i);

            AuthorsItem authorsItem = new AuthorsItem();
            AuthorQuotesItem authorQuotesItem = new AuthorQuotesItem();
            ArrayList<AuthorQuotesItem> authorQuotesItemArrayList = new ArrayList<>();


            authorsItem.setId_author(single_quote.getString(Const.KEY_ID_AUTHOR));
            authorsItem.setAuthor_name(single_quote.getString(Const.KEY_AUTHOR_NAME));
            authorsItem.setAuthor_description(single_quote.getString(Const.KEY_AUTHOR_DESCRIPTION));
            authorsItem.setAuthor_image(single_quote.getString(Const.KEY_AUTHOR_IMAGE));
            authorsItem.setAuthor_likes_count(single_quote.getString(Const.KEY_AUTHOR_LIKES_COUNT));
            authorsItem.setCreated_on(single_quote.getString(Const.KEY_CREATED_ON));


            author = authorsItem.getAuthor_name();
            tv_author_name.setText(author);
            tv_author_description.setText(authorsItem.getAuthor_description());
            title.setText(author);
            author_image_url = authorsItem.getAuthor_image();

            JSONArray quotesArr = single_quote.getJSONArray(Const.KEY_QUOTES);

            for (int j = 0; j < quotesArr.length(); j++) {
                JSONObject author_quotes = quotesArr.getJSONObject(j);


                authorQuotesItem.setId_quote(author_quotes.getString(Const.KEY_ID_QUOTE));
                authorQuotesItem.setId_category(author_quotes.getString(Const.KEY_ID_CATEGORY));
                authorQuotesItem.setQuote(author_quotes.getString(Const.KEY_QUOTE));
                authorQuotesItem.setQuote_likes_count(author_quotes.getString(Const.KEY_QUOTES_LIKES_COUNT));
                authorQuotesItem.setQuote_created_on(author_quotes.getString(Const.KEY_QUOTE_CREATED_ON));
                authorQuotesItem.setCategory_name(author_quotes.getString(Const.KEY_CATEGORY_NAME));


                authorQuotesItemArrayList.add(authorQuotesItem);

                quotesArray.add(authorQuotesItem.getQuote());

            }


            authorsItem.setAuthorQuotesItemArrayList(authorQuotesItemArrayList);

            authorsItemArray.add(authorsItem);

            quotesItem.setAuthorsItems(authorsItemArray);

            initViews(author);

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * @param offset showed how much has been scrolled AppBarLayout;
     *               in this example offset changed from 0 to -XXX
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnMainTitle(percentage);
        handleAlphaOnSecondTitle(percentage);
    }

    private void handleAlphaOnSecondTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnMainTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_SECOND_TITTLE) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }


    private void initViews(String author) {


        Picasso.with(context).load(author_image_url).into(circleImageView);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        RecyclerView.Adapter adapter = new AuthorQuotesDataAdapter(quotesArray, author, getApplicationContext());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    Toast.makeText(getApplicationContext(), quotesArray.get(position), Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }
}
