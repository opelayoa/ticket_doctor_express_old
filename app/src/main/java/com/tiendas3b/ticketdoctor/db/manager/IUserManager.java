package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.User;

import java.util.ArrayList;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface IUserManager extends IDatabaseManager {

    User insertUser(User user);

    void insertOrReplaceInTxUser(User... users);

    ArrayList<User> listUser();

    void updateUser(User user);

    void deleteUser(User user);

    boolean deleteUserById(Long userId);

    User getUserById(Long userId);

    void deleteUser();

    ArrayList<User> listApplicants();

    ArrayList<User> listTechnicians();
}
