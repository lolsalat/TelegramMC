package lolsalat.telgeramMC;

import java.util.concurrent.ConcurrentHashMap;

import org.telegram.telegrambots.meta.api.objects.Message;


public class ServerCommand {

	public ConcurrentHashMap<String, ServerHandler> servers = new ConcurrentHashMap<>();
	
	public void add(ServerHandler server) {
		servers.put(server.name, server);
	}
	
	public void command(CommandBot bot, Message message, String text) {
		text = text.strip();
		
		
		if(!text.contains(" ")) {
			throw new IllegalArgumentException("Syntax: server <name> <command>");
		}
		
		String cmd = text.substring(text.indexOf(" ")).strip();
		
		String name = cmd;
		if(cmd.contains(" ")) {
			name = cmd.substring(0, cmd.indexOf(" "));
		}
		
		ServerHandler handler = servers.get(name);
		
		if(handler == null)
			throw new IllegalArgumentException("No server named '" + name + "'");
		
		handler.handleCommand(bot, message, text);
	}
	
}
