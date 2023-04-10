package client.scenes;


import client.utils.ServerUtils;
import commons.Boards;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javax.inject.Inject;

import java.util.*;

/**
 * Controller for customization
 */
public class CustomizationCtrl {
    private final MainCtrl mainCtrl;
    private final BoardCtrl boardCtrl;
    private final ServerUtils server;

    @FXML
    private final List<ColorPicker> colorPickers = new ArrayList<>();
    @FXML
    private ColorPicker boardBgColor;
    @FXML
    private ColorPicker boardFtColor;

    @FXML
    private GridPane defaultChoice;

    @FXML
    private ColorPicker listBgColor;

    @FXML
    private ColorPicker listFtColor;
    public Map<String, String> currentColorPreset = new HashMap<>();
    @FXML
    private VBox taskColor;
    @FXML
    private ChoiceBox<?> choice;
    private boolean renameActive;
    private Boards currentBoard;
    private List<String> serverURLS;
    private Alert alert;

    /**
     * Initialize method for Customization related currentBoard
     */
    @FXML
    public void initialize() {
        colorPickers.add(boardBgColor);
        colorPickers.add(boardFtColor);
        colorPickers.add(listBgColor);
        colorPickers.add(listFtColor);
    }
    /**
     *Set the initial color of each color picker based on the
     * database storage of the current board
     * @param currentBoard current board set previously by clicking the button customization
     */
    void setColorPickers(Boards currentBoard) {
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        Map<String, String> idToColorMap = new HashMap<>();
        this.currentBoard = currentBoard;

        if(!serverURLS.contains(server.getServer())) {
            serverURLS.add(server.getServer());
            websocketConfig();
        }

        idToColorMap.put("boardBgColor", currentBoard.boardBgColor);
        idToColorMap.put("boardFtColor", currentBoard.boardFtColor);
        idToColorMap.put("listBgColor", currentBoard.listBgColor);
        idToColorMap.put("listFtColor", currentBoard.listFtColor);
        for (ColorPicker colorPicker : colorPickers) {
            String id = colorPicker.getId();
            String color = idToColorMap.get(id);
            if (color != null) {
                colorPicker.setValue(Color.web(color));
            }
            colorPicker.setOnAction(e -> {
                saveCustomization();
                setColorPickers(boardCtrl.getCurrentBoard());
            });
        }
        checkColor();
    }

