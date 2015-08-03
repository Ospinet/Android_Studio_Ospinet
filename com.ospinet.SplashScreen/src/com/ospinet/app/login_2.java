package com.ospinet.app;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import au.com.papercloud.pager.SimpleViewPagerIndicator;


public class login_2 extends FragmentActivity {
    Button SignUp, Login;
Timer swipeTimer;
public static int currentPage =0;
public static int NUM_PAGES = 3;
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_2);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);
        
        SimpleViewPagerIndicator pageIndicator = (SimpleViewPagerIndicator) findViewById(R.id.page_indicator);
        pageIndicator.setViewPager(viewPager);
        pageIndicator.notifyDataSetChanged();
        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage == NUM_PAGES) {
                            currentPage = 0;
                        }
                        viewPager.setCurrentItem(currentPage++, true);
                    }
                });
            }
        }, 500, 3000);
        SignUp = (Button) findViewById(R.id.btn_login_signup);
        Login = (Button) findViewById(R.id.btn_login_button);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(login_2.this, LoginActivity.class);
                login_2.this.startActivity(login);
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(login_2.this, SignUpActivity.class);
                login_2.this.startActivity(signup);
            }
        });
    }
    
    private class ImagePagerAdapter extends PagerAdapter {
        private int[] mImages = new int[] {
            R.drawable.o2,
            R.drawable.o3,
            R.drawable.o4
        };

        @Override
        public int getCount() {
          return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
          return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
          Context context = login_2.this;
          ImageView imageView = new ImageView(context);
          imageView.setScaleType(ImageView.ScaleType.FIT_XY);
          imageView.setImageResource(mImages[position]);
          //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500,500);
          //imageView.setLayoutParams(layoutParams);
          ((ViewPager) container).addView(imageView, 0);
          
          return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
          ((ViewPager) container).removeView((ImageView) object);
        }
      
      }

}


