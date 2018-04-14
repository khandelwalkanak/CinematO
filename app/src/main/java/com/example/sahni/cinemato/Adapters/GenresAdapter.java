package com.example.sahni.cinemato.Adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahni.cinemato.Constant;
import com.example.sahni.cinemato.DataBase.DatabaseClient;
import com.example.sahni.cinemato.DataClasses.Movie;
import com.example.sahni.cinemato.DataClasses.MovieGenre;
import com.example.sahni.cinemato.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sahni on 11/4/18.
 */

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.Holder> {
    private final Context context;
    ArrayList<MovieGenre> genres;
    DatabaseClient client;
    ItemClickCallback callback;
    int layout;

    public GenresAdapter(Context context, ArrayList<MovieGenre> genres,ItemClickCallback callback,int layout){
        this.context=context;
        client=DatabaseClient.getInstance(context);
        this.genres=genres;
        this.callback=callback;
        this.layout=layout;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(layout,parent,false);
        Holder holder=new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        holder.genre.setText(genres.get(position).name);
        holder.itemView.setTag(genres.get(position).id);
        if(layout==R.layout.genre_item) {
            Picasso.get().load(Constant.imageUrl + Constant.Medium + genres.get(position).path).into(holder.backdrop);
            if (client.data().selectedGenre(genres.get(position).id)!=null) {
                if ((client.data().selectedGenre(genres.get(position).id).isLiked) && (client.data().selectedGenre(genres.get(position).id) != null))
                    holder.like.setVisibility(View.VISIBLE);
                else
                    holder.like.setVisibility(View.GONE);
            }
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ImageView like = v.findViewById(R.id.like);
                    long id = (long) v.getTag();
                    if (client.data().selectedGenre(id).isLiked) {
                        client.data().updateLiked(id, false);
                        like.setVisibility(View.GONE);
                    } else {
                        client.data().updateLiked(id, true);
                        like.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
        }
        else {
            Picasso.get().load(Constant.imageUrl + Constant.Large + genres.get(position).path).into(holder.backdrop);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    long id = (long) v.getTag();
                    client.data().updateLiked(id, false);
                    genres.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());

                    return true;
                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.OnClick((Long) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView backdrop;
        TextView genre;
        ImageView like;
        View itemView;
        public Holder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            backdrop=itemView.findViewById(R.id.backdrop);
            genre=itemView.findViewById(R.id.genre);
            like=itemView.findViewById(R.id.like);
        }
    }
}
