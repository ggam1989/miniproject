package main;


import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class Win_close implements WindowListener {
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("창닫는중");
		e.getWindow().setVisible(false);//화면을 끈다.창만꺼지는거(터미네이트 안꺼짐)
		e.getWindow().dispose();//메모리를 날린다.

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowOpened(WindowEvent e) {
	};

}
	class Win_c extends WindowAdapter {// 기능 다갖구잇음(위에)
		@Override
		public void windowClosing(WindowEvent e) {
			System.out.println("창닫는중");
			e.getWindow().setVisible(false);// 화면을 끈다.창만꺼지는거(터미네이트 안꺼짐)
			e.getWindow().dispose();// 메모리를 날린다.
		}
	}
	
	public class Event_c {
		public static void main(String[] args) {
			Frame f = new Frame("창 닫기");
			f.setBounds(0, 0, 300, 200);
//			f.addWindowListener(new Win_close());
			f.addWindowListener(new Win_c());
			
			f.setVisible(true);
		}
	}
