package sourabh.quotes.adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsImageView;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import sourabh.quotes.R;
import sourabh.quotes.data.AuthorQuote;
import sourabh.quotes.data.AuthorQuotes;
import sourabh.quotes.helper.Util;

public class AuthorsAdapter extends RecyclerView.Adapter<AuthorsAdapter.MyViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter
{

    private AuthorQuotes authorQuotes;
    private  Context context;
    int color_common;

    @NonNull
    @Override
    public String getSectionName(int position) {
        return String.valueOf(position);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView author_name, author_likes_count, author_description;
        public CircleImageView circleImageList;
        public  IconicsImageView imgLike;

        public MyViewHolder(View view) {
            super(view);
            author_name = (TextView) view.findViewById(R.id.author_name);
            author_description = (TextView) view.findViewById(R.id.author_description);
            author_likes_count = (TextView) view.findViewById(R.id.author_likes_count);
            circleImageList = (CircleImageView) view.findViewById(R.id.circleImageMFQ);
            imgLike = (IconicsImageView) view.findViewById(R.id.imgLike);

        }
    }


    public AuthorsAdapter(AuthorQuotes authorQuotes, Context context) {
        this.authorQuotes = authorQuotes;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.authors_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //AuthorsItem author = authorsItemList.get(position);

        AuthorQuote authorQuote = authorQuotes.getAuthors().get(position);
        color_common = Util.getRandomColor(context);


        holder.author_name.setText(authorQuote.getAuthor_name());
        holder.author_description.setText(authorQuote.getAuthor_description());
      //  holder.author_likes_count.setText(authorQuote.getAuthor_likes_count());

        holder.circleImageList.setBorderColor(color_common);
        holder.imgLike.setColor(color_common);
        Picasso.with(context).load(authorQuote.getAuthor_image()).into(holder.circleImageList);

    }

    @Override
    public int getItemCount() {
        return authorQuotes.getAuthors().size();
    }
}
