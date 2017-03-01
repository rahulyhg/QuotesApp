package sourabh.quotes;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sourabh.quotes.adaptor.AuthorsAdapter;
import sourabh.quotes.adaptor.RecyclerTouchListener;
import sourabh.quotes.app.Const;
import sourabh.quotes.app.CustomRequest;
import sourabh.quotes.data.AuthorsItem;
import sourabh.quotes.data.Movie;
import sourabh.quotes.helper.DividerItemDecoration;
import sourabh.quotes.helper.JsonSeparator;

public class AuthorsActivity extends AppCompatActivity {
    private List<AuthorsItem> authorsItemsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AuthorsAdapter mAdapter;
Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_author);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_authors);

        mAdapter = new AuthorsAdapter(authorsItemsList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AuthorsItem authorsItem = authorsItemsList.get(position);
                Toast.makeText(getApplicationContext(), authorsItem.getAuthor_name() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
context=getApplicationContext();
        prepareMovieData();
    }

    private void prepareMovieData() {
        HashMap params = new HashMap();
        HashMap headers = new HashMap();
        headers.put("Authorization", Const.GUEST_API_KEY);
        String url = Const.URL_GET_AUTHORS ;

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

                                JSONArray authors = js.getData().getJSONArray(Const.KEY_AUTHORS);

                                //Toast.makeText(context,quotes.toString(),Toast.LENGTH_LONG).show();
                        ParseJsonToAuthors(authors);


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

    public void ParseJsonToAuthors(JSONArray authors) throws JSONException {
        for (int i = 0; i < authors.length(); i++) {
            JSONObject single_author = authors.getJSONObject(i);

            AuthorsItem authorsItem = new AuthorsItem();


            authorsItem.setId_author(single_author.getString(Const.KEY_ID_AUTHOR));
            authorsItem.setAuthor_name(single_author.getString(Const.KEY_AUTHOR_NAME));
            authorsItem.setAuthor_description(single_author.getString(Const.KEY_AUTHOR_DESCRIPTION));
            authorsItem.setAuthor_image(single_author.getString(Const.KEY_AUTHOR_IMAGE));
            authorsItem.setAuthor_likes_count(single_author.getString(Const.KEY_AUTHOR_LIKES_COUNT));
            authorsItem.setCreated_on(single_author.getString(Const.KEY_CREATED_ON));
                authorsItemsList.add(authorsItem);

        }
        mAdapter.notifyDataSetChanged();

    }

}
