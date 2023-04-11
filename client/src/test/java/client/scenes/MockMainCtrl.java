package client.scenes;

import commons.Boards;
import commons.User;

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
    public void showBoard(Boards b) { calledMethods.add("showBoard"); }
}
