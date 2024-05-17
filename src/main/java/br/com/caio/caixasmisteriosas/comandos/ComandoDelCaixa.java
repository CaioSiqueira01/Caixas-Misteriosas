package br.com.caio.caixasmisteriosas.comandos;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import br.com.caio.caixasmisteriosas.utils.DataManager;
public class ComandoDelCaixa implements Listener, CommandExecutor {
    public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("delcaixa")) {

            if (args.length != 1) {
                s.sendMessage("§cComando incorreto, use /delcaixa <id>");
                return false;
            }

            String caixa = args[0].toLowerCase();
            File file = DataManager.getFile(caixa, "caixas");
            if (!file.exists()) {
                s.sendMessage("§cA caixa '" + caixa + "' não existe!");
                ComandoCaixas.ListCaixas(s);
                return false;
            }

            DataManager.deleteFile(file);
            s.sendMessage("§aCaixa '" + caixa + "' deletada com sucesso.");
        }
        return false;
    }
}
