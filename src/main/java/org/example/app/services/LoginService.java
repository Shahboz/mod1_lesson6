package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.LoginForm;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService {

    private Logger logger = Logger.getLogger(LoginService.class);
    private final List<LoginForm> userList = new ArrayList<>();

    public List<LoginForm> retreiveAll() {
        return new ArrayList<>(userList);
    }

    public boolean authenticate(LoginForm loginFrom) {
        logger.info("try auth with user-form: " + loginFrom);
        for(LoginForm user : retreiveAll()) {
            if(user.equals(loginFrom)) {
                return true;
            }
        }
        return false;
        //return loginFrom.getUsername().equals("root") && loginFrom.getPassword().equals("123");
    }

    public boolean addUser(LoginForm user) {
        userList.add(user);
        logger.info("Users count:" + userList.size());
        return true;
    }

}
