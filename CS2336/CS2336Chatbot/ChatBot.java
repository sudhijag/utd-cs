package chatbot;

public class ChatBot {
	public static void main(String[] args) throws Exception
	   {
	//you can read more about what these lines do in the documentation
	       bot ChatBot = new bot();
	       ChatBot.setVerbose(true);
	       ChatBot.connect("irc.freenode.net"); //tells it where to connect to - this is the same as the web interface I linked in the last slide
	       ChatBot.joinChannel("#testChannel"); // Name of channel is you want to connect to - in this case it’s called “#testChannel” 
	//this is the default message it will send when your pircbot first goes live 
	       ChatBot.sendMessage("#testChannel", "Hey! Enter any message and I’ll respond!");
		//That’s it for settingup you bot! After this, you can implemented custom logic that will look similar to the next slide
	   }
}
