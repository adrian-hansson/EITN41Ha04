import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.GridLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.math.BigInteger;


public class B4GUI {

	private JFrame frmEitnHaB;
	private JTextField fieldM;
	private JTextField fieldSeed;
	private JTextField fieldOutput;
	private JLabel lblEm;
	private JTextField fieldEM;
	private JButton btnDecode;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					B4GUI window = new B4GUI();
					window.frmEitnHaB.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public B4GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEitnHaB = new JFrame();
		frmEitnHaB.setTitle("EITN41 Ha04 B4");
		frmEitnHaB.setBounds(100, 100, 651, 290);
		frmEitnHaB.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEitnHaB.getContentPane().setLayout(null);
		
		JLabel lblMgfseed = new JLabel("M: ");
		lblMgfseed.setBounds(10, 14, 86, 14);
		frmEitnHaB.getContentPane().add(lblMgfseed);
		
		fieldM = new JTextField();
		fieldM.setBounds(106, 11, 519, 20);
		frmEitnHaB.getContentPane().add(fieldM);
		fieldM.setColumns(10);
		
		fieldSeed = new JTextField();
		fieldSeed.setColumns(10);
		fieldSeed.setBounds(106, 41, 519, 20);
		frmEitnHaB.getContentPane().add(fieldSeed);
		
		JLabel lblMasklen = new JLabel("Seed");
		lblMasklen.setBounds(10, 44, 86, 14);
		frmEitnHaB.getContentPane().add(lblMasklen);
		
		fieldOutput = new JTextField();
		fieldOutput.setEditable(false);
		fieldOutput.setColumns(10);
		fieldOutput.setBounds(116, 186, 509, 20);
		frmEitnHaB.getContentPane().add(fieldOutput);
		
		JLabel lblHexoutput = new JLabel("hexOutput:");
		lblHexoutput.setBounds(10, 189, 86, 14);
		frmEitnHaB.getContentPane().add(lblHexoutput);
		
		JButton btnEncode = new JButton("Encode M");
		btnEncode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String M = fieldM.getText();
				String seed = fieldSeed.getText();
				B4 b4 = new B4();
				try{
				String answer = b4.OAEP_encode(M, seed);
				fieldOutput.setText(answer);
				}catch(Exception err){
					fieldOutput.setText("An error occurred. See console.");
				}
			}
		});
		btnEncode.setBounds(425, 217, 99, 23);
		frmEitnHaB.getContentPane().add(btnEncode);
		
		lblEm = new JLabel("EM");
		lblEm.setBounds(10, 75, 86, 14);
		frmEitnHaB.getContentPane().add(lblEm);
		
		fieldEM = new JTextField();
		fieldEM.setColumns(10);
		fieldEM.setBounds(106, 72, 519, 20);
		frmEitnHaB.getContentPane().add(fieldEM);
		
		btnDecode = new JButton("Decode EM");
		btnDecode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String EM = fieldEM.getText();
				B4 b4 = new B4();
				try{
					String answer = b4.OAEP_decode(EM);
					fieldOutput.setText(answer);
				}catch(Exception err){
					fieldOutput.setText("An error occurred. See console.");
				}
			}
		});
		btnDecode.setBounds(526, 217, 99, 23);
		frmEitnHaB.getContentPane().add(btnDecode);
	}
}
