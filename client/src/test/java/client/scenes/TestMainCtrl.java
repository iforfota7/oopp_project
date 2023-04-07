package client.scenes;

import commons.Boards;

public class TestMainCtrl extends MainCtrl {

    /**
     * Mocks the behaviour of the closeSecondaryStage method in MainCtrl
     */
    @Override
    public void closeSecondaryStage(){

        System.out.println("Successfully closed secondary stage");
    }

    /**
     * Mocks the behaviour of the showBoard method in MainCtrl
     */
    @Override
    public void showBoard(Boards b) {

        System.out.println("Successfully showed board");
    }
}
