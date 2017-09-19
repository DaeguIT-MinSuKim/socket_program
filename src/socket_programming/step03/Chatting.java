package socket_programming.step03;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Chatting extends JFrame implements ActionListener, Runnable {

	private JPanel contentPane;
	private JTextField tFNickName;
	private JTextField tFTalk;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton btnConnect;
	private JTextArea tAView;
	
	private Socket soc;
	private OutputStream os;
	private BufferedReader br;
	private Thread currentTh;
	private DefaultListModel<String> model;
	private JLabel lblCnt;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chatting frame = new Chatting();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Chatting() {
		setTitle("채팅");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 489, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel pEast = new JPanel();
		contentPane.add(pEast, BorderLayout.EAST);
		pEast.setLayout(new BorderLayout(0, 0));
		
		JPanel pCnt = new JPanel();
		pEast.add(pCnt, BorderLayout.NORTH);
		
		JLabel lblCntTitle = new JLabel("인원 : ");
		pCnt.add(lblCntTitle);
		
		lblCnt = new JLabel("0");
		pCnt.add(lblCnt);
		
		JLabel lblCntUnit = new JLabel("명");
		pCnt.add(lblCntUnit);
		
		JPanel pList = new JPanel();
		pEast.add(pList, BorderLayout.CENTER);
		pList.setLayout(new BorderLayout(0, 0));
		
		model = new DefaultListModel<>();
		JList<String> list = new JList<>(model);
		list.setVisibleRowCount(10);
		pList.add(list, BorderLayout.NORTH);
		
		JPanel pRadio = new JPanel();
		pList.add(pRadio, BorderLayout.SOUTH);
		pRadio.setLayout(new GridLayout(0, 1, 0, 0));
		
		JRadioButton rdbtnHide = new JRadioButton("귓속말 설정");
		buttonGroup.add(rdbtnHide);
		pRadio.add(rdbtnHide);
		
		JRadioButton rdbtnShow = new JRadioButton("귓속말 해제");
		buttonGroup.add(rdbtnShow);
		pRadio.add(rdbtnShow);
		
		JButton btnClose = new JButton("끝내기");
		pEast.add(btnClose, BorderLayout.SOUTH);
		
		JPanel pMain = new JPanel();
		contentPane.add(pMain, BorderLayout.CENTER);
		pMain.setLayout(new BorderLayout(0, 0));
		
		JPanel pNorth = new JPanel();
		pMain.add(pNorth, BorderLayout.NORTH);
		pNorth.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNickName = new JLabel("대화명 : ");
		pNorth.add(lblNickName, BorderLayout.WEST);
		
		tFNickName = new JTextField();
		tFNickName.addActionListener(this);
		pNorth.add(tFNickName, BorderLayout.CENTER);
		tFNickName.setColumns(20);
		
		btnConnect = new JButton("접속");
		btnConnect.addActionListener(this);
		pNorth.add(btnConnect, BorderLayout.EAST);
		
		JPanel pView = new JPanel();
		pMain.add(pView, BorderLayout.CENTER);
		pView.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		pView.add(scrollPane);
		
		tAView = new JTextArea();
		tAView.setEditable(false);
		scrollPane.setViewportView(tAView);
		
		JPanel pSouth = new JPanel();
		pMain.add(pSouth, BorderLayout.SOUTH);
		pSouth.setLayout(new BorderLayout(0, 0));
		
		JLabel lblTalk = new JLabel("대  화 : ");
		pSouth.add(lblTalk, BorderLayout.WEST);
		
		tFTalk = new JTextField();
		pSouth.add(tFTalk, BorderLayout.CENTER);
		tFTalk.setColumns(20);
		
		JButton btnSend = new JButton("전송");
		pSouth.add(btnSend, BorderLayout.EAST);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnConnect || e.getSource() == tFNickName) {
			String str = tFNickName.getText().trim();
			if (str==null||str.length()==0){
				tFNickName.setText("");
				tFNickName.requestFocus();
				tAView.setText("대화명을 적으세요");
				return;
			}
			
			try {
				soc = new Socket("192.168.0.18", 1234);
				os = soc.getOutputStream();
				br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
				
				os.write((str+"\n").getBytes());//서버에게 닉네임을 전송
				currentTh = new Thread(this);
				currentTh.start();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				tAView.setText("서버와 연결이 되지 않았습니다.");
				return;
			}
		}
		
	}

	@Override
	public void run() {
		tFNickName.setEnabled(false);
		btnConnect.setEnabled(false);
		tFTalk.requestFocus();
		tAView.setText("*** 대화에 참여 하셨네요!! ***\n\n\n");
		String message = null;
		while(true){
			//서버와 통신
			try {
				message = br.readLine();
				if (message == null){
					continue;
				}
				
				if (message.charAt(0) == '/'){
					if (message.charAt(1) == 'p'){
						String imsi = message.substring(2).trim();
						model.addElement(imsi);
						int xx = Integer.parseInt(lblCnt.getText())+1;
						lblCnt.setText(xx+"");
					}
				}else{
					tAView.append(message + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
}
