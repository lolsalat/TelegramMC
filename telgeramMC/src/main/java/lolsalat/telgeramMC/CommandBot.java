package lolsalat.telgeramMC;

import java.util.concurrent.ConcurrentHashMap;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class CommandBot extends TelegramLongPollingBot{

	public ConcurrentHashMap<String, TriConsumer<CommandBot, Message, String> > commands = new ConcurrentHashMap<>();
	
	public void register(String command, TriConsumer<CommandBot, Message, String> callback) {
		commands.put(command, callback);
	}
	
	public void onUpdateReceived(Update update) {
		if(update.hasMessage()) {
			Message m = update.getMessage();
			if(m.hasText()) {
				String text = m.getText().strip();
				String command = text;
				if(text.contains(" ")) {
					command = text.substring(0, text.indexOf(' '));
				}
				TriConsumer<CommandBot, Message, String> cmd = commands.get(command);
				
				if(cmd == null) {
					send(m.getChatId(), "Unknown command '" + command + "'");
				} else {
					try {
						cmd.consume(this, m, text);
						send(m.getChatId(), "sucess!");
					} catch(Exception e) {
						send(m.getChatId(), "error executing command: " + e.getMessage());
					}
				}
			}
		}
	}
	
	public boolean send(long chatId, String text) {
		try {
			execute(new SendMessage(chatId, text));
			return true;
		} catch (TelegramApiException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getBotUsername() {
		return "MCRemoteServer_bot";
	}

	@Override
	public String getBotToken() {
		return "<token>";
	}

}
