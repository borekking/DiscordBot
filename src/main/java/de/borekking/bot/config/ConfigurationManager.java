package de.borekking.bot.config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigurationManager {

    private final File config;
    private boolean firstTime;

    public ConfigurationManager(String fileName) throws IOException {
        this.config = new File(fileName);

        this.firstTime = this.config.createNewFile();
    }

    public void load() throws IOException, ParseException {
        JSONObject inputConfig = null;
        // Prevent file being empty
        if (!this.firstTime)
            inputConfig = (JSONObject) new JSONParser().parse(new FileReader(this.config));

        JSONObject config = new JSONObject();
        for (ConfigSetting setting : ConfigSetting.values()) {
            String key = setting.getKey();
            Object value;

            if (inputConfig != null && inputConfig.containsKey(key)) {
                value = inputConfig.get(key);
                setting.setValue(value);
            } else {
                value = setting.getValue();
            }

            config.put(key, value);
        }

        this.write(config);
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
