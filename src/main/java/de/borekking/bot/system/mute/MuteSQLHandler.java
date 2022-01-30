package de.borekking.bot.system.mute;

import de.borekking.bot.Main;
import de.borekking.bot.sql.SQLTable;
import de.borekking.bot.system.SQLHandler;
import de.borekking.bot.util.sql.SQLColumn;
import de.borekking.bot.util.sql.SQLDataType;

import java.sql.ResultSet;

public class MuteSQLHandler implements SQLHandler {

    // Database
    // -> user:long, timestamp:long, duration:long
    public static final SQLColumn DATABASE_MUTE_USER_COLUMN = new SQLColumn(SQLDataType.STRING, "user"), // As userID
            DATABASE_MUTE_TIMESTAMP_COLUMN = new SQLColumn(SQLDataType.BIGINT, "timestamp"),
            DATABASE_MUTE_DURATION_COLUMN = new SQLColumn(SQLDataType.BIGINT, "duration");

    public MuteSQLHandler() {
    }

    @Override
    public ResultSet get() {
        return Main.getMySQLClient().getQuery("SELECT * FROM " + SQLTable.MUTES_TABLE.getName() + ";");
    }

    @Override
    public void init(String userID, long timestamp, long duration) {
        Main.getMySQLClient().update("INSERT INTO " + SQLTable.MUTES_TABLE.getName() + " (" + SQLTable.MUTES_TABLE.getColumns() + ") VALUES ('" + userID + "', " + duration + ", " + timestamp + ");");
    }

    @Override
    public void remove(String userID) {
        Main.getMySQLClient().update("DELETE FROM " + SQLTable.MUTES_TABLE.getName() + " WHERE " + DATABASE_MUTE_USER_COLUMN + " = '" + userID + "';");
    }
}
