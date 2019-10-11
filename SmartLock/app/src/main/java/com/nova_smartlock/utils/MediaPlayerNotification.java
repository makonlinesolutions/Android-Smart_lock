package com.nova_smartlock.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.nova_smartlock.R;

public class MediaPlayerNotification {
    public static MediaPlayer player;

    public static void SoundPlayer(Context ctx) {
        player = MediaPlayer.create(ctx, R.raw.door_bell);
        player.setLooping(false);
        player.setVolume(100.0f, 100.0f);
        player.start();
    }
}
