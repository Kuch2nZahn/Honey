package io.github.thewebcode.honey.config;

import io.github.thewebcode.honey.Honey;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigManager {
    private YamlConfiguration pluginConfig;
    private final File pluginConfigFile;
    private final File configFolder;

    public ConfigManager() {
        File folder = new File("./plugins/honey/");
        File dataFile = new File(folder, "honey-config.yml");

        if (!dataFile.exists()) copyDefaultConfigToDataFile(dataFile);

        YamlConfiguration preConfig = YamlConfiguration.loadConfiguration(dataFile);

        boolean copyDefaultConfig = preConfig.isSet("copy_default_config") && preConfig.getBoolean("copy_default_config");

        boolean devMode = Honey.getInstance().isDevMode();
        if (devMode) {
            Bukkit.getLogger().warning("[Honey-Internal] Overwritting Config with default Config file because Dev Mode is turned on!");
            copyDefaultConfigToDataFile(dataFile);
        }

        if (copyDefaultConfig && !devMode) {
            Bukkit.getLogger().warning("[Honey-Internal] Copying default Config File to normal Config, changes will be overwritten!");
            copyDefaultConfigToDataFile(dataFile);
        }

        boolean forceDefaultConfig = preConfig.isSet("force_default_config") && preConfig.getBoolean("force_default_config");
        if (forceDefaultConfig && !devMode) {
            Bukkit.getLogger().warning("[Honey-Internal] Forcing Default Plugin Configuration! Can´t write Changes to config File!");
            this.pluginConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(Honey.getInstance().getResource("default-honey-config.yml")));
        }

        this.pluginConfig = YamlConfiguration.loadConfiguration(dataFile);
        this.configFolder = folder;
        this.pluginConfigFile = dataFile;
    }

    private void copyDefaultConfigToDataFile(File configFile) {
        try {
            InputStream resource = Honey.getInstance().getResource("default-honey-config.yml");
            FileUtils.copyInputStreamToFile(resource, configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            //pluginConfig.save(pluginConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getPluginConfig() {
        return pluginConfig;
    }
}
