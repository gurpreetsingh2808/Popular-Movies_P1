package com.popular_movies.framework;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popular_movies.DetailedViewFragment;
import com.popular_movies.MainActivity;
import com.popular_movies.MovieDetail;
import com.popular_movies.R;
import com.popular_movies.domain.MovieData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gurpreet on 1/17/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<MovieData> movieItemArrayList;
    private LayoutInflater inflater;
    Context context;
    public static ClickListener clickListener;

    public MovieAdapter(Context context, ArrayList<MovieData> movieDataList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.movieItemArrayList = movieDataList;
        if (MainActivity.mIsDualPane ) {
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail, DetailedViewFragment.getInstance(movieItemArrayList.get(0)))
                    .commit();
        }
    }

    public void setMoviesList(ArrayList<MovieData> listMovies) {
        this.movieItemArrayList = listMovies;
        notifyItemRangeChanged(0, listMovies.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MovieData current = movieItemArrayList.get(position);
        holder.title.setText(current.title);
        Picasso.with(context).load(current.thumbnailURL)
                .placeholder(R.drawable.no_img_preview).into(holder.thumbnail);

        if (!MainActivity.mIsDualPane) {
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MovieDetail.class);
                    intent.putExtra(MovieDetail.KEY_MOVIE, current);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        AppBarLayout barLayout = (AppBarLayout) ((AppCompatActivity) context).findViewById(R.id.actionbar);
                        ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation((AppCompatActivity) context, Pair.create((View) holder.thumbnail, "poster"), Pair.create((View) barLayout, "actionbar"));
                        context.startActivity(intent, compat.toBundle());
                    } else
                        context.startActivity(intent);
                }
            });
        } else {
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top)
                            .replace(R.id.movie_detail, DetailedViewFragment.getInstance(current))
                            .commit();
                }
            });
        }

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return movieItemArrayList.size();
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }


}
