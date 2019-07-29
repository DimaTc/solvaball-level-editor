package Logic;

import Entities.Cell;
import Entities.Entity;
import Entities.Tiles.TileType;

import java.util.ArrayList;
import java.util.Arrays;

public class MapLogic {

    private GameLogic gameLogic = GameLogic.getInstance();

    private String sizeText = "s:%d\n";
    private String tilesText = "t:%s\n";
    private String entitiesText = "e:%s\n";

    public boolean updateGameLogic(String data) {
        boolean res = true;
        String[] lines = data.split("\n");
        for (String line : lines) {
            String[] tmpData = line.split(":");
            String key = tmpData[0];
            String value = tmpData[1];
            res = updateWithKeyValue(key, value);
            if (!res)
                return false;
        }
        return true;
    }

    private boolean updateWithKeyValue(String keyStr, String valueStr) {
        try {
            Key key = Key.valueOf(keyStr.toUpperCase());
            String[] values = valueStr.split(",");
            switch (key) {
                case S:
                    gameLogic.getMainGrid().reset(Integer.valueOf(values[0]));
                    break;
                case T:
                    for (String tile : values) {
                        String[] elements = tile.split(" ");
                        int[] intValues = new int[elements.length - 1];
                        for (int i = 1; i < elements.length; i++)
                            intValues[i - 1] = Integer.valueOf(elements[i]);
                        makeNewTile(elements[0], intValues);
                    }
                    break;
                case E:
                    for (String entity : values) {
                        //only ball so far so in the future I'll recode this section
                        String[] elements = entity.split(" ");
                        int[] intValues = new int[elements.length - 1];
                        for (int i = 1; i < elements.length; i++)
                            intValues[i - 1] = Integer.valueOf(elements[i]);
                        if (elements[0].toLowerCase().equals("b")) {
                            int x = intValues[0];
                            int y = intValues[1];
                            int moves = intValues[2];
                            gameLogic.addBall(x, y, moves);

                        }
                        //
                    }
                    break;
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String translateGameLogic() {
        String res = "";
        ArrayList<Cell> cells = gameLogic.getMainGrid().getCells();
        ArrayList<Entity> entities = gameLogic.getEntities();
        int gridSize = gameLogic.getMainGrid().getSize();
        String tileString = "";
        String entityString = "";
        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i).getTile() == null)
                continue;
            if (tileString.length() != 0)
                tileString = tileString.concat(",");
            tileString = tileString.concat(cells.get(i).getTile().toString());
        }
        for (int i = 0; i < entities.size(); i++) {
            if (i != 0)
                entityString = entityString.concat(",");
            entityString = entityString.concat(entities.get(i).toString());

        }

        res = res.concat(String.format(sizeText, gridSize));
        res = res.concat(String.format(tilesText, tileString));
        res = res.concat(String.format(entitiesText, entityString));

        return res;
    }

    private void makeNewTile(String keyStr, int[] tileStr) throws Exception {
        Key key = Key.valueOf(keyStr.toUpperCase());
        int indexX = tileStr[0];
        int indexY = tileStr[1];
        int[] args = Arrays.copyOfRange(tileStr, 2, tileStr.length);
        switch (key) {
            case E: //end tile
                gameLogic.addTile(TileType.END_TILE, indexX, indexY, args);
                break;
            case S: //sand
                gameLogic.addTile(TileType.SAND_TILE, indexX, indexY, args);
                break;
            case W: //wall
                gameLogic.addTile(TileType.WALL_TILE, indexX, indexY, args);
                break;
            case I: //Ice
                gameLogic.addTile(TileType.ICE_TILE, indexX, indexY, args);
                break;
            case B: //Battery
                gameLogic.addTile(TileType.BATTERY_TILE, indexX, indexY, args);
                break;
        }
    }

    private enum Key {
        S,  //size / sand
        T,  //tile
        E,  //entity / End
        W,  // Wall
        I,  // Ice
        B   // Ball, Battery

    }
}
