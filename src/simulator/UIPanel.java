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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.border.StandardBorderPainter;
import org.jvnet.substance.button.ClassicButtonShaper;
import org.jvnet.substance.button.StandardButtonShaper;
import org.jvnet.substance.painter.StandardGradientPainter;
import org.jvnet.substance.skin.EmeraldDuskSkin;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;
import org.jvnet.substance.theme.SubstanceBottleGreenTheme;
import org.jvnet.substance.theme.SubstanceTerracottaTheme;
import org.jvnet.substance.title.MatteHeaderPainter;
import org.jvnet.substance.watermark.SubstanceBubblesWatermark;
import org.jvnet.substance.watermark.SubstanceStripeWatermark;




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
	JButton uiStyle;
	
	
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
	
	int style;
	
	
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
    
    public static final int ADDI = 7;
    public static final int SUBI = 8;
    public static final int NOP = 9;
    public static final int SHL = 10; // 逻辑左移
    public static final int SAL = 11; // 算术左移(=SHL)
    public static final int SHR = 12; // 逻辑右移.( 每位右移, 低位进 CF, 高位补 0)
    public static final int SAR = 13; // 算数右移
	
	UIPanel(){
		style = 0;
		try {
			UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());  
            JFrame.setDefaultLookAndFeelDecorated(true);  
            //设置主题   
            SubstanceLookAndFeel.setCurrentTheme("org.jvnet.substance.theme.SubstanceSepiaTheme");  
            //设置按钮外观  
            SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper()); //设置水印  
            SubstanceLookAndFeel.setCurrentWatermark(new SubstanceStripeWatermark());  
            //设置边框  
            SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());  
            //设置渐变渲染  
            SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());  
            //设置标题  
            SubstanceLookAndFeel.setCurrentTitlePainter(new MatteHeaderPainter()); 
            
			
			
            
//            UIManager.setLookAndFeel(new SubstanceLookAndFeel());
//            JFrame.setDefaultLookAndFeelDecorated(true);
//            JDialog.setDefaultLookAndFeelDecorated(true);
//            SubstanceLookAndFeel.setCurrentTheme(new SubstanceTerracottaTheme());
//            SubstanceLookAndFeel.setSkin(new EmeraldDuskSkin());
//            SubstanceLookAndFeel.setCurrentButtonShaper(new ClassicButtonShaper());
//            SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());
//            SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());
//            SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());

//            SubstanceLookAndFeel.setCurrentTheme("org.jvnet.substance.theme.SubstanceSepiaTheme");
//            SubstanceLookAndFeel.setCurrentTheme("org.jvnet.substance.theme.SubstanceDarkVioletTheme");
//            SubstanceLookAndFeel.setCurrentTheme("org.jvnet.substance.theme.SubstanceSteelBlueTheme");
//            SubstanceLookAndFeel.setCurrentTheme("org.jvnet.substance.theme.SubstanceTheme$16");

