package com.utp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionExample {
    private static ConnectionExample instance;
    private Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:port/name-bd";
    private static final String USER = "name-user";
    private static final String PASSWORD = "password";

    private ConnectionExample() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver de PostgreSQL no encontrado.");
            e.printStackTrace();
        }
    }

    public static synchronized ConnectionExample getInstance() {
        if (instance == null) {
            instance = new ConnectionExample();
        }
        return instance;
    }

    public Connection getConexion() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos.");
            e.printStackTrace();
        }
        return connection;
    }

    public void cerrarConexion() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
