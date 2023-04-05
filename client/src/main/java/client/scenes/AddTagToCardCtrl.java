package client.scenes;

import com.google.inject.Inject;

public class AddTagToCardCtrl {

    private final MainCtrl mainCtrl;

    /**
     * Creates an instance of AddTagToCardCtrl
     *
     * @param mainCtrl Used for navigating through different scenes
     */
    @Inject
    public AddTagToCardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * The scene is closed by pressing on the 'close' button
     *
     */
    public void close() {
        mainCtrl.closeThirdStage();
    }
}
