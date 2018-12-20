package com.example.sahni.cinemato.Adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import java.util.List;

/**
 * Created by sahni on 7/4/18.
 */

public class UpcomingMoviesPagerAdapter extends FragmentPagerAdapter {
    static ArrayList<Movie> upcomingMovies;
    private static final String PositionKey="position";
    public UpdateAllLists updateAllLists;
    FragmentManager fm;


    public UpcomingMoviesPagerAdapter(FragmentManager fm, ArrayList<Movie> UpcomingMovies,UpdateAllLists updateAllLists) {
        super(fm);
        this.fm=fm;
        this.upcomingMovies =UpcomingMovies;
        this.updateAllLists=updateAllLists;
    }

    public void reload() {
        List<Fragment> fragments = fm.getFragments();
        for(int i=0;i<fragments.size();i++){
            Fragment fragment= fragments.get(i);
            if(fragment instanceof MovieItemFragment) {
                MovieItemFragment frag=(MovieItemFragment) fragment;
                int position=fragment.getArguments().getInt(PositionKey);
                View fragmentView=fragment.getView();
                //UPDATE LIKE
                ImageView favourite=fragmentView.findViewById(R.id.favourite);
                DatabaseClient client=DatabaseClient.getInstance(fragment.getActivity());
                if (client.data().getFavourite(upcomingMovies.get(position).id)!=null)
                    favourite.setImageResource(R.drawable.favourite);
                else
                    favourite.setImageResource(R.drawable.un_favourite);
                //UPDATE DESCRIPTION
                TextView description=fragmentView.findViewById(R.id.description);
                description.setText(frag.getGenreDescription(upcomingMovies.get(position).id));
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        MovieItemFragment fragment=new MovieItemFragment(updateAllLists);
        Bundle bundle=new Bundle();
        bundle.putInt(PositionKey,position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return upcomingMovies.size();
    }

    public static class MovieItemFragment extends Fragment{
        UpdateAllLists updateAllLists;
        private DatabaseClient client;
        public MovieItemFragment(){

        }
        @SuppressLint("ValidFragment")
        public MovieItemFragment(UpdateAllLists updateAllLists){
            this.updateAllLists=updateAllLists;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View itemView=inflater.inflate(R.layout.pager_item,container,false);
            ImageView poster=itemView.findViewById(R.id.poster);
            ImageView backdrop=itemView.findViewById(R.id.backdrop);
            TextView title=itemView.findViewById(R.id.title);
            ImageView favourite=itemView.findViewById(R.id.favourite);
            TextView description=itemView.findViewById(R.id.description);
            String genreDescription="";

            client = DatabaseClient.getInstance(getActivity());
            int position=getArguments().getInt(PositionKey);

            genreDescription = getGenreDescription(upcomingMovies.get(position).id);
            favourite.setTag(upcomingMovies.get(position).id);
            if (client.data().getFavourite(upcomingMovies.get(position).id)!=null)
                favourite.setImageResource(R.drawable.favourite);
            else
                favourite.setImageResource(R.drawable.un_favourite);
            title.setText(upcomingMovies.get(position).title);
            Picasso.get().load(Constant.imageUrl+Constant.Large+upcomingMovies.get(position).backdrop_path).into(backdrop);
            Picasso.get().load(Constant.imageUrl+Constant.Medium+upcomingMovies.get(position).poster_path).into(poster);
            description.setText(genreDescription);
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView fav=(ImageView)v;
                    long id= (long) fav.getTag();
                    Movie movie=DatabaseClient.getInstance(getActivity()).data().selectedMovie(id);
                    if(client.data().getFavourite(movie.id)!=null) {
                        FavouriteMovies movies=client.data().getFavourite(id);
                        client.data().deleteFavourite(movies);
                        fav.setImageResource(R.drawable.un_favourite);
                    }
                    else {
                        client.data().insertFavourite(new FavouriteMovies(id));
                        fav.setImageResource(R.drawable.favourite);
                        Toast.makeText(getActivity(),"Added to Favourites",Toast.LENGTH_SHORT).show();
                    }
                    updateAllLists.updateAll();
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ImageView fav=itemView.findViewById(R.id.favourite);
                    long id= (long) fav.getTag();
                    Movie movie=DatabaseClient.getInstance(getActivity()).data().selectedMovie(id);
                    if(client.data().getFavourite(movie.id)!=null) {
                        FavouriteMovies movies=client.data().getFavourite(id);
                        client.data().deleteFavourite(movies);
                        fav.setImageResource(R.drawable.un_favourite);
                    }
                    else {
                        client.data().insertFavourite(new FavouriteMovies(id));
                        fav.setImageResource(R.drawable.favourite);
                        Toast.makeText(getActivity(),"Added to Favourites",Toast.LENGTH_SHORT).show();
                    }
                    updateAllLists.updateAll();
                    return true;
                }
            });
            return itemView;
        }

        public String getGenreDescription(long id) {
            List<Long> genre=client.data().getGenreIds(id);
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
    }
}
