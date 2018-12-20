package com.example.sahni.cinemato.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahni.cinemato.Constant;
import com.example.sahni.cinemato.DataBase.DatabaseClient;
import com.example.sahni.cinemato.DataClasses.FavouriteMovies;
import com.example.sahni.cinemato.DataClasses.Movie;
import com.example.sahni.cinemato.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sahni on 7/4/18.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.Holder>{
    private Context context;
    private ArrayList<Movie> movies;
    DatabaseClient client;
    UpdateAllLists updateAllLists;
    public MoviesAdapter(Context context, ArrayList<Movie> movies,UpdateAllLists updateAllLists)
    {
        this.movies =movies;
        this.context=context;
        client=DatabaseClient.getInstance(context);
        this.updateAllLists=updateAllLists;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.list_item,parent,false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.ratings.setText(movies.get(position).vote_average+"");
        Picasso.get().load(Constant.imageUrl+Constant.Medium+ movies.get(position).poster_path).into(holder.poster);
        if (client.data().getFavourite(movies.get(position).id)!=null)
                holder.favourite.setImageResource(R.drawable.favourite);
        else
                holder.favourite.setImageResource(R.drawable.un_favourite);
        holder.favourite.setTag(movies.get(position).id);
        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView fav=(ImageView)v;
                long id= (long) fav.getTag();
                Movie movie=DatabaseClient.getInstance(context).data().selectedMovie(id);
                if(client.data().getFavourite(movie.id)!=null) {
                    FavouriteMovies movies=DatabaseClient.getInstance(context).data().getFavourite(id);
                    DatabaseClient.getInstance(context).data().deleteFavourite(movies);
                    fav.setImageResource(R.drawable.un_favourite);
                }
                else {
                    DatabaseClient.getInstance(context).data().insertFavourite(new FavouriteMovies(id));
                    fav.setImageResource(R.drawable.favourite);
                    Toast.makeText(context,"Added to Favourites",Toast.LENGTH_SHORT).show();
                }
                updateAllLists.updateAll();
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return movies.size();
    }
    class Holder extends RecyclerView.ViewHolder{
        ImageView poster;
        TextView ratings;
        ImageView favourite;
        public Holder(View itemView) {
            super(itemView);
            poster=itemView.findViewById(R.id.poster);
            ratings=itemView.findViewById(R.id.rating);
            favourite=itemView.findViewById(R.id.favourite);
        }
    }
}