//            UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel");
//			UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceBusinessLookAndFeel");
//			UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceOfficeSilver2007LookAndFeel");
//			UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceSaharaLookAndFeel");
//			UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceOfficeBlue2007LookAndFeel");
           
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        } catch (Exception e) {
            System.err.println("Something went wrong!");
        }
		
		mainPanel = new JPanel();
		// main
		mainPanel.setLayout(new BorderLayout());
		Border border1 = mainPanel.getBorder();
		Border margin = new EmptyBorder(0, 10, 0, 5);
		mainPanel.setBorder(new CompoundBorder(border1, margin));
		
		
		left = new JPanel();
		right = new JPanel();
		right.setPreferredSize(new Dimension(800, 1000));
		mainPanel.add(left, BorderLayout.CENTER);
		mainPanel.add(right, BorderLayout.EAST);
		
		
		
		// left
		left.setLayout(new BorderLayout());
		
		ltop = new JPanel();
		lcenter = new JPanel();
		ldown = new JPanel();
		

		
		ltop.setPreferredSize(new Dimension(900, 300));
		ldown.setPreferredSize(new Dimension(900, 450));
		
		left.add(ltop, BorderLayout.NORTH);
		left.add(lcenter, BorderLayout.CENTER);
		left.add(ldown, BorderLayout.SOUTH);
		
		clockMes = new JLabel("CLOCK:", JLabel.CENTER);
		instrMes = new JLabel("INSTRU:", JLabel.CENTER);
		clockTex = new JTextField(JTextField.CENTER);
		instrTex = new JTextField(JTextField.CENTER);
		runSimu = new JButton("run");
		file = new JButton("open");
		uiStyle = new JButton("style");
		
		runSimu.setPreferredSize(new Dimension(30,20));
		file.setPreferredSize(new Dimension(30,20));
		uiStyle.setPreferredSize(new Dimension(30,20));
		
		ltop.setBorder(new EmptyBorder(30,30,30,30));
		ltop.setLayout(new GridLayout(3,1,0,0));
		
		warning = new JLabel("Warning", JLabel.CENTER);
		ltop.add(warning);
		
		
		JPanel tmp3 = new JPanel();
		tmp3.setLayout(new GridLayout(1,3,80,0));

		
		tmp3.add(file);
		tmp3.add(runSimu);
		tmp3.add(uiStyle);
		tmp3.setBorder(new EmptyBorder(0, 80, 50, 80));
		ltop.add(tmp3);

		
		
		JPanel tmp4 = new JPanel();
		tmp4.setLayout(new GridLayout(1,2,20,0));
		
		JPanel tmp = new JPanel();
		tmp.setLayout(new GridLayout(1,2,0,30));
		tmp.add(clockMes);
		tmp.add(clockTex);
		tmp.setBorder(new EmptyBorder(10, 0, 10, 10));
		
		tmp4.add(tmp);
		
		JPanel tmp2 = new JPanel();
		tmp2.setLayout(new GridLayout(1,2,0,30));
		tmp2.add(instrMes);
		tmp2.add(instrTex);
		tmp2.setBorder(new EmptyBorder(10, 10, 10, 0));
		
		tmp4.add(tmp2);
		tmp4.setBorder(new EmptyBorder(0, 30, 40, 80));
		
				
		ltop.add(tmp4);
		
		
		JPanel tmp5 = new JPanel();
		tmp5.setLayout(new GridLayout(1,6));
		
		JLabel emptyLabel1 = new JLabel();
		JLabel emptyLabel2 = new JLabel();
		JLabel emptyLabel3 = new JLabel();
		
		instr = new JLabel("", JLabel.CENTER);
		instrForm = new JLabel[6];
		for(int i = 0; i < 6; i++) {
			instrForm[i] = new JLabel("", JLabel.CENTER);
		}
		instrForm[0].setText("Issue");
		instrForm[1].setText("Exec Comp");
		instrForm[2].setText("Write Result");
		
		tmp5.add(emptyLabel1);
		tmp5.add(emptyLabel2);
		tmp5.add(emptyLabel3);
		for(int i = 0; i < 3; i++) {
			tmp5.add(instrForm[i]);
		}
		
		ltop.add(tmp5);
		
		JPanel tmp6 = new JPanel();
		tmp6.setLayout(new BorderLayout());
		instr.setPreferredSize(new Dimension(350,300));
		tmp6.add(instr, BorderLayout.WEST);
		
		JPanel tmp7 = new JPanel();
		tmp7.setLayout(new GridLayout(1,3));
		for(int i = 3; i < 6; i++) {
			tmp7.add(instrForm[i]);
		}
		tmp6.add(tmp7, BorderLayout.CENTER);
		ltop.add(tmp6);
		
		tmp5.setOpaque(false);
		tmp6.setOpaque(false);
		tmp7.setOpaque(false);
		

		
		lcenter.setLayout(new GridLayout(2,1,0,0));
		lcenter.add(tmp5);
		lcenter.add(tmp6);
		
		
		
		ldown.setLayout(new GridLayout(12,8,5,5));
		FU = new JLabel[32];
		F = new JLabel[32];
		FValue = new JLabel[32];
		for(int i = 0; i < 32; i++) {
			FU[i] = new JLabel("F" + i, JLabel.CENTER);
			F[i] = new JLabel("", JLabel.CENTER);
			FValue[i] = new JLabel("", JLabel.CENTER);
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
		Resver[0] = new JLabel("", JLabel.CENTER);
		Resver[1] = new JLabel("isBusy", JLabel.CENTER);
		Resver[2] = new JLabel("isRun", JLabel.CENTER);
		Resver[3] = new JLabel("Time", JLabel.CENTER);
		Resver[4] = new JLabel("Op", JLabel.CENTER);
		Resver[5] = new JLabel("Vj", JLabel.CENTER);
		Resver[6] = new JLabel("Vk", JLabel.CENTER);
		Resver[7] = new JLabel("Qj", JLabel.CENTER);
		Resver[8] = new JLabel("Qk", JLabel.CENTER);
		Resver[9] = new JLabel("addr", JLabel.CENTER);
		
		addRes = new JLabel[6][10];
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 10; j++) {
				addRes[i][j] = new JLabel("", JLabel.CENTER);
			}
			addRes[i][0].setText("add" + String.valueOf(i+1));
		}
		
		mulRes = new JLabel[3][10];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 10; j++) {
				mulRes[i][j] = new JLabel("", JLabel.CENTER);
			}
			mulRes[i][0].setText("mul" + String.valueOf(i+1));
		}
		
		loadRes = new JLabel[3][10];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 10; j++) {
				loadRes[i][j] = new JLabel("", JLabel.CENTER);
			}
			loadRes[i][0].setText("load" + String.valueOf(i+1));
		}
		
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
		
		setMyFont();
		
