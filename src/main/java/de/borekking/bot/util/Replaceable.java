package de.borekking.bot.util;

import java.util.Map;

public interface Replaceable {

    void replace(String key, Object value);

    void replace(Map<String, Object> map);

}
