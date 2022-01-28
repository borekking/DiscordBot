package de.borekking.bot.util.sql;

import java.util.StringJoiner;

public class SQLColumn {

    private final SQLDataType dataType;
    private final String name;

    public SQLColumn(SQLDataType dataType, String name) {
        this.dataType = dataType;
        this.name = name;
    }

    public static SQLColumn of(SQLDataType dataType, String name) {
        return new SQLColumn(dataType, name);
    }

    public static String getAsString(SQLColumn... columns) {
        StringJoiner joiner = new StringJoiner(", ");

        for (SQLColumn column : columns)
            joiner.add(column.getName() + " " + column.getDataType().getName());

        return joiner.toString();
    }

    public SQLDataType getDataType() {
        return dataType;
    }

    public String getName() {
        return name;
    }
}
