package fr.pc3r.jetrouvemonjob.beans;

import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Logger {
    private final static String OUTPUT_FILE = "logs/log.txt";
    // TODO :: change this logger to log4j2
    private static Logger logger;
    private BufferedWriter writter;
    private ExecutorService handlerRequests;
    private static final Object mutex = new Object();

    private Logger() {
        synchronized (mutex) {
            try {
                // this is the production file 
                //String outputFilePath = getClass().getClassLoader().getResource(OUTPUT_FILE).getPath();
                //writter = new BufferedWriter(new FileWriter(new File(outputFilePath)));
                // this is just for debug
                writter = new BufferedWriter(new FileWriter(new File("/tmp/log.txt")));
                handlerRequests = Executors.newFixedThreadPool(2);
                System.out.println(handlerRequests);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Logger getLogger() {
        if(Logger.logger == null) {
            Logger.logger = new Logger();
        }
        return Logger.logger;
    }

    public void SubmitError(String context, Exception cause) {
        Timestamp time = new Timestamp(new Date().getTime());
        this.handlerRequests.execute(() -> {
            StringWriter sw = new StringWriter();
            sw.append("======================\n")
                .append(time.toString())
                .append(" | ")
                .append(context)
                .append(" | \n");
            cause.printStackTrace(new PrintWriter(sw));
            this.writeMessage(sw.toString());
        });
    }

    synchronized
    private void writeMessage(String message) {
        try {
            this.writter.write(message);
            this.writter.newLine();
            this.writter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
