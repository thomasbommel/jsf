package telegram.bot.poll;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class BaseBot extends TelegramLongPollingBot {

	public void sendMessage(long chatId, String messageText) throws TelegramApiException {
		SendMessage message = new SendMessage().setChatId(chatId).setText(messageText);
		execute(message);
	}
	

}
