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
    private final File pluginConfigFile, serverIcon;
    private final File configFolder;

    public ConfigManager() {
        File folder = new File("./plugins/honey/");
        File dataFile = new File(folder, "honey-config.yml");
        File serverIconFile = new File(folder, "honeylogo.png");

        if (!dataFile.exists()) copyResourceToFile(dataFile, "default-honey-config.yml");
        if (!serverIconFile.exists()) copyResourceToFile(serverIconFile, "honeylogo.png");

        YamlConfiguration preConfig = YamlConfiguration.loadConfiguration(dataFile);

        boolean copyDefaultConfig = preConfig.isSet("copy_default_config") && preConfig.getBoolean("copy_default_config");

        boolean devMode = Honey.getInstance().isDevMode();
        if (devMode) {
            Bukkit.getLogger().warning("[Honey-Internal] Overwritting Config with default Config file because Dev Mode is turned on!");
            copyResourceToFile(dataFile, "default-honey-config.yml");
        }

        if (copyDefaultConfig && !devMode) {
            Bukkit.getLogger().warning("[Honey-Internal] Copying default Config File to normal Config, changes will be overwritten!");
            copyResourceToFile(dataFile, "default-honey-config.yml");
        }

        boolean forceDefaultConfig = preConfig.isSet("force_default_config") && preConfig.getBoolean("force_default_config");
        if (forceDefaultConfig && !devMode) {
            Bukkit.getLogger().warning("[Honey-Internal] Forcing Default Plugin Configuration! Can´t write Changes to config File!");
            this.pluginConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(Honey.getInstance().getResource("default-honey-config.yml")));
        }

        this.pluginConfig = YamlConfiguration.loadConfiguration(dataFile);
        this.configFolder = folder;
        this.pluginConfigFile = dataFile;
        this.serverIcon = serverIconFile;
    }

    private void copyResourceToFile(File configFile, String fileName) {
        try {
            InputStream resource = Honey.getInstance().getResource(fileName);
            FileUtils.copyInputStreamToFile(resource, configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getServerIcon() {
        return serverIcon;
    }

    public YamlConfiguration getPluginConfig() {
        return pluginConfig;
    }
}
