package de.borekking.bot.ban;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;

public class BanChecker {

    /*
     * Class for checking bans.
     *
     * length=-1 means permanent.
     *
     */

    private final BiConsumer<BanState, String> c;

    private final BanSQLHandler handler;

    public BanChecker(BanSQLHandler handler, BiConsumer<BanState, String> c) {
        this.c = c;

        this.handler = handler;
    }

    public void checkBans() {
        ResultSet rs = this.handler.getSQLBans();

        try {
            while (rs.next()) {
                BanState state = this.getBanState(rs);
                String userID = rs.getString(BanSQLHandler.DATABASE_BAN_USER_COLUMN.getName());
                this.c.accept(state, userID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private BanState getBanState(ResultSet rs) throws SQLException {
        long timestamp = rs.getLong(BanSQLHandler.DATABASE_BAN_TIMESTAMP_COLUMN.getName());
        long duration = rs.getLong(BanSQLHandler.DATABASE_BAN_DURATION_COLUMN.getName());

        if (this.isOver(timestamp, duration)) return BanState.UNBAN;

        return BanState.BAN;
    }

    private boolean isOver(long timestamp, long duration) {
        if (duration == -1L) return false; // -1 = permanent.

        // Return if current time in millis is >= timestamp of ban + duration, s.t. the ban is over.
        return System.currentTimeMillis() >= timestamp + duration;
    }
}
