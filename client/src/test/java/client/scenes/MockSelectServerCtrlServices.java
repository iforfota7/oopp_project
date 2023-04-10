package client.scenes;

import client.utils.ServerUtils;

public class MockSelectServerCtrlServices extends SelectServerCtrlServices{

    @Override
    public boolean checkConnection(String address, String username, ServerUtils server){
        server.setUsername(username);
        return username != null && address != null;
    }
}
