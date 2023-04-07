package client.scenes;

public class TestMainCtrl extends MainCtrl {

    /**
     * Mocks the behaviour of the closeSecondaryStage method in MainCtrl
     */
    @Override
    public void closeSecondaryStage(){

        System.out.println("Successfully closed secondary stage");
    }
}
