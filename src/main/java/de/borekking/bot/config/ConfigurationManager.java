package de.borekking.bot.config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigurationManager {

    private File config;
    private boolean firstTime;

    public ConfigurationManager(String fileName) throws IOException, ParseException {
        this.config = new File(fileName);

        if (!this.config.exists()) {
            this.firstTime = true;
            this.config.createNewFile();
        }

        this.init();
    }

    private void init() throws IOException, ParseException {
        JSONObject inputConfig = null;
        // Prevent file being empty
        if (!this.firstTime)
            inputConfig = (JSONObject) new JSONParser().parse(new FileReader(this.config));

        JSONObject config = new JSONObject();
        for (ConfigSetting setting : ConfigSetting.values()) {
            String key = setting.getKey();

            if (inputConfig != null && inputConfig.containsKey(key)) {
                Object value = inputConfig.get(key);
                config.put(key, value);
                setting.setValue(value);
            } else {
                Object value = setting.getValue();

                if (value == null) {
                    value = exceptions(key);
                    setting.setValue(value);
                }
                config.put(key, value);
            }
        }

        this.write(config);
    }

    // Will be rewritten (no time rn)
    private Object exceptions(String key) {
        switch (key) {
            case "joinInformation":
                JSONObject object = new JSONObject();
                object.put("enabled", true);
                object.put("text", "Welcome to **%servername%**, %user%!\nYou are the %memberCount%. member!");
                object.put("title", "Welcome!");
                object.put("channelID", "");
                return object;
        }

        return null;
    }

    private void write(JSONObject object) {
        try (FileWriter file = new FileWriter(this.config)) {
            file.write(object.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
