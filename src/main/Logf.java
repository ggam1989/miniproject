package main;


import java.awt.Dialog;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

//�α���������
public class Logf {
	Logf pp = this;
	TextField tid, tpw;
	JFrame lf;
	JLabel Id_1lb, Pw_1lb;
	JButton btn1, btn2, btn3;

	public Logf() {
		lf = new JFrame("ȸ������");
		lf.setBounds(500, 490, 440, 290);
		lf.setLayout(null);

		Id_1lb = new JLabel("ID : ");
		Id_1lb.setBounds(80, 50, 50, 30);
		lf.add(Id_1lb);

		tid = new TextField(10);
		tid.setBounds(150, 50, 200, 30);
		lf.add(tid);

		Pw_1lb = new JLabel("PW : ");
		Pw_1lb.setBounds(80, 80, 50, 30);
		lf.add(Pw_1lb);

		tpw = new TextField(10);
		tpw.setBounds(150, 80, 200, 30);
		tpw.setEchoChar('*');
		lf.add(tpw);

		btn1 = new JButton("�α���");
		btn1.setBounds(70, 120, 80, 50); // ��ư ��ġ ����.
		lf.add(btn1);

		btn2 = new JButton("���̵� / ��й�ȣã��");
		btn2.setBounds(170, 120, 180, 50); // ��ư ��ġ ����.
		lf.add(btn2);

		btn3 = new JButton("ȸ������");
		btn3.setBounds(70, 180, 280, 30);
		lf.add(btn3);

		lf.setVisible(true);

		btn1.addActionListener(new Loginf(pp));
		btn3.addActionListener(new Register(pp));

	}

	// �α��ι�ư(btn1)Ŭ����.
	class Loginf implements ActionListener {

		Logf pp;
		Dialog dd;
		Label la = new Label();
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		String id, pw;
		
		public Loginf(Logf pp) {
			this.pp = pp;
		}

		public void actionPerformed(ActionEvent e) {

			map.put("aaa", new String[] { "11", "����" });
			map.put("bbb", new String[] { "22", "����" });
			map.put("ccc", new String[] { "33", "�ٴ�" });
			map.put("ddd", new String[] { "44", "���" });
			map.put("eee", new String[] { "55", "����" });

			id = pp.tid.getText();
			pw = tpw.getText();
			tpw.setText("");

			dd = new Dialog(pp.lf,"����ġ!",true);

			if (!map.containsKey(id)) {
				// System.out.println("�������� �ʴ� ID �Դϴ�.");
				dd.setBounds(100, 100, 200, 150);
				la.setText("���̵� �������� �ʽ��ϴ�.");
				dd.add(la);

				dd.addWindowListener(new Win_c());
				dd.setVisible(true);

				tid.setText("");
				tid.requestFocus();
			} else if (!map.get(id)[0].equals(pw)) {
				dd.setBounds(100, 100, 200, 150);

				la.setText("��ȣ�� ��ġ���� �ʽ��ϴ�.");
				dd.add(la);

				dd.addWindowListener(new Win_c());
				dd.setVisible(true);
				tpw.requestFocus();

			} else {
				// setVisible(false);
				new Client(pp);
			
		}
	}
	}
	public static void main(String[] args) {

		new Logf();
		// Register rg = new Register();
	}
}