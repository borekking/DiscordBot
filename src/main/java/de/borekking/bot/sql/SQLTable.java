package de.borekking.bot.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLTable {

    private final MySQLClient sqlClient;
    private final String name, with;

    public SQLTable(MySQLClient sqlClient, String name, String with) {
        this.sqlClient = sqlClient;
        this.name = name;
        this.with = with;
    }

    public void create() {
        if (this.sqlClient.isConnected())
            this.sqlClient.update("CREATE TABLE IF NOT EXISTS " + this.name + "(" + this.with + ");");
    }

    public int size() {
        return this.count("SELECT * FROM " + this.name + ";");
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    private int count(String query) {
        int size = 0;
        ResultSet resultSet = this.sqlClient.getQuery(query);

        try {
            while (resultSet.next()) {
                size++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return size;
    }

    public String getName() {
        return name;
    }
}
