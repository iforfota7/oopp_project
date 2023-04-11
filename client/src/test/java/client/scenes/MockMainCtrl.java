package client.scenes;

import commons.Boards;

import java.util.ArrayList;
import java.util.List;

public class MockMainCtrl extends MainCtrl {

    public List<String> calledMethods;

    /**
     * Default constructor for TestMainCtrl
     *
     */
    public MockMainCtrl() {
        super();
        calledMethods = new ArrayList<>();
    }

    /**
     * Mocks the behaviour of the closeSecondaryStage method in MainCtrl
     */
    @Override
    public void closeSecondaryStage(){
        calledMethods.add("closeSecondaryStage");
    }

    /**
     * Mocks the behaviour of the showBoard method in MainCtrl
     */
    @Override
    public void showBoard(Boards b) { calledMethods.add("showBoard"); }

    /**
     * Mocks the behaviour of the customization method in MainCtrl
     */
    @Override
    public void openCardCustomization(Boards b) { calledMethods.add("checkColorPreset"); }
    /**
     * Mocks the behaviour of the closeThirdStage method in MainCtrl
     */
    @Override
    public void closeThirdStage(){calledMethods.add("closeStage");}
}
