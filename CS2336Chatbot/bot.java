package chatbot;
import org.jibble.pircbot.*;

public class bot extends Pircbot{
	//constructor 
		public bot(){
	       		this.setName("myWeatherBot"); //this is the name the bot will use to join the IRC server
	   	}
	//every time a message is sent, this method will be called and this information will be passed on
	//this is how you read a message from the channel 
	public void onMessage(String channel, String sender, String login, String hostname, String message)
	    	{
	// Use this function to read the message that comes in 
	//For example, you can have an if statment that says:
	if (message.contains("weather")) {
	//the user wants weather, so call the weather API you created in part 1

	} 
	//or to start, do something small like:
	if (message.contains("Hello")) {
	
		sendMessage(channel, "Hey " + sender + "! ");

	} 
		}

}
