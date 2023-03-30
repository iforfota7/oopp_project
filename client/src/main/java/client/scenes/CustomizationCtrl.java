package client.scenes;


import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    @FXML
    public void initialize() {
        colorPickers.add(boardBgColor);
        colorPickers.add(boardFtColor);
        colorPickers.add(cardBgColor);
        colorPickers.add(cardFtColor);
        colorPickers.add(listBgColor);
        colorPickers.add(listFtColor);
        revertCustomization();
    }
    @Inject
    public CustomizationCtrl(MainCtrl mainCtrl, BoardCtrl boardCtrl) {
        this.mainCtrl = mainCtrl;
        this.boardCtrl = boardCtrl;
    }

    @FXML
    void saveCustomization() {
        readCustomization();
        boardCtrl.refreshCustomization();
        mainCtrl.closeCustomization();
    }
    private void readCustomization() {
        try {
            FileWriter writer = new FileWriter(
                    "client/src/main/resources/client/scenes/customization", false);
            for (ColorPicker colorPicker : colorPickers) {
                String id = colorPicker.getId();
                String color = "#" + colorPicker.getValue().toString().substring(2, 8);
                writer.write(id + ":" + color + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void revertCustomization() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(
                    "client/src/main/resources/client/scenes/customization(default)"));
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
