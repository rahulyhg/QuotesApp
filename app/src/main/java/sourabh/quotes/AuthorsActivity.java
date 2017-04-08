package sourabh.quotes;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.LayoutInflaterCompat;
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
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sourabh.quotes.adaptor.AuthorsAdapter;
import sourabh.quotes.adaptor.RecyclerTouchListener;
import sourabh.quotes.data.AuthorQuote;
import sourabh.quotes.data.AuthorQuotes;
import sourabh.quotes.helper.CommonUtilities;
import sourabh.quotes.helper.Const;
import sourabh.quotes.app.CustomRequest;
import sourabh.quotes.data.AuthorsItem;
import sourabh.quotes.helper.JsonSeparator;

public class AuthorsActivity extends AppCompatActivity {
    private List<AuthorsItem> authorsItemsList = new ArrayList<>();
    private FastScrollRecyclerView recyclerView;
    private AuthorsAdapter mAdapter;
    ArrayList<String>mDataArray = new ArrayList<>();

Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authors);
        context=getApplicationContext();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_author);
        setSupportActionBar(toolbar);
        recyclerView = (FastScrollRecyclerView) findViewById(R.id.recycler_view_authors);




        mDataArray.add("A");
        mDataArray.add("B");




        getAuthors();
    }

    private void getAuthors() {
        HashMap params = new HashMap();
        HashMap headers = new HashMap();
        headers.put("Authorization", Const.GUEST_API_KEY);
        String url = Const.URL_GET_AUTHORS ;

        System.out.println(url);
        Volley.newRequestQueue(this).add(new CustomRequest(this, this,
                true, 0, url, params, headers,


                new com.android.volley.Response.Listener() {

                    @Override
                    public void onResponse(Object response) {
                        JSONObject jsonObject = (JSONObject) response;
                        JsonSeparator js = new JsonSeparator(context, jsonObject);

                        try {
                            if (js.isError()) {

                                Toast.makeText(context, js.getMessage().toString(), Toast.LENGTH_LONG).show();
                            } else {

                                //JSONArray authors = js.getData().getJSONArray(Const.KEY_AUTHORS);

                                //Toast.makeText(context,quotes.toString(),Toast.LENGTH_LONG).show();
                                ParseJsonToAuthors(js.getData());


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

    public void ParseJsonToAuthors(JSONObject jsonObject) throws JSONException {

        AuthorQuotes authorQuotes = CommonUtilities.getObjectFromJson(jsonObject, AuthorQuotes.class);


        mAdapter = new AuthorsAdapter(authorQuotes,context);

        setupRecycleView();

    }

    void setupRecycleView(){

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AuthorsItem authorsItem = authorsItemsList.get(position);
                Intent i = new Intent(AuthorsActivity.this, AuthorQuotesActivity.class);
                i.putExtra("id_author",authorsItem.getAuthor_id()) ;  startActivity(i); }


            @Override
            public void onLongClick(View view, int position) {

            }
        }));





    }

}
