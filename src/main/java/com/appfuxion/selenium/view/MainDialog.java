package com.appfuxion.selenium.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.appfuxion.selenium.controller.CloseListener;
import com.appfuxion.selenium.model.ParamsObject;

public class MainDialog {
	private JDialog mainDialog;
	private JPanel mainPanel;
	private JLabel progressLabel;
	private JProgressBar progressBar;
	private JButton actionButton;
	private JLabel passedLabel;
	private JLabel failedLabel;
	private JLabel runLabel;
	private JLabel totalLabel;

	public JLabel getRunLabel() {
		return runLabel;
	}

	public void setRunLabel(JLabel runLabel) {
		this.runLabel = runLabel;
	}

	public JLabel getTotalLabel() {
		return totalLabel;
	}

	public void setTotalLabel(JLabel totalLabel) {
		this.totalLabel = totalLabel;
	}

	public JDialog getMainDialog() {
		return mainDialog;
	}

	public void setMainDialog(JDialog mainDialog) {
		this.mainDialog = mainDialog;
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public JLabel getProgressLabel() {
		return progressLabel;
	}

	public void setProgressLabel(JLabel progressLabel) {
		this.progressLabel = progressLabel;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public JButton getActionButton() {
		return actionButton;
	}

	public void setActionButton(JButton actionButton) {
		this.actionButton = actionButton;
	}

	public JLabel getPassedLabel() {
		return passedLabel;
	}

	public void setPassedLabel(JLabel passedLabel) {
		this.passedLabel = passedLabel;
	}

	public JLabel getFailedLabel() {
		return failedLabel;
	}

	public void setFailedLabel(JLabel failedLabel) {
		this.failedLabel = failedLabel;
	}

	public void removeCloseButton(Component comp) {
		if (comp instanceof JMenu) {
			Component[] children = ((JMenu) comp).getMenuComponents();
			for (int i = 0; i < children.length; ++i)
				removeCloseButton(children[i]);
		} else if (comp instanceof AbstractButton) {
			Action action = ((AbstractButton) comp).getAction();
			String cmd = (action == null) ? "" : action.toString();
			if (cmd.contains(ParamsObject.PARAM_CLOSE_ACTION)) {
				comp.getParent().remove(comp);
			}
		} else if (comp instanceof Container) {
			Component[] children = ((Container) comp).getComponents();
			for (int i = 0; i < children.length; ++i)
				removeCloseButton(children[i]);
		}
	}

	public MainDialog() {
		JDialog.setDefaultLookAndFeelDecorated(true);
		mainDialog = new JDialog(new JFrame(), ParamsObject.PARAM_GUI_TITLE, true);
		mainDialog.setModal(true);
		removeCloseButton(mainDialog);		

		mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());

		progressLabel = new JLabel(ParamsObject.PARAM_GUI_PROGRESS_LABEL);
		progressLabel.setHorizontalAlignment(JLabel.LEFT);		
		progressBar = new JProgressBar(0, 100);
		actionButton = new JButton(ParamsObject.PARAM_GUI_ACTION_LABEL);

		actionButton.addActionListener(new CloseListener());

		passedLabel = new JLabel(ParamsObject.PARAM_GUI_PASSED_LABEL);
		passedLabel.setHorizontalAlignment(JLabel.LEFT);		
		passedLabel.setOpaque(true);
		passedLabel.setForeground(Color.WHITE);
		passedLabel.setBackground(new Color(113, 178, 113));
		
		failedLabel = new JLabel(ParamsObject.PARAM_GUI_FAILED_LABEL);
		failedLabel.setOpaque(true);
		failedLabel.setHorizontalAlignment(JLabel.LEFT);		
		failedLabel.setForeground(Color.WHITE);
		failedLabel.setBackground(Color.RED);
		
		runLabel = new JLabel(ParamsObject.PARAM_GUI_RUN_LABEL);
		runLabel.setHorizontalAlignment(JLabel.LEFT);		
		runLabel.setOpaque(true);
		runLabel.setForeground(Color.WHITE);
		runLabel.setBackground(new Color(204,204,0));
		
		totalLabel = new JLabel(ParamsObject.PARAM_GUI_TOTAL_LABEL);
		totalLabel.setOpaque(true);
		totalLabel.setHorizontalAlignment(JLabel.LEFT);		
		totalLabel.setForeground(Color.WHITE);
		totalLabel.setBackground(Color.BLUE);
		

		mainPanel.add(progressLabel);
		mainPanel.add(progressBar);
		mainPanel.add(actionButton);
		mainPanel.add(passedLabel);
		mainPanel.add(failedLabel);
		mainPanel.add(runLabel);
		mainPanel.add(totalLabel);

		mainDialog.add(mainPanel);
		mainDialog.setSize(185, 155);
		removeCloseButton(mainDialog);		

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		int x = (int) rect.getMaxX() - mainDialog.getWidth();
		int y = 0;
		mainDialog.setLocation(x, y);

		Thread t = new Thread(new Runnable() {

			public void run() {

				mainDialog.setVisible(true);
			}
		});
		t.start();

	}

}
