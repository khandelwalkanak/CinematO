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
import com.example.sahni.cinemato.DataClasses.LikedMovieGenre;
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
            holder.like.setTag(genres.get(position).id);
            Picasso.get().load(Constant.imageUrl + Constant.Medium + genres.get(position).path).into(holder.backdrop);
            if (client.data().selectedGenre(genres.get(position).id)!=null) {
                if (client.data().getLiked(genres.get(position).id)!=null)
                    holder.like.setImageResource(R.drawable.ratings);
                else
                    holder.like.setImageResource(R.drawable.unlike);
            }
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ImageView like = v.findViewById(R.id.like);
                    long id = (long) like.getTag();
                    if (client.data().getLiked(id)!=null) {
                        LikedMovieGenre likedGenre=client.data().getLiked(id);
                        client.data().deleteLiked(likedGenre);
                        like.setImageResource(R.drawable.unlike);
                    } else {
                        client.data().insertLiked(new LikedMovieGenre(id));
                        like.setImageResource(R.drawable.ratings);
                        Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView like=(ImageView)v;
                    long id = (long) like.getTag();
                    if (client.data().getLiked(id)!=null) {
                        LikedMovieGenre likedGenre=client.data().getLiked(id);
                        client.data().deleteLiked(likedGenre);
                        like.setImageResource(R.drawable.unlike);
                    } else {
                        client.data().insertLiked(new LikedMovieGenre(id));
                        like.setImageResource(R.drawable.ratings);
                        Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Picasso.get().load(Constant.imageUrl + Constant.Large + genres.get(position).path).into(holder.backdrop);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    long id = (long) v.getTag();
                    LikedMovieGenre likedGenre=client.data().getLiked(id);
                    client.data().deleteLiked(likedGenre);
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
