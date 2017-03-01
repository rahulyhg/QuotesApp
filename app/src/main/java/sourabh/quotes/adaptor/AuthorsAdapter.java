package sourabh.quotes.adaptor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sourabh.quotes.R;
import sourabh.quotes.data.AuthorsItem;

public class AuthorsAdapter extends RecyclerView.Adapter<AuthorsAdapter.MyViewHolder> {

    private List<AuthorsItem> authorsItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView author_name, author_likes_count, author_description;

        public MyViewHolder(View view) {
            super(view);
            author_name = (TextView) view.findViewById(R.id.author_name);
            author_description = (TextView) view.findViewById(R.id.author_description);
            author_likes_count = (TextView) view.findViewById(R.id.author_likes_count);
        }
    }


    public AuthorsAdapter(List<AuthorsItem> authorsItemList) {
        this.authorsItemList = authorsItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.authors_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AuthorsItem author = authorsItemList.get(position);
        holder.author_name.setText(author.getAuthor_name());
        holder.author_description.setText(author.getAuthor_description());
        holder.author_likes_count.setText(author.getAuthor_likes_count());
    }

    @Override
    public int getItemCount() {
        return authorsItemList.size();
    }
}
