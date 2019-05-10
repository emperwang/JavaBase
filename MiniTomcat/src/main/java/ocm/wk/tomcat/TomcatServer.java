package ocm.wk.tomcat;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.StringTokenizer;

public class TomcatServer {
    private final static Integer port = 8080;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            ServletHandler servletHandler = new ServletHandler(serverSocket);
            servletHandler.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ServletHandler extends Thread {
        private ServerSocket server = null;

        public ServletHandler(ServerSocket socket) {
            this.server = socket;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Socket client = null;
                    client = server.accept();
                    if (client != null){
                        try{
                            System.out.println("接收到一个客户请求");
                            //根据客户端socket对象获取输入流对象
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            String s = bufferedReader.readLine();  //GET /123.jpg HTTP/1.1
                            System.out.println("line :"+s);
                            //拆分http请求路径，取http需要请求的资源完整路径
                            String resource = s.substring(s.indexOf("/"),s.lastIndexOf("/")-5);  //减5是为了去除 HTTP
                            System.out.println("resource is :"+resource);
                            resource = URLDecoder.decode(resource, "UTF-8");

                            //获取到这次请求的方法类型,比如 get post
                            String method = new StringTokenizer(s).nextElement().toString();
                            System.out.println("rquest method is:"+method);
                            //继续循环读取浏览器发出的一行一行数据, 也就是读取http header
                            while ((s = bufferedReader.readLine())!= null){
                                if (s.equals("")){ //当s为空的时候表示header结束
                                    break;
                                }
                                System.out.println("the http header is :"+s);
                            }
                            if ("post".equals(method.toLowerCase())){
                                System.out.println("the post request body is :"+bufferedReader.readLine());
                            }else if ("get".equals(method.toLowerCase())){
                                //根据http请求的资源后缀来确定返回数据
                                // 比如下载一个图片
                                if (resource.endsWith(".jpg")){
                                    transferFileHandle("E:\\123.jpg",client);
                                    closeSocket(client);
                                }else{ //直接返回一个网页
                                    PrintWriter writer = new PrintWriter(client.getOutputStream());
                                    writer.println("HTTP/1.0 200 OK");
                                    writer.println("Content-type:Text/html;charset=utf-8");
                                    writer.println();
                                    writer.println("<html><body>");
                                    writer.println("<h1>百度<h1>");
                                    writer.println("</body></html>");
                                    writer.println();
                                    writer.close();
                                    closeSocket(client);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         *  读取一个图片  发送到客户端
         * @param path
         * @param client
         */
        private void transferFileHandle(String path, Socket client) {
            //这里没有做健康检查
            //读取一个文件
            File fileToSend = new File(path);
            if (fileToSend.exists() && !fileToSend.isDirectory()){
                try{
                    PrintWriter writer = new PrintWriter(client.getOutputStream());
                    writer.println("HTTP/1.0 200 OK");
                    writer.println("Content-Type:application/binary");
                    writer.println("Content-Length:"+fileToSend.length());
                    writer.println();
                    //读取文件
                    FileInputStream stream = new FileInputStream(fileToSend);
                    byte[] buf = new byte[stream.available()];
                    stream.read(buf);
                    //发送到客户端
                    writer.write(String.valueOf(buf));
                    writer.close();
                    stream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        /**
         * 关闭socket
         * @param client
         */
        private void closeSocket(Socket client) {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}