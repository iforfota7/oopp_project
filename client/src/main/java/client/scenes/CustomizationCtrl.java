package client.scenes;


import client.utils.ServerUtils;
import commons.Boards;
import commons.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;

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
    private final SelectServerCtrl selectServerCtrl;
    private final BoardOverviewCtrl boardOverviewCtrl;

    @FXML
    private List<ColorPicker> colorPickers = new ArrayList<>();
    @FXML
    private ColorPicker boardBgColor;
    @FXML
    private ColorPicker boardFtColor;
    @FXML
    private ColorPicker cardBgColor;

    @FXML
    private ColorPicker cardFtColor;


    @FXML
    private ColorPicker listBgColor;

    @FXML
    private ColorPicker listFtColor;
    private Boards setBoard;
    private Map<String, String> currentColorPreset = new HashMap<>();
    @FXML
    private VBox taskColor;

    /**
     * Initialize method for Customization related currentBoard
     */
    @FXML
    public void initialize() {
        colorPickers.add(boardBgColor);
        colorPickers.add(boardFtColor);
        colorPickers.add(cardBgColor);
        colorPickers.add(cardFtColor);
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
        idToColorMap.put("cardBgColor", currentBoard.cardBgColor);
        idToColorMap.put("cardFtColor", currentBoard.cardFtColor);
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
     * @param selectServerCtrl  Used for connection to selectServerCtrl function
     * @param boardOverviewCtrl
     */
    @Inject
    public CustomizationCtrl(MainCtrl mainCtrl, BoardCtrl boardCtrl,
                             ServerUtils server, SelectServerCtrl selectServerCtrl,
                             BoardOverviewCtrl boardOverviewCtrl) {
        this.mainCtrl = mainCtrl;
        this.boardCtrl = boardCtrl;
        this.server = server;
        this.selectServerCtrl = selectServerCtrl;
        this.boardOverviewCtrl = boardOverviewCtrl;
    }
    /**
     *Store all color information, then add it to the server,
     * close this window, and finally refresh the displayed board colors.
     */
    @FXML
    void saveCustomization() {
        readCustomizationChange();
        setBoardToDB();
        boardCtrl.refreshCustomization();
        mainCtrl.closeSecondaryStage();
        saveTaskColor();
        User currentUser = selectServerCtrl.getCurrentUser();
        currentUser.isAdmin = server.checkAdmin(currentUser);
        currentUser.colorPreset = currentColorPreset;
        server.refreshTaskColor(currentUser);
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
        setBoard.cardBgColor = idToColorMap.get("cardBgColor");
        setBoard.cardFtColor = idToColorMap.get("cardFtColor");
        boardCtrl.setCurrentBoard(setBoard);
    }

    /**
     * Reset all buttons to their initial values stored in a file on the client side.
     */
    @FXML
    void revertCustomization() {
        boardBgColor.setValue(Color.rgb(230, 230, 250));
        boardFtColor.setValue(Color.rgb(0, 0, 0));
        cardBgColor.setValue(Color.rgb(230, 230, 250));
        cardFtColor.setValue(Color.rgb(0, 0, 0));
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
        taskColor.getChildren().clear();
        currentColorPreset = server.checkTaskColor(selectServerCtrl.getCurrentUser());
        for (Map.Entry<String, String> entry : currentColorPreset.entrySet()) {
            String name = entry.getKey();
            String[] colors = entry.getValue().split(" ");

            HBox taskBox = new HBox();
            taskBox.setAlignment(Pos.CENTER_LEFT);
            taskBox.setSpacing(10);
            taskBox.setStyle("-fx-background-color: #e6f2ff; " +
                    "-fx-background-radius: 3; -fx-padding: 5px;");

            Label nameLabel = new Label(name);
            nameLabel.setPrefWidth(100);

            ColorPicker colorPicker1 = new ColorPicker(Color.web(colors[0]));
            colorPicker1.setMaxWidth(60);
            colorPicker1.setStyle("-fx-background-color: #e6f2ff; " +
                    "-fx-background-radius: 3; -fx-padding: 3px;");

            ColorPicker colorPicker2 = new ColorPicker(Color.web(colors[1]));
            colorPicker2.setMaxWidth(60);
            colorPicker2.setStyle("-fx-background-color: #e6f2ff; " +
                    "-fx-background-radius: 3; -fx-padding: 3px;");

            Button deleteButton = new Button("X");
            deleteButton.setPrefSize(26, 26);
            deleteButton.setStyle("-fx-background-color: #FF9999; " +
                    "-fx-background-radius: 3; -fx-text-fill: white;");

            deleteButton.setOnAction(e -> {
                currentColorPreset.remove(name);
                taskColor.getChildren().remove(taskBox);
            });
            if(name=="default"){
                deleteButton.setVisible(false);
            }
            taskBox.getChildren().addAll(nameLabel, colorPicker1, colorPicker2, deleteButton);
            taskColor.getChildren().add(taskBox);
        }
        HBox addTaskBox = createAddTaskBox();
        taskColor.getChildren().add(addTaskBox);
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
        nameInput.setPromptText("Task name");
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
        addButton.setPrefSize(26, 26);
        addButton.setStyle("-fx-background-color: #99FF99; " +
                "-fx-background-radius: 3; -fx-text-fill: white;");
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
        User currentUser = selectServerCtrl.getCurrentUser();
        currentUser.isAdmin = server.checkAdmin(currentUser);
        currentUser.colorPreset = currentColorPreset;
        server.refreshTaskColor(currentUser);
        checkTaskColor();
    }

    /**
     * Retrieve the colors modified by the user and store
     * them back to the storage map of color presets.
     */
    void saveTaskColor() {
        for (Node node : taskColor.getChildren()) {
            if (node instanceof HBox) {
                HBox taskBox = (HBox) node;
                if (!"newTask".equals(taskBox.getId())) {
                    Label nameLabel = (Label) taskBox.getChildren().get(0);
                    ColorPicker colorPicker1 = (ColorPicker) taskBox.getChildren().get(1);
                    ColorPicker colorPicker2 = (ColorPicker) taskBox.getChildren().get(2);

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
