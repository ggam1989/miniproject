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
		System.out.println("â�ݴ���");
		e.getWindow().setVisible(false);//ȭ���� ����.â�������°�(�͹̳���Ʈ �Ȳ���)
		e.getWindow().dispose();//�޸𸮸� ������.

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
	class Win_c extends WindowAdapter {// ��� �ٰ�������(����)
		@Override
		public void windowClosing(WindowEvent e) {
			System.out.println("â�ݴ���");
			e.getWindow().setVisible(false);// ȭ���� ����.â�������°�(�͹̳���Ʈ �Ȳ���)
			e.getWindow().dispose();// �޸𸮸� ������.
		}
	}
	
	public class Event_c {
		public static void main(String[] args) {
			Frame f = new Frame("â �ݱ�");
			f.setBounds(0, 0, 300, 200);
//			f.addWindowListener(new Win_close());
			f.addWindowListener(new Win_c());
			
			f.setVisible(true);
		}
	}
