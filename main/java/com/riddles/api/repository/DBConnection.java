package com.riddles.api.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/riddles";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "ColaIsTree";

    private static Connection connection;

    public static Connection getConnection() {return connection;}

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            if(connection == null) System.out.println("Is null");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
