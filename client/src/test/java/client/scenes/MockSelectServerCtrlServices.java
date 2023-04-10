package client.scenes;

import client.utils.ServerUtils;
import commons.User;

import java.util.ArrayList;

public class MockSelectServerCtrlServices extends SelectServerCtrlServices{

    @Override
    public User checkConnection(String address, String username, ServerUtils server){
        if(username == null || address == null) return null;
        return new User(username, new ArrayList<>(), false);
    }
}
