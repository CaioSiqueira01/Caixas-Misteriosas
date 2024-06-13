package br.com.caio.caixasmisteriosas.comandos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import br.com.caio.caixasmisteriosas.utils.DataManager;

public class ComandoCaixas implements Listener, CommandExecutor {
    public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("caixas")) {
            ListCaixas(s);
        }
        return false;
    }

    public static void ListCaixas(CommandSender s) {
        File folder = DataManager.getFolder("caixas");
        File[] file = folder.listFiles();
        List<String> caixasnome = new ArrayList<String>();
        if (file.length == 0) {
            s.sendMessage("§cNenhuma caixa foi criada té o momento.");
            return;
        }

        int cont = 0;
        for (int i=0; i < file.length; i++) {
            if (file[i].isFile()) {
                caixasnome.add(file[i].getName().replace(".yml", ""));
                cont++;
            }
        }

        String separador = "§8,§7";
        String caixaslist = caixasnome.toString();
        String caixas = "§eCaixas disponiveis("+cont+"): §7%caixas%";
        s.sendMessage(caixas.replace("%caixas%", caixaslist.substring(1,caixaslist.length() -1)).replace(",", separador));
    }
}
