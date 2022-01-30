package de.borekking.bot.system;

import de.borekking.bot.system.ban.BanSQLHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Checker {

    /*
     * Class for checking (bans and mutes).
     *
     * length=-1 means permanent.
     *
     */

    private final SQLHandler sqlHandler;

    private final Handler handler;

    public Checker(Handler handler, SQLHandler sqlHandler) {
        this.handler = handler;
        this.sqlHandler = sqlHandler;
    }

    public void checkBans(String defaultReason) {
        ResultSet rs = this.sqlHandler.get();

        try {
            while (rs.next()) {
                State state = this.getState(rs);
                String userID = rs.getString(BanSQLHandler.DATABASE_BAN_USER_COLUMN.getName());

                switch (state) {
                    case NOT_OVER:
                        break;
                    case OVER:
                        this.handler.undo(userID, defaultReason);
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    protected abstract State getState(ResultSet rs) throws SQLException;
}
