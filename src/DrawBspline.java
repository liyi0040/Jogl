import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.EventListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

public class DrawBspline implements GLEventListener, ActionListener {
	
      static DrawBspline bcr = new DrawBspline();
      static UIforBS window = new UIforBS();
	  int degree;
	  int cntNum;
//	  int samplingPnt=0;
	  double knots[];
	Point2D.Double[] cnt;
	  double tessNum = 10;
//	  int showCP=0;
	  int adp=0;//uniform = 0, adp=1
	  String filename = null;  



	public static void main(String[] args) {
		// TODO Auto-generated method stub
	      final GLProfile profile = GLProfile.get(GLProfile.GL2);
	      GLCapabilities capabilities = new GLCapabilities(profile);
	      // The canvas 
	      final GLCanvas glcanvas = new GLCanvas(capabilities);
	      window.initialize(glcanvas);
		  window.Adaptive.addActionListener(bcr);
		  window.ControlPolygon.addActionListener(bcr);
//			window.lblTessnum.addAncestorListener(listener);
	      window.SamplingPoints.addActionListener(bcr);
	      window.btnSelectFile.addActionListener(bcr);
	      glcanvas.addGLEventListener(bcr);
	      glcanvas.setSize(600,600);
	      
//	      System.out.println("surface width="+glcanvas.getSurfaceWidth());



	      //creating frame
//	      final JFrame frame = new JFrame (" Uniform Frame");
//	      //adding canvas to it
//	      frame.getContentPane().add(glcanvas);
//	      frame.setSize(frame.getContentPane().getPreferredSize());
//	      frame.setVisible(true);

	}

		



	
	
