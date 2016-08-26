/**
 * GFW.Press
 * Copyright (C) 2016  chinashiyu ( chinashiyu@gfw.press ; http://gfw.press )
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
package press.gfw.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;

import press.gfw.decrypt.DecryptForwardThread;
import press.gfw.decrypt.EncryptForwardThread;

/**
 *
 * GFW.Press服务器线程
 *
 * @author chinashiyu ( chinashiyu@gfw.press ; http://gfw.press )
 *
 */
public class ServerThread extends PointThread {

    private static Logger logger = Logger.getLogger(ServerThread.class);

    private String proxyHost = null;

    private int proxyPort = 0;

    private Socket clientSocket = null;

    private Socket proxySocket = null;

    private SecretKey key = null;

    private boolean forwarding = false;

    public ServerThread(Socket clientSocket, String proxyHost, int proxyPort, SecretKey key) {

        this.clientSocket = clientSocket;

        this.proxyHost = proxyHost;

        this.proxyPort = proxyPort;

        this.key = key;

    }


    /**
     * 关闭所有连接，此线程及转发子线程调用
     */
    public synchronized void over() {

        try {

            proxySocket.close();

        } catch (Exception e) {
            logger.error("ProxySocket 关闭失败: ", e);
        }

        try {

            clientSocket.close();

        } catch (Exception e) {
            logger.error("ClientSocket 关闭失败: ", e);
        }

        if (forwarding) {

            forwarding = false;

        }

    }

    /**
     * 启动服务器与客户端之间的转发线程，并对数据进行加密及解密
     */
    public void run() {

        InputStream clientIn = null;

        OutputStream clientOut = null;

        InputStream proxyIn = null;

        OutputStream proxyOut = null;

        try {

            // 连接代理服务器
            logger.debug("创建 ProxySocket...");
            proxySocket = new Socket(proxyHost, proxyPort);

            // 设置3分钟超时
            logger.debug("设置 ProxySocket ClientSocket 超时3分钟...");
            proxySocket.setSoTimeout(180000);
            clientSocket.setSoTimeout(180000);

            // 打开 keep-alive
            logger.debug("开启 KeepAlive...");
            proxySocket.setKeepAlive(true);
            clientSocket.setKeepAlive(true);

            // 获取输入输出流
            clientIn = clientSocket.getInputStream();
            clientOut = clientSocket.getOutputStream();

            proxyIn = proxySocket.getInputStream();
            proxyOut = proxySocket.getOutputStream();

        } catch (IOException ex) {

            logger.error("连接代理服务器出错：" + proxyHost + ":" + proxyPort, ex);

            over();

            return;

        }

        // 开始转发
        forwarding = true;

        logger.debug("解密转发线程创建...");
        DecryptForwardThread forwardProxy = new DecryptForwardThread(this, clientIn, proxyOut, key);
        logger.debug("解密转发 " + forwardProxy.getName() + " 线程启动...");
        forwardProxy.start();

        logger.debug("加密转发线程创建...");
        EncryptForwardThread forwardClient = new EncryptForwardThread(this, proxyIn, clientOut, key);
        logger.debug("加密转发 " + forwardClient.getName() + " 线程启动...");
        forwardClient.start();

    }

}
