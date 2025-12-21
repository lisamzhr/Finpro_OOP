package com.finpro.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import java.util.HashMap;
import java.util.Map;

public class MusicManager {
    private static MusicManager instance;

    private Map<String, Music> musicTracks;
    private Music currentMusic;
    private String currentMusicKey;
    private float volume = 0.5f;

    public static final String DEFAULT_MUSIC = "default";  // Menu, Play, Result, dll
    public static final String DATING_MUSIC = "dating";    // Conversation, Challenge, Dating
    public static final String DRESSING_MUSIC = "dressing"; // Dressing room

    private MusicManager() {
        musicTracks = new HashMap<>();
        loadAllMusic();
    }

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    private void loadAllMusic() {
        try {
            musicTracks.put(DEFAULT_MUSIC, Gdx.audio.newMusic(Gdx.files.internal("music/default_music.mp3")));
            musicTracks.put(DATING_MUSIC, Gdx.audio.newMusic(Gdx.files.internal("music/dating_music.mp3")));
            musicTracks.put(DRESSING_MUSIC, Gdx.audio.newMusic(Gdx.files.internal("music/dressing_music.mp3")));

            for (Music music : musicTracks.values()) {
                music.setLooping(true);
                music.setVolume(volume);
            }

            System.out.println("✓ All music loaded successfully!");
        } catch (Exception e) {
            System.err.println("✗ Error loading music: " + e.getMessage());
        }
    }

    public void playMusic(String musicKey) {
        if (currentMusicKey != null && currentMusicKey.equals(musicKey)) {
            if (currentMusic != null && currentMusic.isPlaying()) {
                return; // Musik tetap jalan
            }
        }

        // Stop musik sebelumnya
        if (currentMusic != null) {
            currentMusic.stop();
        }

        // Play musik baru
        Music newMusic = musicTracks.get(musicKey);
        if (newMusic != null) {
            currentMusic = newMusic;
            currentMusicKey = musicKey;
            currentMusic.setVolume(volume);
            currentMusic.play();
            System.out.println("♪ Now playing: " + musicKey);
        } else {
            System.err.println("✗ Music not found: " + musicKey);
        }
    }

    public void dispose() {
        for (Music music : musicTracks.values()) {
            if (music != null) {
                music.dispose();
            }
        }
        musicTracks.clear();
        currentMusic = null;
        currentMusicKey = null;
        System.out.println("✓ Music manager disposed");
    }
}