	@Override
	public void display(GLAutoDrawable  drawable) {
		// TODO Auto-generated method stub
		final GL2 gl = drawable.getGL().getGL2();
		GLU glu = GLU.createGLU(gl);
	      int i = 0;
//	      tessNum=Double.parseDouble(window.spinner.getValue().toString());
	       tessNum = Double.parseDouble(window.spinner.getValue().toString());



		  gl.glClear(gl.GL_COLOR_BUFFER_BIT|gl.GL_ACCUM_BUFFER_BIT);
		  gl.glPushMatrix();
		  gl.glLoadIdentity();
		  gl.glTranslatef(-1, -1, 0);
		  
//========================== draw control polygon ===========================

	      if (window.ControlPolygon.isSelected()) {

		  gl.glLineWidth(3);
	      gl.glBegin (GL2.GL_LINE_STRIP);
			gl.glColor3f(1, 0, 0);
			for (i = 0; i < cntNum; i++) {
				Point2D curr_p = cnt[i];
				System.out.println(curr_p+"------"+i);
				gl.glVertex2d((cnt[i].getX())/200,(cnt[i].getY())/200);
				}				
	      gl.glEnd(); gl.glFlush();
	      
//========================== draw control points ===========================
			
		
			
 	       gl.glPointSize(6);
			gl.glBegin(GL2.GL_POINTS);
			gl.glColor3d(0, 1, 0);
				for(i=0;i<cntNum;i++){
					gl.glVertex2d((cnt[i].getX())/200,(cnt[i].getY())/200);

				}
			gl.glEnd(); gl.glFlush();
//			gl.glFlush();
	      }
			
//=========================  draw curves ===================================
			
//============adaptive rendering start========================================

	if(window.Adaptive.isSelected()) {
		// TODO Auto-generated method stub
		Point2D.Double[] bez = new Point2D.Double[30];
		Point2D.Double[] ptOncurve = new Point2D.Double[30] ;
		double step = (double)1.0/(tessNum-1);
		for (int seg=bcr.degree; seg< bcr.cntNum; seg++)
		{
			if (Math.abs(bcr.knots[seg]-bcr.knots[seg+1]) < 0.00001) continue;  // no segment, skip over
			extractBezier (bez, seg);        // extract the k-th Bezier curve
			
			System.out.println("complete extractBezier++++++k"+seg);
			
//============draw plotBezier=======================
			for(int m=0;m<degree+1;m++)
			{
				ptOncurve[m]= bez[m];
				System.out.println("ptOncurve m"+ptOncurve[m]);

			}
			gl.glLineWidth(3);
			gl.glColor3f(0.0f, 0.0f, 0.0f);;
			gl.glBegin(GL2.GL_LINE_STRIP);
			
				for(double j=0.0; j<=1.0; j+=step)
				{
					
					for(int n = degree; n>0; n--)
						for(int i1=0; i1<n; i1++)
						{
	//						System.out.println(degree +"cooooooooc"+bez[i1]);
	//						System.out.println("j="+j+"------n="+n+"-------i="+i1);
	
							if((1.0-j)<step)
							{
								j=1.0;
								ptOncurve[0]=new Point2D.Double(bez[degree].getX(), bez[degree].getY());
	//							System.out.println(ptOncurve[0]+"-----------ptOncurve[0]");
	//							ptOncurve[0].x = bez[degree].x;
	//							ptOncurve[0].getY() = bez[degree].getY();
							}
							else
							{
								ptOncurve[i1]=new Point2D.Double(getPoint(ptOncurve[i1].getX(), ptOncurve[i1+1].getX(), j), getPoint(ptOncurve[i1].getY(), ptOncurve[i1+1].getY(), j));
	//							System.out.println(ptOncurve[i1]+"-----------ptOncurve["+i1+"]");
	
	//							ptOncurve[i1].x=getPoint(ptOncurve[i1].x, ptOncurve[i1+1].x, j);
	//							ptOncurve[i1].getY()=getPoint(ptOncurve[i1].getY(), ptOncurve[i1+1].getY(), j);
							}
						}
//					System.out.println("the final ptoncurve"+ptOncurve[0]);
					gl.glVertex2d(ptOncurve[0].getX()/200, ptOncurve[0].getY()/200);
					if(window.SamplingPoints.isSelected())
					{
						gl.glColor3f(0, 0, 1);
				 	    gl.glPointSize(6);
						gl.glBegin(GL2.GL_POINTS);
							gl.glVertex2d(ptOncurve[0].getX()/200, ptOncurve[0].getY()/200);
						gl.glEnd(); 
						System.out.println("draw adp points");
						gl.glFlush();
					}

				}

				System.out.println("draw adp line");
				gl.glEnd(); gl.glFlush();
//===========complete draw plotBezier============================		
		}
		
//============complete adaptive rendering=========================
	gl.glPopMatrix();
	}
	
//===============uniform rendering=================================	
	if (!window.Adaptive.isSelected()  ) {
		
		System.out.println("do uniform rendering");
		Point2D.Double[][] bez = new Point2D.Double[10][30];
		Point2D.Double u1 = new Point2D.Double(-1, -1);
		Point2D.Double u21 = new Point2D.Double(-1, -1);
		int h;//右上角degree
		int iu = 0;//右下角 ，第几个control point
		for (int l =0; l <=  degree; l++)//l:degree form 3 to 0
		{

			for (int u=0;u<bcr.cntNum;u++){ //u: control
				
			
				if(l==0){
					bez[l][u] = new Point2D.Double(cnt[u].getX(),cnt[u].getY());
					System.out.println(bez[l][u]);
					}
				else
				{
					bez[l][u] = new Point2D.Double(0, 0);
					System.out.println("bez["+l+"]["+u+"]"+bez[l][u]);
				}
			}

		}		
		int j = degree ;
		System.out.println("j===================="+j);
		System.out.println("cntnum===================="+bcr.cntNum);

		while (j < bcr.cntNum )
		{
//			for (double t = bcr.knots[j]; t <= bcr.knots[j + 1];t+=(bcr.knots[j+1]-bcr.knots[j])/(Math.pow(2,(tessNum))))///!!!!????
			for (double t = bcr.knots[j]; t <= bcr.knots[j + 1];t+=(bcr.knots[j+1]-bcr.knots[j])/(tessNum))///!!!!????

			{
				System.out.println("t===="+t);
				System.out.println("cnt="+cntNum);
				for (h = 1; h <= degree; h++)
				{
					for (iu = j - degree + h; iu <= j; iu++)
					{
						int l = iu + degree + 1 - h;
//						System.out.println("degree======"+degree);
//						System.out.println("h=="+h);
//						System.out.println("iu==="+iu);
						bez[h][iu] = new Point2D.Double(((bcr.knots[l] - t) / (bcr.knots[l] - bcr.knots[iu]))*bez[h - 1][iu - 1].getX() + ((t - bcr.knots[iu]) / (bcr.knots[l] - bcr.knots[iu]))*bez[h - 1][iu].getX(),((bcr.knots[l] - t) / (bcr.knots[l] - bcr.knots[iu]))*bez[h - 1][iu - 1].getY() + ((t - bcr.knots[iu]) / (bcr.knots[l] - bcr.knots[iu]))*bez[h - 1][iu].getY());
//						bez[h][iu].y = ((bcr.knots[l] - t) / (bcr.knots[l] - bcr.knots[iu]))*bez[h - 1][iu - 1].getY() + ((t - bcr.knots[iu]) / (bcr.knots[l] - bcr.knots[iu]))*bez[h - 1][iu].getY();
						System.out.println("bez["+h+"]["+iu+"]"+bez[h][iu]);
					}
				}
				//draw curve
				if (u1.x != -1)
				{
//					u21 = new (bez[h-1][iu-1].getX(),bez[h-1][iu-1].getY());
					u21 = new Point2D.Double(bez[h-1][iu-1].getX(), bez[h-1][iu-1].getY());
					gl.glColor3f(0.0f, 0f, 0f);
					gl.glBegin(GL.GL_LINES);
						gl.glVertex2d(u1.getX()/200, u1.getY()/200);
						gl.glVertex2d(u21.getX()/200, u21.getY()/200);
					gl.glEnd(); gl.glFlush();
					
					if (window.SamplingPoints.isSelected()) {
					gl.glColor3f(0.0f, 0f, 1.0f);
					gl.glBegin(GL.GL_POINTS);
						gl.glVertex2d(u1.getX()/200, u1.getY()/200);
						gl.glVertex2d(u21.getX()/200, u21.getY()/200);
					gl.glEnd(); gl.glFlush();
						
					}

					u1 = u21;
				}
				else
				{
					u1.x = bez[h-1][iu-1].getX();
					u1.y = bez[h-1][iu-1].getY();
				}
			}
			j++;
		}
		gl.glPopMatrix();

	}//=================complete uniform rendering====================================


//=======================complete draw curve===========================
	
	}
	


