package adaptor;


import handler.CoreHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

@Service
public class BitmexAdaptor implements ApplicationContextAware {

    private long pongTimeMillis = 0;

    private Log logger = LogFactory.getLog(this.getClass());

    public final String BITMEX_URL = "wss://testnet.bitmex.com/realtime";

    private WebSocketSession webSocketSession;

    private ApplicationContext context;

    public WebSocketSession getWebSocketSession() {
        if (webSocketSession == null) {
            init();
        }
        return webSocketSession;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }


    public void init() {
        try {
            pongTimeMillis = 0;
            WebSocketClient webSocketClient = new StandardWebSocketClient();
            webSocketSession = webSocketClient.doHandshake(new TextWebSocketHandler() {
                @Override
                public void handleTextMessage(WebSocketSession session, TextMessage message) {
                    if (message.getPayload().equals("pong")) {

                    } else {
                        try {
                            handleJsonMessage(message.getPayload());
                        } catch (JSONException err) {
                            logger.error("failed to convert string to JSONObject:" + message.getPayload());
                            logger.error(err.getMessage());
                        }
                    }
                }

                @Override
                public void afterConnectionEstablished(WebSocketSession session) {
                    logger.info("established connection - " + session);
                }
            }, new WebSocketHttpHeaders(), URI.create(BITMEX_URL /*+ "?subscribe=orderBookL2_25:XPTUSD"*/)).get();

            ScheduledExecutorService executorService = newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(() -> {
                try {
                    if (pongTimeMillis > 0 && System.currentTimeMillis() - pongTimeMillis > 60000) {
                        logger.error("Remote server doesn't answer. Reconnect...");
                        webSocketSession.close();
                        webSocketSession = null;
                        this.getWebSocketSession();
                        shutdownAndAwaitTermination(executorService);
                    } else {
                        System.out.println("ping sent");
                        TextMessage message = new TextMessage("ping");
                        webSocketSession.sendMessage(message);
                    }
                } catch (Exception e) {
                    shutdownAndAwaitTermination(executorService);
                    //                    logger.error("Exception while sending a message", e);
                }
            }, 1, 10, TimeUnit.SECONDS);

        } catch (Exception e) {
            logger.error("Error while accessing websockets", e);
        }
    }


    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    logger.error("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void handleJsonMessage(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        if (jsonObject.keySet().contains("table")) {
            String tableName = jsonObject.getString("table");
            if (context.containsBean(tableName + "_handler")) {
                CoreHandler handler = (CoreHandler) context.getBean(tableName + "_handler");
                handler.handleMessage(jsonObject);
            }
        }
    }


}




