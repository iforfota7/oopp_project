package client.scenes;

import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MockAdListCtrl extends AdListCtrl{
    public Text warning;
    public List<String> calledMethods;

    /**
     * Constructor for the TestAddListCtrl
     *
     * @param mockBoardCtrl Reference to testBoardCtrl
     * @param mockMainCtrl Reference to testMainCtrl
     * @param adListCtrlServices Reference to addListCtrlServices
     */
    public MockAdListCtrl(BoardCtrl mockBoardCtrl, MainCtrl mockMainCtrl,
                          AdListCtrlServices adListCtrlServices){

        super(mockBoardCtrl, mockMainCtrl, adListCtrlServices);

        this.warning = new Text();
        calledMethods = new ArrayList<>();
    }

    /**
     * Overrides the method setWarningVisibility in
     * AdListCtrl
     *
     * @param isVisible True iff the warning is visible
     */
    @Override
    public void setWarningVisibility(Boolean isVisible) {
        warning.setVisible(isVisible);
        calledMethods.add("setWarningVisibility");
    }

    /**
     * Overrides the method setNewListName
     * in AdListCtrl
     *
     * @param text The text that the text field will have
     */
    @Override
    public void setNewListName(String text) {
        calledMethods.add("setNewListName");
    }
}
