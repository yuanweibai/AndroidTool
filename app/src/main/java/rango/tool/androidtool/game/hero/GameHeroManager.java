package rango.tool.androidtool.game.hero;

import rango.tool.common.utils.Preferences;

public class GameHeroManager {

    private static final String PREF_KEY_GAME_BEST_SCORE = "pref_key_game_best_score";
    private static volatile GameHeroManager sInstance;
    private Preferences gamePreferences;

    private GameHeroManager() {
        gamePreferences = Preferences.getDefault();
    }

    public static GameHeroManager getInstance() {
        if (sInstance == null) {
            synchronized (GameHeroManager.class) {
                if (sInstance == null) {
                    sInstance = new GameHeroManager();
                }
            }
        }
        return sInstance;
    }

    public void updateBestScore(int score) {
        gamePreferences.putInt(PREF_KEY_GAME_BEST_SCORE, score);
    }

    public int getBestScore() {
        return gamePreferences.getInt(PREF_KEY_GAME_BEST_SCORE, 0);
    }
}
