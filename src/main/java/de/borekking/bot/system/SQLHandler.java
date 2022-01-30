package de.borekking.bot.system;

import java.sql.ResultSet;

public interface SQLHandler {

    ResultSet get();

    void init(String userID, long timestamp, long duration);

    /*
     * Might be replaced by Column "ignored" to be able to analyse Bans.
     *
     */
    void remove(String userID);
}
