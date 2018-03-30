package com.example.fluper.clinsher.appActivity.controller.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fluper.clinsher.R;

/**
 * Created by fluper on 19/2/18.
 */

public class IntroFragment extends Fragment {




        private static final String BACKGROUND_COLOR = "backgroundColor";
        private static final String PAGE = "page";

        private int mBackgroundColor, mPage;

        public static IntroFragment newInstance(int backgroundColor, int page) {
            IntroFragment frag = new IntroFragment();
            Bundle b = new Bundle();
            b.putInt(BACKGROUND_COLOR, backgroundColor);
            b.putInt(PAGE, page);
            frag.setArguments(b);
            return frag;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (!getArguments().containsKey(BACKGROUND_COLOR))
                throw new RuntimeException("Fragment must contain a \"" + BACKGROUND_COLOR + "\" argument!");
            mBackgroundColor = getArguments().getInt(BACKGROUND_COLOR);

            if (!getArguments().containsKey(PAGE))
                throw new RuntimeException("Fragment must contain a \"" + PAGE + "\" argument!");
            mPage = getArguments().getInt(PAGE);
        }

        @Override


        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            // Select a layout based on the current page
            int layoutResId;
            switch (mPage) {
                case 0:
                    layoutResId = R.layout.fragment_home_fragment_one;
                    break;
                case 1:
                    layoutResId = R.layout.fragment_home_fragment_two;
                    break;
                case 2:
                    layoutResId = R.layout.home_layout_three;
                    break;
                case 3:
                    layoutResId = R.layout.home_layout_four;
                    break;
                default:
                    layoutResId = R.layout.home_layout_five;
                    break;
            }

            // Inflate the layout resource file
            View view = getActivity().getLayoutInflater().inflate(layoutResId, container, false);

            // Set the current page index as the View's tag (useful in the PageTransformer)
            view.setTag(mPage);

            return view;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // Set the background color of the root view to the color specified in newInstance()
            View background = view.findViewById(R.id.intro_background);
            //background.setBackgroundColor(mBackgroundColor);
        }

    }




