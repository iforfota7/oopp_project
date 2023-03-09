/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import com.sun.jdi.InvocationException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Invocation;
import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static String SERVER;

    public void getQuotesTheHardWay() throws IOException {
        var url = new URL("http://localhost:8080/api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {});
    }

    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

    /**
     * Setter method for the server attribute
     * @param server the server address to be set
     */
    public static void setServer(String server){
        SERVER = server;
    }

    /**
     * Sends a simple get request to /api/test-connection in order to check if the
     * provided URL is a running instance of a Talio server
     *
     * @return True iff the client-server connection can be established
     */
    public static boolean checkServer(){
        try {
            ClientBuilder.newClient(new ClientConfig())
                    .target(SERVER).path("api/test-connection")
                    .request(TEXT_PLAIN)
                    .accept(TEXT_PLAIN)
                    .get();
            return true;
        }catch(Exception e) {
            return false;
        }


    }
}