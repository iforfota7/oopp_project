package client.scenes;

import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class TestAdListCtrl extends AdListCtrl{

    private TestBoardCtrl boardCtrl;
    private TestMainCtrl mainCtrl;
    private AdListCtrlServices adListCtrlServices;
    public Text warning;
    public List<String> calledMethods;

    /**
     * Constructor for the TestAddListCtrl
     *
     * @param testBoardCtrl Reference to testBoardCtrl
     * @param testMainCtrl Reference to testMainCtrl
     * @param adListCtrlServices Reference to addListCtrlServices
     */
    public TestAdListCtrl(BoardCtrl testBoardCtrl, MainCtrl testMainCtrl,
                      AdListCtrlServices adListCtrlServices){

        super(testBoardCtrl, testMainCtrl, adListCtrlServices);

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
