package iut.paci.services;

import iut.paci.paciapp.ChatActivity;
import iut.paci.paciapp.Comment;


public class ServiceForum implements IServiceForum {

	private ChatActivity activity;
	
	public ServiceForum(ChatActivity activity) {
		this.activity = activity;
        }

	public ServiceForum()
	{
		
	}
	
//	public String concat(String... args) {
//		if (args == null || args.length == 0) {
//			return "";
//		}
//		String concat = "";
//		for (String arg : args) {
//			concat += arg;
//		}
//		gui.println("Concat:" + concat);
//		return concat;
//	}
	


	public String toUpper(String text) {
            
            return text.toUpperCase();
	}


//	public void ping() {
//		// TODO Auto-generated method stub
//            gui.println("Ping: ");     
//	}

//        public String echo(String text) {
//            gui.println("Echo: " + text);
//            return text;
//        }

    public void sendMessage(Message msg) {

		activity.chatAdapter.add(new Comment(true,msg.contenu));
        activity.chatAdapter.notifyDataSetChanged();
    }
	
        
	

 
}
