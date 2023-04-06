package client.scenes;

import org.springframework.stereotype.Service;

@Service
public class AdListCtrlServices {

    /**
     * Calls the addListToBoard method of boardCtrl with the given list name and
     * position inside the board.
     * @param boardCtrl the board controller, which we use to add a list
     * @param newListName the name of the new list
     * @param positionInsideBoard the position of the list inside the board
     * @return true if adding the list was successful
     */
    public boolean addListToBoard(BoardCtrl boardCtrl,
                                  String newListName, int positionInsideBoard) {
        boardCtrl.addListToBoard(newListName, positionInsideBoard);
        return true;
    }

    /**
     * Gets the position inside the board of a newly created list
     * @param boardCtrl the board controller, from which we determine the position
     * @return the position of the list inside the board
     */
    public int getPositionOfListInsideBoard(BoardCtrl boardCtrl) {

        return boardCtrl.getFirstRow().getChildren().size();
    }
}