	private void readfile() {
		// TODO Auto-generated method stub
		try {
      	  //the ui style
      	  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
            JFileChooser jdir = new JFileChooser();  
            //path mode  
            jdir.setFileSelectionMode(JFileChooser.FILES_ONLY);  
            //fliter  
            FileNameExtensionFilter filter = new FileNameExtensionFilter(    
                     "txt文件(*.txt;)","txt");   
            jdir.setFileFilter(filter);  
            jdir.setDialogTitle("Choose the data file");  
            if (JFileChooser.APPROVE_OPTION == jdir.showOpenDialog(null)) {//用户点击了确定  
          	  filename = jdir.getSelectedFile().getAbsolutePath();//取得路径选择  
            }  
            System.out.println(filename);  			
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		File file = new File(filename);
		BufferedReader Linereader =null;
//		int degree;
//		int cntNum=0;
//		Point2D.Double[] cnt;
		try {
			System.out.println("read the file:");
			
			Linereader = new BufferedReader(new FileReader(file));
			 String eachLine = null;
			 int line = 1;
			 String eachCharacter = null;
			 
			 while ((eachLine=Linereader.readLine())!=null ) {
				if(eachLine.length()!=0){
//					System.out.println("line"+line+":"+eachLine);
					if(line==1){
						  degree = Integer.parseInt(eachLine);
//						System.out.println("degree="+degree);
					}
					else if (line==2) {
						  cntNum = Integer.parseInt(eachLine);
//						System.out.println("cntNum="+cntNum);
						 cnt = new Point2D.Double[cntNum];
						 knots = new double[cntNum+degree+1];

						}
					else if (line==3) {
						 String[] knotsLine = eachLine.split(",") ;// knotsLine[] ={} 
						 System.out.println(cntNum+"cntnum===="+"degree==="+degree+"knotsLine length"+knotsLine.length);
						 for(int ks=0;ks<cntNum+degree+1;ks++){
//							 System.out.println("ks====="+ks);
						 knots[ks]= Double.parseDouble(knotsLine[ks]);
//						 System.out.println("knots["+ks+"]===="+knots[ks]);
						 }
					}
					else {
						String[] cntS = eachLine.split(",");
						System.out.println(cntS[0]);
						Point2D.Double cntp=  new Point2D.Double(Double.parseDouble(cntS[0]), Double.parseDouble(cntS[1]));
						System.out.println(cntp);
						cnt[line-4] = cntp;
						
					}
				
					line++;
				 }
			}
			 Linereader.close();
		} catch (IOException e1) {
			// TODO: handle exception
          e1.printStackTrace();

		}
	}







	void extractBezier (Point2D.Double[] bez, int ind)//bez,3 to 7
	{
		int     i, j;
		int     k;
		double[]  knots = new double[50] ;
		Point2D.Double cntInit = new Point2D.Double(0, 0);
		Point2D.Double[] cnt = new Point2D.Double[]{cntInit,cntInit,cntInit,cntInit,cntInit,cntInit,cntInit,cntInit,cntInit,cntInit};
		k = bcr.degree;

		// copy one segment
		for (i=ind-k, j=0; i<=ind; i++) {//改了这里 i<=ind为i<=bcr,cntNum i 0-3 - 4-7 
			double cX = bcr.cnt[i].getX();
			double cY = bcr.cnt[i].getY();
			cnt[j] = new Point2D.Double(cX,cY);

			j++;
		}
		for (i=ind-k, j=0; i<= ind+k+1; i++) {
			knots[j] = bcr.knots[i];
			j++;
		}//j=3+3+1=7 8个knots
		
		// insert knots to make the left end be Bezier end
		while(1==1) {
			for (i=k-1; i>0; i--) {
				if (knots[i] < knots[k]) {
					j = i;
					break;
				}
				j = 0;
			}

			if(j==0) break;

			// update control points
			for (i=0; i<j; i++) {
				cnt[i]= new Point2D.Double(((knots[k+1+i]-knots[k])/(knots[k+i+1]-knots[i+1]))*cnt[i].getX() 
						  + ((knots[k]-knots[i+1])/(knots[k+i+1]-knots[i+1]))*cnt[i+1].getX(),
				((knots[k+1+i]-knots[k])/(knots[k+i+1]-knots[i+1]))*cnt[i].getY() 
						  + ((knots[k]-knots[i+1])/(knots[k+i+1]-knots[i+1]))*cnt[i+1].getY());
				
			}
			// update knots
			for (i=0; i<j; i++)
				knots[i] = knots[i+1];
			knots[j] = knots[k];
		}

		// insert knots to make the right end be Bezier end
		while(2==2) {
			for (i=k+2; i< k+k+1; i++) {
				if (knots[i] > knots[k+1]) {
					j = i;
					break;
				}
				j = 0;
			}

			if(j==0) break;

			// update control points
			for (i=k; i>=j-k; i--) {//i=3 to i=7-3=4
				cnt[i]= new Point2D.Double(((knots[k+i]-knots[k+1])/(knots[k+i]-knots[i]))*cnt[i-1].getX() 
						  + ((knots[k+1]-knots[i])/(knots[k+i]-knots[i]))*cnt[i].getX(),((knots[k+i]-knots[k+1])/(knots[k+i]-knots[i]))*cnt[i-1].getY() 
						  + ((knots[k+1]-knots[i])/(knots[k+i]-knots[i]))*cnt[i].getY());
			}
			// update knots
			for (i=k+k+1; i>j; i--)
				knots[i] = knots[i-1];
			knots[j] = knots[k+1];
		}

		// return the Bezier control points
		for (i=0; i< bcr.cntNum; i++) {//i=0 to 6  ,5
			//System.out.println("i===="+i+"+-----------"+cnt[i]);
			//bez[i].x = cnt[i].getX();
			double bX=cnt[i].getX();
			double bY =cnt[i].getY();
			bez[i]=new Point2D.Double(bX, bY);
			
			System.out.println("i===="+i+"+-----------"+bez[i]);
			//bez[i].getY() = cnt[i].getY();
		}
	}
	
	
	
	//============================================================
	double getPoint(double n1, double n2, double step)
	{
		double dis = n2-n1;
		return (n1+ (dis * step));
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawableInit) {
		// TODO Auto-generated method stub
		final GL2 gl = drawableInit.getGL().getGL2();
		GLU glu = GLU.createGLU(gl);		
		
		gl.glClearColor (1.0f, 1.0f, 1.0f, 0.0f);         // set display-window color to white
	    gl.glMatrixMode (gl.GL_PROJECTION);	           // set projection parameters
		glu.gluOrtho2D (-50.0, 100.0, -50, 100);   // set an orthogonal projection
		
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}







	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if (window.btnSelectFile.isEnabled()) {
			try {
	        	  //the ui style
	        	  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
	              JFileChooser jdir = new JFileChooser();  
	              //path mode  
	              jdir.setFileSelectionMode(JFileChooser.FILES_ONLY);  
	              //fliter  
	              FileNameExtensionFilter filter = new FileNameExtensionFilter(    
	                       "txt文件(*.txt;)","txt");   
	              jdir.setFileFilter(filter);  
	              jdir.setDialogTitle("Choose the data file");  
	              if (JFileChooser.APPROVE_OPTION == jdir.showOpenDialog(null)) {//用户点击了确定  
	            	  filename = jdir.getSelectedFile().getAbsolutePath();//取得路径选择  
	              }  
	              System.out.println(filename);  			
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedLookAndFeelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
			File file = new File(filename);
			BufferedReader Linereader =null;
//			int degree;
//			int cntNum=0;
//			Point2D.Double[] cnt;
			try {
				System.out.println("read the file:");
				
				Linereader = new BufferedReader(new FileReader(file));
				 String eachLine = null;
				 int line = 1;
				 String eachCharacter = null;
				 
				 while ((eachLine=Linereader.readLine())!=null ) {
					if(eachLine.length()!=0){
//						System.out.println("line"+line+":"+eachLine);
						if(line==1){
							  degree = Integer.parseInt(eachLine);
//							System.out.println("degree="+degree);
						}
						else if (line==2) {
							  cntNum = Integer.parseInt(eachLine);
//							System.out.println("cntNum="+cntNum);
							 cnt = new Point2D.Double[cntNum];
							 knots = new double[cntNum+degree+1];

							}
						else if (line==3) {
							 String[] knotsLine = eachLine.split(",") ;// knotsLine[] ={} 
							 System.out.println(cntNum+"cntnum===="+"degree==="+degree+"knotsLine length"+knotsLine.length);
							 for(int ks=0;ks<cntNum+degree+1;ks++){
//								 System.out.println("ks====="+ks);
							 knots[ks]= Double.parseDouble(knotsLine[ks]);
//							 System.out.println("knots["+ks+"]===="+knots[ks]);
							 }
						}
						else {
							String[] cntS = eachLine.split(",");
							System.out.println(cntS[0]);
							Point2D.Double cntp=  new Point2D.Double(Double.parseDouble(cntS[0]), Double.parseDouble(cntS[1]));
							System.out.println(cntp);
							cnt[line-4] = cntp;
							
	 					}
					
						line++;
					 }
				}
				 Linereader.close();
				 window.btnSelectFile.setSelected(false);
			} catch (IOException e1) {
				// TODO: handle exception
	            e1.printStackTrace();

			}
		}
//===========================adaptive plot================
//		if (window.Adaptive.isSelected()) {
//			adp=1;
//		
//		}
//=======================sampling points=============
//		if (window.SamplingPoints.isSelected()) {
//			samplingPnt = 1;
//		}
//========================show control polygon=========
//		if (window.ControlPolygon.isSelected()) {
//			showCP = 1;
//			
//		}
//========================change tessNum=============
//		if (window.spinner.isFocusable()) {
//			double curr_tessNum = Double.parseDouble(window.spinner.getValue().toString());
//
//			if (curr_tessNum>100) {
//				tessNum = 100;
//			}
//			else if (curr_tessNum<5) {
//				tessNum = 5;
//			}else {
//				tessNum = curr_tessNum;
//			}
//		}
	}

}
