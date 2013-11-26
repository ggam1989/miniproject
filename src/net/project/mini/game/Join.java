package net.project.mini.game;

import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import db.GameDAO;
import db.GameVo;

// 회원가입 프레임
class Join implements ActionListener, ItemListener {
 JFrame frame;
 Font font;
 JLabel pw, lb, lbs, name, id, pw_re, emailLabel_1, email_la, phone, itl,
   nick;
 TextField pwt, tf, email_t_1, phone2, phone3, pwt_re, email_tf, itt,
   nick_n;
 TextField id_t;
 // 이름 : tf, 아이디 : id_t, 비밀번호 : pwt, 비밀번호2 : pwt_re, 2차비번 : itt
 // 이메일 : email_t_1, 도메인email_tf, 핸드폰 : phone2, phone3,
 CheckboxGroup cg;
 JButton b, b1, b2; // b : 아이디중복확인 , b1 : 회원가입 , b2 : 취소
 Choice email, phone1;

/* public Join() {

 }*/
 public void actionPerformed(ActionEvent e) {
  frame = new JFrame();
  frame.setBounds(300, 300, 600, 600);
  frame.setLayout(null);
  frame.setResizable(false);
  
  lb = new JLabel("L O G I N");
  font = new Font("Serif", Font.BOLD + Font.ITALIC, 25);
  lb.setFont(font);
  lb.setForeground(new Color(255, 0, 0));
  lb.setBounds(30, 40, 150, 40);
  lbs = new JLabel(" * 표기란을 모두 기재해주셔야 가입이 됩니다.");
  font = new Font("Serif", Font.BOLD, 15);
  lbs.setFont(font);
  lbs.setForeground(new Color(0, 0, 255));
  lbs.setBounds(180, 45, 400, 40);

  pw_re = new JLabel("* 비번확인:");
  name = new JLabel("* 이름");
  tf = new TextField();// 이름
  cg = new CheckboxGroup();
  nick = new JLabel("닉 네임 ");
  nick_n = new TextField();// 닉네임
  id = new JLabel("* 아이디:");
  id_t = new TextField();
  b = new JButton(" 아이디 중복 확인");
  pw = new JLabel("* 비밀번호:");
  pwt = new TextField("");
  pwt.setEchoChar('*');

  itl = new JLabel("* 2차비밀번호(아이디/비번찾기) ");

  pwt_re = new TextField("");
  pwt_re.setEchoChar('*');

  itt = new TextField("");
  itt.setEchoChar('*');
  email_la = new JLabel(" E-mail입력 :");
  email_tf = new TextField();
  emailLabel_1 = new JLabel("@");
  email_t_1 = new TextField();
  phone = new JLabel("* 폰 번호 :");
  phone1 = new Choice();
  phone1.add("010");
  phone1.add("011");
  phone1.add("019");
  phone2 = new TextField();
  phone3 = new TextField();
  b1 = new JButton("작성완료");
  email = new Choice();
  email.add("Choice");
  email.add("naver.com");
  email.add("daum.net");
  email.add("google.com");
  b2 = new JButton("취소");

  name.setBounds(20, 100, 80, 50);
  tf.setBounds(140, 110, 80, 20);
  nick.setBounds(250, 100, 50, 40);
  nick_n.setBounds(300, 108, 80, 20);
  id.setBounds(20, 150, 80, 50);
  id_t.setBounds(140, 160, 80, 20);
  b.setBounds(230, 160, 160, 20);
  pw.setBounds(20, 200, 80, 50);
  pw_re.setBounds(20, 260, 80, 50);
  itl.setBounds(300, 215, 200, 20);

  pwt.setBounds(140, 215, 130, 20);
  pwt_re.setBounds(140, 270, 130, 20);
  itt.setBounds(300, 240, 100, 20);

  email_la.setBounds(20, 300, 80, 50);
  email_tf.setBounds(140, 310, 100, 20);
  emailLabel_1.setBounds(240, 310, 20, 20);
  email_t_1.setBounds(260, 310, 100, 20);
  phone.setBounds(20, 350, 80, 50);
  phone1.setBounds(140, 360, 50, 20);
  phone2.setBounds(200, 360, 70, 20);
  phone3.setBounds(280, 360, 70, 20);
  email.setBounds(360, 310, 150, 50);
  b1.setBounds(180, 450, 100, 20);
  b2.setBounds(320, 450, 100, 20);
  
  frame.add(lb);
  frame.add(lbs);
  frame.add(name);
  frame.add(tf);
  frame.add(nick);
  frame.add(nick_n);
  frame.add(id);
  frame.add(id_t);
  frame.add(b);
  frame.add(pw);
  frame.add(pw_re);
  frame.add(itl);
  frame.add(pwt);
  frame.add(pwt_re);
  frame.add(itt);
  frame.add(email_la);
  frame.add(email_tf);
  frame.add(emailLabel_1);
  frame.add(email_t_1);
  frame.add(phone);
  frame.add(phone1);
  frame.add(phone2);
  frame.add(phone3);
  frame.add(email);
  frame.add(b1);
  frame.add(b2);
  email.addItemListener(this);
  
  frame.setVisible(true);
  b1.setEnabled(false);
  
  b.addActionListener(new selectId(this));
  b1.addActionListener(new insertOne(this));
  b2.addActionListener(new ActionListener() {

   @Override
   public void actionPerformed(ActionEvent e) {
    if (e.getSource() == b2)
     frame.setVisible(false);
   }

  });
  
  // b : 아이디중복확인 , b1 : 회원가입 , b2 : 취소
 }

