package br.com.caio.caixasmisteriosas.eventos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.caio.caixasmisteriosas.utils.Caixas;
import br.com.caio.caixasmisteriosas.utils.DataManager;

public class ComandosListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getPlayer();
        InventoryHolder holder = inv.getHolder();
        String title = null;

        if (holder instanceof HumanEntity) {
            HumanEntity humanEntity = (HumanEntity) holder;
            title = humanEntity.getOpenInventory().getTitle();
        }

        if (title != null) { // Verificando se title não é nulo antes de usá-lo
            if (title.contains("§0Criar Caixa §n")) {
                criarCaixa(inv, p);
                return;
            }

            if (title.contains("§0Editar Caixa §n")) {
                editarCaixaItens(inv, p);
                return;
            }

            if (title.contains("§0Editar Icone §n")) {
                editarCaixaIcone(inv, p);
                return;
            }
        }
    }
    private void criarCaixa(Inventory inv, Player p) {
        ItemStack[] itens = inv.getContents();
        int j = 0;
        InventoryView view = p.getOpenInventory();
        String title = view.getTitle();
        String caixa = title.substring(16);
        File file = DataManager.getListFiles(caixa, "caixas");
        FileConfiguration config = DataManager.getConfiguration(file);
        DataManager.createFile(file);
        List<String> lore = new ArrayList<>();
        lore.add("§aLore padrão - Caixa §n" + caixa);
        lore.add("§eEdite a caixa usando /editarcaixa!");
        config.set("Icone", iconePadrao(caixa));
        config.set("Nome", "§a§lCaixa §n" + caixa);
        config.set("Lore", lore);
        config.createSection("Itens");
        for (ItemStack item : itens) {
            if (item == null) continue;
            config.set("Itens." + j, item);
            j++;
        }
        try {
            config.save(file);
            p.sendMessage("§aCaixa '" + caixa + "' criada com sucesso.");
            Caixas.carregarCaixas();
        } catch (IOException ex) {
            Bukkit.getConsoleSender().sendMessage("§cNao foi possivel salvar o arquivo da caixa '" + caixa + "'.");
        }
    }

    private void editarCaixaItens(Inventory inv, Player p) {
        ItemStack[] itens = inv.getContents();
        int j = 0;
        InventoryView view = p.getOpenInventory();
        String title = view.getTitle();
        String caixa = title.substring(17);
        File file = DataManager.getListFiles(caixa, "caixas");
        FileConfiguration config = DataManager.getConfiguration(file);
        config.set("Itens", null);
        config.createSection("Itens");
        for (ItemStack item : itens) {
            if (item == null) continue;
            config.set("Itens." + j, item);
            j++;
        }
        try {
            config.save(file);
            p.sendMessage("§aCaixa '" + caixa + "' editada com sucesso.");
            Caixas.carregarCaixas();
        } catch (IOException ex) {
            Bukkit.getConsoleSender().sendMessage("§cNao foi possivel salvar o arquivo da caixa '" + caixa + "'.");
        }
    }

    private void editarCaixaIcone(Inventory inv, Player p) {
        InventoryView view = p.getOpenInventory();
        String title = view.getTitle(); // Alteração aqui
        String caixa = title.substring(19);
        File file = DataManager.getListFiles(caixa, "caixas");
        FileConfiguration config = DataManager.getConfiguration(file);
        ItemStack icone = inv.getItem(14);
        if (icone == null || icone.getType().equals(Material.AIR)) {
            p.sendMessage("§cIcone invalido, a caixa não foi editada.");
        } else {
            icone.setAmount(1);
            config.set("Icone", icone);
            try {
                config.save(file);
                p.sendMessage("§aCaixa '" + caixa + "' editada com sucesso.");
                Caixas.carregarCaixas();
            } catch (IOException ex) {
                Bukkit.getConsoleSender().sendMessage("§cNao foi possivel salvar o arquivo da caixa '" + caixa + "'.");
            }
        }
    }

    private ItemStack iconePadrao(String id) {
        ItemStack icone = new ItemStack(Material.CHEST, 1);
        ItemMeta meta = icone.getItemMeta();
        meta.setDisplayName("§aCaixa §2" + id);
        icone.setItemMeta(meta);
        return icone;
    }
}
