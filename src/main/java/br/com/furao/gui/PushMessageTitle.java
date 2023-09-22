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
	private String pacs008Template = "{\"xml\":\"\\u003c?xml version\\u003d\\\"1.0\\\" encoding\\u003d\\\"UTF-8\\\" standalone\\u003d\\\"no\\\"?\\u003e\\u003cEnvelope xmlns\\u003d\\\"https://www.bcb.gov.br/pi/pacs.008/1.9\\\"\\u003e\\u003cAppHdr\\u003e\\u003cFr\\u003e\\u003cFIId\\u003e\\u003cFinInstnId\\u003e\\u003cOthr\\u003e\\u003cId\\u003e00038166\\u003c/Id\\u003e\\u003c/Othr\\u003e\\u003c/FinInstnId\\u003e\\u003c/FIId\\u003e\\u003c/Fr\\u003e\\u003cTo\\u003e\\u003cFIId\\u003e\\u003cFinInstnId\\u003e\\u003cOthr\\u003e\\u003cId\\u003e00360305\\u003c/Id\\u003e\\u003c/Othr\\u003e\\u003c/FinInstnId\\u003e\\u003c/FIId\\u003e\\u003c/To\\u003e\\u003cBizMsgIdr\\u003e#CODIGO_ORIGINAL#\\u003c/BizMsgIdr\\u003e\\u003cMsgDefIdr\\u003epacs.008.spi.1.9\\u003c/MsgDefIdr\\u003e\\u003cCreDt\\u003e2023-08-23T15:11:07.588Z\\u003c/CreDt\\u003e\\u003cSgntr\\u003e\\u003cds:Signature  xmlns:ds\\u003d\\\"http://www.w3.org/2000/09/xmldsig#\\\"\\u003e\\u003cds:SignedInfo\\u003e\\u003cds:CanonicalizationMethod  Algorithm\\u003d\\\"http://www.w3.org/2001/10/xml-exc-c14n#\\\"/\\u003e\\u003cds:SignatureMethod  Algorithm\\u003d\\\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\\\"/\\u003e\\u003cds:Reference  URI\\u003d\\\"#key-info-id\\\"\\u003e\\u003cds:Transforms\\u003e\\u003cds:Transform Algorithm\\u003d\\\"http://www.w3.org/2001/10/xml-exc-c14n#\\\"/\\u003e\\u003c/ds:Transforms\\u003e\\u003cds:DigestMethod  Algorithm\\u003d\\\"http://www.w3.org/2001/04/xmlenc#sha256\\\"/\\u003e\\u003cds:DigestValue\\u003efP/HFS5THGMXKt0aQSFg/xnmHeFXDuvba/b0UULkj9U\\u003d\\u003c/ds:DigestValue\\u003e\\u003c/ds:Reference\\u003e\\u003cds:Reference  URI\\u003d\\\"\\\"\\u003e\\u003cds:Transforms\\u003e\\u003cds:Transform Algorithm\\u003d\\\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\\\"/\\u003e\\u003cds:Transform  Algorithm\\u003d\\\"http://www.w3.org/2001/10/xml-exc-c14n#\\\"/\\u003e\\u003c/ds:Transforms\\u003e\\u003cds:DigestMethod  Algorithm\\u003d\\\"http://www.w3.org/2001/04/xmlenc#sha256\\\"/\\u003e\\u003cds:DigestValue\\u003elafwtMVNjm+iZkaI8gmRBiAXNlOcCr+zsFJ2+34N8kk\\u003d\\u003c/ds:DigestValue\\u003e\\u003c/ds:Reference\\u003e\\u003cds:Reference\\u003e\\u003cds:Transforms\\u003e\\u003cds:Transform  Algorithm\\u003d\\\"http://www.w3.org/2001/10/xml-exc-c14n#\\\"/\\u003e\\u003c/ds:Transforms\\u003e\\u003cds:DigestMethod  Algorithm\\u003d\\\"http://www.w3.org/2001/04/xmlenc#sha256\\\"/\\u003e\\u003cds:DigestValue\\u003ehqq8PnW+UoL0Orpk1LW+55xiXVpvoP5hA5j0f5x/KSM\\u003d\\u003c/ds:DigestValue\\u003e\\u003c/ds:Reference\\u003e\\u003c/ds:SignedInfo\\u003e\\u003cds:SignatureValue\\u003eUvFLdCzwJ4Jp4IJNsWwTzakAVz+MgjWDVViPl7YL5VXCjcq/ouxSeQsg+pHQetdDJFnMFMHy8MA2\\u0026#13; \\r\\nLCWkqHQhtEbfKw/9HTe83RXjoUmmzsK0qkJZ+vTUrz+WHmBe+ajfZMCFSybg2qdTWHO8NonW1xIE\\u0026#13;\\r\\nwECMdkNwOfl3r4av1MYLFoXM+Tg3UEoCs2mBm4vzuuBACZUxVCTtTdQmtD76lGdCJDFSlpaWyr1Z\\u0026#13;\\r\\nAoNgQxkW+UEays6X9nFFHpIx+ZENaDtsb+kUyVACQq91m79Q2qBRHwVaAAPfOIluW7svUhyacdba\\u0026#13;\\r\\nv61lxknOcAcLFEtmwn0PK9OVF3t6Tw3vNO/AUA\\u003d\\u003d\\u003c/ds:SignatureValue\\u003e\\u003cds:KeyInfo Id\\u003d\\\"key-info-id\\\"\\u003e\\u003cds:X509Data\\u003e\\u003cds:X509IssuerSerial\\u003e\\u003cds:X509IssuerName\\u003eCN\\u003dAutoridade Certificadora do SERPRO Final SSL, OU\\u003dServico Federal de Processamento de Dados - SERPRO, OU\\u003dCSPB-1, O\\u003dICP-Brasil, C\\u003dBR\\u003c/ds:X509IssuerName\\u003e\\u003cds:X509SerialNumber\\u003e16220846674141377239913245580\\u003c/ds:X509SerialNumber\\u003e\\u003c/ds:X509IssuerSerial\\u003e\\u003c/ds:X509Data\\u003e\\u003c/ds:KeyInfo\\u003e\\u003c/ds:Signature\\u003e\\u003c/Sgntr\\u003e\\u003c/AppHdr\\u003e\\u003cDocument\\u003e\\u003cFIToFICstmrCdtTrf\\u003e\\u003cGrpHdr\\u003e\\u003cMsgId\\u003e#CODIGO_ORIGINAL#\\u003c/MsgId\\u003e\\u003cCreDtTm\\u003e2023-08-23T15:11:07.588Z\\u003c/CreDtTm\\u003e\\u003cNbOfTxs\\u003e1\\u003c/NbOfTxs\\u003e\\u003cSttlmInf\\u003e\\u003cSttlmMtd\\u003eCLRG\\u003c/SttlmMtd\\u003e\\u003c/SttlmInf\\u003e\\u003cPmtTpInf\\u003e\\u003cInstrPrty\\u003eHIGH\\u003c/InstrPrty\\u003e\\u003cSvcLvl\\u003e\\u003cPrtry\\u003ePAGAGD\\u003c/Prtry\\u003e\\u003c/SvcLvl\\u003e\\u003c/PmtTpInf\\u003e\\u003c/GrpHdr\\u003e#PAGAMENTOS#\\u003c/FIToFICstmrCdtTrf\\u003e\\u003c/Document\\u003e\\u003c/Envelope\\u003e\",\"piResourceId\":\"UEkBiiLzCURHVMruPQNIM4wv35eSkedr\",\"horarioPollingBacen\":\"Sep 1, 2023 4:10:55 PM\"}";
	private String pacs008Pagamento = "\\u003cCdtTrfTxInf\\u003e\\u003cPmtId\\u003e\\u003cEndToEndId\\u003e#END_TO_END#\\u003c/EndToEndId\\u003e\\u003c/PmtId\\u003e\\u003cIntrBkSttlmAmt Ccy\\u003d\\\"BRL\\\"\\u003e0.01\\u003c/IntrBkSttlmAmt\\u003e\\u003cAccptncDtTm\\u003e2023-08-23T15:11:06.599Z\\u003c/AccptncDtTm\\u003e\\u003cChrgBr\\u003eSLEV\\u003c/ChrgBr\\u003e\\u003cMndtRltdInf\\u003e\\u003cTp\\u003e\\u003cLclInstrm\\u003e\\u003cPrtry\\u003eMANU\\u003c/Prtry\\u003e\\u003c/LclInstrm\\u003e\\u003c/Tp\\u003e\\u003c/MndtRltdInf\\u003e\\u003cDbtr\\u003e\\u003cNm\\u003eTestes Comercio LTDA\\u003c/Nm\\u003e\\u003cId\\u003e\\u003cPrvtId\\u003e\\u003cOthr\\u003e\\u003cId\\u003e00019642000\\u003c/Id\\u003e\\u003c/Othr\\u003e\\u003c/PrvtId\\u003e\\u003c/Id\\u003e\\u003c/Dbtr\\u003e\\u003cDbtrAcct\\u003e\\u003cId\\u003e\\u003cOthr\\u003e\\u003cId\\u003e46666000066\\u003c/Id\\u003e\\u003cIssr\\u003e0001\\u003c/Issr\\u003e\\u003c/Othr\\u003e\\u003c/Id\\u003e\\u003cTp\\u003e\\u003cCd\\u003eCACC\\u003c/Cd\\u003e\\u003c/Tp\\u003e\\u003c/DbtrAcct\\u003e\\u003cDbtrAgt\\u003e\\u003cFinInstnId\\u003e\\u003cClrSysMmbId\\u003e\\u003cMmbId\\u003e01027058\\u003c/MmbId\\u003e\\u003c/ClrSysMmbId\\u003e\\u003c/FinInstnId\\u003e\\u003c/DbtrAgt\\u003e\\u003cCdtrAgt\\u003e\\u003cFinInstnId\\u003e\\u003cClrSysMmbId\\u003e\\u003cMmbId\\u003e00360305\\u003c/MmbId\\u003e\\u003c/ClrSysMmbId\\u003e\\u003c/FinInstnId\\u003e\\u003c/CdtrAgt\\u003e\\u003cCdtr\\u003e\\u003cId\\u003e\\u003cPrvtId\\u003e\\u003cOthr\\u003e\\u003cId\\u003e66650014500\\u003c/Id\\u003e\\u003c/Othr\\u003e\\u003c/PrvtId\\u003e\\u003c/Id\\u003e\\u003c/Cdtr\\u003e\\u003cCdtrAcct\\u003e\\u003cId\\u003e\\u003cOthr\\u003e\\u003cId\\u003e3678120000\\u003c/Id\\u003e\\u003cIssr\\u003e7488\\u003c/Issr\\u003e\\u003c/Othr\\u003e\\u003c/Id\\u003e\\u003cTp\\u003e\\u003cCd\\u003eCACC\\u003c/Cd\\u003e\\u003c/Tp\\u003e\\u003c/CdtrAcct\\u003e\\u003cPurp\\u003e\\u003cCd\\u003eIPAY\\u003c/Cd\\u003e\\u003c/Purp\\u003e\\u003c/CdtTrfTxInf\\u003e";
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
