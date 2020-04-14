package lolsalat.telgeramMC;

import java.io.File;
import java.io.FileReader;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        
        CommandBot bot = new CommandBot();
        
        ServerCommand server = new ServerCommand();
        
        try {
			JsonObject config = JsonParser.parseReader(new FileReader(new File("settings.json"))).getAsJsonObject();
			if(config.has("java")) {
				ServerHandler.javaPath = config.get("java").getAsString();
			}
			JsonArray servers = config.get("servers").getAsJsonArray();
			servers.forEach(x -> {
				ServerHandler handler = new ServerHandler(x.getAsJsonObject());
				server.add(handler);
			});
		} catch(Exception e1) {
			e1.printStackTrace();
		}
        
        bot.register("server", server::command);
        bot.register("Server", server::command);

        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
