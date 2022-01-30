package de.borekking.bot.system.ban;

import de.borekking.bot.system.Checker;
import de.borekking.bot.system.SQLHandler;
import de.borekking.bot.system.State;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BanChecker extends Checker {

    /*
     * Class for checking bans.
     *
     * length=-1 means permanent.
     *
     */

    public BanChecker(BanHandler handler, SQLHandler sqlHandler) {
        super(handler, sqlHandler);
    }

    protected State getState(ResultSet rs) throws SQLException {
        long timestamp = rs.getLong(BanSQLHandler.DATABASE_BAN_TIMESTAMP_COLUMN.getName());
        long duration = rs.getLong(BanSQLHandler.DATABASE_BAN_DURATION_COLUMN.getName());

        if (this.isOver(timestamp, duration)) return State.OVER;

        return State.NOT_OVER;
    }

    private boolean isOver(long timestamp, long duration) {
        if (duration == -1L) return false; // -1 = permanent.

        // Return if current time in millis is >= timestamp of ban + duration, s.t. the ban is over.
        return System.currentTimeMillis() >= timestamp + duration;
    }
}
