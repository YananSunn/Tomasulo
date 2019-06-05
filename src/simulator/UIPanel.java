package simulator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;




public class UIPanel extends JFrame {
	JPanel mainPanel;
	
	JPanel left;
	JPanel right;
	JPanel ltop;
	JPanel lcenter;
	JPanel ldown;
	
	JLabel clockMes;
	JLabel instrMes;
	JLabel warning;
	JTextField clockTex;
	JTextField instrTex;
	
	JButton runSimu;
	JButton file;
	
	
	JLabel instr;
	JLabel[] instrForm;
	
	JLabel[] FU;
	JLabel[] F;
	JLabel[] FValue;
	
	JLabel[] Resver;
	JLabel[][] addRes;
	JLabel[][] mulRes;
	JLabel[][] loadRes;
	
	String openPath;
	int checkClock;
	int checkInstr;
	boolean illegal;
	
	
	Simulator sim;

	public static final int ADD5 = -1;
	public static final int ADD4 = -2;
	public static final int ADD3 = -3;
	public static final int ADD2 = -4;
	public static final int ADD1 = -5;
	public static final int ADD0 = -6;
	public static final int MUL2 = -7;
	public static final int MUL1 = -8;
	public static final int MUL0 = -9;
	public static final int LOAD2 = -10;
	public static final int LOAD1 = -11;
	public static final int LOAD0 = -12;
	
	public static final int LD = 0;
    public static final int JUMP = 1;
    public static final int ADD = 2;
    public static final int SUB = 3;
    public static final int MUL = 4;
    public static final int DIV = 5;
	
	UIPanel(){
		
		mainPanel = new JPanel();
		// main
		mainPanel.setLayout(new BorderLayout());
		Border border1 = mainPanel.getBorder();
		Border margin = new EmptyBorder(0, 10, 0, 5);
		mainPanel.setBorder(new CompoundBorder(border1, margin));
		
		
		left = new JPanel();
		right = new JPanel();
		right.setPreferredSize(new Dimension(600, 900));
		mainPanel.add(left, BorderLayout.CENTER);
		mainPanel.add(right, BorderLayout.EAST);
		
		
		
		// left
		left.setLayout(new BorderLayout());
		
		ltop = new JPanel();
		lcenter = new JPanel();
		ldown = new JPanel();
		ltop.setPreferredSize(new Dimension(600, 300));
		ldown.setPreferredSize(new Dimension(600, 450));
		
		left.add(ltop, BorderLayout.NORTH);
		left.add(lcenter, BorderLayout.CENTER);
		left.add(ldown, BorderLayout.SOUTH);
		
		clockMes = new JLabel("CLOCK:");
		instrMes = new JLabel("INSTRUCTION:");
		clockTex = new JTextField();
		instrTex = new JTextField();
		runSimu = new JButton("run");
		file = new JButton("open");
		
		ltop.setBorder(new EmptyBorder(30,30,30,30));
		ltop.setLayout(new GridLayout(4,1,0,5));
		JPanel tmp3 = new JPanel();
		tmp3.setLayout(new GridLayout(1,2,30,0));

		
		tmp3.add(file);
		tmp3.add(runSimu);
		ltop.add(tmp3);

		warning = new JLabel("Warning");
		ltop.add(warning);
		
		JPanel tmp = new JPanel();
		tmp.setLayout(new GridLayout(1,2,30,0));
		tmp.add(clockMes);
		tmp.add(clockTex);
		ltop.add(tmp);
		JPanel tmp2 = new JPanel();
		tmp2.setLayout(new GridLayout(1,2,30,0));
		tmp2.add(instrMes);
		tmp2.add(instrTex);
		ltop.add(tmp2);
		
		
		
		instr = new JLabel();
		instrForm = new JLabel[6];
		for(int i = 0; i < 6; i++) {
			instrForm[i] = new JLabel();
		}
		instrForm[0].setText("Issue");
		instrForm[1].setText("Exec Comp");
		instrForm[2].setText("Write Result");
		
		lcenter.setLayout(new GridLayout(1,2));
		lcenter.add(instr);
		
		JPanel form = new JPanel();
		form.setLayout(new GridLayout(2,3,5,5));
		for(int i = 0; i < 6; i++) {
			form.add(instrForm[i]);
		}
		lcenter.add(form);
		
		
		ldown.setLayout(new GridLayout(12,8,5,5));
		FU = new JLabel[32];
		F = new JLabel[32];
		FValue = new JLabel[32];
		for(int i = 0; i < 32; i++) {
			FU[i] = new JLabel("F" + i);
			F[i] = new JLabel(String.valueOf(i));
			FValue[i] = new JLabel(String.valueOf(i+32));
		}
		
		for(int i = 0; i < 4; i++) {
			for(int j = i*8; j < (i+1)*8; j++) {
				ldown.add(FU[j]);
			}
			for(int j = i*8; j < (i+1)*8; j++) {
				ldown.add(F[j]);
			}
			for(int j = i*8; j < (i+1)*8; j++) {
				ldown.add(FValue[j]);
			}
		}
		
		
		Resver = new JLabel[10];
		Resver[0] = new JLabel();
		Resver[1] = new JLabel("isBusy");
		Resver[2] = new JLabel("isRun");
		Resver[3] = new JLabel("Time");
		Resver[4] = new JLabel("Op");
		Resver[5] = new JLabel("Vj");
		Resver[6] = new JLabel("Vk");
		Resver[7] = new JLabel("Qj");
		Resver[8] = new JLabel("Qk");
		Resver[9] = new JLabel("addr");
		
		addRes = new JLabel[6][10];
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 10; j++) {
				addRes[i][j] = new JLabel();
			}
			addRes[i][0].setText("add" + String.valueOf(i+1));
		}
		
		mulRes = new JLabel[3][10];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 10; j++) {
				mulRes[i][j] = new JLabel();
			}
			mulRes[i][0].setText("mul" + String.valueOf(i+1));
		}
		
		loadRes = new JLabel[3][10];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 10; j++) {
				loadRes[i][j] = new JLabel();
			}
			loadRes[i][0].setText("load" + String.valueOf(i+1));
		}
		
