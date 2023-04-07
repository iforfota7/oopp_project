package client.scenes;

import java.util.ArrayList;
import java.util.List;

public class TestMainCtrl extends MainCtrl {

    public List<String> calledMethods;

    /**
     * Default constructor for TestMainCtrl
     *
     */
    public TestMainCtrl() {
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
}
