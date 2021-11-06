package com.oyelabs.marveluniverse.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.oyelabs.marveluniverse.R;


public class CharacterDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CHARACTER = "character";
    private Character mCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);





    }
}
