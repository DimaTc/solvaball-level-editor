public final class LevelManager {
    private final static Object lock = new Object();
    private static LevelManager instance;

    private int lastLevel = 1;
    // TODO:
    //  -Load files
    //  -Save to file

    private LevelManager() {
    }

    public static LevelManager getInstance() {
        if (instance == null)
            synchronized (lock) {
                if (instance == null)
                    instance = new LevelManager();
            }
        return instance;
    }

    public int getLastLevel() {
        return lastLevel;
    }
}
