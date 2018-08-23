package telegram.bot.poll;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

public class TestPollbot{
	  public static void main(String[] args) {
	        // Initialize Api Context
	        ApiContextInitializer.init();

	        // Instantiate Telegram Bots API
	        TelegramBotsApi botsApi = new TelegramBotsApi();

	        LongPollingEchoBot bot = new LongPollingEchoBot();
	        
	        // Register our bot
	        try {
	            botsApi.registerBot(bot);
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
	    }
	}