//		ltop.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		
		ltop.setBorder(BorderFactory.createLineBorder(new Color(125,204,209), 3, true));
		 
		lcenter.setBorder(new EmptyBorder(20, 20, 20, 20));
		ldown.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		left.setBorder(new EmptyBorder(20, 20, 20, 20));
		right.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		
//		ltop.setBackground(Color.black);
//		lcenter.setBackground(Color.blue);
//		ldown.setBackground(Color.green);

		
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
		
		uiStyle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeStyle();
			}
		});
		
		
	}
	
	public void changeStyle() {
		style = (style+1) % 3;
		switch(style) {
		case 0:
			try {
				UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());    
	            //设置主题   
	            SubstanceLookAndFeel.setCurrentTheme("org.jvnet.substance.theme.SubstanceSepiaTheme");  
	            //设置按钮外观  
	            SubstanceLookAndFeel.setCurrentButtonShaper(new StandardButtonShaper()); //设置水印  
	            SubstanceLookAndFeel.setCurrentWatermark(new SubstanceStripeWatermark());  
	            //设置边框  
	            SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());  
	            //设置渐变渲染  
	            SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());  
	            //设置标题  
	            SubstanceLookAndFeel.setCurrentTitlePainter(new MatteHeaderPainter()); 
	            ltop.setBorder(BorderFactory.createLineBorder(new Color(125,204,209), 3, true));
	        } catch (Exception e) {
	            System.err.println("Something went wrong!");
	        }
			break;
		case 1:
			try {
				UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());    
	            SubstanceLookAndFeel.setCurrentTheme("org.jvnet.substance.theme.SubstanceDarkVioletTheme");
	            ltop.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(206,120,235)));
	        } catch (Exception e) {
	            System.err.println("Something went wrong!");
	        }
			break;
		case 2:
			try {
	            
	            UIManager.setLookAndFeel(new SubstanceLookAndFeel());
	            SubstanceLookAndFeel.setCurrentTheme(new SubstanceTerracottaTheme());
	            SubstanceLookAndFeel.setSkin(new EmeraldDuskSkin());
	            SubstanceLookAndFeel.setCurrentButtonShaper(new ClassicButtonShaper());
	            SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());
	            SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());
	            SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());
	            ltop.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(124,173,152)));
	        } catch (Exception e) {
	            System.err.println("Something went wrong!");
	        }
			break;
		}
	}
	
	public void setMyFont() {
		Font font1 = new Font("细明本",Font.PLAIN,20);
		
		clockMes.setFont(font1);
		instrMes.setFont(font1);
		warning.setFont(font1);
		clockTex.setFont(font1);
		instrTex.setFont(font1);
		
		runSimu.setFont(font1);
		file.setFont(font1);
		
		
		instr.setFont(font1);
		
		for(int i = 0; i < instrForm.length; i++) {
			instrForm[i].setFont(font1);
		}
		for(int i = 0; i < FU.length; i++) {
			FU[i].setFont(font1);
		}
		for(int i = 0; i < F.length; i++) {
			F[i].setFont(font1);
		}
		for(int i = 0; i < FValue.length; i++) {
			FValue[i].setFont(font1);
		}
		for(int i = 0; i < Resver.length; i++) {
			Resver[i].setFont(font1);
		}
		
		for(int i = 0; i < addRes.length; i++) {
			for(int j = 0; j < addRes[i].length; j++) {
				addRes[i][j].setFont(font1);
			}
		}
		for(int i = 0; i < mulRes.length; i++) {
			for(int j = 0; j < mulRes[i].length; j++) {
				mulRes[i][j].setFont(font1);
			}
		}
		for(int i = 0; i < loadRes.length; i++) {
			for(int j = 0; j < loadRes[i].length; j++) {
				loadRes[i][j].setFont(font1);
			}
		}
	}
	
	public void updateUI() {
		// need to update
		
		warning.setText(String.valueOf(sim.currentInstr));
		
		if(checkInstr != -1) {
			instr.setText(sim.instructions.instructionsString[checkInstr]);
			if(sim.instructions.instruction[checkInstr].issueClock != -1) {
				instrForm[3].setText(String.valueOf(sim.instructions.instruction[checkInstr].issueClock));
			}
			else {
				instrForm[3].setText("");
			}
			if(sim.instructions.instruction[checkInstr].execCompClock != -1) {
				instrForm[4].setText(String.valueOf(sim.instructions.instruction[checkInstr].execCompClock));
			}
			else {
				instrForm[4].setText("");
			}
			if(sim.instructions.instruction[checkInstr].writeResultClock != -1) {
				instrForm[5].setText(String.valueOf(sim.instructions.instruction[checkInstr].writeResultClock));
			}
			else {
				instrForm[5].setText("");
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
				if(F[i].getText() == "") {
					F[i].setText(decToHex(getValue(sim.reg.fuValue[i])));
				}
			}
			else {
				FValue[i].setText("");
			}
		}
		
		
		clearReForm();

		
		for(int i = 0; i < 6; i++) {
//			addRes[i][1].setText(String.valueOf(sim.addReserv.reservationStation[i].isBusy));
			if(sim.addReserv.reservationStation[i].isBusy) {
				addRes[i][1].setText("√");
				if(sim.addReserv.reservationStation[i].isRun) {
					addRes[i][2].setText("√");
				}
				else {
					addRes[i][2].setText("×");
				}
				addRes[i][3].setText(String.valueOf(sim.addReserv.reservationStation[i].time));
				addRes[i][4].setText(getOp(sim.addReserv.reservationStation[i].type));
				switch(sim.addReserv.reservationStation[i].type) {
				case ADD:
				case SUB:
					if(sim.addReserv.reservationStation[i].V[0] >= 0) {
						addRes[i][5].setText(decToHex(getValue(sim.addReserv.reservationStation[i].V[0])));
					}
					if(sim.addReserv.reservationStation[i].V[1] >= 0) {
						addRes[i][6].setText(decToHex(getValue(sim.addReserv.reservationStation[i].V[1])));
					}
					if(sim.addReserv.reservationStation[i].Q[0] < 0) {
						addRes[i][7].setText(getState(sim.addReserv.reservationStation[i].Q[0]));
						System.out.println("??? " +i + " Q[0] " + sim.addReserv.reservationStation[i].Q[0] + " text "+ getState(sim.addReserv.reservationStation[i].Q[0]));
						
					}
					if(sim.addReserv.reservationStation[i].Q[1] < 0) {
						addRes[i][8].setText(getState(sim.addReserv.reservationStation[i].Q[1]));
					}
					break;
				case JUMP:
					if(sim.addReserv.reservationStation[i].V[0] >= 0) {
						addRes[i][5].setText(decToHex(getValue(sim.addReserv.reservationStation[i].V[0])));
					}
					if(sim.addReserv.reservationStation[i].Q[0] < 0) {
						addRes[i][7].setText(getState(sim.addReserv.reservationStation[i].Q[0]));
					}
					break;
				case ADDI:
				case SUBI:
					if(sim.addReserv.reservationStation[i].V[0] >= 0) {
						addRes[i][5].setText(decToHex(getValue(sim.addReserv.reservationStation[i].V[0])));
					}
					if(sim.addReserv.reservationStation[i].V[1] >= 0) {
						addRes[i][6].setText(decToHex(sim.addReserv.reservationStation[i].V[1]));
					}
					if(sim.addReserv.reservationStation[i].Q[0] < 0) {
						addRes[i][7].setText(getState(sim.addReserv.reservationStation[i].Q[0]));
					}
					
					break;
				}
			}
			else {
				for(int j = 2; j < 9; j++) {
					addRes[i][j].setText("");
				}
			}
		}
		for(int i = 0; i < 3; i++) {
//			mulRes[i][1].setText(String.valueOf(sim.mulReserv.reservationStation[i].isBusy));
			if(sim.mulReserv.reservationStation[i].isBusy) {
				mulRes[i][1].setText("√");
				if(sim.mulReserv.reservationStation[i].isRun) {
					mulRes[i][2].setText("√");
				}
				else {
					mulRes[i][2].setText("×");
				}
				mulRes[i][3].setText(String.valueOf(sim.mulReserv.reservationStation[i].time));
				mulRes[i][4].setText(getOp(sim.mulReserv.reservationStation[i].type));
				
				switch(sim.mulReserv.reservationStation[i].type) {
				case MUL:
				case DIV:
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
					break;
				case SHL:
				case SAL:
				case SHR:
				case SAR:
					if(sim.mulReserv.reservationStation[i].V[0] >= 0) {
						mulRes[i][5].setText(decToHex(getValue(sim.mulReserv.reservationStation[i].V[0])));
					}
					if(sim.mulReserv.reservationStation[i].V[1] >= 0) {
						mulRes[i][6].setText(decToHex(sim.mulReserv.reservationStation[i].V[1]));
					}
					if(sim.mulReserv.reservationStation[i].Q[0] < 0) {
						mulRes[i][7].setText(getState(sim.mulReserv.reservationStation[i].Q[0]));
					}
					break;
				}
							
			}
			else {
				for(int j = 2; j < 9; j++) {
					mulRes[i][j].setText("");
				}
			}
		}
		for(int i = 0; i < 3; i++) {
//			loadRes[i][1].setText(String.valueOf(sim.loadReserv.reservationStation[i].isBusy));
			if(sim.loadReserv.reservationStation[i].isBusy) {
				loadRes[i][1].setText("√");
				if(sim.loadReserv.reservationStation[i].isRun) {
					loadRes[i][2].setText("√");
				}
				else {
					loadRes[i][2].setText("×");
				}
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
			addRes[i][9].setText("×");
		}
		
		for(int i = 0; i < 3; i++) {
			for(int j = 1; j < 9; j++) {
				mulRes[i][j].setText("");
			}
			mulRes[i][9].setText("×");
		}
		
		for(int i = 0; i < 3; i++) {
			for(int j = 4; j < 9; j++) {
				loadRes[i][j].setText("×");
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
		case ADDI:
			strOp = "ADDI";
			break;
		case SUB:
			strOp = "SUB";
			break;
		case SUBI:
			strOp = "SUBI";
			break;
		case MUL:
			strOp = "MUL";
			break;
		case DIV:
			strOp = "DIV";
			break;
		case SHL:
			strOp = "SHL";
			break;
		case SAL:
			strOp = "SAL";
			break;
		case SHR:
			strOp = "SHR";
			break;
		case SAR:
			strOp = "SAR";
			break;
		}
		return strOp;
	}
	
	public void initFrame(){
//    	ImageIcon icon = new ImageIcon("calculator-icon.png");
//    	this.setIconImage(icon.getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(1700, 1000);
        this.setLocation((int)(dim.width * 0.1), (int)(dim.height * 0.05));
        this.setVisible(true);
    }
}
