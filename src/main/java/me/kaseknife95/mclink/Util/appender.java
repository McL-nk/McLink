package me.kaseknife95.mclink.Util;

import me.kaseknife95.mclink.Websocket_Handler;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
// other imports that you need here

public class appender extends AbstractAppender {
private Websocket_Handler WShandler;
    // your variables

    public appender(Websocket_Handler WShandler) {
        // do your calculations here before starting to capture
        super("appender", null, null);
        this.WShandler = WShandler;
        start();
    }

    @Override
    public void append(LogEvent event) {
        // if you don`t make it immutable, than you may have some unexpected behaviours
        LogEvent log = event.toImmutable();

        // do what you have to do with the log

        // you can get only the log message like this:
        String message = log.getMessage().getFormattedMessage().replaceAll("\u001b"," ");

        Format formatter = new SimpleDateFormat("HH:mm:ss");
        // and you can construct your whole log message like this:
        message = "[" +formatter.format(new Date(event.getTimeMillis())) + " " + event.getLevel().toString() + "] " + message;

        WShandler.Emit("console", message);
    }

}
