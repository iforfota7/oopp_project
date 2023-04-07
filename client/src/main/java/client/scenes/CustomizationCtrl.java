package client.scenes;


import client.utils.ServerUtils;
import commons.Boards;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
    public String defaultColor;

    @FXML
    private ColorPicker listBgColor;

    @FXML
    private ColorPicker listFtColor;
    private Boards setBoard;
    public Map<String, String> currentColorPreset = new HashMap<>();
    @FXML
    private VBox taskColor;
    @FXML
    private ChoiceBox<?> choice;

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
        Map<String, String> idToColorMap = new HashMap<>();
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
        }
        checkTaskColor();
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

    }
    /**
     *Store all color information, then add it to the server,
     * close this window, and finally refresh the displayed board colors.
     */
    @FXML
    void saveCustomization() {
        saveCardColor();
        readCustomizationChange();
        setBoardToDB();
        boardCtrl.refresh();
        mainCtrl.closeSecondaryStage();

    }

    /**
     * Synchronize and save the user's modified colors, and synchronize them back to the board.
     */
    private void readCustomizationChange() {
        this.setBoard = boardCtrl.getCurrentBoard();
        Map<String, String> idToColorMap = new HashMap<>();
        for (ColorPicker colorPicker : colorPickers) {
            String id = colorPicker.getId();
            String color = "#" + colorPicker.getValue().toString().substring(2, 8);
            idToColorMap.put(id, color);
        }
        setBoard.boardBgColor = idToColorMap.get("boardBgColor");
        setBoard.boardFtColor = idToColorMap.get("boardFtColor");
        setBoard.listBgColor = idToColorMap.get("listBgColor");
        setBoard.listFtColor = idToColorMap.get("listFtColor");
        setBoard.colorPreset = currentColorPreset;
        setBoard.defaultColor = (String) this.choice.getSelectionModel().getSelectedItem();
        boardCtrl.setCurrentBoard(setBoard);
    }

    /**
     * Reset all buttons to their initial values stored in a file on the client side.
     */
    @FXML
    void revertCustomization() {
        boardBgColor.setValue(Color.rgb(230, 230, 250));
        boardFtColor.setValue(Color.rgb(0, 0, 0));
        listBgColor.setValue(Color.rgb(255, 255, 255));
        listFtColor.setValue(Color.rgb(0, 0, 0));
    }

    /**
     * Upload all new color modifications to the database.
     */
    public void setBoardToDB() {
        server.setBoardCss(setBoard);
        boardCtrl.refresh();
    }

    /**
     * Read the color information stored by the user in the database,
     * and generate a visual button window to display it in the customization section.
     * All information is mapped to colorPickers through Strings for display.
     */
    void checkTaskColor() {
        createChoiceBox();
        for (Map.Entry<String, String> entry : currentColorPreset.entrySet()) {
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
            deleteButton.setOnAction(e -> {
                currentColorPreset.remove(name);
                taskColor.getChildren().remove(taskBox);
            });
            taskBox.getChildren().addAll(nameLabel, editButton,
                    colorPicker1, colorPicker2, deleteButton);
            taskColor.getChildren().add(taskBox);
        }
        HBox addTaskBox = createAddTaskBox();
        taskColor.getChildren().add(addTaskBox);
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
        Button checkButton = new Button("S");
        checkButton.setPrefSize(35, 23);
        checkButton.setPadding(new Insets(2, 8, 1, 6));
        checkButton.setStyle("-fx-background-color: #66cc66; " +
                "-fx-background-radius: 3; -fx-text-fill: white;");
        Button editButton = new Button("R");
        editButton.setPrefSize(35, 23);
        editButton.setPadding(new Insets(2, 8, 1, 6));
        editButton.setStyle("-fx-background-color: #66cc66; " +
                "-fx-background-radius: 3; -fx-text-fill: white;");
        editButton.setOnAction(e -> {
            taskBox.getChildren().removeAll(editButton, nameLabel);
            taskBox.getChildren().add(0, newName);
            taskBox.getChildren().add(1, checkButton);
        });
        checkButton.setOnAction(e -> {
            String inputName = newName.getText();
            if (inputName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Name cannot be empty.");
                alert.showAndWait();
            } else {
                nameLabel.setText(inputName);
                newName.clear();
                taskBox.getChildren().removeAll(checkButton, newName);
                taskBox.getChildren().add(0, nameLabel);
                taskBox.getChildren().add(1, editButton);

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
        this.currentColorPreset = boardCtrl.getCurrentBoard().colorPreset;
        String[] choices = currentColorPreset.keySet().toArray(new String[0]);

        for (int i = 1; i < choices.length; i++) {
            if (Objects.equals(choices[i], boardCtrl.getCurrentBoard().defaultColor)) {
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
        choiceBox.setOnAction(event -> this.defaultColor = choiceBox.getValue());
    }


    /**
     * In addition to displaying all corresponding preset color schemes,
     * add a new input and color button to collect user's
     * new preset name and color, and store it on the server,
     * then refresh the display.
     * @return New Button Entity
     */
    private HBox createAddTaskBox() {
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
            if (taskName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Please input name!", ButtonType.OK);
                alert.showAndWait();
            } else {
                addTaskColor(taskName, colorPicker1.getValue(), colorPicker2.getValue());
                nameInput.setText("");
            }
        });

        addTaskBox.getChildren().addAll(nameInput, colorPicker1, colorPicker2, addButton);
        return addTaskBox;
    }

    /**
     * Create a trigger button to create a new color preset and save it to the database.
     * @param name name of new task color preset
     * @param color1 new before color of new task color preset
     * @param color2 new finish color of new task color preset
     */
    void addTaskColor(String name, Color color1, Color color2) {
        String value = color1.toString() + " " + color2.toString();
        currentColorPreset.put(name, value);
        boardCtrl.getCurrentBoard().colorPreset = currentColorPreset;
        checkTaskColor();
    }

    /**
     * Retrieve the colors modified by the user and store
     * them back to the storage map of color presets.
     */
    void saveCardColor() {
        for (Node node : taskColor.getChildren()) {
            if (node instanceof HBox) {
                HBox taskBox = (HBox) node;
                if (!"newTask".equals(taskBox.getId())) {
                    Label nameLabel = (Label) taskBox.getChildren().get(0);
                    ColorPicker colorPicker1 = (ColorPicker) taskBox.getChildren().get(2);
                    ColorPicker colorPicker2 = (ColorPicker) taskBox.getChildren().get(3);

                    String name = nameLabel.getText();
                    String color1 = "#" + colorPicker1.getValue().toString().substring(2, 8);
                    String color2 = "#" + colorPicker2.getValue().toString().substring(2, 8);
                    String colors = color1 + " " + color2;
                    currentColorPreset.put(name, colors);
                }
            }
        }
    }


}
