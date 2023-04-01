package client.scenes;


import commons.Boards;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Controller for customization
 */
public class CustomizationCtrl {
    private MainCtrl mainCtrl;
    private BoardCtrl boardCtrl;
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
    @FXML
    public void initialize() {
        colorPickers.add(boardBgColor);
        colorPickers.add(boardFtColor);
        colorPickers.add(cardBgColor);
        colorPickers.add(cardFtColor);
        colorPickers.add(listBgColor);
        colorPickers.add(listFtColor);
        if(boardCtrl.getCurrentBoard()==null){
            this.setBoard = boardCtrl.getCurrentBoard();
            revertCustomization();
        }else {
            this.setBoard = boardCtrl.getCurrentBoard();
            setColorPickers(setBoard);
        }
    }

    private void setColorPickers(Boards currentBoard) {
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
    }

    @Inject
    public CustomizationCtrl(MainCtrl mainCtrl, BoardCtrl boardCtrl) {
        this.mainCtrl = mainCtrl;
        this.boardCtrl = boardCtrl;
    }

    @FXML
    void saveCustomization() {
        readCustomizationChange();
        boardCtrl.setBoardToDB();
        boardCtrl.refreshCustomization();
        mainCtrl.closeCustomization();
    }
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
    @FXML
    void revertCustomization() {
        try {
            String fileName = "client/src/main/resources/client/scenes/customization(default)";
            List<String> lines = Files.readAllLines(Paths.get(
                    fileName));
            Map<String, String> idToColorMap = lines.stream()
                    .map(line -> line.split(":"))
                    .collect(Collectors.toMap(parts -> parts[0], parts -> parts[1]));
            for (ColorPicker colorPicker : colorPickers) {
                String id = colorPicker.getId();
                String color = idToColorMap.get(id);
                if (color != null) {
                    colorPicker.setValue(Color.valueOf(color));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