 @Override
 public void itemStateChanged(ItemEvent e) {

  // if()// 해당 아이템이 선택되었다면

  String a = (String) e.getItem();
  email_t_1.setText(a);

 }
}

class selectId implements ActionListener {
 Join j;
 GameDAO gd;
 GameVo vo;

 JDialog dl1 = new JDialog();
 JDialog dl2 = new JDialog();
 JDialog dl3 = new JDialog();

 JLabel lb1, lb2, lb3;
 Font font;

 public selectId(Join j) {
  this.j = j;
 }

 @Override
 public void actionPerformed(ActionEvent e) {

  String str1 = j.id_t.getText();

  if (str1.equals("")) {
   font = new Font("Serif", Font.BOLD, 20);
   lb3 = new JLabel("ID를 입력하세요.");
   j.id_t.requestFocus();
   lb3.setFont(font);
   dl3.setBounds(100, 100, 400, 150);
   dl3.add(lb3);
   j.b1.setEnabled(false);
   dl3.setVisible(true);
  } else {
   gd = new GameDAO();

   vo = gd.selectTwo(str1);
   if (vo.getId() == null) {// == 은 주소를 비교
    font = new Font("Serif", Font.BOLD, 20);
    lb2 = new JLabel("입력하신 ID를 사용하셔도 좋아요");
    lb2.setFont(font);
    dl2.setBounds(100, 100, 400, 150);
    dl2.add(lb2);
    j.b1.setEnabled(true);
    dl2.setVisible(true);
    System.out.println(vo.getId());
    System.out.println(str1);
   } else if (vo.getId() != null) { // equals는 값을 비교
    System.out.println(vo.getId());
    System.out.println(str1);
    font = new Font("Serif", Font.BOLD, 20);
    lb1 = new JLabel("입력하신 ID는 중복된 ID 입니다.");
    j.id_t.setText("");
    j.id_t.requestFocus();
    lb1.setFont(font);
    dl1.setBounds(100, 100, 400, 150);
    dl1.add(lb1);
    j.b1.setEnabled(false);
    dl1.setVisible(true);
   }
  }
 }
}

class insertOne implements ActionListener {
 Join j;
 GameDAO gd;

 // ArrayList<GameVo> list = new ArrayList<GameVo>();
 public insertOne(Join j) {
  this.j = j;
 }

 @Override
 public void actionPerformed(ActionEvent e) {
  gd = new GameDAO();
  GameVo vo = new GameVo();
  Font font;
  JLabel lb1, lb2;
  JDialog dl1 = new JDialog();
  JDialog dl2 = new JDialog();

  if (j.tf.getText().equals("") || j.id_t.getText().equals("")
    || j.pwt.getText().equals("") || j.pwt_re.getText().equals("")
    || j.itt.getText().equals("")
    || j.email_tf.getText().equals("")
    || j.email_t_1.getText().equals("")
    || j.phone2.getText().equals("")
    || j.phone3.getText().equals("")) {
   font = new Font("Serif", Font.BOLD, 20);
   lb1 = new JLabel("필수입력 사항을 모두 입력하세요.");
   j.id_t.setText("");
   j.id_t.requestFocus();
   lb1.setFont(font);
   dl1.setBounds(100, 100, 400, 150);
   dl1.add(lb1);
   dl1.setVisible(true);
  } else {
   vo.setName(j.tf.getText());
   vo.setId(j.id_t.getText());
   vo.setPw(j.pwt.getText());
   vo.setPw2(j.itt.getText());
   vo.setEmail(j.email_tf.getText() + "@" + j.email_t_1.getText());
   vo.setPhone(j.phone1.getSelectedItem() + j.phone2.getText()
					+ j.phone3.getText());
			vo.setNick(j.nick_n.getText());
   vo.setScore(0);
   vo.setLevel("약자");
   font = new Font("Serif", Font.BOLD, 20);
   lb2 = new JLabel("회원가입 성공");
   j.id_t.setText("");
   j.id_t.requestFocus();
   lb2.setFont(font);
   dl2.setBounds(100, 100, 400, 150);
   dl2.add(lb2);
   dl2.setVisible(true);
   gd.insertOne(vo);
   j.frame.dispose();
  }
 }
}

