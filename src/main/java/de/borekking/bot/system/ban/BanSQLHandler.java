package de.borekking.bot.system.ban;

import de.borekking.bot.Main;
import de.borekking.bot.sql.SQLTable;
import de.borekking.bot.system.SQLHandler;
import de.borekking.bot.util.sql.SQLColumn;
import de.borekking.bot.util.sql.SQLDataType;

import java.sql.ResultSet;

public class BanSQLHandler implements SQLHandler {

    // Database
    // -> user:long, timestamp:long, duration:long
    public static final SQLColumn DATABASE_BAN_USER_COLUMN = new SQLColumn(SQLDataType.STRING, "user"), // As userID
            DATABASE_BAN_TIMESTAMP_COLUMN = new SQLColumn(SQLDataType.BIGINT, "timestamp"),
            DATABASE_BAN_DURATION_COLUMN = new SQLColumn(SQLDataType.BIGINT, "duration");

    public BanSQLHandler() {
    }

    @Override
    public ResultSet get() {
        return Main.getMySQLClient().getQuery("SELECT * FROM " + SQLTable.BAN_TABLE.getName() + ";");
    }

    @Override
    public void init(String userID, long timestamp, long duration) {
        Main.getMySQLClient().update("INSERT INTO " + SQLTable.BAN_TABLE.getName() + " (" + SQLTable.BAN_TABLE.getColumns() + ") VALUES ('" + userID + "', " + duration + ", " + timestamp + ");");
    }

    /*
     * Might be replaced by Column "ignored" to be able to analyse Bans.
     *
     */
    @Override
    public void remove(String userID) {
        Main.getMySQLClient().update("DELETE FROM " + SQLTable.BAN_TABLE.getName() + " WHERE " + DATABASE_BAN_USER_COLUMN + " = '" + userID + "';");
    }
}
