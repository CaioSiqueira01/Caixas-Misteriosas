package br.com.caio.caixasmisteriosas.comandos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.caio.caixasmisteriosas.utils.Caixas;
import br.com.caio.caixasmisteriosas.utils.DataManager;

public class ComandoEditarCaixa implements Listener, CommandExecutor {

    public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("editarcaixa")) {

            if (!(s instanceof Player)) {
                s.sendMessage("§cO console não pode utilizar este comando!");
                return false;
            }

            if (args.length < 2) {
                s.sendMessage("§cComando incorreto, use:");
                s.sendMessage("§c/editarcaixa <id> itens §8-§7 Para editar os itens.");
                s.sendMessage("§c/editarcaixa <id> icone §8-§7 Para editar o ícone da caixa.");
                s.sendMessage("§c/editarcaixa <id> nome <nome> §8-§7 Para editar o nome da caixa.");
                s.sendMessage("§c/editarcaixa <id> removelore §8-§7 Para remover a lore da caixa.");
                s.sendMessage("§c/editarcaixa <id> addlore <frase> §8-§7 Para adicionar uma linha de lore.");
                s.sendMessage("§c/editarcaixa <id> probabilidade <itemIndex> <probabilidade> §8-§7 Para editar a probabilidade de um item.");
                return false;
            }

            String caixa = args[0].toLowerCase();
            File file = DataManager.getFile(caixa, "caixas");
            if (!file.exists()) {
                s.sendMessage("§cA caixa '" + caixa + "' não existe!");
                return false;
            }

            FileConfiguration config = DataManager.getConfiguration(file);

            if (args[1].equalsIgnoreCase("itens")) {
                Player p = (Player) s;
                Set<String> ITENS = config.getConfigurationSection("Itens").getKeys(false);
                int n = ITENS.size();
                Inventory inv = Bukkit.getServer().createInventory(p, 36, "§0Editar Caixa §n" + caixa);
                for (int i = 0; i < n; i++) {
                    ItemStack item = config.getItemStack("Itens." + i + ".Item");
                    if (item != null) inv.setItem(i, item);
                }
                p.openInventory(inv);
                return true;
            }

            if (args[1].equalsIgnoreCase("nome")) {
                if (args.length < 3) {
                    s.sendMessage("§cComando incorreto, use /editarcaixa <id> nome <nome>");
                    return false;
                }
                String nome = String.join(" ", args).substring(args[0].length() + args[1].length() + 2);
                config.set("Nome", nome.replace("&", "§"));
                try {
                    config.save(file);
                    s.sendMessage("§aCaixa '" + caixa + "' editada com sucesso.");
                    Caixas.carregarCaixas();
                } catch (IOException e) {
                    Bukkit.getConsoleSender().sendMessage("§cNão foi possível salvar o arquivo da caixa '" + caixa + "'.");
                }
                return true;
            }

            if (args[1].equalsIgnoreCase("icone")) {
                abrirMenuEditarIcone((Player) s, caixa);
                return true;
            }

            if (args[1].equalsIgnoreCase("removelore")) {
                config.set("Lore", null);
                try {
                    config.save(file);
                    s.sendMessage("§aCaixa '" + caixa + "' editada com sucesso.");
                    Caixas.carregarCaixas();
                } catch (IOException e) {
                    Bukkit.getConsoleSender().sendMessage("§cNão foi possível salvar o arquivo da caixa '" + caixa + "'.");
                }
                return true;
            }

            if (args[1].equalsIgnoreCase("addlore")) {
                if (args.length < 3) {
                    s.sendMessage("§cComando incorreto, use /editarcaixa <id> addlore <frase>");
                    return false;
                }
                List<String> lore = new ArrayList<>();
                lore.addAll(config.getStringList("Lore"));
                String novaLinha = String.join(" ", args).substring(args[0].length() + args[1].length() + 2);
                lore.add(novaLinha.replace("&", "§"));
                config.set("Lore", lore);
                try {
                    config.save(file);
                    s.sendMessage("§aCaixa '" + caixa + "' editada com sucesso.");
                    Caixas.carregarCaixas();
                } catch (IOException e) {
                    Bukkit.getConsoleSender().sendMessage("§cNão foi possível salvar o arquivo da caixa '" + caixa + "'.");
                }
                return true;
            }

            if (args[1].equalsIgnoreCase("probabilidade")) {
                if (args.length < 4) {
                    s.sendMessage("§cComando incorreto, use /editarcaixa <id> probabilidade <itemIndex> <probabilidade>");
                    return false;
                }
                int itemIndex;
                double probabilidade;
                try {
                    itemIndex = Integer.valueOf(args[2]);
                    probabilidade = Double.valueOf(args[3]);
                } catch (NumberFormatException e) {
                    s.sendMessage("§cIndice ou probabilidade inválida!");
                    return false;
                }
                config.set("Itens." + itemIndex + ".Probabilidade", probabilidade);
                try {
                    config.save(file);
                    s.sendMessage("§aProbabilidade do item " + itemIndex + " editada com sucesso.");
                } catch (IOException e) {
                    Bukkit.getConsoleSender().sendMessage("§cNão foi possível salvar o arquivo da caixa '" + caixa + "'.");
                }
                return true;
            }

            s.sendMessage("§cComando incorreto, use:");
            s.sendMessage("§c/editarcaixa <id> itens §8-§7 Para editar os itens.");
            s.sendMessage("§c/editarcaixa <id> icone §8-§7 Para editar o ícone da caixa.");
            s.sendMessage("§c/editarcaixa <id> nome <nome> §8-§7 Para editar o nome da caixa.");
            s.sendMessage("§c/editarcaixa <id> removelore §8-§7 Para remover a lore da caixa.");
            s.sendMessage("§c/editarcaixa <id> addlore <frase> §8-§7 Para adicionar uma linha de lore.");
            s.sendMessage("§c/editarcaixa <id> probabilidade <itemIndex> <probabilidade> §8-§7 Para editar a probabilidade de um item.");
        }
        return false;
    }

    private void abrirMenuEditarIcone(Player p, String caixa) {
        File file = DataManager.getFile(caixa, "caixas");
        Inventory inv = Bukkit.createInventory(null, 27, "§0Icone da Caixa §n" + caixa);

        ItemStack atual = DataManager.getConfiguration(file).getItemStack("Icone");
        ItemStack vidro = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

        ItemMeta atualMeta = atual.getItemMeta();
        ItemMeta vidroMeta = vidro.getItemMeta();

        atualMeta.setDisplayName("§a§lIcone atual da caixa");
        vidroMeta.setDisplayName("§8-/-");

        atual.setItemMeta(atualMeta);
        vidro.setItemMeta(vidroMeta);

        for (int i = 0; i < 27; i++) {
            inv.setItem(i, vidro);
        }
        inv.setItem(12, atual);
        inv.setItem(14, null);

        p.openInventory(inv);
    }
}
