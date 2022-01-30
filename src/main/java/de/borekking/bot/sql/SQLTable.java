package de.borekking.bot.sql;

import de.borekking.bot.Main;
import de.borekking.bot.system.ban.BanSQLHandler;
import de.borekking.bot.system.mute.MuteSQLHandler;
import de.borekking.bot.util.sql.SQLColumn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;

public enum SQLTable {

    // Ban
    BANS_TABLE("bans", BanSQLHandler.DATABASE_BAN_USER_COLUMN, BanSQLHandler.DATABASE_BAN_DURATION_COLUMN, BanSQLHandler.DATABASE_BAN_TIMESTAMP_COLUMN),

    // Mute
    MUTES_TABLE("mutes", MuteSQLHandler.DATABASE_MUTE_USER_COLUMN, MuteSQLHandler.DATABASE_MUTE_DURATION_COLUMN, MuteSQLHandler.DATABASE_MUTE_TIMESTAMP_COLUMN);

    private final MySQLClient sqlClient;
    private final String name;
    private final SQLColumn[] sqlColumns;

    SQLTable(String name, SQLColumn... columns) {
        this.sqlClient = Main.getMySQLClient();
        this.name = name;
        this.sqlColumns = columns;
    }

    public static void loadTables() {
        for (SQLTable table : values())
            table.create();
    }

    private void create() {
        if (this.sqlClient.isConnected())
            this.sqlClient.update("CREATE TABLE IF NOT EXISTS " + this.name + "(" + SQLColumn.getAsString(this.sqlColumns) + ");");
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

    public String getColumns() {
        StringJoiner joiner = new StringJoiner(", ");

        for (SQLColumn column : this.sqlColumns)
            joiner.add(column.getName());

        return joiner.toString();
    }

    public String getName() {
        return name;
    }
}
