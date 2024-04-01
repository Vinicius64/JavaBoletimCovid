package br.ifsp.covid.persistence;

import java.sql.*;

public class ConnectionFactory {
    private static Connection connection;

    public static Connection createConnection() throws SQLException {
        if(connection == null)
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        return connection;
    }

    public static PreparedStatement createPreparedStatement(String sql) throws SQLException {
        return createConnection().prepareStatement(sql);
    }
}
