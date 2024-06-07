package br.com.furao.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PushMessageTitle  {

	private JFrame frame;
	private JTextArea textArea;
	private Queue queue;
	private Session session;
	private String pacs008Template = "";
	private String pacs008Pagamento = "";
	private String pacs002Template = "";
	private JTextField txtQtdMsg;
	private int countPix = 0;
	
	private JLabel lblQtdMsg;
	private JButton btnPacs002;
	private JButton btnPacs008;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PushMessageTitle window = new PushMessageTitle(null, null);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PushMessageTitle(Session session, Queue queue) {
		this.queue = queue;
		this.session = session;
		initialize();
		this.frame.setVisible(true);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1293, 763);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Message");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(516, 32, 289, 54);
		frame.getContentPane().add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(57, 107, 1044, 504);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setRows(20);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		
		JButton btnNewButton = new JButton("Push");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pushMessage();
			}
		});
		btnNewButton.setBackground(new Color(0, 255, 0));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton.setBounds(57, 637, 185, 54);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
			}
		});
		btnBack.setBackground(new Color(255, 128, 128));
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnBack.setBounds(916, 637, 185, 54);
		frame.getContentPane().add(btnBack);
		
		JButton btnNewButton_1 = new JButton("Format");
		btnNewButton_1.setBackground(new Color(255, 159, 255));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton_1.setBounds(1111, 124, 158, 47);
		frame.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_1_1 = new JButton("Scape");
		btnNewButton_1_1.setBackground(new Color(128, 255, 255));
		btnNewButton_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton_1_1.setBounds(1111, 181, 158, 47);
		frame.getContentPane().add(btnNewButton_1_1);
		
		btnPacs008 = new JButton("Gerar Pacs008");
		btnPacs008.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gerarPacs008();
			}

		});
		btnPacs008.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnPacs008.setBackground(new Color(255, 166, 123));
		btnPacs008.setBounds(1111, 468, 158, 47);
		btnPacs008.setVisible(false);

		frame.getContentPane().add(btnPacs008);
		
		btnPacs002 = new JButton("Gerar Pacs002");
		btnPacs002.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnPacs002.setBackground(new Color(255, 166, 123));
		btnPacs002.setBounds(1111, 538, 158, 47);
		btnPacs002.setVisible(false);

		frame.getContentPane().add(btnPacs002);
		
		lblQtdMsg = new JLabel("Qtd. Msg");
		lblQtdMsg.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblQtdMsg.setBounds(1111, 425, 92, 33);
		lblQtdMsg.setVisible(false);

		frame.getContentPane().add(lblQtdMsg);
		
		txtQtdMsg = new JTextField();
		txtQtdMsg.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtQtdMsg.setBounds(1185, 431, 84, 27);
		txtQtdMsg.setVisible(false);
		frame.getContentPane().add(txtQtdMsg);
		txtQtdMsg.setColumns(10);
		
		JButton btnPix = new JButton("PIX");
		btnPix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(countPix > 3) {
					enablePixMode();
				} else {
					countPix++;
				}
			}

			private void enablePixMode() {
				txtQtdMsg.setVisible(true);				
				lblQtdMsg.setVisible(true);				
				btnPacs002.setVisible(true);				
				btnPacs008.setVisible(true);				

			}
		});
		btnPix.setBackground(new Color(255, 166, 123));
		btnPix.setFont(new Font("Tahoma", Font.BOLD, 22));
		btnPix.setBounds(1111, 329, 158, 64);
		frame.getContentPane().add(btnPix);
		
		JButton btnLimpar = new JButton("Reset");
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpar();
			}
		});
		btnLimpar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnLimpar.setBackground(new Color(128, 255, 255));
		btnLimpar.setBounds(1111, 238, 158, 47);
		frame.getContentPane().add(btnLimpar);
	}
	
	private void pushMessage() {
		if(textArea.getText() != null) {
			
	        try {
	        	
				MessageProducer producer = session.createProducer(queue);
				TextMessage msg = session.createTextMessage(textArea.getText());
				producer.send(msg);
				frame.setVisible(false);

			} catch (JMSException e) {
				JLabel label = new JLabel(e.getCause().getMessage());
				label.setFont(new Font("Arial", Font.PLAIN, 18));
				
				JOptionPane.showMessageDialog(frame, label, "Algo deu errado.", JOptionPane.ERROR_MESSAGE);
				System.out.println("ERRO: " + e);
			}
			
			
		}
	}
	
	private void limpar() {
		textArea.setText("");
	}
	
	private void gerarPacs008() {
		if(txtQtdMsg.getText() != "" && txtQtdMsg.getText() != null && !txtQtdMsg.getText().isEmpty()) {
			
			int quantidade = Integer.parseInt(txtQtdMsg.getText());
			String codigoOriginal = generateCodigoOriginal();
			System.out.println(codigoOriginal);
			String endToEnd = "";
			String pagamentos = "";
			
			for (int i = 0; i < quantidade; i++) {
				endToEnd = generateEndToEnd();
				System.out.println(endToEnd);
				String pagamento = pacs008Pagamento;
				pagamento = pagamento.replace("#END_TO_END#", endToEnd);
				
				pagamentos = pagamentos.concat(pagamento);
			}
			
			pacs008Template = pacs008Template.replace("#PAGAMENTOS#", pagamentos).replaceAll("#CODIGO_ORIGINAL#", codigoOriginal);

			textArea.setText(pacs008Template);
			
		} else {
			JLabel label = new JLabel("Preencha o campo Quantidade de mensagens");
			label.setFont(new Font("Arial", Font.PLAIN, 18));
			
			JOptionPane.showMessageDialog(frame, label, "Atenção", JOptionPane.WARNING_MESSAGE);
		}
		
	}

	private String generateEndToEnd() {
		String ed = UUID.randomUUID().toString().replace("-", "");
		ed = "E123456789123153729456" + ed.substring(0, ed.length() - 22);
		return ed;
	}

	private String generateCodigoOriginal() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		uuid = "M0123456789" + uuid.substring(0, uuid.length() - 11);
		return uuid;
	}
}
