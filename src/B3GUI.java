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


public class B3GUI {

	private JFrame frmEitnHaB;
	private JTextField fieldSeed;
	private JTextField fieldMaskLen;
	private JTextField fieldOutput;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					B3GUI window = new B3GUI();
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
	public B3GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEitnHaB = new JFrame();
		frmEitnHaB.setTitle("EITN41 Ha04 B3");
		frmEitnHaB.setBounds(100, 100, 651, 179);
		frmEitnHaB.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEitnHaB.getContentPane().setLayout(null);
		
		JLabel lblMgfseed = new JLabel("mgfSeed:");
		lblMgfseed.setBounds(10, 14, 86, 14);
		frmEitnHaB.getContentPane().add(lblMgfseed);
		
		fieldSeed = new JTextField();
		fieldSeed.setBounds(106, 11, 519, 20);
		frmEitnHaB.getContentPane().add(fieldSeed);
		fieldSeed.setColumns(10);
		
		fieldMaskLen = new JTextField();
		fieldMaskLen.setColumns(10);
		fieldMaskLen.setBounds(106, 41, 519, 20);
		frmEitnHaB.getContentPane().add(fieldMaskLen);
		
		JLabel lblMasklen = new JLabel("maskLen:");
		lblMasklen.setBounds(10, 44, 86, 14);
		frmEitnHaB.getContentPane().add(lblMasklen);
		
		fieldOutput = new JTextField();
		fieldOutput.setEditable(false);
		fieldOutput.setColumns(10);
		fieldOutput.setBounds(116, 72, 509, 20);
		frmEitnHaB.getContentPane().add(fieldOutput);
		
		JLabel lblHexoutput = new JLabel("hexOutput:");
		lblHexoutput.setBounds(10, 75, 86, 14);
		frmEitnHaB.getContentPane().add(lblHexoutput);
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String mgfSeed = fieldSeed.getText();
				BigInteger maskLen = new BigInteger(fieldMaskLen.getText());
				B3 b3 = new B3(mgfSeed, maskLen);
				String answer = b3.run();
				fieldOutput.setText(answer);
			}
		});
		btnRun.setBounds(536, 106, 89, 23);
		frmEitnHaB.getContentPane().add(btnRun);
	}
}
