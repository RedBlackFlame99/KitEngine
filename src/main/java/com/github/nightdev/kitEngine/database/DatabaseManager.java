package com.github.nightdev.kitEngine.database;

import java.io.File;
import java.sql.*;

public class DatabaseManager {
    private Connection connection;

    public void connect(File file) {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + file.getAbsolutePath()
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection connection() {
        return this.connection;
    }

    public void execute(String sql, Object... objects) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String sql, Object... objects) {

        try {
            PreparedStatement statement =
                    connection.prepareStatement(sql);

            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }

            return statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void close() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
