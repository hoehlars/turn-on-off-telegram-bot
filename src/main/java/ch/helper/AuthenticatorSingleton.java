package ch.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class AuthenticatorSingleton {
    private static AuthenticatorSingleton instance = new AuthenticatorSingleton();
    private Set<String> authenticatedUsers;
    public static final String AUTHENTICATED_USERS_FILE = "/authenticatedUsers/authenticatedUsers.txt";


    private AuthenticatorSingleton() {
        authenticatedUsers = getAuthenticatedUsers();
    }

    private Set<String> getAuthenticatedUsers() {
        Set<String> authedUsers = null;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(AUTHENTICATED_USERS_FILE)))) {
            authedUsers = new HashSet<String>();

            String line;
            while((line = br.readLine())!=null) {
                line = line.replace("ö", "oe");
                authedUsers.add(line);
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
        return authedUsers;
    }

    public boolean authenticate(String user) {
        return authenticatedUsers.contains(user);
    }

    public static AuthenticatorSingleton getInstance() {
        return instance;
    }
}
