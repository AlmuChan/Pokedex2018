package com.example.almu.pokedex2018;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class PokeUtils {

    public static void playSound(int numCapturado) {
        String idFormatted = String.format("%03d", numCapturado);
        String url = "http://pokedream.com/pokedex/images/cries/" + idFormatted + ".mp3";
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
        }
        mediaPlayer.prepareAsync();
        //You can show progress dialog here untill it prepared to play
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }
}
