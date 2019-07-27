import Entities.Handlers.ActionHandler;
import Entities.Tiles.TileType;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class ControlPanel extends MenuBar {

    private Menu newMenuBtn;
    private Menu modeMenuBtn;
    private Menu saveMenuBtn;
    private Menu loadMenuBtn;
    private Menu tileMenuBtn;
    private LevelManager levelManager;
    private ActionHandler actionHandler;

    public ControlPanel() {
        levelManager = LevelManager.getInstance();
        newMenuBtn = new Menu("New");
        modeMenuBtn = new Menu("Change Mode...");
        saveMenuBtn = new Menu("Save");
        loadMenuBtn = new Menu("Load");
        tileMenuBtn = new Menu("Tiles...");
        initButtons();
        getMenus().addAll(newMenuBtn, modeMenuBtn, saveMenuBtn, loadMenuBtn, tileMenuBtn);
    }

    private void initButtons() {
        //Mode Initialization
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton editButton = new RadioButton("Edit Mode");
        editButton.setSelected(true);
        editButton.setTextFill(Color.BLACK);
        editButton.setToggleGroup(toggleGroup);
        RadioButton runButton = new RadioButton("Run Mode");
        runButton.setOnAction(event -> {
            if (runButton.isSelected()) actionHandler.onPlaySelected();
        });
        runButton.setToggleGroup(toggleGroup);
        runButton.setTextFill(Color.BLACK);
        CustomMenuItem editModeSelectItem = new CustomMenuItem();
        editButton.setOnAction(event -> {
            if (editButton.isSelected()) actionHandler.onEditSelected();
        });
        editModeSelectItem.setContent(editButton);
        editModeSelectItem.setHideOnClick(false);
        CustomMenuItem runModeSelectItem = new CustomMenuItem();
        runModeSelectItem.setContent(runButton);
        runModeSelectItem.setHideOnClick(false);
        modeMenuBtn.getItems().addAll(editModeSelectItem, runModeSelectItem);
        //////////////////////////////////////////////////
        //Save Initialization
        MenuItem appendSaveButton = new MenuItem("Save level #" + levelManager.getLastLevel());
        MenuItem saveAsButton = new MenuItem("Save as...");
        saveMenuBtn.getItems().addAll(appendSaveButton, saveAsButton);
        //////////////////////////////////////////////////
        // Load Initialization
        MenuItem loadLastLevelButton = new MenuItem("Load last level (#" + levelManager.getLastLevel() + ")");
        MenuItem loadLevelFromFile = new MenuItem("Load from file");
        loadMenuBtn.getItems().addAll(loadLastLevelButton, loadLevelFromFile);
        //////////////////////////////////////////////////
        // Tiles Initialization
        ToggleGroup group = new ToggleGroup();
        CustomMenuItem endMenuItem = new CustomMenuItem();
        CustomMenuItem iceMenuItem = new CustomMenuItem();
        CustomMenuItem sandMenuItem = new CustomMenuItem();
        CustomMenuItem batteryMenuItem = new CustomMenuItem();
        CustomMenuItem wallMenuItem = new CustomMenuItem();
        RadioButton endPointButton = new RadioButton("End Tiles");
        RadioButton iceAreaButton = new RadioButton("Ice Tiles");
        RadioButton sandAreaButton = new RadioButton("Sand Tiles");
        RadioButton batteryAreaButton = new RadioButton("Battery Tiles");
        RadioButton wallButton = new RadioButton("Wall Tiles");

        endPointButton.setSelected(true);
        endPointButton.setTextFill(Color.BLACK);
        endPointButton.setToggleGroup(group);
        iceAreaButton.setToggleGroup(group);
        iceAreaButton.setTextFill(Color.BLACK);
        sandAreaButton.setToggleGroup(group);
        sandAreaButton.setTextFill(Color.BLACK);
        batteryAreaButton.setToggleGroup(group);
        batteryAreaButton.setTextFill(Color.BLACK);
        wallButton.setToggleGroup(group);
        wallButton.setTextFill(Color.BLACK);
        endPointButton.setOnAction(event -> {
            if (endPointButton.isSelected()) actionHandler.onTileSelected(TileType.END_TILE);
        });

        endMenuItem.setContent(endPointButton);
        iceMenuItem.setContent(iceAreaButton);
        iceAreaButton.setOnAction(event -> {
            if (iceAreaButton.isSelected()) actionHandler.onTileSelected(TileType.ICE_TILE);
        });
        sandMenuItem.setContent(sandAreaButton);
        sandAreaButton.setOnAction(event -> {
            if (sandAreaButton.isSelected()) actionHandler.onTileSelected(TileType.SAND_TILE);
        });
        batteryMenuItem.setContent(batteryAreaButton);
        batteryAreaButton.setOnAction(event -> {
            if (batteryAreaButton.isSelected()) actionHandler.onTileSelected(TileType.BATTERY_TILE);
        });
        wallMenuItem.setContent(wallButton);
        wallButton.setOnAction(event -> {
            if (wallButton.isSelected()) actionHandler.onTileSelected(TileType.WALL_TILE);
        });

        tileMenuBtn.getItems().addAll(endMenuItem, iceMenuItem, sandMenuItem, batteryMenuItem, wallMenuItem);
        //////////////////////////////////////////////////
    }

    public void setActionHandler(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }
}
