package com.example.stockapi.utility;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class MessagingService {

    // In a production applicatoin this would go into a config file and not be uploaded to git.
    // only sharing for interview purposes.
    public static final String ACCOUNT_SID = "AC96941a2f6813e33d7436e64145c32d47";
    public static final String AUTH_TOKEN =  "250b9f3d42be04c16d51913501e5a5c3";

    public MessagingService() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }


    public void SendMessage(String messageText) {
        log.info("Sending twilio message");

        try {
            Message message = Message
                    .creator(new PhoneNumber("+14696471464"), // to
                            new PhoneNumber("+13462753511"), // from
                            messageText)
                    .create();

            log.info("Successful message sent");

        } catch(Exception ex){
            log.warn(ex.toString());
        }

    }

}
