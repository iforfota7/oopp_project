package client.scenes;

import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class MockBoardCtrl extends BoardCtrl {

    public HBox firstRow;
    public List<String> calledMethods;

    /**
     * Constructor of the TestBoardCtrl class
     */
    public MockBoardCtrl() {

        super();
        firstRow = new HBox();
        calledMethods = new ArrayList<>();
    }

    /**
     * Mocks the behaviour of the addListToBoard method in BoardCtrl
     * @param text the name of the list
     * @param position the position of the list
     */
    @Override
    public void addListToBoard(String text, int position) {
        calledMethods.add("addListToBoard " + position);
    }

    /**
     * Mocks the behaviour of the getFirstRow method in BoardCtrl
     * @return the first row of lists
     */
    @Override
    public HBox getFirstRow() {
        return firstRow;
    }

    /**
     * Mocks the behaviour of the deleteCard method
     */
    @Override
    public void deleteCard(){}

    /**
     * Mocks the behaviour of the undeleteCard method
     */
    @Override
    public void undeleteCard(){}
}
