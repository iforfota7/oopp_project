package client.scenes;

import client.utils.ServerUtils;
import org.jvnet.hk2.annotations.Service;

@Service
public class SelectServerCtrlServices {

    /**
     * Method that checks whether a connection to the server can be established
     * @param address the server address
     * @param username the username of the user
     * @param server instance of ServerUtils
     * @return true if the user can exist or can be created as well as connected
     *          to the server, otherwise false
     */
    public boolean checkConnection(String address, String username, ServerUtils server){
        //if address is empty do nothing
        if(address == null || address.equals("")){
            return false;
        }
        // transforms to complete url
        // if begins with colon assumed to be localhost address with specified port
        if(address.startsWith("http://")) server.setServer(address);
        else if(address.startsWith(":")) server.setServer("http://localhost" + address);
        else server.setServer("http://" + address);
        // if you can connect to the specified server address
        if(server.checkServer()){
            server.setWebsockets();

            if(username == null || username.equals("")) return false;

            // set the username in the frontend
            server.setUsername(username);
            return true;
        }
        return false;
    }
}
