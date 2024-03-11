package com.example.examend.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    private static Connection c = null;

    private static final String BBDD = "clientes";

    public static Connection getConnection() throws SQLException {
        startConnection();
        return c;
    }

    private static void startConnection() throws SQLException {
        if (c==null){
            c = DriverManager.getConnection( "jdbc:mysql://localhost/"+BBDD, "root", "");
        }
        else{
            throw new RuntimeException( "Error de conexion" );
        }
    }
}
