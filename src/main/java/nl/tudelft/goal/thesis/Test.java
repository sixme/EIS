package nl.tudelft.goal.thesis;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;

public class Test {
	public static JFrame createFrame() {

		JFrame frame = new JFrame("Environment");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(true);
		final JTextArea textArea = new JTextArea(15, 100);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(Font.getFont(Font.SANS_SERIF));
		JScrollPane scroller = new JScrollPane(textArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel inputpanel = new JPanel();
		inputpanel.setLayout(new FlowLayout());
		final JTextField input = new JTextField(35);
		JButton button = new JButton("Enter");
		final JTextField textField = new JTextField(35);
		textField.setName("TextField");
		textField.setEditable(false);
		textField.setVisible(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textField.setText(input.getText());
				input.setText("");
			}
		});
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		panel.add(scroller);
		inputpanel.add(input);
		inputpanel.add(button);
		inputpanel.add(textField);
		panel.add(inputpanel);
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.getRootPane().setDefaultButton(button);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.setResizable(false);
		input.requestFocus();
		return frame;
	}

	public static void main(String[] args) {
		JFrame frame = createFrame();
		System.out.println("Created");
		JPanel a = (JPanel) ((JPanel) (((JPanel) ((JLayeredPane) frame.getRootPane().getComponents()[1]).getComponents()[0]).getComponents()[0])).getComponents()[1];
		for (Component c : a.getComponents()) {
			System.out.println(c.toString());
		}
		JTextField text = (JTextField) a.getComponents()[2];
		JViewport b = (JViewport) ((JScrollPane) ((JPanel) (((JPanel) ((JLayeredPane) frame.getRootPane().getComponents()[1]).getComponents()[0]).getComponents()[0])).getComponents()[0]).getComponents()[0];

		while (text != null && text.getText().equals(""))
			;
		System.out.println("The text is: " + text.getText());
		System.out.println(text.getText().equals(""));

	}
}
