package ua.kiev.prog.hw2.controller;

public class CommandProcessor {
    private static final CommandProcessor processor = new CommandProcessor();
    private Users users = Users.getInstance();
    private OnlineUsers onlineUsers = OnlineUsers.getInstance();
    private MessageList msgList = MessageList.getInstance();

    private CommandProcessor() {
    }

    public static CommandProcessor getInstance() {
        return processor;
    }

    public void process(Message message) {
        if (message.getText().matches("^/.*")) {
            String command = message.getText().substring(0, 2).toLowerCase();
            switch (command) {
                case "/?":
                    printHelp(message);
                    break;
                case "/w":
                    whisper(message);
                    break;
                case "/u":
                    getUserList(message);
                    break;
                case "/i":
                    invite(message);
                    break;
                case "/l":
                    leave(message);
                    break;
                case "/s":
                    getUserStatus(message);
                    break;
                default:
                    msgList.add(new Message("System", "Unknown command.", 0, message.getFrom()));
            }
        } else {
            String room;
            if ((room = onlineUsers.getRoom(message.getToken())) == null) {
                msgList.add(message);
            } else {
                message.setTo(room);
                msgList.add(message);
            }
        }
    }

    private void printHelp(Message message) {
        msgList.add(new Message("System", "Command list:\n" +
                "/? - Get help.\n" +
                "/w login message - Send private message.\n" +
                "/u - Get user list.\n" +
                "/i login - Invite to chat room.\n" +
                "/l - Leave chat room.\n" +
                "/s login - Get user status.",
                0, message.getFrom()));
    }

    private void whisper(Message message) {
        String[] parts = message.getText().substring(2).trim().split(" ");
        if (parts.length > 0 && users.isLoginTaken(parts[0])) {
            String text= "";
            for (int i = 1; i < parts.length; i++) {
                text = text + " " + parts[i];
            }
            text = text.trim();
            msgList.add(new Message(message.getFrom(), text, 0, users.getRealLogin(parts[0])));
        } else {
            msgList.add(new Message("System", "Receiver not found.", 0, message.getFrom()));
        }
    }

    private void getUserList(Message message) {
        msgList.add(new Message("System", users.getUserList(), 0, message.getFrom()));
    }

    private void getUserStatus(Message message) {
        String login = message.getText().substring(2).trim();
        if (users.isLoginTaken(login)) {
            if (onlineUsers.isUserOnline(login)) {
                msgList.add(new Message("System", users.getRealLogin(login) + " - Online.", 0, message.getFrom()));
            } else {
                msgList.add(new Message("System", users.getRealLogin(login) + " - Offline.", 0, message.getFrom()));
            }
        } else {
            msgList.add(new Message("System", "User not found.", 0, message.getFrom()));
        }
    }

    private void invite(Message message) {
        String login = message.getText().substring(2).trim();
        if (users.isLoginTaken(login)) {
            if (onlineUsers.isUserOnline(login)) {
                if (onlineUsers.getRoom(login) == null) {
                    String room;
                    if ((room = onlineUsers.getRoom(message.getToken())) == null) {
                        room = onlineUsers.getLogin(message.getToken()) + "'s room";
                    }
                    onlineUsers.setRoom(message.getToken(), room);
                    onlineUsers.setRoom(login, room);
                    msgList.add(new Message("System", message.getFrom() + " invited you to " + room + ". /l - leave.", 0, users.getRealLogin(login)));
                    msgList.add(new Message("System", message.getFrom() + " joined to " + room + ".", 0, room));
                } else {
                    msgList.add(new Message("System", users.getRealLogin(login) + " is already invited to another room.", 0, message.getFrom()));
                }
            } else {
                msgList.add(new Message("System", users.getRealLogin(login) + " - Offline.", 0, message.getFrom()));
            }
        } else {
            msgList.add(new Message("System", "User not found.", 0, message.getFrom()));
        }
    }

    private void leave(Message message) {
        String room;
        if ((room = onlineUsers.getRoom(message.getToken())) == null) {
            msgList.add(new Message("System", "You are not a member of room.", 0, message.getFrom()));
        } else {
            onlineUsers.setRoom(message.getToken(), null);
            msgList.add(new Message("System", message.getFrom() + " leave " + room + ".", 0, room));
            msgList.add(new Message("System", "You leave " + room + ".", 0, message.getFrom()));
        }
    }
}
