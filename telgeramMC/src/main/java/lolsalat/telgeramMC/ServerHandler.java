package lolsalat.telgeramMC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.telegram.telegrambots.meta.api.objects.Message;

import com.google.gson.JsonObject;


public class ServerHandler {

	public static int GIT_TIMEOUT = 60000;
	public static String javaPath = "java";

	public boolean started;
	public Process process;
	public String path;
	
	public ArrayList<String> cmdline = new ArrayList<>();
	
	public BufferedWriter out;
	
	public String name;
	
	public ServerHandler(JsonObject json) {
		this.name = json.get("name").getAsString();
		this.path = json.get("path").getAsString();
		cmdline.add(javaPath);
		cmdline.add("-jar");
		json.get("cmd").getAsJsonArray().forEach(
				x->cmdline.add(x.getAsString())
		);
		cmdline.add(path);
		cmdline.add("nogui");
	}
 
	public ServerHandler(String name, int ram, String path) {
		this.name = name;
		this.path = path;
		
		cmdline.add(javaPath);
		cmdline.add("-Xmx" + ram + "M");
		cmdline.add("-jar");
		cmdline.add(path);
		cmdline.add("nogui");
	}
	
	public void handleCommand(CommandBot bot, Message m, String commandline) {
	
		if(!commandline.contains(name))
			throw new IllegalArgumentException("illegal commandline!");
		
		
		
		String cmd = commandline.substring(commandline.indexOf(name) + name.length()).strip();
		String CMD = cmd;
		
		if(cmd.contains(" ")) {
			CMD = cmd.substring(0, cmd.indexOf(' '));
		}
		
		switch(CMD) {
		case "cmd" :
			command(cmd.substring(cmd.indexOf("cmd") + 3).strip());
			break;
		
		case "start":
			start();
			break;
			
		case "stop":
			stop();
			break;
			
		case "alive":
			if(alive()) {
				bot.send(m.getChatId(), "Server is alive :)");
			} else {
				bot.send(m.getChatId(), "Server is dead :(");
			}
			break;
			
		case "help": // TODO
			bot.send(m.getChatId(), "Sorry help is not implemented yet!");
			break;
		
			
			// TODO IO on load and save? Generally a logger would kinda be a good idea I guess
		case "save":
			if(alive())
				throw new IllegalStateException("Please stop server first!");
			try {
				Process shell = new ProcessBuilder().command(System.getProperty("os.name").startsWith("Windows") ? "cmd" : "/bin/sh").start();
				String folder = new File(path).getParent();
				shell.getOutputStream().write(
						String.format("git -C %s add .\n"
								    + "git -C %s commit -m \"%s\"\n"
								    + "git -C %s push\n"
								    + "exit\n", folder, folder, Calendar.getInstance().getTime(), folder).getBytes()
				);
				shell.getOutputStream().flush();
				shell.waitFor(GIT_TIMEOUT, TimeUnit.MILLISECONDS);
				if(shell.isAlive()) {
					shell.destroyForcibly();
					throw new RuntimeException("I had to kill the shell, hopefully I didn't kill your git repo ^^\nYou should probably check it :)");
				}
				if(shell.exitValue() != 0) {
					throw new RuntimeException("For some reason the shell didn't exit with exit value 0 but " + shell.exitValue() + "\nIf I were you I would check my git repo ^^");
				}
			} catch (Exception e) {
				throw new RuntimeException("Exception: " + e.getMessage());
			}
			break;
			
		case "load":
			try {
				Process shell = new ProcessBuilder().command(System.getProperty("os.name").startsWith("Windows") ? "cmd" : "/bin/sh").start();
				String folder = new File(path).getParent();
				shell.getOutputStream().write(
						String.format("git -C %s pull\n"
								    + "exit\n", folder).getBytes()
				);
				shell.getOutputStream().flush();
				shell.waitFor(GIT_TIMEOUT, TimeUnit.MILLISECONDS);
				if(shell.isAlive()) {
					shell.destroyForcibly();
					throw new RuntimeException("I had to kill the shell, hopefully I didn't kill your git repo ^^\nYou should probably check it :)");
				}
				if(shell.exitValue() != 0) {
					throw new RuntimeException("For some reason the shell didn't exit with exit value 0 but " + shell.exitValue() + "\nIf I were you I would check my git repo ^^");
				}
			} catch (Exception e) {
				throw new RuntimeException("Exception: " + e.getMessage());
			}
			break;
			
		default:
			throw new IllegalArgumentException("Syntax: server <name> <command>");
			
		}
	}
	
	public void command(String command) {
		if(!started || !process.isAlive()) {
			started = false;
			throw new IllegalStateException("Server is not started!");
		}
		try {
			out.write(command + "\n");
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException("IOException: " + e.getMessage());
		}
	}
	
	public boolean alive() {
		return started && process.isAlive();
	}
	
	public void start() {
		if(alive())
			throw new RuntimeException("Server is already started!");
		started = true;
		try {
			process = new ProcessBuilder().directory(new File(path).getParentFile()).redirectError(Redirect.DISCARD).redirectOutput(Redirect.DISCARD).command(cmdline).start();
			out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		} catch (IOException e) {
			throw new RuntimeException("IOException: " + e.getMessage());
		}
	}
	
	public void stop() {
		command("stop");
	}
	
}
