import Entities.Tiles.TileType;
import Handlers.ActionHandler;
import Logic.GameLogic;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class ControlPanel extends MenuBar {

    private Menu resetMenuButton;
    private Menu modeMenuBtn;
    private Menu saveMenuBtn;
    private Menu loadMenuBtn;
    private Menu tileMenuBtn;
    private Menu gridSizeMenuBtn;
    private LevelManager levelManager;
    private ActionHandler actionHandler;

    public ControlPanel() {
        levelManager = LevelManager.getInstance();
        resetMenuButton = new Menu();
        modeMenuBtn = new Menu("Change Mode...");
        saveMenuBtn = new Menu("Save");
        loadMenuBtn = new Menu("Load");
        tileMenuBtn = new Menu("Tiles...");
        gridSizeMenuBtn = new Menu();
        initButtons();
        getMenus().addAll(resetMenuButton, modeMenuBtn, saveMenuBtn, loadMenuBtn, tileMenuBtn, gridSizeMenuBtn);
    }

    private void initButtons() {
        //Mode Initialization
        Label resetLabel = new Label("Reset");
        resetLabel.setOnMouseClicked(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Reset the level?",
                    ButtonType.YES, ButtonType.CANCEL);
            alert.setHeaderText(null);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                levelManager.newLevel();
                actionHandler.onRedraw();
            }
        });
        resetMenuButton.setGraphic(resetLabel);
        Label gridSizeLabel = new Label("Grid Size");
        ArrayList<Integer> choices = new ArrayList<>();
        for (int i = 3; i < 8; i++)
            choices.add(i);
        gridSizeLabel.setOnMouseClicked(e -> {

            ChoiceDialog<Integer> dialog = new ChoiceDialog<>(5, choices);
            dialog.setHeaderText(null);
            dialog.setTitle("Grid Size");
            dialog.setContentText("Choose the grid size:");
            Optional<Integer> res = dialog.showAndWait();
            res.ifPresent(actionHandler::onGridSizeChanged);

        });
        gridSizeMenuBtn.setGraphic(gridSizeLabel);
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
        GameLogic.getInstance().setOnEditModeChanged(editMode -> {
            if (editMode)
                editButton.setSelected(true);
            else
                runButton.setSelected(true);
        });
        editModeSelectItem.setContent(editButton);
        editModeSelectItem.setHideOnClick(false);
        CustomMenuItem runModeSelectItem = new CustomMenuItem();
        runModeSelectItem.setContent(runButton);
        runModeSelectItem.setHideOnClick(false);
        modeMenuBtn.getItems().addAll(editModeSelectItem, runModeSelectItem);
        //////////////////////////////////////////////////
        //Save Initialization
        MenuItem appendSaveButton = new MenuItem("Save level (#" + (levelManager.getLastLevel() + 1) + ")");
        MenuItem saveAsButton = new MenuItem("Save as...");
        saveMenuBtn.getItems().addAll(appendSaveButton, saveAsButton);
        //////////////////////////////////////////////////
        // Load Initialization
        MenuItem loadLastLevelButton = new MenuItem("Load last level (#" + levelManager.getLastLevel() + ")");
        loadLastLevelButton.setOnAction(event -> {
            boolean res = levelManager.loadLevel();
            actionHandler.onRedraw();
        });
        if (levelManager.getLastLevel() == 0)
            loadLastLevelButton.setDisable(true);
        appendSaveButton.setOnAction(event -> {
                    levelManager.saveLevel();
                    loadLastLevelButton.setDisable(false);
                }
        );

        saveAsButton.setOnAction(event -> {
            String filePath = getFilePath(true);
            if (filePath.length() == 0)
                return;
            levelManager.saveLevel(filePath);
        });
        MenuItem loadLevelFromFile = new MenuItem("Load from file");
        loadLevelFromFile.setOnAction(event -> {
                    String filePath = getFilePath(false);
                    if (filePath.length() == 0)
                        return;
                    levelManager.loadLevel(filePath);
                    actionHandler.onRedraw();
                }
        );
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
        levelManager.setOnLevelChanged(e -> {
            appendSaveButton.setText("Save last level (#" + (e + 1) + ")");
            loadLastLevelButton.setText("Load last level (#" + e + ")");
            if (e <= 0)
                loadLastLevelButton.setDisable(true);
        });

    }

    private String getFilePath(boolean save) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Dat files (*.dat)", "*.dat");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setInitialDirectory(new File("./levels"));
        File file;
        if (save)
            file = fileChooser.showSaveDialog(this.getContextMenu());
        else
            file = fileChooser.showOpenDialog(this.getContextMenu());
        if (file == null)
            return "";
        return file.getPath();
    }

    public void setActionHandler(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }
}
