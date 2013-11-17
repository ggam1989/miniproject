package net.project.mini.listener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowListener extends WindowAdapter {
	@Override
	public void windowClosing(WindowEvent e) {
		e.getWindow().setVisible(false);//화면을 끈다. 창만꺼지는거(터미네이트 안꺼짐)
		e.getWindow().dispose();// 메모리를 날린다.
	}
}
