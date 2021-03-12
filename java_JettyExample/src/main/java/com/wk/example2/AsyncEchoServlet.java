package com.wk.example2;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: ekiawna
 * @Date: 2021/3/12 17:11
 * @Description
 */
public class AsyncEchoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext asyncContext = req.startAsync(req, resp);
        asyncContext.setTimeout(0);
        Echoer echoer = new Echoer(asyncContext);

        req.getInputStream().setReadListener(echoer);
        resp.getOutputStream().setWriteListener(echoer);
    }

    private class Echoer implements ReadListener, WriteListener {

        private final byte[] buffer = new byte[4096];
        private final AsyncContext asyncContext;
        private final ServletInputStream inputStream;
        private final ServletOutputStream outputStream;
        private final AtomicBoolean complete  = new AtomicBoolean(false);

        public Echoer(AsyncContext asyncContext) throws IOException {
            this.asyncContext = asyncContext;
            this.inputStream = asyncContext.getRequest().getInputStream();
            this.outputStream = asyncContext.getResponse().getOutputStream();
        }

        @Override
        public void onDataAvailable() throws IOException {
            handleAsyncIO();
        }

        @Override
        public void onAllDataRead() throws IOException {
            handleAsyncIO();
        }

        @Override
        public void onWritePossible() throws IOException {
            handleAsyncIO();
        }

        @Override
        public void onError(Throwable throwable) {
            new Throwable("onError", throwable).printStackTrace();
            asyncContext.complete();
        }

        private void handleAsyncIO() throws IOException{
            // This method is called:
            //   1) after first registering a WriteListener (ready for first write)
            //   2) after first registering a ReadListener if write is ready
            //   3) when a previous write completes after an output.isReady() returns false
            //   4) from an input callback

            while (true){
                if (!outputStream.isReady()){
                    break;
                }
                if (!inputStream.isReady()){
                    break;
                }
                int read = inputStream.read(buffer);
                if (read < 0){
                    if (complete.compareAndSet(false,true)){
                        asyncContext.complete();
                        break;
                    }
                }else if (read > 0){
                    outputStream.write(buffer,0, read);
                }
            }
        }
    }
}