//		loadRes[0][3].setBackground(Color.PINK);
//		loadRes[0][3].setOpaque(true);
		
		right.setLayout(new GridLayout(13,10,5,5));
		for(int i = 0; i < 10; i++) {
			right.add(Resver[i]);
		}
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 10; j++) {
				right.add(addRes[i][j]);
			}
		}
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 10; j++) {
				right.add(mulRes[i][j]);
			}
		}
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 10; j++) {
				right.add(loadRes[i][j]);
			}
		}
		
		
//		right.setBackground(Color.GREEN);
//		ltop.setBackground(Color.BLACK);
//		lcenter.setBackground(Color.BLUE);
//		ldown.setBackground(Color.GRAY);

		
        this.add(mainPanel);
        initFrame();
        openPath = "";
        
        
        
        // 浏览文件以打开用户想要播放的音频文件
        JFileChooser fileChooser = new JFileChooser();
        
		// 设置当前目录
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setAcceptAllFileFilterUsed(false);

		final String[] fileENames ={ ".nel" };
		// 显示所有文件
		fileChooser.addChoosableFileFilter(new FileFilter() {
			public boolean accept(File file) {
		    return true;
		   }
			public String getDescription() {
			    return "所有文件(*.*)";
		   }
		});
		
		// 循环添加需要显示的文件
		for (final String fileEName : fileENames) {
			fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
				public boolean accept(File file) { 
					if (file.getName().endsWith(fileEName) || file.isDirectory()) {
						return true;
					}
					return false;
				}
		 
				public String getDescription() {
					return fileEName;
				}
			});
		}
		fileChooser.setVisible(false);
	
		// 点击选择音乐按钮后弹出浏览文件的对话框，并播放选中的音频
		file.addActionListener(e -> {
			new Thread() {
				public void run() {
					fileChooser.setVisible(true);
					fileChooser.showOpenDialog(mainPanel);
					
					openPath=fileChooser.getSelectedFile().getPath();
					
					try {
						System.out.println(openPath);
						sim = new Simulator(openPath);
						sim.readFileByLines(sim.fileName);
						
					} catch (Exception g) {
						g.printStackTrace();
					}
				}
			}.start();
		});
		
		runSimu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("try to run the Simulator");
				illegal = false;
				warning.setText("");
				if(openPath != "") {
					// can run
					try {
						if(clockTex.getText().length() != 0) {
							checkClock = Integer.valueOf(clockTex.getText()).intValue();
							System.out.println("checkClock = " + checkClock);
							if(checkClock <= 0) {
								// illegal
								warning.setText("Clock index is illegal.");
								illegal = true;
							}
						}
						else {
							checkClock = -1;
						}
						
					}catch(NumberFormatException ex){
						warning.setText("Clock index is illegal.");
						illegal = true;
					}
					
					try {
						if((instrTex.getText().length()) != 0) {
							checkInstr = Integer.valueOf(instrTex.getText()).intValue();
							System.out.println("checkInstr = " + checkInstr);
							if(checkInstr < 0 || checkInstr >= sim.instructions.instrSize) {
								warning.setText("Instruction index is illegal.");
								illegal = true;
							}
						}
						else {
							System.out.println("heeeeeeeeeeeeeere");
							checkInstr = -1;
						}
					}catch(NumberFormatException ex){
						warning.setText("Instruction index is illegal.");
						illegal = true;
					}
				}
				else {
					// can not run
					warning.setText("No file is opened!");
					illegal = true;
				}
				
				if(illegal == false) {
					if(checkClock == -1) {
						sim.runSimulator();
					}
					else {
						int tmp = sim.runPartSimulator(checkClock);
						if(checkClock > tmp) {
							checkClock = tmp;
						}
					}
					updateUI();
				}
			}
		});
		
		
	}
	
	public void updateUI() {
		// need to update
		
		warning.setText(String.valueOf(sim.currentInstr));
		
		if(checkInstr != -1) {
			instr.setText(sim.instructions.instructionsString[checkInstr]);
			if(sim.instructions.instruction[checkInstr].issueClock != -1) {
				instrForm[3].setText(String.valueOf(sim.instructions.instruction[checkInstr].issueClock));
			}
			if(sim.instructions.instruction[checkInstr].execCompClock != -1) {
				instrForm[4].setText(String.valueOf(sim.instructions.instruction[checkInstr].execCompClock));
			}
			if(sim.instructions.instruction[checkInstr].writeResultClock != -1) {
				instrForm[5].setText(String.valueOf(sim.instructions.instruction[checkInstr].writeResultClock));
			}
		}
		else {
			for(int i = 3; i < 6; i++) {
				instrForm[i].setText("");
			}
		}
		
		if(checkClock != -1) {
			clockTex.setText(String.valueOf(checkClock));
		}
		for(int i = 0; i < 32; i++) {
			if(sim.reg.fuState[i] != -10086) {
				F[i].setText(getState(sim.reg.fuState[i]));
			}
			else {
				F[i].setText("");
			}
			if(sim.reg.fuValue[i] != -10086) {
				FValue[i].setText(decToHex(getValue(sim.reg.fuValue[i])));
			}
			else {
				FValue[i].setText("");
			}
		}
		
		clearReForm();

//	    Resver[0] = new JLabel();
//		Resver[1] = new JLabel("isBusy");
//		Resver[2] = new JLabel("isRun");
//		Resver[3] = new JLabel("Time");
//		Resver[4] = new JLabel("Op");
//		Resver[5] = new JLabel("Vj");
//		Resver[6] = new JLabel("Vk");
//		Resver[7] = new JLabel("Qj");
//		Resver[8] = new JLabel("Qk");
//		Resver[9] = new JLabel("addr");
//		JLabel[][] addRes;
//		JLabel[][] mulRes;
//		JLabel[][] loadRes;
		
		for(int i = 0; i < 6; i++) {
			addRes[i][1].setText(String.valueOf(sim.addReserv.reservationStation[i].isBusy));
			if(sim.addReserv.reservationStation[i].isBusy) {
				addRes[i][2].setText(String.valueOf(sim.addReserv.reservationStation[i].isRun));
				addRes[i][3].setText(String.valueOf(sim.addReserv.reservationStation[i].time));
				addRes[i][4].setText(getOp(sim.addReserv.reservationStation[i].type));
				if(sim.addReserv.reservationStation[i].V[0] >= 0) {
					addRes[i][5].setText(decToHex(getValue(sim.addReserv.reservationStation[i].V[0])));
				}
				if(sim.addReserv.reservationStation[i].V[1] >= 0) {
					addRes[i][6].setText(decToHex(getValue(sim.addReserv.reservationStation[i].V[1])));
				}
				if(sim.addReserv.reservationStation[i].Q[0] < 0) {
					addRes[i][7].setText(getState(sim.addReserv.reservationStation[i].Q[0]));
				}
				if(sim.addReserv.reservationStation[i].Q[1] < 0) {
					addRes[i][8].setText(getState(sim.addReserv.reservationStation[i].Q[1]));
				}
			}
			else {
				for(int j = 2; j < 9; j++) {
					addRes[i][j].setText("");
				}
			}
		}
		for(int i = 0; i < 3; i++) {
			mulRes[i][1].setText(String.valueOf(sim.mulReserv.reservationStation[i].isBusy));
			if(sim.mulReserv.reservationStation[i].isBusy) {
				mulRes[i][2].setText(String.valueOf(sim.mulReserv.reservationStation[i].isRun));
				mulRes[i][3].setText(String.valueOf(sim.mulReserv.reservationStation[i].time));
				mulRes[i][4].setText(getOp(sim.mulReserv.reservationStation[i].type));
				if(sim.mulReserv.reservationStation[i].V[0] >= 0) {
					mulRes[i][5].setText(decToHex(getValue(sim.mulReserv.reservationStation[i].V[0])));
				}
				if(sim.mulReserv.reservationStation[i].V[1] >= 0) {
					mulRes[i][6].setText(decToHex(getValue(sim.mulReserv.reservationStation[i].V[1])));
				}
				if(sim.mulReserv.reservationStation[i].Q[0] < 0) {
					mulRes[i][7].setText(getState(sim.mulReserv.reservationStation[i].Q[0]));
				}
				if(sim.mulReserv.reservationStation[i].Q[1] < 0) {
					mulRes[i][8].setText(getState(sim.mulReserv.reservationStation[i].Q[1]));
				}			
			}
			else {
				for(int j = 2; j < 9; j++) {
					mulRes[i][j].setText("");
				}
			}
		}
		for(int i = 0; i < 3; i++) {
			loadRes[i][1].setText(String.valueOf(sim.loadReserv.reservationStation[i].isBusy));
			if(sim.loadReserv.reservationStation[i].isBusy) {
				loadRes[i][2].setText(String.valueOf(sim.loadReserv.reservationStation[i].isRun));
				loadRes[i][3].setText(String.valueOf(sim.loadReserv.reservationStation[i].time));
				loadRes[i][9].setText(decToHex(sim.loadReserv.reservationStation[i].addr));
			}
			else {
				for(int j = 2; j < 4; j++) {
					loadRes[i][j].setText("");
				}
				loadRes[i][9].setText("");
			}

		}		
	}
	
	public void clearReForm() {
		for(int i = 0; i < 6; i++) {
			for(int j = 1; j < 9; j++) {
				addRes[i][j].setText("");
			}
			addRes[i][9].setText("x");
		}
		
		for(int i = 0; i < 3; i++) {
			for(int j = 1; j < 9; j++) {
				mulRes[i][j].setText("");
			}
			mulRes[i][9].setText("x");
		}
		
		for(int i = 0; i < 3; i++) {
			for(int j = 4; j < 9; j++) {
				loadRes[i][j].setText("x");
			}
			loadRes[i][1].setText("");
			loadRes[i][2].setText("");
			loadRes[i][3].setText("");
			loadRes[i][9].setText("");
		}
	}
	
	public String decToHex(int dec) {
		String strHex = Integer.toHexString(dec);
		strHex = "0x" + strHex;
		return strHex;
	}
	
	public String getState(int s) {
//		public static final int ADD5 = -1;
//		public static final int ADD4 = -2;
//		public static final int ADD3 = -3;
//		public static final int ADD2 = -4;
//		public static final int ADD1 = -5;
//		public static final int ADD0 = -6;
//		public static final int MUL2 = -7;
//		public static final int MUL1 = -8;
//		public static final int MUL0 = -9;
//		public static final int LOAD2 = -10;
//		public static final int LOAD1 = -11;
//		public static final int LOAD0 = -12;
		String strState = "UNK";
		switch(s) {
		case ADD5:
			strState = "Ars6";
			break;
		case ADD4:
			strState = "Ars5";
			break;
		case ADD3:
			strState = "Ars4";
			break;
		case ADD2:
			strState = "Ars3";
			break;
		case ADD1:
			strState = "Ars2";
			break;
		case ADD0:
			strState = "Ars1";
			break;
		case MUL2:
			strState = "Mrs3";
			break;
		case MUL1:
			strState = "Mrs2";
			break;
		case MUL0:
			strState = "Mrs1";
			break;
		case LOAD2:
			strState = "LB3";
			break;
		case LOAD1:
			strState = "LB2";
			break;
		case LOAD0:
			strState = "LB1";
			break;
		}
		return strState;
	}
	
	public int getValue(int index) {
		return sim.reg.tempReg[index];
	}
	
	public String getOp(int op) {
//		public static final int LD = 0;
//	    public static final int JUMP = 1;
//	    public static final int ADD = 2;
//	    public static final int SUB = 3;
//	    public static final int MUL = 4;
//	    public static final int DIV = 5;
		String strOp = "UNKNOWN";
		switch(op) {
		case LD:
			strOp = "LD";
			break;
		case JUMP:
			strOp = "JUMP";
			break;
		case ADD:
			strOp = "ADD";
			break;
		case SUB:
			strOp = "SUB";
			break;
		case MUL:
			strOp = "MUL";
			break;
		case DIV:
			strOp = "DIV";
			break;
		}
		return strOp;
	}
	
	public void initFrame(){
//    	ImageIcon icon = new ImageIcon("calculator-icon.png");
//    	this.setIconImage(icon.getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//        this.setSize((int)(dim.width * 0.32), (int)(dim.height * 0.8));
        this.setSize(1200, 900);
        this.setLocation((int)(dim.width * 0.2), (int)(dim.height * 0.1));
        this.setVisible(true);
    }
}
