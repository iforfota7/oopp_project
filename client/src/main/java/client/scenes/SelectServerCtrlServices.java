package client.scenes;

import client.utils.ServerUtils;
import commons.User;
import org.jvnet.hk2.annotations.Service;

import java.util.ArrayList;

@Service
public class SelectServerCtrlServices {

    /**
     * Method that checks whether a connection to the server can be established
     * @param address the server address
     * @param username the username of the user
     * @param server instance of ServerUtils
     * @return the user if they can exist or can be created as well as connected
     *          to the server, otherwise null
     */
    public User checkConnection(String address, String username, ServerUtils server){
        //if address is empty do nothing
        if(address == null || address.equals("")){
            return null;
        }
        // transforms to complete url
        // if begins with colon assumed to be localhost address with specified port
        if(address.startsWith("http://")) server.setServer(address);
        else if(address.startsWith(":")) server.setServer("http://localhost" + address);
        else server.setServer("http://" + address);
        // if you can connect to the specified server address
        if(server.checkServer()){
            server.setWebsockets();

            if(username == null || username.equals("")) return null;

            // set the username in the frontend
            ServerUtils.setUsername(username);
            // create user from information

            if(!server.existsUser()){
                User user = new User(username, new ArrayList<>(), false);
                server.addUser(user); // try to add user if not already in database
                return user;
            }
            else{
                User user = server.findUser();
                return user;
            }
        }
        return null;
    }
}
