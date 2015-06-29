import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SMSFilterServer {

	private JFrame mainFrame;
	private JLabel headerLabel;
	private JPanel contentPanel;
	private JButton openButton;
	private JButton uploadButton;
	private JTextArea textArea;
	private JFileChooser fileChooser;
	private File selectedFile;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SMSFilterServer smsFilterServer = new SMSFilterServer();
		smsFilterServer.prepareGUI();
	}

	private void prepareGUI() {

		mainFrame = new JFrame("SMS Filter server");
		mainFrame.setMinimumSize(new Dimension(600, 400));
		// mainFrame.setResizable(false);
		mainFrame.setLocationRelativeTo(null);

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		mainFrame.setContentPane(contentPanel);

		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BorderLayout());
		contentPanel.add(headerPanel, BorderLayout.NORTH);

		//
		headerLabel = new JLabel("", JLabel.CENTER);
		headerLabel.setText("Open the sms keyword file upload to server:");
		Border paddingBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		headerLabel.setBorder(BorderFactory.createCompoundBorder(null, paddingBorder));
		// headerLabel.setma
		headerPanel.add(headerLabel, BorderLayout.NORTH);

		//
		fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.csv", "csv");
		fileChooser.setFileFilter(filter);

		//
		openButton = new JButton("Open file");
		openButton.setPreferredSize(new Dimension(100, 50));
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int returnVal = fileChooser.showOpenDialog(mainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					selectedFile = fileChooser.getSelectedFile();
					// This is where a real application would open the file.

					textArea.setText(readFile(selectedFile.getAbsolutePath()));
					
					uploadButton.setEnabled(true);
					System.out.append("Opening: " + selectedFile.getName());
				} else {
					System.out.append("Open command cancelled by user.");
				}
			}
		});
		headerPanel.add(openButton, BorderLayout.CENTER);
		headerPanel.setBorder(BorderFactory.createCompoundBorder(null, paddingBorder));

		//
		textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		contentPanel.add(scroll, BorderLayout.CENTER);

		//
		uploadButton = new JButton("Upload");
		uploadButton.setEnabled(false);
		uploadButton.setPreferredSize(new Dimension(100, 50));
		uploadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (selectedFile != null) {
					uploadButton.setEnabled(false);
					DeleteFileTask deleteFileTask = new DeleteFileTask();
					deleteFileTask.execute();	
				}else{
					JOptionPane.showMessageDialog(mainFrame, "Please select sms data file first!","Error",JOptionPane.OK_OPTION );
				}
			}
		});
		contentPanel.add(uploadButton, BorderLayout.SOUTH);
		contentPanel.setBorder(BorderFactory.createCompoundBorder(null, paddingBorder));

		//
		mainFrame.setVisible(true);
	}

	static String readFile(String path) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	class PostFileTask extends SwingWorker<String, String> {

		@Override
		public String doInBackground() {
			String bodyContent = readFile(selectedFile.getAbsolutePath());
			String response = ApiHelper.postFile(bodyContent);

			return response;
		}

		@Override
		protected void done() {
			uploadButton.setEnabled(true);
		}
	}

	class DeleteFileTask extends SwingWorker<String, String> {

		@Override
		public String doInBackground() {
			String response = ApiHelper.deleteFile();
			return response;
		}

		@Override
		protected void done() {
			PostFileTask postFileTask = new PostFileTask();
			postFileTask.execute();
		}
	}
}
