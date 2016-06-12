/*
 * Created by JFormDesigner on Sun Jun 12 20:36:32 CST 2016
 */

package press.gfw.client;

import javax.swing.*;
import java.awt.*;

/**
 * @author mritd
 */
public class GfwFrame extends JFrame {
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar;
    private JMenu setting;
    private JMenuItem loadConfig;
    private JMenuItem exportConfig;
    private JMenu about;
    private JPanel gfwPanel;

    public GfwFrame() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar = new JMenuBar();
        setting = new JMenu();
        loadConfig = new JMenuItem();
        exportConfig = new JMenuItem();
        about = new JMenu();
        gfwPanel = new JPanel();

        //======== this ========
        Container contentPane = getContentPane();

        //======== menuBar ========
        {

            //======== setting ========
            {
                setting.setText("\u8bbe\u7f6e");

                //---- loadConfig ----
                loadConfig.setText("\u52a0\u8f7d\u914d\u7f6e\u6587\u4ef6");
                setting.add(loadConfig);

                //---- exportConfig ----
                exportConfig.setText("\u5bfc\u51fa\u914d\u7f6e\u6587\u4ef6");
                setting.add(exportConfig);
            }
            menuBar.add(setting);

            //======== about ========
            {
                about.setText("\u5173\u4e8e");
            }
            menuBar.add(about);
        }
        setJMenuBar(menuBar);

        //======== gfwPanel ========
        {

            GroupLayout gfwPanelLayout = new GroupLayout(gfwPanel);
            gfwPanel.setLayout(gfwPanelLayout);
            gfwPanelLayout.setHorizontalGroup(
                    gfwPanelLayout.createParallelGroup()
                            .addGap(0, 668, Short.MAX_VALUE)
            );
            gfwPanelLayout.setVerticalGroup(
                    gfwPanelLayout.createParallelGroup()
                            .addGap(0, 459, Short.MAX_VALUE)
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addComponent(gfwPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addComponent(gfwPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
