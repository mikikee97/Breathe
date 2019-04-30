package com.example.breathe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class soundAdapter extends RecyclerView.Adapter<soundAdapter.SoundAdapterViewHolder> {

    Context context;
    List<UploadSong> arrayListSongs;

    public soundAdapter(Context context, List<UploadSong> arrayListSongs){
        this.context = context;
        this.arrayListSongs = arrayListSongs;
    }

    @NonNull
    @Override
    public SoundAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.sound_item,viewGroup,false);
        return new SoundAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundAdapterViewHolder holder, int i) {
        UploadSong uploadSong = arrayListSongs.get(i);
        holder.titleTxt.setText(uploadSong.getSongTitle());
        holder.durationText.setText(uploadSong.getSongDuration());
    }

    @Override
    public int getItemCount() {
        return arrayListSongs.size();
    }

    public class SoundAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleTxt, durationText;
        public SoundAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = (TextView)itemView.findViewById(R.id.song_title);
            durationText = (TextView)itemView.findViewById(R.id.song_duration);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                ((SongList)context).playSong(arrayListSongs,getAdapterPosition());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
