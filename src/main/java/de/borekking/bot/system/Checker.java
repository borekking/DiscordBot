package de.borekking.bot.system;

import de.borekking.bot.Main;
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

    public void startChecker(String defaultReason) {
        new CheckerThread(this, defaultReason).start();
    }

    private static class CheckerThread extends Thread {

        private final Checker banChecker;

        private final String defaultReason;

        private CheckerThread(Checker banChecker, String defaultReason) {
            this.banChecker = banChecker;
            this.defaultReason = defaultReason;
        }

        @Override
        public void run() {
            while (true) {
                if (Main.isReload()) return;
                this.banChecker.checkBans(this.defaultReason);
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkBans(String defaultReason) {
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
