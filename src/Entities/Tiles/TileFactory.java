package Entities.Tiles;

import Entities.Cell;

public class TileFactory {
    public static Tile getTile(TileType tileType, Cell cell) {
        Tile tile = null;
        switch (tileType) {
            case END_TILE:
                tile = new EndTile(cell, 10);
                break;
            case SAND_TILE:
                tile = new SandTile(cell, 10);
                break;
            case BATTERY_TILE:
                tile = new BatteryTile(cell, 10);
                break;
            case ICE_TILE:
                tile = new IceTile(cell, 10);
                break;
            case WALL_TILE:
                tile = new WallTile(cell, 10);
                break;
            default:
                break;
        }
        return tile;
    }

}
