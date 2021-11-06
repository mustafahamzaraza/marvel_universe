package com.oyelabs.marveluniverse.ui;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oyelabs.marveluniverse.R;
import com.oyelabs.marveluniverse.api.Character;
import com.oyelabs.marveluniverse.api.Url;
import com.oyelabs.marveluniverse.dao.CharacterContract;
import com.oyelabs.marveluniverse.dao.CharacterProvider;
import com.oyelabs.marveluniverse.util.MarvelUtil;

public class DetailCharacterFragment extends Fragment {
    private static final String EXTRA_CHARACTER = "character";
    private Character mCharacter;
    private ShareActionProvider mShareActionProvider;
    private TextView txtDescription;
    private TextView txtName;
    private TextView txtLastUpdate;
    private ImageButton btnUrlWiki;
    private ImageButton btnUrlDetail;
    private ImageView imgThumbnail;
    private RecyclerView recyclerviewComics;
    private FloatingActionButton fltactbtnAddFavorite;

    public static DetailCharacterFragment newInstance(Character character) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_CHARACTER, character);
        DetailCharacterFragment fragment = new DetailCharacterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getResources().getBoolean(R.bool.phone)){
            mCharacter = getActivity().getIntent().getParcelableExtra(EXTRA_CHARACTER);
        }else {
            mCharacter = (Character) getArguments().getParcelable(EXTRA_CHARACTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        this.btnUrlDetail       = (ImageButton) view.findViewById(R.id.btnDetailMore);
        this.btnUrlWiki         = (ImageButton) view.findViewById(R.id.btnWiki);
        this.txtDescription     = (TextView) view.findViewById(R.id.detail_text_character_description);
        this.txtName            = (TextView) view.findViewById(R.id.detail_text_character_name);
        this.imgThumbnail       = (ImageView) view.findViewById(R.id.detail_character_thumbnail);
        updateUI();
        return view;
    }


    private long insertCharacter(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CharacterContract.COL_ID_WEB , mCharacter.getIdWeb());
        contentValues.put(CharacterContract.COL_NAME   , mCharacter.getName());
        contentValues.put(CharacterContract.COL_DESCRIPTION   , mCharacter.getDescription());
        contentValues.put(CharacterContract.COL_THUMBNAIL    , mCharacter.getThumbnail().getPath());
        contentValues.put(CharacterContract.COL_MODIFIED  , mCharacter.getModified());
        Uri uri = getActivity().getContentResolver().insert(CharacterProvider.CHARACTERS_URI, contentValues);
        return ContentUris.parseId(uri);
    }

    private void updateUI() {
            getActivity().setTitle(mCharacter.getName());
            this.txtName.setText(mCharacter.getName());
            this.txtDescription.setText(mCharacter.getDescription());
            if ( mCharacter.getUrls() != null && mCharacter.getUrls().size() > 0) {

                for (Url url : mCharacter.getUrls()) {
                    if (url.getType().equalsIgnoreCase(MarvelUtil.URL_TYPE_DETAIL)) {
                        this.btnUrlDetail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Uri uri = Uri.parse("");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        });

                    } else if (url.getType().equalsIgnoreCase(MarvelUtil.URL_TYPE_WIKI)) {
                    }
                }
        Glide.with(this).load(mCharacter.getThumbnail().getStandardFantastic()).into(imgThumbnail);
        }
    }
}
