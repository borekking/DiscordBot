package de.borekking.bot.util.sql;

public enum SQLDataType {

    /*
     * SQL datatypes
     *
     */

    STRING("VARCHAR(255)"),
    BIGINT("BIGINT"),
    BOOL("BOOLEAN");

    private final String name;

    SQLDataType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