    /**
     * When the customization details of a board are changed
     * by a user, they are synchronized with all other users
     *
     */
    public void websocketConfig() {
        server.registerForMessages("/topic/boards/setCss", Boards.class, board->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    setColorPickers(board);
                }
            });
        });
    }

    /**
     * Auxiliary call to mainCtrl Inject function
     *
     * @param mainCtrl          The master controller, which will later be replaced
     *                          by a class of window controllers
     * @param boardCtrl         instance of BoardCtrl
     * @param server            Used for connection to backend and websockets to function
     */
    @Inject
    public CustomizationCtrl(MainCtrl mainCtrl, BoardCtrl boardCtrl,
                             ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.boardCtrl = boardCtrl;
        this.server = server;
        serverURLS = new ArrayList<>();

    }
    /**
     *Store all color information, then add it to the server,
     * close this window, and finally refresh the displayed board colors.
     */
    @FXML
    void close() {
        saveCustomization();
        mainCtrl.closeSecondaryStage();

    }

    /**
     * Method used for saving customization
     *
     */
    public void saveCustomization() {
        renameActive = false;
        readCustomizationChange();
        setBoardToDB();
    }

    /**
     * Synchronize and save the user's modified colors, and synchronize them back to the board.
     */
    private void readCustomizationChange() {
        Map<String, String> idToColorMap = new HashMap<>();
        for (ColorPicker colorPicker : colorPickers) {
            String id = colorPicker.getId();
            String color = hexToColor(colorPicker.getValue());
            idToColorMap.put(id, color);
        }
        currentBoard.boardBgColor = idToColorMap.get("boardBgColor");
        currentBoard.boardFtColor = idToColorMap.get("boardFtColor");
        currentBoard.listBgColor = idToColorMap.get("listBgColor");
        currentBoard.listFtColor = idToColorMap.get("listFtColor");
        currentBoard.defaultColor = (String) this.choice.getSelectionModel().getSelectedItem();
    }

    /**
     * Reverts the Board's background and font color to the default ones
     */
    @FXML
    private void revertBoardCustomization(){
        boardBgColor.setValue(Color.rgb(230, 230, 250));
        boardFtColor.setValue(Color.rgb(0, 0, 0));

        saveCustomization();
    }

    /**
     * Reverts the List's background and font color to the default ones
     */
    @FXML
    private void revertListCustomization(){
        listBgColor.setValue(Color.rgb(255, 255, 255));
        listFtColor.setValue(Color.rgb(0, 0, 0));

        saveCustomization();
    }

    /**
     * Upload all new color modifications to the database.
     */
    public void setBoardToDB() {
        server.setBoardCss(currentBoard);
    }

    /**
     * Read the color information stored by the user in the database,
     * and generate a visual button window to display it in the customization section.
     * All information is mapped to colorPickers through Strings for display.
     */
    void checkColor() {
        createChoiceBox();

        TreeMap<String, String> sortedCurrentColorPreset = new TreeMap<>(currentColorPreset);

        for (Map.Entry<String, String> entry : sortedCurrentColorPreset.entrySet()) {
            String name = entry.getKey();
            String[] colors = entry.getValue().split(" ");

            HBox taskBox = new HBox();
            taskBox.setAlignment(Pos.CENTER_LEFT);
            taskBox.setSpacing(10);
            taskBox.setStyle("-fx-background-color: #e6f2ff; " +
                    "-fx-background-radius: 3; -fx-padding: 5px;");
            Label nameLabel = new Label(name);
            nameLabel.setPrefWidth(90);
            Button editButton = createNameBtn(nameLabel,taskBox);

            ColorPicker colorPicker1= createColorPicker(Color.web(colors[0]));
            ColorPicker colorPicker2= createColorPicker(Color.web(colors[1]));

            Button deleteButton = new Button("x");
            deleteButton.setPrefSize(35, 24);
            deleteButton.setPadding(new Insets(0, 5, 0, 4));
            deleteButton.setStyle("-fx-background-color: #e05252; " +
                    "-fx-background-radius: 3; -fx-text-fill: white;");
            if(nameLabel.getText()==this.choice.getSelectionModel().getSelectedItem()){
                deleteButton.setVisible(false);
                editButton.setVisible(false);
            }

            colorPicker1.setOnAction(this::presetColorPickerOnAction);
            colorPicker2.setOnAction(this::presetColorPickerOnAction);

            deleteButton.setOnAction(e -> {
                currentColorPreset.remove(name);
                saveCustomization();
            });
            taskBox.getChildren().addAll(nameLabel, editButton,
                    colorPicker1, colorPicker2, deleteButton);
            taskColor.getChildren().add(taskBox);
        }
        HBox addTaskBox = createAddColorBox();
        taskColor.getChildren().add(addTaskBox);
    }

    /**
     * When the color of a preset changes
     * The scene is re-rendered and the board is saved
     *
     * @param event Contains information about the ActionEvent
     */
    public void presetColorPickerOnAction(ActionEvent event) {
        ColorPicker target = (ColorPicker) event.getSource();
        HBox taskBox = (HBox)target.getParent();

        Color color1 = ((ColorPicker) taskBox.getChildren().get(2)).getValue();
        Color color2 = ((ColorPicker) taskBox.getChildren().get(3)).getValue();
        currentColorPreset.put(((Label)taskBox.getChildren().get(0)).getText(),
                hexToColor(color1) + " " + hexToColor(color2));

        saveCustomization();
    }

    /**
     * By creating an edit button and a text field, the renaming function is implemented.
     * When the edit button is clicked, a new UI replaces the original one.
     * Clicking on "S" button will save the latest settings.
     * @param nameLabel name Label
     * @param taskBox color preset HBox
     * @return new edit button
     */
    private Button createNameBtn(Label nameLabel, HBox taskBox) {
        TextField newName = new TextField();
        newName.setPromptText("Rename");
        newName.setPrefWidth(90);
        Button saveRename = new Button("S");
        saveRename.setPrefSize(35, 23);
        saveRename.setPadding(new Insets(2, 8, 1, 6));
        saveRename.setStyle("-fx-background-color: #66cc66; " +
                "-fx-background-radius: 3; -fx-text-fill: white;");
        Button editButton = new Button("R");
        editButton.setPrefSize(35, 23);
        editButton.setPadding(new Insets(2, 8, 1, 6));
        editButton.setStyle("-fx-background-color: #66cc66; " +
                "-fx-background-radius: 3; -fx-text-fill: white;");
        editButton.setOnAction(e -> {
            if(renameActive) {
                alert.setHeaderText("You can't rename multiple things" +
                        " at the same time!");
                alert.showAndWait();
            } else {
                renameActive = true;
                newName.setText(nameLabel.getText());
                taskBox.getChildren().removeAll(editButton, nameLabel);
                taskBox.getChildren().add(0, newName);
                taskBox.getChildren().add(1, saveRename);
            }
        });
        saveRename.setOnAction(e -> {
            String inputName = newName.getText();
            if (inputName.isEmpty()) {
                alert.setHeaderText("Name cannot be empty.");
                alert.showAndWait();
            } else if(!inputName.equals(nameLabel.getText()) &&
                    currentColorPreset.containsKey(inputName)) {
                alert.setHeaderText("There exists a preset with this name!");
                alert.showAndWait();
            } else {
                renameActive = false;
                currentColorPreset.remove(nameLabel.getText());
                Color color1 = ((ColorPicker) taskBox.getChildren().get(2)).getValue();
                Color color2 = ((ColorPicker) taskBox.getChildren().get(3)).getValue();
                currentColorPreset.put(inputName, hexToColor(color1) + " " + hexToColor(color2));
                saveCustomization();
            }
        });
        return editButton;
    }

    /**
     *  Create a new color button
     * @param color The name of the color that should be displayed
     * @return new colorPicker
     */
    private ColorPicker createColorPicker(Color color) {
        ColorPicker colorPicker1 = new ColorPicker(color);
        colorPicker1.setMaxWidth(60);
        colorPicker1.setStyle("-fx-background-color: #e6f2ff; " +
                "-fx-background-radius: 3; -fx-padding: 3px;");
        return colorPicker1;
    }

    /**
     * This method implements the function of setting the default color preset.
     * Firstly, it adds the default color value of the current board
     * to the top of the choice box in the customization section.
     * Then, it adds all the presets to the choice box.
     * Finally, it sets an action to the choice box,
     * so that it can save the user's current default value when clicked
     */
    private void createChoiceBox() {
        taskColor.getChildren().clear();
        this.currentColorPreset = currentBoard.colorPreset;
        String[] choices = currentColorPreset.keySet().toArray(new String[0]);

        for (int i = 1; i < choices.length; i++) {
            if (Objects.equals(choices[i], currentBoard.defaultColor)) {
                String temp = choices[i];
                choices[i] = choices[0];
                choices[0] = temp;
                break;
            }
        }

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        defaultChoice.add(choiceBox, 1, 0);
        choiceBox.setMaxHeight(15);
        choiceBox.setMaxWidth(100);
        this.choice = choiceBox;
        choiceBox.setStyle("-fx-background-color: #e6f2ff;" +
                "-fx-background-radius: 3; -fx-padding: 3px;");
        ObservableList<String> items = choiceBox.getItems();
        items.clear();
        items.addAll(choices);
        choiceBox.getSelectionModel().selectFirst();
        choiceBox.setOnAction(event -> {
            saveCustomization();
        });
    }


    /**
     * In addition to displaying all corresponding preset color schemes,
     * add a new input and color button to collect user's
     * new preset name and color, and store it on the server,
     * then refresh the display.
     * @return New Button Entity
     */
    private HBox createAddColorBox() {
        HBox addTaskBox = new HBox();
        addTaskBox.setId("newTask");
        addTaskBox.setAlignment(Pos.CENTER_LEFT);
        addTaskBox.setSpacing(10);
        addTaskBox.setStyle("-fx-background-color: #e6f2ff; " +
                "-fx-background-radius: 3; -fx-padding: 5px;");
        TextField nameInput = new TextField();
        nameInput.setPromptText("New name");
        nameInput.setPrefWidth(100);
        ColorPicker colorPicker1 = new ColorPicker();
        colorPicker1.setMaxWidth(60);
        colorPicker1.setStyle("-fx-background-color: #e6f2ff; " +
                "-fx-background-radius: 3; -fx-padding: 3px;");
        ColorPicker colorPicker2 = new ColorPicker();
        colorPicker2.setValue(Color.BLACK);
        colorPicker2.setMaxWidth(60);
        colorPicker2.setStyle("-fx-background-color: #e6f2ff; " +
                "-fx-background-radius: 3; -fx-padding: 3px;");
        Button addButton = new Button("+");
        addButton.setPrefSize(26, 23);
        addButton.setPadding(new Insets(-2, 0, -2, 0));
        addButton.setStyle("-fx-background-color: #66cc66;" +
                "-fx-background-radius: 3; -fx-text-fill: white; -fx-font-size: 15");
        addButton.setOnAction(e -> {
            String taskName = nameInput.getText();
            if(renameActive) {
                alert.setHeaderText("Please finish renaming the current preset!");
                alert.showAndWait();
            }
            else if (taskName.isEmpty()) {
                alert.setHeaderText("Please input name!");
                alert.showAndWait();
            } else if(currentColorPreset.containsKey(taskName)) {
                alert.setHeaderText("There exists a preset with this name!");
                alert.showAndWait();
            } else {
                Color color1 = ((ColorPicker) addTaskBox.getChildren().get(1)).getValue();
                Color color2 = ((ColorPicker) addTaskBox.getChildren().get(2)).getValue();
                currentColorPreset.put(taskName, hexToColor(color1) + " " + hexToColor(color2));

                saveCustomization();
            }
        });
        addTaskBox.getChildren().addAll(nameInput, colorPicker1, colorPicker2, addButton);
        return addTaskBox;
    }

    /**
     * Method for converting between hex representation
     * of a color to the css representation of it
     *
     * @param color The color to be converted
     * @return String containing the css representation
     */
    public String hexToColor(Color color) {
        return '#' + color.toString().substring(2, 8);
    }


}
