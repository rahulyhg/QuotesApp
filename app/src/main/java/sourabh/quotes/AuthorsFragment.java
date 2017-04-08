package sourabh.quotes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import sourabh.quotes.helper.Const;
import sourabh.quotes.app.CustomRequest;
import sourabh.quotes.data.AuthorsItem;
import sourabh.quotes.helper.DividerItemDecoration;
import sourabh.quotes.helper.JsonSeparator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AuthorsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AuthorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthorsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<AuthorsItem> authorsItemsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AuthorsAdapter mAdapter;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private void prepareMovieData() {
        HashMap params = new HashMap();
        HashMap headers = new HashMap();
        headers.put("Authorization", Const.GUEST_API_KEY);
        String url = Const.URL_GET_AUTHORS;

        System.out.println(url);
        Volley.newRequestQueue(getActivity()).add(new CustomRequest(getActivity().getApplicationContext(), getActivity(),
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


            authorsItem.setAuthor_id(single_author.getString(Const.KEY_AUTHOR_ID));
            authorsItem.setAuthor_name(single_author.getString(Const.KEY_AUTHOR_NAME));
            authorsItem.setAuthor_description(single_author.getString(Const.KEY_AUTHOR_DESCRIPTION));
            authorsItem.setAuthor_image(single_author.getString(Const.KEY_AUTHOR_IMAGE));
            authorsItem.setAuthor_likes_count(single_author.getString(Const.KEY_AUTHOR_LIKES_COUNT));
            authorsItem.setCreated_on(single_author.getString(Const.KEY_CREATED_AT));
            authorsItemsList.add(authorsItem);

        }
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_authors, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_authors);

       // mAdapter = new AuthorsAdapter(authorsItemsList,context);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AuthorsItem authorsItem = authorsItemsList.get(position);
                                    Intent i = new Intent(getActivity(), AuthorQuotesActivity.class);
                 i.putExtra("id_author",authorsItem.getAuthor_id()) ;  startActivity(i); }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        context = getActivity();
        prepareMovieData(); return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
