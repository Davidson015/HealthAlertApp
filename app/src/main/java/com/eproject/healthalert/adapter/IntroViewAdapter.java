package com.eproject.healthalert.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.eproject.healthalert.R;

public class IntroViewAdapter extends androidx.viewpager.widget.PagerAdapter {

    private Animation floater;

    private Context context;
    private LayoutInflater layoutInflater;
    private final Integer[] images;
    private final Integer[] titles;
    private final Integer[] descriptions;

    public IntroViewAdapter(Context context, Integer[] images, Integer[] titles, Integer[] descriptions)
    {
        this.context=context;
        this.images=images;
        this.titles = titles;
        this.descriptions = descriptions;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
        View view =layoutInflater.inflate(R.layout.intro_slide,null);

        // Getting the views from the layout
        ImageView imageView = (ImageView) view.findViewById(R.id.img_view);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView descriptionView = (TextView) view.findViewById(R.id.desc);

        // Initializing floater animation
        floater = AnimationUtils.loadAnimation(context, R.anim.floater);
        // Setting the animation to the ImageView
        imageView.setAnimation(floater);

        // Setting the views
        imageView.setImageResource(images[position]);
        titleView.setText(titles[position]);
        descriptionView.setText(descriptions[position]);

        // Adding the view to the container
        ViewPager viewPager=(ViewPager) container;
        viewPager.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager=(ViewPager) container;
        View view=(RelativeLayout) object;
        viewPager.removeView(view);
    }
}
