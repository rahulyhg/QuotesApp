package sourabh.quotes.helper;

import android.content.Context;
import sourabh.quotes.R;
import sourabh.quotes.app.Const;
import sourabh.quotes.data.AuthorQuotesItem;
import sourabh.quotes.data.AuthorsItem;
import sourabh.quotes.data.QuotesItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonSeparator {
    Context con;
    JSONObject json;

    public JsonSeparator(Context con, JSONObject json) {
        this.con = con;
        this.json = json;
    }

    public String getMessage() throws JSONException {
        return this.json.getString(this.con.getResources().getString(R.string.key_get_message));
    }

    public JSONObject getData() throws JSONException {
        return this.json.getJSONObject(this.con.getResources().getString(R.string.key_get_data));
    }

    public boolean isError() throws JSONException {
        if (this.json.getBoolean(this.con.getResources().getString(R.string.key_get_error))) {
            return true;
        }
        return false;
    }


    public QuotesItem JsonToQuotes(JSONArray quotes) throws JSONException
    {

        ArrayList<QuotesItem> quotesItemArrayList = new ArrayList<>();
        QuotesItem quotesItem = new QuotesItem();
        ArrayList<AuthorsItem> authorsItemArray = new ArrayList<>();


        for (int i = 0; i < quotes.length(); i++)
        {
            JSONObject single_quote = quotes.getJSONObject(i);

            AuthorsItem authorsItem = new AuthorsItem();
            AuthorQuotesItem authorQuotesItem = new AuthorQuotesItem();
            ArrayList<AuthorQuotesItem> authorQuotesItemArrayList = new ArrayList<>();


            authorsItem.setId_author(single_quote.getString(Const.KEY_ID_AUTHOR));
            authorsItem.setAuthor_name(single_quote.getString(Const.KEY_AUTHOR_NAME));
            authorsItem.setAuthor_likes_count(single_quote.getString(Const.KEY_AUTHOR_LIKES_COUNT));
            authorsItem.setCreated_on(single_quote.getString(Const.KEY_CREATED_ON));


            JSONArray quotesArr = single_quote.getJSONArray(Const.KEY_QUOTES);

            for (int j = 0; j < quotesArr.length(); j++)
            {
                JSONObject author_quotes = quotesArr.getJSONObject(j);


                authorQuotesItem.setId_quote(author_quotes.getString(Const.KEY_ID_QUOTE));
                authorQuotesItem.setId_category(author_quotes.getString(Const.KEY_ID_CATEGORY));
                authorQuotesItem.setQuote(author_quotes.getString(Const.KEY_QUOTE));
                authorQuotesItem.setQuote_likes_count(author_quotes.getString(Const.KEY_QUOTES_LIKES_COUNT));
                authorQuotesItem.setQuote_created_on(author_quotes.getString(Const.KEY_QUOTE_CREATED_ON));
                authorQuotesItem.setCategory_name(author_quotes.getString(Const.KEY_CATEGORY_NAME));


                authorQuotesItemArrayList.add(authorQuotesItem);


            }


            authorsItem.setAuthorQuotesItemArrayList(authorQuotesItemArrayList);

            authorsItemArray.add(authorsItem);

            quotesItem.setAuthorsItems(authorsItemArray);

            System.out.println("");

        }
        return quotesItem;
    }}

