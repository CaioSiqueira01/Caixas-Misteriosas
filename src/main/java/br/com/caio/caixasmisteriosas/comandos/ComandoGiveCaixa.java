package br.com.caio.caixasmisteriosas.comandos;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.caio.caixasmisteriosas.utils.DataManager;

public class ComandoGiveCaixa implements Listener, CommandExecutor {

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("givecaixa")) {

            if (!(s instanceof Player)) {
                if (args.length != 3) {
                    s.sendMessage("§cComando incorreto, use /givecaixa <id> [quantia] [player]");
                    return false;
                }

                String caixa = args[0].toLowerCase();
                File file = DataManager.getFile(caixa, "caixas");
                if (!file.exists()) {
                    s.sendMessage("§cA caixa '" + caixa + "' não existe!");
                    ComandoCaixas.ListCaixas(s);
                    return false;
                }

                FileConfiguration config = DataManager.getConfiguration(file);
                int quantidade;
                try {
                    quantidade = Integer.valueOf(args[1]);
                }
                catch (NumberFormatException e) {
                    s.sendMessage("§cQuantidade invalida!");
                    return false;
                }

                ItemStack ItemCaixa = caixa(config);
                ItemCaixa.setAmount(quantidade);
                if (args[2].equalsIgnoreCase("all")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        PlayerInventory inv = p.getInventory();
                        inv.addItem(ItemCaixa);
                    }
                    s.sendMessage("§a" + quantidade + "§a caixa(s) '" + caixa + "' enviada(s) para todos os players do server.");
                    return false;
                } else {
                    Player p = Bukkit.getPlayer(args[2]);
                    if (p == null) {
                        s.sendMessage("§cEste player não esta online no momento ou não existe!");
                        return false;
                    }

                    PlayerInventory inv = p.getInventory();
                    inv.addItem(ItemCaixa);
                    s.sendMessage("§a" + quantidade + "§a caixa(s) '" + caixa + "' enviada(s) com sucesso para " + p.getName() + ".");
                    return false;
                }
            }

            if (args.length < 1 || args.length > 3) {
                s.sendMessage("§cComando incorreto, use /givecaixa <id> [quantia] [player]");
                return false;
            }

            String caixa = args[0].toLowerCase();
            File file = DataManager.getFile(caixa, "caixas");
            if (!file.exists()) {
                s.sendMessage("§cA caixa '" + caixa + "' não existe!");
                ComandoCaixas.ListCaixas(s);
                return false;
            }

            FileConfiguration config = DataManager.getConfiguration(file);

            if (args.length == 2) {
                int quantidade;
                try {
                    quantidade = Integer.valueOf(args[1]);
                }
                catch (NumberFormatException e) {
                    s.sendMessage("§cQuantidade invalida!");
                    return false;
                }
                Player p = (Player)s;
                PlayerInventory inv = p.getInventory();
                ItemStack ItemCaixa = caixa(config);
                ItemCaixa.setAmount(quantidade);
                inv.addItem(ItemCaixa);
                s.sendMessage("§a" + quantidade + "§a caixa(s) '" + caixa + "' enviada(s) para o seu inventário.");
                return false;
            }

            if (args.length == 3) {
                int quantidade;
                try {
                    quantidade = Integer.valueOf(args[1]);
                }
                catch (NumberFormatException e) {
                    s.sendMessage("§cQuantidade invalida!");
                    return false;
                }

                ItemStack ItemCaixa = caixa(config);
                ItemCaixa.setAmount(quantidade);
                if (args[2].equalsIgnoreCase("all")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        PlayerInventory inv = p.getInventory();
                        inv.addItem(ItemCaixa);
                    }
                    s.sendMessage("§a" + quantidade + "§a caixa(s) '" + caixa + "' enviada(s) para todos os players do server.");
                    return false;
                } else {
                    Player p = Bukkit.getPlayer(args[2]);
                    if (p == null) {
                        s.sendMessage("§cEste player não esta online no momento ou não existe!");
                        return false;
                    }

                    PlayerInventory inv = p.getInventory();
                    inv.addItem(ItemCaixa);
                    s.sendMessage("§a" + quantidade + "§a caixa(s) '" + caixa + "' enviada(s) com sucesso para " + p.getName() + ".");
                    return false;
                }
            }

            Player p = (Player)s;
            PlayerInventory inv = p.getInventory();
            inv.addItem(caixa(config));
            s.sendMessage("§a1 caixa '" + caixa + "' enviada para o seu inventário.");
        }
        return false;
    }

    private ItemStack caixa(FileConfiguration config) {
        ItemStack caixa = config.getItemStack("Icone");
        ItemMeta meta = caixa.getItemMeta();
        meta.setDisplayName(config.getString("Nome"));
        List<String> lore = config.getStringList("Lore");
        if (!lore.isEmpty()) meta.setLore(lore);
        caixa.setItemMeta(meta);
        return caixa;
    }
}
