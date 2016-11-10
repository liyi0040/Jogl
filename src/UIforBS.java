import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.SpinnerNumberModel;

public class UIforBS {


	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					UIforBS window = new UIforBS();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	/**
//	 * Create the application.
//	 */
//	public UIforBS() {
//		initialize();
//	}
	JSpinner spinner;
	 JLabel lblTessnum;
	 JCheckBox SamplingPoints;
	 JCheckBox Adaptive;
	 JCheckBox ControlPolygon ;
	 JButton btnSelectFile;
	/**
	 * Initialize the contents of the frame.
	 * @param glcanvas 
	 * @wbp.parser.entryPoint
	 */
	 void initialize(GLCanvas glcanvas) {
		final JFrame frmLiying = new JFrame();
		frmLiying.setTitle("LiYing");
		frmLiying.setBounds(100, 100, 450, 300);
		frmLiying.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		glcanvas.setBounds(20, 60,600,600);
	    frmLiying.getContentPane().add(glcanvas);
	    frmLiying.getContentPane().setLayout(null);
	    
	     spinner = new JSpinner();
	     spinner.setModel(new SpinnerNumberModel(10, 5, 100, 1));
	    spinner.setBounds(468, 12,37, 20);
	    spinner.setValue(10);
	    frmLiying.getContentPane().add(spinner);
	    
	     lblTessnum = new JLabel("tessNum");
	    lblTessnum.setBounds(510, 15, 100, 20);
	    frmLiying.getContentPane().add(lblTessnum);
	    
	     SamplingPoints = new JCheckBox("sampling Points");
	    SamplingPoints.setBounds(342, 11, 120, 23);
	    frmLiying.getContentPane().add(SamplingPoints);
	    
	     Adaptive = new JCheckBox("adaptivePlot");
	    Adaptive.setBounds(120, 11, 109, 23);
	    frmLiying.getContentPane().add(Adaptive);
	    
	     ControlPolygon = new JCheckBox("control polygon");
	    ControlPolygon.setBounds(231, 7, 120, 30);
	    frmLiying.getContentPane().add(ControlPolygon);
	    
	     btnSelectFile = new JButton("select file");
	    btnSelectFile.setBounds(10, 11, 89, 23);
	    btnSelectFile.setSelected(false);
	    frmLiying.getContentPane().add(btnSelectFile);
	    


	    

	    
		frmLiying.setSize(700,700);
		frmLiying.setVisible(true);
	}
}
