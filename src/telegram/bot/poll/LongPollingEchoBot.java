package telegram.bot.poll;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class LongPollingEchoBot extends BaseBot {

	Logger log = Logger.getLogger(LongPollingEchoBot.class.getName());

	@Override
	public String getBotToken() {
		return "697190623:AAEWZIZ213z5vBT75RaP9RF2DCq0oyjBAk0";
	}

	@Override
	public void onUpdateReceived(Update update) {
		log.info("onUpdateReceived from:" + update.getMessage().getFrom().getUserName()+", message:"+update.getMessage().getText());

		if (update.hasMessage() && update.getMessage().hasText()) {
			String message_text = update.getMessage().getText();
			long chat_id = update.getMessage().getChatId();
			
			try {
				sendMessage(chat_id, message_text);
			} catch (TelegramApiException e) {
				log.log(Level.SEVERE, "wasn't able to send the message ", e);
			}
		}

	}

	@Override
	public String getBotUsername() {
		return "TSATestbot";
	}

}