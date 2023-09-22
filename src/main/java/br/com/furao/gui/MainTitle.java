package br.com.furao.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.IOUtils;

import com.ibm.jms.JMSBytesMessage;
import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.jms.MQDestination;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class MainTitle extends JFrame {

	private JFrame frame;
	private JTextField tfHost;
	private JTextField tfPort;
	private JTextField tfChannel;
	private JTextField tfQueueManager;
	private JTextField tfUser;
	private JPasswordField tfPassword;
	private JTextField tfQueue;
	private String msgIdSelected;
	
	private String versao = "v1.0.11";
	private String separatorSession = "mustelaputoriusfuro";

	private JmsFactoryFactory jmsFactory;
	private JmsConnectionFactory jmsConnectionFactory;
	private Session session;
	private MQDestination targetQueue;
	private Connection connection;
	
	private JTable tableMessages;
	private JScrollPane scrollPane;
	private JButton btnPushMessage;
	private JButton btnCopyToNew;
	private JButton btnPurgeMessage;
	private JButton btnRefresh;
	private JButton btnSaveSession;
	private JButton btnCleanQueue;
	private JComboBox cmbLoadSession;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private JLabel lblFurao;
	private JLabel lblNewLabel_1;
	private JLabel lblVersao;
	private JPanel panel;
	private JButton btnConectar;
	
	private Image imageFurao;
	private Image imageLoading; 
	private JLabel lblMsgFila;
	private JLabel lblTotalMsg;
	private JLabel lblNewLabel_2;
	
	private List<String[]> sessions = new ArrayList<String[]>();
	private String pathDir = System.getProperty("user.home") + File.separator + ".o-furao-mq";
	private String pathFiles = System.getProperty("user.home") + File.separator + ".o-furao-mq" + File.separator  + "session.txt";
	File sessionDir = new File(pathDir);
	File sessionFile = new File(pathFiles);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainTitle window = new MainTitle();
					window.frame.setVisible(true);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public MainTitle() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		loadSession(true);
		frame = new JFrame("O Furao - Solução para administrar conteúdo de filas");
		frame.setBounds(100, 100, 1495, 871);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		
		panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBounds(187, 74, 1255, 160);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblHost = new JLabel("Host");
		lblHost.setBounds(24, 10, 45, 13);
		panel.add(lblHost);
		lblHost.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(228, 10, 45, 13);
		panel.add(lblPort);
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblQueueManager = new JLabel("Queue Manager");
		lblQueueManager.setBounds(571, 10, 164, 13);
		panel.add(lblQueueManager);
		lblQueueManager.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblChannel = new JLabel("Channel");
		lblChannel.setBounds(345, 10, 122, 13);
		panel.add(lblChannel);
		lblChannel.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblUser = new JLabel("User");
		lblUser.setBounds(830, 12, 122, 13);
		panel.add(lblUser);
		lblUser.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(1015, 12, 122, 13);
		panel.add(lblPassword);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblNewLabel = new JLabel("Queue");
		lblNewLabel.setBounds(290, 54, 76, 40);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

		tfHost = new JTextField();
		tfHost.setBounds(24, 33, 194, 26);
		panel.add(tfHost);
		tfHost.setFont(new Font("Tahoma", Font.PLAIN, 18));
		tfHost.setColumns(10);


		tfPort = new JTextField();
		tfPort.setBounds(228, 33, 103, 26);
		panel.add(tfPort);
		tfPort.setFont(new Font("Tahoma", Font.PLAIN, 18));
		tfPort.setColumns(10);


		tfChannel = new JTextField();
		tfChannel.setBounds(341, 33, 220, 26);
		panel.add(tfChannel);
		tfChannel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		tfChannel.setColumns(10);


		tfQueueManager = new JTextField();
		tfQueueManager.setBounds(571, 33, 250, 26);
		panel.add(tfQueueManager);
		tfQueueManager.setFont(new Font("Tahoma", Font.PLAIN, 18));
		tfQueueManager.setColumns(10);


		tfUser = new JTextField();
		tfUser.setBounds(830, 35, 173, 24);
		panel.add(tfUser);
		tfUser.setFont(new Font("Tahoma", Font.PLAIN, 18));
		tfUser.setColumns(10);


		tfPassword = new JPasswordField();
		tfPassword.setBounds(1015, 35, 220, 24);
		panel.add(tfPassword);
		tfPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));

		tfQueue = new JTextField();
		tfQueue.setHorizontalAlignment(SwingConstants.CENTER);
		tfQueue.setBounds(290, 93, 553, 26);
		panel.add(tfQueue);
		tfQueue.setFont(new Font("Tahoma", Font.PLAIN, 18));
		tfQueue.setColumns(10);

		panel.add(cmbLoadSession);
		
		btnConectar = new JButton("Connect!");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectQueue();
				try {
					listQueue();
				} catch (Exception e1) {
					e1.printStackTrace();
					JLabel label = new JLabel(e1.getCause().getMessage());
					label.setFont(new Font("Arial", Font.PLAIN, 18));
					
					JOptionPane.showMessageDialog(frame, label, "Algo deu errado.", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnConectar.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnConectar.setBounds(853, 81, 235, 51);
		panel.add(btnConectar);
		
		btnSaveSession = new JButton("Save Session");
		btnSaveSession.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSession();
				loadSession(false);
			}
		});
		btnSaveSession.setBackground(new Color(0, 255, 64));
		btnSaveSession.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSaveSession.setBounds(1098, 81, 137, 51);
		btnSaveSession.setVisible(false);
		panel.add(btnSaveSession);
		
		JLabel lblNewLabel_3 = new JLabel("Load Session");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_3.setBounds(24, 63, 249, 23);
		panel.add(lblNewLabel_3);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(56, 337, 1372, 487);
		scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL)); 
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		tableMessages = new JTable();

		tableMessages.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int row = tableMessages.rowAtPoint(evt.getPoint());
		        if (row >= 0) {
		        		
		        	msgIdSelected = tableMessages.getValueAt(row, 1).toString();
		        	btnPurgeMessage.setEnabled(true);
						
		        }
		    }
		    
		});
		scrollPane.setViewportView(tableMessages);
		

		btnPushMessage = new JButton("Push Message");
		btnPushMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showModalPushMessage();
			}
		});
		btnPushMessage.setBackground(new Color(0, 255, 64));
		btnPushMessage.setForeground(new Color(0, 0, 0));
		btnPushMessage.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnPushMessage.setBounds(197, 244, 204, 51);
		btnPushMessage.setVisible(false);
		frame.getContentPane().add(btnPushMessage);

		btnCopyToNew = new JButton("Copy To New");
		btnCopyToNew.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnCopyToNew.setBounds(1227, 244, 204, 51);
		btnCopyToNew.setVisible(false);
		frame.getContentPane().add(btnCopyToNew);

		btnPurgeMessage = new JButton("Purge Message");
		btnPurgeMessage.setBackground(new Color(255, 0, 128));
		btnPurgeMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
	        		
					if(!checkIsNullOrBlank(msgIdSelected)) {
						purgeMessage(msgIdSelected);
						listQueue();
						btnPurgeMessage.setEnabled(false);
					}
					
	        	} catch (Exception eq) {
					JLabel label = new JLabel(eq.getCause().getMessage());
					label.setFont(new Font("Arial", Font.PLAIN, 18));
					
					JOptionPane.showMessageDialog(frame, label, "Algo deu errado.", JOptionPane.ERROR_MESSAGE);
					
					System.out.println("ERROR: " + eq);
				}
			}
		});
		btnPurgeMessage.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnPurgeMessage.setBounds(693, 244, 204, 51);
		btnPurgeMessage.setVisible(false);
		frame.getContentPane().add(btnPurgeMessage);
		
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("furao.png");
        imageFurao = ImageIO.read(input);
        
        input = classLoader.getResourceAsStream("loading.gif");
        imageLoading = Toolkit.getDefaultToolkit().createImage(IOUtils.toByteArray(input));

        ClassLoader cldr = this.getClass().getClassLoader();
       // java.net.URL imageURL   = cldr.getResource(input);
        
		lblFurao = new JLabel(new ImageIcon(imageFurao.getScaledInstance(180, 240,  java.awt.Image.SCALE_SMOOTH)));
		lblFurao.setBounds(0, -24, 194, 319);
		frame.getContentPane().add(lblFurao);
		
		lblNewLabel_1 = new JLabel("O Furão");
		lblNewLabel_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
		lblNewLabel_1.setBounds(187, 23, 194, 24);
		frame.getContentPane().add(lblNewLabel_1);
		
		lblVersao = new JLabel(versao);
		lblVersao.setFont(new Font("Tahoma", Font.ITALIC, 14));
		lblVersao.setBounds(187, 41, 111, 24);
		lblVersao.setText(versao);
		frame.getContentPane().add(lblVersao);
		
		lblMsgFila = new JLabel("Não há mensagens na fila :(");
		lblMsgFila.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblMsgFila.setBounds(458, 428, 933, 118);
		lblMsgFila.setVisible(false);
		frame.getContentPane().add(lblMsgFila);
		
		lblTotalMsg = new JLabel("");
		lblTotalMsg.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTotalMsg.setBounds(1227, 313, 215, 24);
		frame.getContentPane().add(lblTotalMsg);
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					listQueue();
				} catch (Exception eq) {
					JLabel label = new JLabel(eq.getCause().getMessage());
					label.setFont(new Font("Arial", Font.PLAIN, 18));
					
					JOptionPane.showMessageDialog(frame, label, "Algo deu errado.", JOptionPane.ERROR_MESSAGE);
					
					System.out.println("ERROR: " + e);
				}
			}
		});
		btnRefresh.setBackground(new Color(0, 255, 255));
		btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnRefresh.setBounds(463, 244, 158, 51);
		btnRefresh.setVisible(false);

		frame.getContentPane().add(btnRefresh);
		
		JLabel lblGithub = new JLabel("Contribua com o projeto clicando aqui (GITHUB)");
		lblGithub.setFont(new Font("Segoe UI Symbol", Font.BOLD, 16));
		lblGithub.setBounds(1056, 22, 386, 30);
		lblGithub.setForeground(Color.BLUE.darker());
		lblGithub.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblGithub.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	try {
		            Desktop.getDesktop().browse(new URI("https://github.com/deleonsimoni/o-furao-mq"));
		        } catch (Exception e1) {
		            e1.printStackTrace();
		        }
		    }
		});
		frame.getContentPane().add(lblGithub);
		
		lblNewLabel_2 = new JLabel("LGPL V3 - Dê estrela no github");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(1056, 51, 312, 13);
		frame.getContentPane().add(lblNewLabel_2);
		
		btnCleanQueue = new JButton("Clean Queue");
		btnCleanQueue.setForeground(new Color(255, 255, 255));
		btnCleanQueue.setBackground(new Color(255, 0, 0));
		btnCleanQueue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int dialogResult = JOptionPane.showConfirmDialog (null, "Essa opção limpará a fila toda, tem certeza?", "Calma lá", JOptionPane.YES_NO_OPTION);
					if(dialogResult == JOptionPane.YES_OPTION){
						cleanQueue();
						listQueue();
					}
				} catch (Exception e1) {
					JLabel label = new JLabel(e1.getCause().getMessage());
					label.setFont(new Font("Arial", Font.PLAIN, 18));
					
					JOptionPane.showMessageDialog(frame, label, "Algo deu errado.", JOptionPane.ERROR_MESSAGE);
					
					System.out.println("ERROR: " + e1);
				}
			}
		});
		btnCleanQueue.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		btnCleanQueue.setBounds(959, 244, 204, 51);
		btnCleanQueue.setVisible(false);
		frame.getContentPane().add(btnCleanQueue);
		
		frame.setIconImage(imageFurao);
		frame.setName("Fura Fila " + versao);
		
	}

	private void connectQueue() {
		if (!checkIsNullOrBlank(tfHost.getText()) && !checkIsNullOrBlank(tfPort.getText()) && !checkIsNullOrBlank(tfChannel.getText())
				&& !checkIsNullOrBlank(tfQueueManager.getText()) && !checkIsNullOrBlank(tfUser.getText())
				&& !checkIsNullOrBlank(tfQueue.getText())) {

			lblMsgFila.setVisible(false);

			try {
				Thread.sleep(1000);
				// Create a connection factory
				jmsFactory = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
				jmsConnectionFactory = jmsFactory.createConnectionFactory();

				// Set the properties
				jmsConnectionFactory.setStringProperty(WMQConstants.WMQ_HOST_NAME, tfHost.getText());
				jmsConnectionFactory.setIntProperty(WMQConstants.WMQ_PORT, Integer.valueOf(tfPort.getText()));
				jmsConnectionFactory.setStringProperty(WMQConstants.WMQ_CHANNEL, tfChannel.getText());
				jmsConnectionFactory.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
				jmsConnectionFactory.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, tfQueueManager.getText());
				// cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
				// cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
				jmsConnectionFactory.setStringProperty(WMQConstants.USERID, tfUser.getText());
				if(!checkIsNullOrBlank(tfPassword.getText())) {
					jmsConnectionFactory.setStringProperty(WMQConstants.PASSWORD, tfPassword.getText());					
				}
				// cf.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SUITE, "*TLS12");

				// Create JMS objects
				connection = jmsConnectionFactory.createConnection();
				connection.start();
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

				targetQueue = (MQDestination) session.createQueue("queue:///" + tfQueue.getText());
				
				btnSaveSession.setVisible(true);
				System.out.println("Conexão sucedida");

			} catch (Exception e) {

				JLabel label = new JLabel(e.getCause().getMessage());
				label.setFont(new Font("Arial", Font.PLAIN, 18));
				
				JOptionPane.showMessageDialog(frame, label, "Algo deu errado.", JOptionPane.ERROR_MESSAGE);
				
				System.out.println("ERROR: " + e);
			}

		} else {
			JLabel label = new JLabel("Preencha todos os campos");
			label.setFont(new Font("Arial", Font.PLAIN, 18));
			
			JOptionPane.showMessageDialog(frame, label, "Acho que esqueceu de algum campo", JOptionPane.WARNING_MESSAGE);

		}

	}
	
	private void cleanQueue() throws JMSException {
		MessageConsumer consumer = session.createConsumer((Queue) targetQueue);
		Message msg = null;
		do {
			msg = consumer.receiveNoWait();
		} while(msg != null);
	}

	private void listQueue() throws JMSException, UnsupportedEncodingException {
		
		/*targetQueue .setReceiveCCSID(WMQConstants.CCSID_UTF8);
		targetQueue .setReceiveConversion(WMQConstants.WMQ_RECEIVE_CONVERSION_QMGR);
		targetQueue .setMessageBodyStyle(WMQConstants.WMQ_MESSAGE_BODY_MQ);*/
		
		QueueBrowser navigator = session.createBrowser((Queue) targetQueue);
		Enumeration enumeration = navigator.getEnumeration();

		Vector<Vector<Object>> lista = new Vector<Vector<Object>>();

		while (enumeration.hasMoreElements()) {
			try {
				Object element = enumeration.nextElement();
				
				if(element instanceof TextMessage) {
					lista.add(formatMessageText((TextMessage) element));
				} else if(element instanceof JMSBytesMessage) {
					lista.add(formatMessageBinary((JMSBytesMessage) element));
				} else if(element instanceof JMSTextMessage) {
					lista.add(formatMessageJMSText((JMSTextMessage) element));
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				JLabel label = new JLabel(e1.getCause().getMessage());
				label.setFont(new Font("Arial", Font.PLAIN, 18));
				JOptionPane.showMessageDialog(frame, label, "Algo deu errado.", JOptionPane.ERROR_MESSAGE);			
			}
		}

		if(lista.isEmpty()) {
			btnCleanQueue.setVisible(false);
			btnPushMessage.setVisible(true);
			btnRefresh.setVisible(true);
			btnPurgeMessage.setVisible(false);
			lblMsgFila.setVisible(true);
			tableMessages.setVisible(false);
			
		} else {
			tableMessages.setVisible(true);
			btnCleanQueue.setVisible(true);
			lblMsgFila.setVisible(false);

			DefaultTableModel tableModel = new DefaultTableModel(lista, getNameColumnsMessages());
				tableMessages.setModel(tableModel);
			tableMessages.setFont(new Font("Tahoma", Font.PLAIN, 16));
			tableModel.fireTableDataChanged();
			
			frame.getContentPane().add(scrollPane);
			
			lblTotalMsg.setText("Temos " + lista.size() + " itens na fila");
			
			enableButtons();
			
		}
	}
	
	private void purgeMessage(String msgIdSelected) throws JMSException {
		MessageConsumer consumer = session.createConsumer((Queue) targetQueue, "JMSMessageID='"+msgIdSelected+"'");
		consumer.receiveNoWait();
	}
	
	private void saveSession()  {
		
		String text = tfHost.getText() + separatorSession + tfPort.getText() + separatorSession + tfChannel.getText() + separatorSession + tfQueueManager.getText() + separatorSession + tfUser.getText() + separatorSession + tfPassword.getText() + separatorSession + tfQueue.getText();
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter
					   (sessionFile, true));
			writer.write(codificaBase64Encoder(text));
			writer.newLine();
			writer.close();
			JOptionPane.showMessageDialog(frame, "Sessão salva com sucesso!", "Isso aí!", JOptionPane.PLAIN_MESSAGE);

		} catch (IOException e) {
			JLabel label = new JLabel(e.getCause().getMessage());
			label.setFont(new Font("Arial", Font.PLAIN, 18));
			JOptionPane.showMessageDialog(frame, label, "Algo deu errado.", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void loadSession(boolean isCreateCombo) {
		 try {
			 if(!sessionDir.exists()) {
				 sessionDir.mkdir();
			 }
			 
			 if(!sessionFile.exists()) {
				 sessionFile.createNewFile();
			 }
			 
			 BufferedReader reader = new BufferedReader(new FileReader(sessionFile));
			 extractDataFromFile(reader, isCreateCombo);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}

	private void extractDataFromFile(BufferedReader reader, boolean isCreateCombo) throws IOException {
		
		String linha = reader.readLine();
		if(isCreateCombo) {
			cmbLoadSession = new JComboBox();
			cmbLoadSession.setFont(new Font("Tahoma", Font.PLAIN, 18));
			cmbLoadSession.setBounds(24, 93, 255, 27);
			cmbLoadSession.addActionListener (new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					itemComboSessionSelect(e);
				}
			});
			
		} else {
			sessions = new ArrayList<String[]>();
			cmbLoadSession.removeAllItems();
		}
		
		cmbLoadSession.addItem("Escolha a sessão");
		
		while(linha != null){
			String[] tokens = decodificaBase64Decoder(linha).split(separatorSession);
			sessions.add(tokens);
			cmbLoadSession.addItem(tokens[6]);
			linha = reader.readLine();
		}
	}

	
	public void itemComboSessionSelect(ActionEvent e)
    {
        // if the state combobox is changed
        if (e.getSource() == cmbLoadSession) {
 
        	if(cmbLoadSession.getSelectedItem() != null && !checkIsNullOrBlank(cmbLoadSession.getSelectedItem().toString())) {
        		for (String[] data : sessions) {
        			if(data[6].equalsIgnoreCase(cmbLoadSession.getSelectedItem().toString())) {
        				
        				tfHost.setText(data[0]);
        				tfPort.setText(data[1]);
        				tfChannel.setText(data[2]);
        				tfQueueManager.setText(data[3]);
        				tfUser.setText(data[4]);
        				tfPassword.setText(data[5]);
        				tfQueue.setText(data[6]);
        				
        			}
        		}
        	}
        }
    }
	
	public void showModalPushMessage() {
		new PushMessageTitle(session, (Queue) targetQueue);
	}
	
	public void enableButtons() {

		btnPushMessage.setVisible(true);
		btnCopyToNew.setVisible(true);
		btnPurgeMessage.setVisible(true);
		btnRefresh.setVisible(true);

		btnCopyToNew.setEnabled(false);
		btnPurgeMessage.setEnabled(false);
	}
	
	
	
	public Vector<Object> formatMessageBinary(JMSBytesMessage message) throws JMSException, UnsupportedEncodingException {
		
		Vector<Object> messageVector = new Vector<Object>();
		BytesMessage msgResponse = (BytesMessage) message;
		byte[] bytes = null;
		 
		if (msgResponse.getBodyLength() > 0) {
			
			messageVector.add(dateFormat.format(new java.util.Date(message.getJMSTimestamp())));
			messageVector.add(message.getJMSMessageID());
			
			bytes = new byte[(int) msgResponse.getBodyLength()];
			msgResponse.readBytes(bytes);
			messageVector.add(new String(bytes, "Cp1047"));
			
	      } 


		return messageVector;

	}
	
	public Vector<Object> formatMessageJMSText(JMSTextMessage message) throws JMSException, UnsupportedEncodingException {
		
		Vector<Object> messageVector = new Vector<Object>();
		BytesMessage msgResponse = (BytesMessage) message;
		byte[] bytes = null;
		 
		if (msgResponse.getBodyLength() > 0) {
			
			messageVector.add(dateFormat.format(new java.util.Date(message.getJMSTimestamp())));
			messageVector.add(message.getJMSMessageID());
			
			bytes = new byte[(int) msgResponse.getBodyLength()];
			msgResponse.readBytes(bytes);
			messageVector.add(new String(bytes, "Cp1047"));
			
	      } 


		return messageVector;

	}

	public Vector<Object> formatMessageText(TextMessage message) throws JMSException {

		Vector<Object> messageVector = new Vector<Object>();

		messageVector.add(dateFormat.format(new java.util.Date(message.getJMSTimestamp())));
		messageVector.add(message.getJMSMessageID());
		messageVector.add(message.getText());
		message.getText();

		return messageVector;

	}

	private Vector<String> getNameColumnsMessages() {
		Vector<String> nameColumns = new Vector<String>();
		nameColumns.add("Data");
		nameColumns.add("JmsID");
		nameColumns.add("Text");
		return nameColumns;
	}
	
	public boolean checkIsNullOrBlank(String text) {
		
		return text == null || text == "" || text.isEmpty();
	}
	
    /**
     * Codifica string na base 64 (Encoder)
     */
    public static String codificaBase64Encoder(String msg) {
        return Base64.getEncoder().encodeToString(msg.getBytes());
    }

    /**
     * Decodifica string na base 64 (Decoder)
     */
    public static String decodificaBase64Decoder(String msg) {
        return new String(Base64.getDecoder().decode(msg));
    }
}
