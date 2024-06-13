package br.com.caio.caixasmisteriosas.comandos;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.caio.caixasmisteriosas.utils.DataManager;

public class ComandoCriarCaixa implements Listener, CommandExecutor {
    public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("criarcaixa")) {

            if (!(s instanceof Player)) {
                s.sendMessage("§cO console nao pode utilizar este comando!");
                return false;
            }

            if (args.length != 1) {
                s.sendMessage("§cComando incorreto, use /criarcaixa <id>");
                return false;
            }

            String caixa = args[0].toLowerCase();
            if (args[0].length() > 100) {
                s.sendMessage("§cID invalido! Por favor, use no máximo 100 digitos.");
                return false;
            }

            File file = DataManager.getFile(String.valueOf(caixa), "caixas");
            if (file.exists()) {
                s.sendMessage("§aUma caixa ja foi criada com o id '" + caixa + "', por favor escolha outro id.");
                return false;
            }

            Player p = (Player)s;
            Inventory inv = Bukkit.getServer().createInventory(p, 36, "§0Criar Caixa §n" + caixa);
            p.openInventory(inv);
        }
        return false;
    }
}
