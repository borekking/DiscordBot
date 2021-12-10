package de.borekking.bot.sql;

import de.borekking.bot.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLClient {

    private boolean connected;
    private Connection connection;
    private final String host, database, user, password;

    public MySQLClient(String host, String database, String user, String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;

        this.connect();
    }

    public void close() {
        if (this.connection == null)
            return;

        try {
            this.connection.close();
        } catch (SQLException ignored) {}

        this.connected = false;
    }

    public ResultSet getQuery(String qry) {
        ResultSet rs = null;

        try {
            Statement st = this.connection.createStatement();
            rs = st.executeQuery(qry);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Connection to MySQL-Database lost!");
            this.connect();
        }

        return rs;
    }

    public void update(String qry) {
        try {
            Statement st = this.connection.createStatement();
            st.executeUpdate(qry);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Connection to MySQL-Database lost!");
            this.connect();
        }
    }

    public boolean existingQuery(String query) {
        ResultSet resultSet = this.getQuery(query);
        if (resultSet == null) return false;

        try {
            return resultSet.next();
        } catch (SQLException e) {
            return false;
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void connect() {
        try {
            String url = "jdbc:mysql://" + this.host + ":3306/" + this.database + "?autoReconnect=true";
            this.connection = DriverManager.getConnection(url, this.user, this.password);
            this.connected = true;
        } catch (SQLException e) {
            e.printStackTrace();
            Main.exit();
            System.err.println("MYSQL USERNAME, IP OR PASSWORD WRONG! -> Disabled");
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
