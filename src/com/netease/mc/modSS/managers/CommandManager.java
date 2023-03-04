package com.netease.mc.modSS.managers;

import net.minecraft.client.network.*;
import com.netease.mc.modSS.command.*;
import com.netease.mc.modSS.protecter.debugger.*;
import com.netease.mc.modSS.command.commands.*;
import java.util.*;

public class CommandManager
{
    public NetHandlerPlayClient sendQueue;
    private static CommandManager me;
    private List<Command> commands;
    private String prefix;
    
    public CommandManager() {
        this.commands = new ArrayList<Command>();
        this.prefix = ".";
        this.add(new Help());
        this.add(new Bind());
        this.add(new Toggle());
        this.add(new ValueCommand());
        this.add(new AuthCommand());
        this.add(new ConfigCommand());
        this.add(new AbuseCommand());
        this.add(new Dump());
        this.add(new Plugin());
    }
    
    public void add(final Command command) {
        this.commands.add(command);
    }
    
    public static CommandManager get() {
        return CommandManager.me;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public List<Command> getCommands() {
        return this.commands;
    }
    
    public boolean execute(String text) {
        if (!text.startsWith(this.prefix)) {
            return false;
        }
        text = text.substring(1);
        final String[] arguments = text.split(" ");
        final String ranCmd = arguments[0];
        for (final Command cmd : this.commands) {
            if (cmd.getName().equalsIgnoreCase(arguments[0])) {
                final String[] args = Arrays.copyOfRange(arguments, 1, arguments.length);
                final String[] args2 = text.split(" ");
                cmd.execute(args);
                return true;
            }
        }
        Command.msg("The command \"¡ì9" + ranCmd + "¡ì7\" has not been found!");
        return false;
    }
    
    static {
        CommandManager.me = new CommandManager();
    }
}
