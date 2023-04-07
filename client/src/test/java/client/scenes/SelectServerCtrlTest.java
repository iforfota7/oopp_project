package client.scenes;

import commons.Boards;
import commons.User;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SelectServerCtrlTest {
    private TestMainCtrl testMainCtrl;
    private TestServerUtils testServerUtils;
    private TestSelectServerCtrlServices testSelectServerCtrlServices;
    private SelectServerCtrl sut;

    @BeforeEach
    public void setup() {
        testMainCtrl = new TestMainCtrl();
        testServerUtils = new TestServerUtils();
        testSelectServerCtrlServices = new TestSelectServerCtrlServices();
        sut = new SelectServerCtrl(testServerUtils,testMainCtrl, testSelectServerCtrlServices);
        sut.setCurrentUser(new User("test", new ArrayList<>(), false));

        Text text = new Text();
        text.setVisible(true);
        sut.setServerWarning(text);
    }

    @Test
    public void constructorTest(){
        assertNotNull(sut);
    }

    @Test
    public void getterTests(){
        assertEquals(sut.getCurrentUser(), new User("test", new ArrayList<>(), false));
    }

    @Test
    public void setAdminTest(){
        assertTrue(sut.setAdmin());
    }

    @Test
    public void removeAdminTest(){
        assertTrue(sut.removeAdmin());
    }

    @Test
    public void refreshUserDetailsTest(){
        assertTrue(sut.refreshUserDetails());
    }

    @Test
    public void setBoardsOfCurrentUserTest(){
        ArrayList<Boards> boards = new ArrayList<>();
        boards.add(new Boards("name", new ArrayList<>(), new ArrayList<>()));
        sut.setBoardsOfCurrentUser(boards);
        assertEquals(sut.getCurrentUser().boards, boards);
    }

    @Test
    public void connectTestEmptyUsername(){
        assertFalse(sut.connect(":8080", null));
    }

    @Test
    public void connectTest(){
        assertTrue(sut.connect(":8080", "test"));
    }
}
