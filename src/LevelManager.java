import Logic.GameLogic;
import Logic.MapLogic;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LevelManager {
    private static final Object lock = new Object();
    private static final String DIRECTORY = "levels";
    private static final String LEVEL_NAME = "level%d.dat";
    private static LevelManager instance;
    private int lastLevel = 0;
    private GameLogic gameLogic = GameLogic.getInstance();
    private MapLogic mapLogic;
    private LevelChangedHandler levelChangedHandler;

    private LevelManager() {
        mapLogic = new MapLogic();
        if (!new File(DIRECTORY).exists()) {
            new File(DIRECTORY).mkdir();
        }
        init();
    }

    public static LevelManager getInstance() {
        if (instance == null)
            synchronized (lock) {
                if (instance == null)
                    instance = new LevelManager();
            }
        return instance;
    }

    public void setOnLevelChanged(LevelChangedHandler handler) {
        levelChangedHandler = handler;
    }

    private void init() {
        File levelDir = new File(DIRECTORY);
        String[] files = levelDir.list();
        if (files == null || files.length == 0) {
            lastLevel = 0;
            return;
        }

        for (String file : files) {
            if (!file.contains("level"))
                continue;
            String levelName = file.split("\\.")[0];
            int level = Integer.parseInt(levelName.replace("level", ""));
            if (lastLevel < level)
                lastLevel = level;
        }
        if (levelChangedHandler != null)
            levelChangedHandler.onLevelChanged(lastLevel);
    }

    public void newLevel() {
        gameLogic.reset();
    }

    public boolean saveLevel(String path) {
        String saveData = mapLogic.translateGameLogic();
        File newFile = new File(path);
        try {
            FileWriter fw = new FileWriter(newFile);
            fw.write(saveData);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        gameLogic.reset();
        return true;
    }

    public boolean saveLevel() {
        if (levelChangedHandler != null)
            levelChangedHandler.onLevelChanged(++lastLevel);
        String path = new File(DIRECTORY + "/" + String.format(LEVEL_NAME, lastLevel)).getPath();
        return saveLevel(path);
    }

    public boolean loadLevel(String path) {
        gameLogic.reset();
        File file = new File(path);
        String data = "";
        if (!file.exists())
            return false;
        try {
            FileReader fr = new FileReader(file);
            int readData = fr.read();
            do {
                data = data.concat(String.valueOf((char) readData));
                readData = fr.read();
            }
            while (readData != -1);
            mapLogic.updateGameLogic(data);
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean loadLevel(int levelNum) {
        String path = new File(DIRECTORY + "/" + String.format(LEVEL_NAME, levelNum)).getPath();

        return loadLevel(path);
    }

    public boolean loadLevel() {
        if (levelChangedHandler != null)
            levelChangedHandler.onLevelChanged(lastLevel);
        return this.loadLevel(lastLevel > 1 ? lastLevel : 1);
    }

    public int getLastLevel() {
        return lastLevel;
    }

    public interface LevelChangedHandler {
        void onLevelChanged(int newLevel);
    }

}
