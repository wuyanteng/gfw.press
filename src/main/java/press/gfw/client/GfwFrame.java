package press.gfw.client;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import press.gfw.utils.BareBonesBrowserLaunch;

/**
 * 
 * Copyright © Mritd. All rights reserved.
 *
 * @ClassName: GfwFrame
 * @Description: TODO Swing图形化界面
 * @author: mritd
 * @date: 2016年6月3日 下午3:15:07
 */
public class GfwFrame extends JFrame{

	private Logger logger = Logger.getLogger(GfwFrame.class);
	private static final long serialVersionUID = -269939047660955009L;
	private TrayIcon trayIcon;			//托盘图标
	private SystemTray systemTray;		//获得系统托盘的实例 	
	private PopupMenu pop;
	private final MenuItem exit;
	private final MenuItem show;
	private JTextField serverPort;
	private JTextField listenPort;
	private JPasswordField serverPasswd;
	private JTextField serverAddress;
	
	private Client client = null;			// 客户端实例
	
	/**
	 * 
	 * @Title: main
	 * @Description: TODO 主方法
	 * @param args
	 * @return: void
	 */
	public static void main(String[] args) {
		
		try {
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
			UIManager.put("RootPane.setupButtonVisible", false);
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
		} catch (Exception e) {
			Logger.getLogger(Windows.class).error("BeautyEye初始化失败,异常信息: ", e);
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GfwFrame frame = new GfwFrame();
					Dimension dimemsion = Toolkit.getDefaultToolkit().getScreenSize();
					frame.setSize(700, 500);			// 设置窗口大小
					frame.setLocation((int) (dimemsion.getWidth() - frame.getWidth()) / 2, (int) (dimemsion.getHeight() - frame.getHeight()) / 2);  // 窗口正中央显示
//					frame.setLocationRelativeTo(null);	// 窗口正中央显示
					frame.init(); 	 					// 其他信息初始化
					frame.setVisible(true);				// 显示窗口ß
				} catch (Exception e) {
					Logger.getLogger(GfwFrame.class).error("XXXXXX 异常信息: ", e);
				}
			}
		});
		Logger.getLogger(GfwFrame.class).info("++++++ 程序启动!");
		
	}
	
	
	/**
	 * 
	 * @Title: init
	 * @Description: TODO 初始化方法
	 * @return: void
	 */
	private void init(){
		
		
	}
	
	/**
	 * 
	 * @Title: loadConfig
	 * @Description: TODO 加载配置文件
	 * @return: Map<String,String>
	 */
	public Map<String,String> loadConfig(){
		
		logger.debug("###### 加载配置文件...");
		
		Map<String,String> config = new HashMap<String,String>();
		
		return config;
	}
	
	public boolean saveConfig(Map<String,String> config){
		
		logger.debug("###### 保存配置文件...");
		
		return false;
	}
	
	
	public GfwFrame() {
		
		//窗口最小化设置
		setIconImage(Toolkit.getDefaultToolkit().getImage(GfwFrame.class.getResource("/press/gfw/images/logo.png")));
		pop = new PopupMenu();
		show = new MenuItem("打开程序");
		exit = new MenuItem("退出程序");
		pop.add(show);
		pop.add(exit);
		try {
			systemTray = SystemTray.getSystemTray();
			trayIcon = new TrayIcon(ImageIO.read(GfwFrame.class.getResourceAsStream("/press/gfw/images/logo.png")),"邮件定时发送工具",pop);
			trayIcon.setImageAutoSize(true);  //必须设置为true 让图盘图标自适应大小
			systemTray.add(trayIcon);
		} catch (IOException e) {
			logger.info("###### 初始化托盘图标I/O失败!");
			logger.error("异常信息: ", e);
		} catch (AWTException e) {
			logger.info("###### 设置托盘图标失败!");
			logger.error("XXXXXX 异常信息: ", e);
		}
		
		//监听关闭事件，点击关闭后隐藏进系统托盘
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				trayIcon.displayMessage("提示", "程序已经最小化到托盘!",TrayIcon.MessageType.INFO);
			}
		});
		
		//双击后显示
		trayIcon.addMouseListener(
            new MouseAdapter() {
            	public void mouseClicked(MouseEvent e){
            		if (e.getClickCount()==2) {
            			setExtendedState(JFrame.NORMAL);
            			setVisible(true);
					}
                }
			});
		
		// 选项注册事件
		ActionListener al2 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 退出程序
				if (e.getSource() == exit) {
					logger.info("------ 程序退出!");
					System.exit(0);// 退出程序
				}
				// 打开程序
				if (e.getSource() == show) {
					setExtendedState(JFrame.NORMAL);// 设置状态为正常
					setVisible(true);
				}
			}
		};
		
		show.addActionListener(al2);
		exit.addActionListener(al2);
		
		
		setTitle("Gfw.Press");
		
		JPanel bashPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(bashPanel, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(bashPanel, GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
		);
		
		JLabel serverAddressLabel = new JLabel("服务器地址:");
		
		JLabel serverPortLabel = new JLabel("服务器端口:");
		
		JLabel serverPasswdLabel = new JLabel("服务器密码:");
		
		JLabel listenPortLable = new JLabel("本地监听端口:");
		
		serverAddress = new JTextField();
		serverAddress.setColumns(10);
		
		serverPort = new JTextField();
		serverPort.setColumns(10);
		
		listenPort = new JTextField();
		listenPort.setColumns(10);
		
		serverPasswd = new JPasswordField();
		
		JButton ok = new JButton("确定");
		ok.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				new SwingWorker<Boolean, String>(){

					@Override
					protected Boolean doInBackground() throws Exception {
						
						logger.info("###### 启动客户端代理服务...");
						
						String sAddress = serverAddress.getText();
						String sPort = serverPort.getText();
						String sPasswd = String.valueOf(serverPasswd.getPassword());
						String lPort = listenPort.getText();
						
						if (StringUtils.isBlank(sAddress)||
								StringUtils.isBlank(sPort)||
								StringUtils.isBlank(sPasswd)||
								StringUtils.isBlank(lPort)) {
							
							logger.error("XXXXXX 参数为空...");
							
							if (client!=null) {
								logger.warn("###### 客户端终止运行...");
								client.kill();
							}
							publish("paramError");
							return false;
						}
						
						logger.debug("###### 创建客户端实例...");
						client = new Client(sAddress, sPort, sPasswd, lPort);
						logger.debug("###### 客户端实例启动...");
						client.start();
						
						try {
							Socket testSocket= new Socket("127.0.0.1", Integer.parseInt(lPort)); 
							testSocket.close();
						} catch (Exception e) {
							publish("failed");
						}
						
						
						publish("success");
						return true;
					}

					@Override
					protected void process(List<String> chunks) {
						
						if (chunks!=null&&!chunks.equals("paramError")) {
							JOptionPane.showMessageDialog(bashPanel, "参数不正确，客户端启动失败!\n请查看日志!", "错误", JOptionPane.ERROR_MESSAGE);
						}else if (chunks!=null&&chunks.equals("failed")) {
							JOptionPane.showMessageDialog(bashPanel, "客户端启动失败!\n请查看日志!", "错误", JOptionPane.ERROR_MESSAGE);
							logger.info("@@@@@@ 客户端启动成功!");
						}else if (chunks!=null&&chunks.equals("success")) {
							JOptionPane.showMessageDialog(bashPanel, "客户端启动成功!", "提示", JOptionPane.INFORMATION_MESSAGE);
							logger.info("@@@@@@ 客户端启动成功!");
						}
						
					}

					@Override
					protected void done() {
						
						try {
							
							// 测试代理是否开启
							String lPort = StringUtils.isNotBlank(listenPort.getText())?listenPort.getText():"3128";
							Socket testSocket= new Socket("127.0.0.1", Integer.parseInt(lPort)); 
							testSocket.close();
							
							// 刷新UI 
							ok.setText("停止");
							ok.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
							
						} catch (Exception e) {
							
							// 刷新UI 
							ok.setText("启动");
							ok.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
						}
						
					}
					
				}.execute();
				
				
				
			}
		});
		
		JButton clean = new JButton("清空");
		clean.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		clean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				serverAddress.setText("");
				serverPort.setText("");
				serverPasswd.setText("");
				listenPort.setText("");
				
			}
		});
		GroupLayout gl_bashPanel = new GroupLayout(bashPanel);
		gl_bashPanel.setHorizontalGroup(
			gl_bashPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bashPanel.createSequentialGroup()
					.addGap(28)
					.addGroup(gl_bashPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_bashPanel.createParallelGroup(Alignment.LEADING, false)
							.addComponent(listenPortLable, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
							.addComponent(serverPasswdLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(serverPortLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(serverAddressLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(ok, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_bashPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_bashPanel.createParallelGroup(Alignment.LEADING, false)
							.addComponent(serverPasswd)
							.addComponent(listenPort)
							.addComponent(serverPort)
							.addComponent(serverAddress, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
						.addComponent(clean, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(355, Short.MAX_VALUE))
		);
		gl_bashPanel.setVerticalGroup(
			gl_bashPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bashPanel.createSequentialGroup()
					.addGap(51)
					.addGroup(gl_bashPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(serverAddressLabel)
						.addComponent(serverAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_bashPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(serverPortLabel)
						.addComponent(serverPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_bashPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(serverPasswdLabel)
						.addComponent(serverPasswd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_bashPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(listenPortLable)
						.addComponent(listenPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(48)
					.addGroup(gl_bashPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(ok)
						.addComponent(clean))
					.addContainerGap(178, Short.MAX_VALUE))
		);
		
		gl_bashPanel.setAutoCreateGaps(true);
		gl_bashPanel.setAutoCreateContainerGaps(true);
		bashPanel.setLayout(gl_bashPanel);
		getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu settingButton = new JMenu("设置");
		settingButton.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		menuBar.add(settingButton);
		
		JMenuItem saveConfigButton = new JMenuItem("导出配置");
		settingButton.add(saveConfigButton);
		
		JMenuItem loadConfigButton = new JMenuItem("加载配置");
		settingButton.add(loadConfigButton);
		
		JMenuItem exitButton = new JMenuItem("退出");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("-------- 程序退出!");
				System.exit(0);
			}
		});
		settingButton.add(exitButton);
		
		JMenu aboutButton = new JMenu("关于");
		menuBar.add(aboutButton);
		
		JMenuItem homePageButton = new JMenuItem("项目主页");
		homePageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BareBonesBrowserLaunch.openURL("https://github.com/mritd/gfw.press");
			}
		});
		aboutButton.add(homePageButton);
	}
}
