package br.com.caio.caixasmisteriosas;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.caio.caixasmisteriosas.comandos.ComandoCaixas;
import br.com.caio.caixasmisteriosas.comandos.ComandoCriarCaixa;
import br.com.caio.caixasmisteriosas.comandos.ComandoDelCaixa;
import br.com.caio.caixasmisteriosas.comandos.ComandoEditarCaixa;
import br.com.caio.caixasmisteriosas.comandos.ComandoGiveCaixa;
import br.com.caio.caixasmisteriosas.eventos.AbrirCaixa;
import br.com.caio.caixasmisteriosas.eventos.ComandosListener;
import br.com.caio.caixasmisteriosas.utils.Caixas;
import br.com.caio.caixasmisteriosas.utils.DataManager;

public class CaixasMisteriosas extends JavaPlugin implements Listener {
    private static CaixasMisteriosas instance;
    @Override
    public void onEnable() {
        instance = this;
        gerarConfigs();
        registrarEventos();
        registrarComandos();
        Caixas.carregarCaixas();
    }

    public void onDisable() {
        HandlerList.unregisterAll((Listener) this);
    }

    public static CaixasMisteriosas getInstance() {
        return instance;
    }

    public void gerarConfigs() {
        saveDefaultConfig();
        DataManager.createFolder("caixas");
    }

    public void registrarEventos() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new ComandosListener(), this);
        pm.registerEvents(new AbrirCaixa(), this);
    }

    public void registrarComandos() {
        getCommand("caixas").setExecutor(new ComandoCaixas());
        getCommand("delcaixa").setExecutor(new ComandoDelCaixa());
        getCommand("editarcaixa").setExecutor(new ComandoEditarCaixa());
        getCommand("givecaixa").setExecutor(new ComandoGiveCaixa());
        getCommand("criarcaixa").setExecutor(new ComandoCriarCaixa());
    }
}