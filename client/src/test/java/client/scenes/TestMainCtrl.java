package client.scenes;

import commons.Boards;
import commons.User;

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

    @Override
    public void showBoardOverview(){
        System.out.println("Successfully showed board overview");
    }

    @Override
    public void showConfirmUsername(){
        System.out.println("Successfully showed confirmation scene for username");
    }

    @Override
    public void showUserDetails(User user){
        System.out.println("Successfully showed user details");
    }
}
