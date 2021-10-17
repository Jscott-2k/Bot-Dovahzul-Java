import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MessageListener extends ListenerAdapter {


    private Translator translator;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        Message msg = event.getMessage();
        MessageChannel channel = msg.getChannel();

        String[] messageParts = msg.getContentRaw().split("\\s+");
        String command = messageParts[0];
        char prefix = Driver.getDriver().getEnvVar("PREFIX").charAt(0);


        if (command.charAt(0) != prefix || event.getAuthor() == jda.getSelfUser() || event.getAuthor().isBot()) {
            return;
        } else {
            command = command.substring(1);
        }
        String[] messageArgs = new String[messageParts.length-1];
//        MessageAction action = msg.reply("Message Parts -> \n");
        for (int i = 0; i < messageParts.length; i++) {
//            action = action.append(messageParts[i]).append("\n");
            if (i > 0) {
                messageArgs[i-1] = messageParts[i];
            }
        }
//        action.queue();
        System.out.println("Received command '" + command + "'");
        switch (command) {
            case "test":
                msg.reply("Never Should've Come Here!").queue();
                break;
            case "dova":

                translator = new Translator();
                translator.configure(String.join(" ", messageArgs), Driver.getDriver().getEnvVar("FUN_API_KEY"));
                String translation = translator.translate();

                if(translation!=null)
                    msg.reply(translation).queue();
                else{
                    msg.reply("Error: Null response check logs").queue();
                }
                break;
            default:
                msg.reply("Command '" + command + "' unknown").queue();
                break;
        }
    }
}