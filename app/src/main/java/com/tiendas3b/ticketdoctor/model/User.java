package com.tiendas3b.ticketdoctor.model;

/**
 * Created by dfa on 19/02/2016.
 */
public class User {

    private int id;
    private long employeeNum;
    private String login;
    private String password;
    private String name;
    private String lastName;
    private String lastName2;
    private String mail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getEmployeeNum() {
        return employeeNum;
    }

    public void setEmployeeNum(long employeeNum) {
        this.employeeNum = employeeNum;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
