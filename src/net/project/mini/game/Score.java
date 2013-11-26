package net.project.mini.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

//client;
import db.GameDAO;
import db.GameVo;
import db.LogInfo;

public class Score extends JFrame {

	JFrame f;
	TextArea rt, mrt;
	JButton bye; // client;
	JLabel mscore_lb, evscore_lb, evname, mname;
	JLabel ilb, nlb, jlb, llb; 		// every 라벨 ( ilb:아이디, nlb:닉네임, jlb:스코어,llb:레벨 )
	JLabel ilb2, nlb2, jlb2, llb2;  // my 라벨
	JLabel evscore;
	GameDAO gd;
	ArrayList<GameVo> al;

	GameVo gv;
	GameVo vo;

	// client cl;
	
	public Score() {

		gd = new GameDAO();
		al = gd.Select_Lg_All();

		f = new JFrame("Score");
		
		f.setBounds(200, 200, 320, 520);
		f.setBackground(new Color(255, 0, 0));
		f.setLayout(null);
		f.setResizable(false);

		bye = new JButton("종료");
		rt = new TextArea(20, 50);
		mrt = new TextArea(15, 40);

		evscore_lb = new JLabel("Score");	//메인 라벨
		
		evscore = new JLabel("Every Score");
		mscore_lb = new JLabel("My Score");
		
		evname = new JLabel("Name");
		ilb = new JLabel("ID");
		nlb = new JLabel("Nick");
		jlb = new JLabel("Score");
		llb = new JLabel("Level");
		
		mname = new JLabel("Name");
		ilb2 = new JLabel("ID");
		nlb2 = new JLabel("Nick");
		jlb2 = new JLabel("Score");
		llb2 = new JLabel("Level");
		

		bye.setBounds(100, 400, 80, 30);//종료
		rt.setBounds(0, 100, 300, 150);//모두의 TextArea
		mrt.setBounds(0, 320, 300, 50);//나의 TextArea
		
		evscore_lb.setBounds(110, 20, 100, 30);
		evscore.setBounds(5, 30, 100, 50);
		mscore_lb.setBounds(5, 250, 100, 50);
		
		evname.setBounds(10, 60, 50, 50);
		ilb.setBounds(60, 60, 50, 50);
		nlb.setBounds(100, 60, 50, 50);
		jlb.setBounds(150, 60, 50, 50);
		llb.setBounds(200,60,50,50);
		
		mname.setBounds(10, 280, 50, 50);
		ilb2.setBounds(60, 280, 50, 50);
		nlb2.setBounds(100, 280, 50, 50);
		jlb2.setBounds(150, 280, 50, 50);
		llb2.setBounds(200,280,50,50);
		
		
		evscore_lb.setFont(new Font("Serif", Font.BOLD, 30));
		evscore.setFont(new Font("Serif", Font.BOLD, 18));
		mscore_lb.setFont(new Font("Serif", Font.BOLD, 18));
		
		evname.setFont(new Font("Serif", Font.BOLD, 15));
		ilb.setFont(new Font("Serif", Font.BOLD, 15));
		nlb.setFont(new Font("Serif", Font.BOLD, 15));
		jlb.setFont(new Font("Serif", Font.BOLD, 15));
		llb.setFont(new Font("Serif", Font.BOLD, 15));
		
		mname.setFont(new Font("Serif", Font.BOLD, 15));
		ilb2.setFont(new Font("Serif", Font.BOLD, 15));
		nlb2.setFont(new Font("Serif", Font.BOLD, 15));
		jlb2.setFont(new Font("Serif", Font.BOLD, 15));
		llb2.setFont(new Font("Serif", Font.BOLD, 15));

		f.add(bye);
		f.add(rt);
		f.add(mrt);
		
		f.add(evscore_lb);
		f.add(evscore);
		f.add(mscore_lb);

		f.add(evname);
		f.add(ilb);
		f.add(nlb);
		f.add(jlb);
		f.add(llb);
		
		f.add(mname);
		f.add(ilb2);
		f.add(nlb2);
		f.add(jlb2);
		f.add(llb2);

		
		Iterator<GameVo> it = al.iterator();
		while (it.hasNext()) {
			gv = it.next();
			System.out.println(gv.getId());
			System.out.println(gv.getNick());
			System.out.println(gv.getScore());
			rt.append(gv.getName() + "\t" + gv.getId() + "\t" + gv.getNick()
					+ "\t" + gv.getScore() + "\t" + gv.getLevel() + "\t" + "\n");
		}
		
		vo = gd.Select_Lg_Me(LogInfo.getId());

		mrt.append(vo.getName() + "\t" + vo.getId() + "\t" + vo.getNick()
				+ "\t" + vo.getScore() + "\t" + vo.getLevel() + "\t" + "\n");

		bye.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// System.out.println(gv.getId());
				f.setVisible(false);
				f.dispose();
			}
		});

		f.setVisible(true);

	}
}
