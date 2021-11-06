package com.oyelabs.marveluniverse.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.oyelabs.marveluniverse.R;
import com.oyelabs.marveluniverse.api.Character;
import com.oyelabs.marveluniverse.api.ThumbnailCharacter;
import com.oyelabs.marveluniverse.dao.CharacterContract;
import com.oyelabs.marveluniverse.listener.OnCharacterClickListener;

import java.util.List;


public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.VH> {


    private List<Character> mCharacters;
    private Context mContext;
    private OnCharacterClickListener mCharacterClickListener;
    private Cursor mCursor;

    public CharacterAdapter(List<Character> mCharacters, Context mContext) {
        this.mCharacters = mCharacters;
        this.mContext = mContext;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_character,parent,false);



        VH viewHolder = new VH(view);

        view.setTag(viewHolder);
        view.setOnClickListener(mClickListener);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Character character;

        if ( mCursor != null){
            mCursor.moveToPosition(position);
            character = getCharacterFromCursor();
        }else {
            character = mCharacters.get(position);
        }

        Glide.with(mContext)
                .load(character.getThumbnail().getStandardMedium())

                .into(holder.imageThumbNail);

        Glide.with(mContext).load(character.getThumbnail().getStandardMedium())
                .into(holder.imageThumbNail);
        holder.txtCharacterName.setText(character.getName());

        holder.txtCharacterName.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/MAINBRG_.TTF"));

    }

    @Override
    public int getItemCount() {
        if ( mCursor != null){
            return mCursor.getCount();
        }else {
            return mCharacters != null ? mCharacters.size() : 0;
        }
    }

    @Override
    public long getItemId(int position) {
        if ( mCursor != null){
            if ( mCursor.moveToPosition(position)){
                return mCursor.getLong(mCursor.getColumnIndex(CharacterContract._ID));
            }
        }
        return super.getItemId(position);
    }

    public Cursor getCursor(){
        return mCursor;
    }

    public void setCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }



    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Character character;

            if ( mCharacterClickListener != null){
                VH vh = (VH) view.getTag();

                int position = vh.getAdapterPosition();

                if ( mCursor != null){
                    mCursor.moveToPosition(position);
                    character = getCharacterFromCursor();
                }else{
                    character = mCharacters.get(position);
                }

                mCharacterClickListener.onCharacterClick(character, position);
            }
        }
    };

    public void setCharacterClickListener( OnCharacterClickListener clickListener){
        this.mCharacterClickListener = clickListener;
    }


    public class VH extends  RecyclerView.ViewHolder{
        ImageView imageThumbNail;
        TextView  txtCharacterName;

        public VH(View itemView) {
            super(itemView);
            imageThumbNail   = (ImageView) itemView.findViewById(R.id.character_item_image_thumbnail);
            txtCharacterName = (TextView)  itemView.findViewById(R.id.character_item_txt_name);


        }
    }

    private Character getCharacterFromCursor(){
        Character character = new Character();

        ThumbnailCharacter thumbnail = new ThumbnailCharacter();

        character.setId(mCursor.getLong(mCursor.getColumnIndex(CharacterContract._ID)));
        character.setIdWeb(mCursor.getInt(mCursor.getColumnIndex(CharacterContract.COL_ID_WEB)));
        character.setName(mCursor.getString(mCursor.getColumnIndex(CharacterContract.COL_NAME)));
        character.setDescription(mCursor.getString(mCursor.getColumnIndex(CharacterContract.COL_DESCRIPTION)));
        thumbnail.setExtension("jpg");
        thumbnail.setPath((mCursor.getString(mCursor.getColumnIndex(CharacterContract.COL_THUMBNAIL))));
        character.setThumbnail(thumbnail);
        character.setModified(mCursor.getString(mCursor.getColumnIndex(CharacterContract.COL_MODIFIED)));
        return character;
    }
}
