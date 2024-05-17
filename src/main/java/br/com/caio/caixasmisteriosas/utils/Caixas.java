package br.com.caio.caixasmisteriosas.utils;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Caixas {
    public static ConcurrentHashMap<ItemStack, String> caixas = new ConcurrentHashMap<ItemStack, String>();

    public static void carregarCaixas() {
        File folder = DataManager.getFolder("caixas");
        File[] files = folder.listFiles();

        for (int i=0; i < files.length; i++) {
            if (files[i].isFile()) {
                File file = files[i];
                String id = file.getName().replace(".yml", "");
                FileConfiguration config = DataManager.getConfiguration(file);
                ItemStack caixa = caixa(config);
                caixas.put(caixa, id);
            }
        }
    }

    private static ItemStack caixa(FileConfiguration config) {
        ItemStack caixa = config.getItemStack("Icone");
        ItemMeta meta = caixa.getItemMeta();
        meta.setDisplayName(config.getString("Nome"));
        List<String> lore = config.getStringList("Lore");
        if (!lore.isEmpty()) meta.setLore(lore);
        caixa.setItemMeta(meta);
        return caixa;
    }
}
