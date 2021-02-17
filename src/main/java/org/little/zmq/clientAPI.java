package org.little.zmq;

import java.util.Formatter;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class clientAPI{

    private String     broker;
    private ZContext   ctx;
    private ZMQ.Socket client;
    private long       timeout = 2500;
    private int        retries = 3;
    private boolean    verbose;
    private Formatter  log     = new Formatter(System.out);


    public clientAPI(String broker, boolean verbose){
        this.broker = broker;
        this.verbose = verbose;
        ctx = new ZContext();
        reconnectToBroker();
    }

    public long getTimeout(){return timeout;}
    public int  getRetries() {return retries;}

    public void setTimeout(long timeout){this.timeout = timeout;}
    public void setRetries(int retries){this.retries = retries;}

    /**
     * Connect or reconnect to broker
     */
    void reconnectToBroker(){
        if (client != null){
            client.close();
        }
        client = ctx.createSocket(SocketType.REQ);
        client.connect(broker);
        if (verbose)log.format("I: connecting to broker at %s\n", broker);
    }

    /**
     * Send request to broker and get reply by hook or crook Takes ownership of
     * request message and destroys it when sent. Returns the reply message or
     * NULL if there was no reply.
     *
     * @param service
     * @param request
     * @return
     */
    public ZMsg send(String service, ZMsg request){

        request.push(new ZFrame(service));
        request.push(CMD.C_CLIENT.newFrame());
        if (verbose) {
            log.format("I: send request to '%s' service: \n", service);
            request.dump(log.out());
        }
        ZMsg reply = null;

        int retriesLeft = retries;

        while(retriesLeft > 0 && !Thread.currentThread().isInterrupted()) {

             request.duplicate().send(client);

            // Poll socket for a reply, with timeout
            ZMQ.Poller items = ctx.createPoller(1);
            items.register(client, ZMQ.Poller.POLLIN);
            if (items.poll(timeout) == -1)
                break; // Interrupted

            if (items.pollin(0)) {
                ZMsg msg = ZMsg.recvMsg(client);
                if (verbose) {
                    log.format("I: received reply: \n");
                    msg.dump(log.out());
                }
                // Don't try to handle errors, just assert noisily
                assert (msg.size() >= 3);

                ZFrame header = msg.pop();
                assert (CMD.C_CLIENT.equals(header.toString()));
                header.destroy();

                ZFrame replyService = msg.pop();
                assert (service.equals(replyService.toString()));
                replyService.destroy();

                reply = msg;
                break;
            }
            else {
                items.unregister(client);
                if (--retriesLeft == 0) {
                    log.format("W: permanent error, abandoning\n");
                    break;
                }
                log.format("W: no reply, reconnecting\n");
                reconnectToBroker();
            }
            items.close();
        }
        request.destroy();
        return reply;
    }

    public void destroy(){ ctx.destroy();}
}
