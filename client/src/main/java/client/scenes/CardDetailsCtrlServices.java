package client.scenes;

import commons.Cards;
import commons.Subtask;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardDetailsCtrlServices {

    /**
     * Swaps subtasks at positions i and j in the subtasks list
     *
     * @param subtaskList The list containing the subtasks
     * @param i The position of the first subtask
     * @param j The position of the second subtask
     * @return true
     */
    public boolean swapSubtasks(List<Subtask> subtaskList, int i, int j) {
        //if the positions are out of bounds of the list size
        if(i>=subtaskList.size() || j>=subtaskList.size())
            return false;
        Subtask tmp = subtaskList.get(i);
        subtaskList.set(i, subtaskList.get(j));
        subtaskList.set(j, tmp);

        return true;
    }

    /**
     * If we have multiple created subtasks a
     * JSON error will be thrown because it will have to serialize
     * multiple objects with the same id (0).
     * This block of code ensures that the ids sent are distinct.
     * @param openedCard the card whose details are opened
     * @return true if the opened card has subtasks, false otherwise
     */
    public boolean ensureIdDistinct(Cards openedCard) {

        int index = 0;
        if(openedCard.subtasks != null) {
            for(Subtask subtask : openedCard.subtasks)
                if(subtask.id == 0)
                    subtask.id = --index;

            return true;
        }

        return false;
    }
}
