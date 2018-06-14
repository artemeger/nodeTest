import com.github.jtendermint.websocket.Websocket;
import com.github.jtendermint.websocket.WebsocketException;
import com.github.jtendermint.websocket.WebsocketStatus;
import com.github.jtendermint.websocket.jsonrpc.JSONRPC;
import com.github.jtendermint.websocket.jsonrpc.Method;
import com.github.jtendermint.websocket.jsonrpc.calls.EmptyParam;
import com.github.jtendermint.websocket.jsonrpc.calls.StringParam;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Communication implements WebsocketStatus {

    private Websocket wsClient;
    private URI path = null;

    public Communication(){
        try {
            path = new URI("ws://localhost:46661/websocket");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        wsClient = new Websocket(path, this);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(() -> reconnectWS(), 5, TimeUnit.SECONDS);
    }

    private void reconnectWS() {
        System.out.println("Trying to connect to Websocket...");
        try {
            System.out.println("wsClient.connect()");
            wsClient.connect();
            System.out.println("connected");
        } catch (WebsocketException e) {
            System.out.println("failed");
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        byte[] b = new byte[20000];
        new Random().nextBytes(b);
        JSONRPC rpc = new StringParam(Method.BROADCAST_TX_ASYNC, b);
        wsClient.sendMessage(rpc, e -> {
            // no interest
        });
    }

    public void sendStatusRequest() {
        JSONRPC rpc = new EmptyParam(Method.STATUS);
        rpc.id = UUID.randomUUID().toString();
        final String statusRequestId = rpc.id;
        wsClient.sendMessage(rpc, e -> {
            if (statusRequestId.equals(e.id)) {
                System.out.println("Status response " + e);
            }
        });
    }

    public void sendDumpConsensusRequest() {
        JSONRPC rpc = new EmptyParam(Method.DUMP_CONSENSUS_STATE);
        rpc.id = UUID.randomUUID().toString();
        final String statusRequestId = rpc.id;
        wsClient.sendMessage(rpc, e -> {
            if (statusRequestId.equals(e.id)) {
                System.out.println("DUMP_CONSENSUS response " + e);
            }
        });
    }

}
