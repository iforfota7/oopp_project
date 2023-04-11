package client.scenes;

import client.utils.ServerUtils;

public class MockCardCustomizationCtrl extends CardCustomizationCtrl {
    /**
     * Auxiliary call to mainCtrl Inject function
     *
     * @param boardCtrl       instance of BoardCtrl
     * @param mainCtrl        The master controller, which will later be replaced
     *                        by a class of window controllers
     * @param server          Used for connection to backend and websockets to function
     * @param cardDetailsCtrl Used to pass the information of
     *                        the current card to the scene
     */
    public MockCardCustomizationCtrl(BoardCtrl boardCtrl, MainCtrl mainCtrl,
                                     ServerUtils server, CardDetailsCtrl cardDetailsCtrl) {
        super(boardCtrl, mainCtrl, server, cardDetailsCtrl);
    }
}
