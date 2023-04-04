package client.scenes;

import javafx.scene.layout.HBox;

public class TestBoardCtrl extends BoardCtrl {

    private HBox firstRow;

    /**
     * Constructor of the TestBoardCtrl class
     */
    public TestBoardCtrl() {

        super();
        firstRow = new HBox();
    }

    /**
     * Mocks the behaviour of the addListToBoard method in BoardCtrl
     * @param text the name of the list
     * @param position the position of the list
     */
    @Override
    public void addListToBoard(String text, int position) {

        System.out.println("Successfully added");
    }

    /**
     * Mocks the behaviour of the getFirstRow method in BoardCtrl
     * @return the first row of lists
     */
    @Override
    public HBox getFirstRow() {
        return firstRow;
    }
}
