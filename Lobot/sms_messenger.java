package Twilio.twilio;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;

public class LobotSmsSender {

 /* Find your sid and token at twilio.com/user/account */
 public static final String ACCOUNT_SID = "";
 public static final String AUTH_TOKEN = "";

 public static void main(String[] args) throws TwilioRestException {

     TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

     Account account = client.getAccount();

     MessageFactory messageFactory = account.getMessageFactory();
     List<NameValuePair> params = new ArrayList<NameValuePair>();
     params.add(new BasicNameValuePair("To", ""));
     params.add(new BasicNameValuePair("From", "+12407536576")); 
     params.add(new BasicNameValuePair("Body", "This is a message from Lobot. This is only a test!"));
     messageFactory.create(params);
 }
}