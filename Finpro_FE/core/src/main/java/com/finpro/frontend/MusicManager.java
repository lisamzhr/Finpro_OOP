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

    // ✅ Only 3 music types
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
            // ✅ Load 3 musik saja - sesuaikan nama file
            musicTracks.put(DEFAULT_MUSIC, Gdx.audio.newMusic(Gdx.files.internal("music/default_music.mp3")));
            musicTracks.put(DATING_MUSIC, Gdx.audio.newMusic(Gdx.files.internal("music/dating_music.mp3")));
            musicTracks.put(DRESSING_MUSIC, Gdx.audio.newMusic(Gdx.files.internal("music/dressing_music.mp3")));

            // Set semua musik untuk loop
            for (Music music : musicTracks.values()) {
                music.setLooping(true);
                music.setVolume(volume);
            }

            System.out.println("✓ All music loaded successfully!");
        } catch (Exception e) {
            System.err.println("✗ Error loading music: " + e.getMessage());
        }
    }

    /**
     * Play music by key. Jika musik yang sama sudah playing, tidak akan restart.
     */
    public void playMusic(String musicKey) {
        // Jika musik yang sama sudah playing, skip
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

    /**
     * Stop musik saat ini
     */
    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    /**
     * Pause musik saat ini
     */
    public void pauseMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.pause();
        }
    }

    /**
     * Resume musik yang di-pause
     */
    public void resumeMusic() {
        if (currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }

    /**
     * Set volume (0.0f - 1.0f)
     */
    public void setVolume(float volume) {
        this.volume = Math.max(0f, Math.min(1f, volume));
        if (currentMusic != null) {
            currentMusic.setVolume(this.volume);
        }
    }

    /**
     * Get volume saat ini
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Check apakah musik sedang playing
     */
    public boolean isPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }

    /**
     * Get current music key
     */
    public String getCurrentMusicKey() {
        return currentMusicKey;
    }

    /**
     * Dispose semua musik - panggil saat game exit
     */
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
