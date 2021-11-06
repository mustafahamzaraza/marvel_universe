package com.oyelabs.marveluniverse.ui;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.oyelabs.marveluniverse.R;


public class MainFragment extends Fragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CharactersPageAdapter charactersPageAdapter = new CharactersPageAdapter(getActivity().getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        viewPager.setAdapter(charactersPageAdapter);
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }



    class CharactersPageAdapter extends FragmentPagerAdapter {

        private static final int FAVORITE_CHARACTER_PAGE = 0;
        private static final int SEARCH_CHARACTER_PAGE = 1;

        public CharactersPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public androidx.fragment.app.Fragment getItem(int position) {
            switch (position) {

                case SEARCH_CHARACTER_PAGE: {
                    CharacterListFragment characterListFragment = new CharacterListFragment();
                    return characterListFragment;
                }
                default: {
                    throw new RuntimeException("invalid option");
                }
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 1) return getString(R.string.tab_search);
            else return "";
        }
    }


}
