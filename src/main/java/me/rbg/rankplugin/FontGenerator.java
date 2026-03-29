package me.rbg.rankplugin;

import com.google.gson.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FontGenerator {
    public static void generate(JavaPlugin plugin, RankManager rankManager) {
        int height = plugin.getConfig().getInt("icon.height", 8);
        int ascent = plugin.getConfig().getInt("icon.ascent", 8);

        JsonArray providers = new JsonArray();
        int counter = 1;

        for (String rank : rankManager.getAllRanks()) {
            JsonObject provider = new JsonObject();
            provider.addProperty("type", "bitmap");
            provider.addProperty("file", "minecraft:rank/" + rank + "_tag.png");
            provider.addProperty("ascent", ascent);
            provider.addProperty("height", height);

            JsonArray chars = new JsonArray();
            // Unicode karakteri runtime’da oluşturuyoruz (illegal escape hatası çözülür)
            chars.add(new String(new char[]{(char) (0xE000 + counter)}));
            provider.add("chars", chars);

            providers.add(provider);
            counter++;
        }

        JsonObject root = new JsonObject();
        root.add("providers", providers);

        try {
            File fontFile = new File(plugin.getDataFolder().getParentFile(),
                    "resourcepack/assets/minecraft/font/default.json");
            fontFile.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(fontFile)) {
                new GsonBuilder().setPrettyPrinting().create().toJson(root, writer);
            }
            plugin.getLogger().info("✅ Font default.json başarıyla oluşturuldu!");
        } catch (IOException e) {
            plugin.getLogger().severe("❌ Font dosyası oluşturulamadı!");
            e.printStackTrace();
        }
    }
}
