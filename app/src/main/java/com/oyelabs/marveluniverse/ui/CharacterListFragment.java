package com.oyelabs.marveluniverse.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.oyelabs.marveluniverse.R;
import com.oyelabs.marveluniverse.adapter.CharacterAdapter;
import com.oyelabs.marveluniverse.api.Character;
import com.oyelabs.marveluniverse.http.CharacterSearchTask;
import com.oyelabs.marveluniverse.listener.OnCharacterClickListener;
import java.util.ArrayList;
import java.util.List;
import com.oyelabs.marveluniverse.R;

public class CharacterListFragment extends Fragment {

    private static final int ID_LOADER = 0;
    private static final String QUERY_PARAM = "search_param";
    private List<Character> mCharacters;
    private CharacterAdapter mAdapter;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private View mLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        if (mCharacters == null) {
            mCharacters = new ArrayList();
        }
        mAdapter = new CharacterAdapter(mCharacters, getContext());
        mAdapter.setCharacterClickListener(mClickListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_character_list, container, false);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview_Character);
        mEmptyView = layout.findViewById(R.id.empty_result);
        mEmptyView.setVisibility(View.GONE);
        setLayout();
        getActivity().getSupportLoaderManager().initLoader(ID_LOADER, null, loaderCallbacks);
        return layout;
    }

    private void setLayout() {
        if (getResources().getBoolean(R.bool.phone)) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            }
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            }
        }

        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(searchListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mLoading.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            startCharacterLoader(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }


    };

    private void startCharacterLoader(String query) {
        Bundle params = new Bundle();
        params.putString(QUERY_PARAM, query);
        getActivity().getSupportLoaderManager().restartLoader(ID_LOADER, params, loaderCallbacks);
        if (mSearchView != null)
            mSearchView.clearFocus();
    }



    private LoaderManager.LoaderCallbacks<List<Character>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<Character>>() {


        @Override
        public Loader<List<Character>> onCreateLoader(int id, Bundle args) {
            String query = args != null ? args.getString(QUERY_PARAM) : null;
            return new CharacterSearchTask(getContext(), query);
        }

        @Override
        public void onLoadFinished(Loader<List<Character>> loader, List<Character> characters) {
            if (characters != null && characters.size() > 0) {
                mCharacters.clear();
                mCharacters.addAll(characters);
                mAdapter.notifyDataSetChanged();
                mEmptyView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
            mLoading.setVisibility(View.GONE);
        }

        @Override
        public void onLoaderReset(Loader<List<Character>> loader) {

        }
    };


    private OnCharacterClickListener mClickListener = new OnCharacterClickListener() {
        @Override
        public void onCharacterClick(Character character, int position) {


            if (getResources().getBoolean(R.bool.phone)) {
                Intent it = new Intent(getActivity(), CharacterDetailActivity.class);

                it.putExtra(CharacterDetailActivity.EXTRA_CHARACTER, character);
                startActivity(it);
            } else {
                DetailCharacterFragment detailCharacterFragment = DetailCharacterFragment.newInstance(character);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_detail, detailCharacterFragment)
                        .commit();
            }
        }
    };


}
