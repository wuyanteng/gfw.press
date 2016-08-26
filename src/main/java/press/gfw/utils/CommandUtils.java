package press.gfw.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Copyright © Mritd. All rights reserved.
 *
 * @ClassName: CommandUtils
 * @Description: TODO 命令行执行工具
 * @author: mritd
 * @date: 2016年5月28日 下午11:13:55
 */
public class CommandUtils {

    private static Logger logger = Logger.getLogger(CommandUtils.class);

    /**
     * 执行命令
     *
     * @param commandLine
     * @return String 执行结果
     * @throws Exception
     */
    public static String execute(String commandLine) throws Exception {

        logger.info("执行命令: " + commandLine);

        String cmd = null;
        Properties props = System.getProperties();
        String osName = props.getProperty("os.name").toLowerCase();
        logger.debug("当前操作系统: " + osName);
        String charset = null;
        String result = "";

        if (osName.startsWith("windows")) {

            cmd = "cmd.exe /C " + commandLine;
            charset = "GBK";
        } else if (osName.startsWith("linux") || osName.startsWith("mac os x")) {
            cmd = "bash " + commandLine;
            charset = "UTF-8";
        }

        logger.debug("最终执行命令: " + cmd);

        Process ps = Runtime.getRuntime().exec(cmd);
        String line = null;

        BufferedReader logInput = new BufferedReader(new InputStreamReader(ps.getInputStream(), charset));
        BufferedReader errorInput = new BufferedReader(new InputStreamReader(ps.getErrorStream(), charset));
        while ((line = logInput.readLine()) != null) {
            result += line + "\n";
        }

        while ((line = errorInput.readLine()) != null) {
            result += line + "\n";
        }

        logInput.close();
        errorInput.close();
        ps.destroy();
        return result;
    }


}
