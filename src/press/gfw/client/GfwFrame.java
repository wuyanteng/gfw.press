package press.gfw.client;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

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
	
	/**
	 * 
	 * @Title: main
	 * @Description: TODO 主方法
	 * @param args
	 * @return: void
	 */
	public static void main(String[] args) {
		
		try {
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			UIManager.put("RootPane.setupButtonVisible", false);
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
		} catch (Exception e) {
			Logger.getLogger(Windows.class).error("BeautyEye初始化失败,异常信息: ", e);
		}
		
		
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
		setTitle("Gfw.Press");
		
		JPanel bashPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(bashPanel, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(bashPanel, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("设置");
		menu.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		menuBar.add(menu);
		
		JMenuItem menuItem_1 = new JMenuItem("导出配置");
		menu.add(menuItem_1);
		
		JMenuItem menuItem_2 = new JMenuItem("加载配置");
		menu.add(menuItem_2);
		
		JMenu menu_1 = new JMenu("关于");
		menuBar.add(menu_1);
		
		JMenuItem menuItem = new JMenuItem("项目主页");
		menu_1.add(menuItem);
	}
}
