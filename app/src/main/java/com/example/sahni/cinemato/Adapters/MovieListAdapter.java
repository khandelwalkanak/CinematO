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
import com.example.sahni.cinemato.DataClasses.MovieGenreRelationship;
import com.example.sahni.cinemato.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahni on 13/4/18.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.Holder> {
    ArrayList<Movie> movies;
    Context context;
    DatabaseClient client;
    int type;
    public MovieListAdapter(Context context, ArrayList<Movie> movies,int type){
        this.context=context;
        this.movies=movies;
        this.type=type;
        client=DatabaseClient.getInstance(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.display_list_item,parent,false);
        Holder holder=new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
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
                Movie movie=client.data().selectedMovie(id);
                if(movie==null){
                    client.data().insertMovie(movies.get(position));
                    movie=client.data().selectedMovie(id);
                    for(int i=0;i<movies.get(position).genre_ids.size();i++){
                        long genreId=movies.get(position).genre_ids.get(i);
                        long movieId=movies.get(position).id;
                        if(client.data().getRelation(genreId,movieId).size()==0){
                            client.data().InsertMovieGenreRelation(new MovieGenreRelationship(genreId,movieId));
                        }
                    }
                }
                if(client.data().getFavourite(movie.id)!=null) {
                    FavouriteMovies favouriteMovies=DatabaseClient.getInstance(context).data().getFavourite(id);
                    client.data().deleteFavourite(favouriteMovies);
                    fav.setImageResource(R.drawable.un_favourite);
                    if(type==Constant.FAVOURITE_LIST){
                        movies.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                }
                else {
                    client.data().insertFavourite(new FavouriteMovies(id));
                    fav.setImageResource(R.drawable.favourite);
                    Toast.makeText(context,"Added to Favourites",Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ImageView fav=v.findViewById(R.id.favourite);
                long id= (long) fav.getTag();
                Movie movie=client.data().selectedMovie(id);
                if(movie==null){
                    client.data().insertMovie(movies.get(position));
                    movie=client.data().selectedMovie(id);
                    for(int i=0;i<movies.get(position).genre_ids.size();i++){
                        long genreId=movies.get(position).genre_ids.get(i);
                        long movieId=movies.get(position).id;
                        if(client.data().getRelation(genreId,movieId).size()==0){
                            client.data().InsertMovieGenreRelation(new MovieGenreRelationship(genreId,movieId));
                        }
                    }
                }
                if(client.data().getFavourite(movie.id)!=null) {
                    FavouriteMovies favouriteMovies=DatabaseClient.getInstance(context).data().getFavourite(id);
                    client.data().deleteFavourite(favouriteMovies);
                    fav.setImageResource(R.drawable.un_favourite);
                    if(type==Constant.FAVOURITE_LIST){
                        movies.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                }
                else {
                    client.data().insertFavourite(new FavouriteMovies(id));
                    fav.setImageResource(R.drawable.favourite);
                    Toast.makeText(context,"Added to Favourites",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        holder.title.setText(movies.get(position).title);
        client = DatabaseClient.getInstance(context);
        holder.description.setText(getGenreDescription(position));
    }

    private String getGenreDescription(int position) {
        List<Long> genre;
        genre=client.data().getGenreIds(movies.get(position).id);
        if(client.data().selectedMovie(movies.get(position).id)==null)
            genre.addAll(movies.get(position).genre_ids);
        ArrayList<String> name=new ArrayList<>();
        StringBuffer buffer=new StringBuffer();
        for(int i=0;i<genre.size();i++) {
            name.add(client.data().selectedGenre(genre.get(i)).name);
            if(i!=0)
                buffer.append(", ");
            buffer.append(name.get(i));
        }
        return buffer.toString();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
    class Holder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        TextView description;
        TextView ratings;
        ImageView favourite;
        public Holder(View itemView) {
            super(itemView);
            poster=itemView.findViewById(R.id.poster);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);
            ratings=itemView.findViewById(R.id.rating);
            favourite=itemView.findViewById(R.id.favourite);
        }
    }
}
