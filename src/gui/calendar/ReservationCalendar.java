package gui.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import database.ReservationDao;
import database.dbObjects.Room;
import gui.layout.main.reservation_window.ReservationLayout;

public class ReservationCalendar extends JFrame implements ActionListener {
	Container container = getContentPane();
	ReservationDao adminDao = new ReservationDao();
	ReservationLayout mainFrame;
	// 선택한 날짜를 저장함
	int clickCount = 0;

	JPanel bar = new JPanel();
	JButton lastMonth = new JButton("◀");

	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();

	JButton prevBtn = new JButton("◀");
	JButton nextBtn = new JButton("▶");

	JLabel label = new JLabel("0월 00년");

	JButton[] buttons = new JButton[49];
	String[] dayNames = { "일", "월", "화", "수", "목", "금", "토" };

	// private JLabel selectDateLabel = new JLabel("선택한 날짜");
	// private JTextField selectDateTextField = new JTextField(10);

	CalendarFunction cf = new CalendarFunction();

	public ReservationCalendar(ReservationLayout mainFrame) {
		this.mainFrame = mainFrame;
		setSize(500, 400);
		setLocation(400, 400);
		init();
		start();
		setVisible(true);

		for (int i = 7; i < buttons.length; i++) {
			buttons[i].addActionListener(this);
		}
	}

	private void init() {
		container.setLayout(new BorderLayout());
		container.add("North", panel1);
		container.add("Center", panel2);

		panel1.setLayout(new FlowLayout());

		panel1.add(prevBtn);
		panel1.add(label);
		panel1.add(nextBtn);

		Font font = new Font("SansSerif", Font.BOLD, 20);
		nextBtn.setFont(font);
		prevBtn.setFont(font);
		label.setFont(font);

//		 prevBtn.setEnabled(false);

		label.setText(cf.getCalText());

		panel2.setLayout(new GridLayout(7, 7, 5, 5));

		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton();
			panel2.add(buttons[i]);

			buttons[i].setForeground(new Color(0, 0, 0));
			buttons[i].setBackground(new Color(255, 255, 255));

			buttons[i].setFont(new Font("SansSerif", Font.BOLD, 12));

			// 요일 클릭 안되게
			if (i < 7) {
				buttons[i].setText(dayNames[i]);
				buttons[i].setEnabled(false);
			}

			// 일요일 빨간색
			if (i % 7 == 0) {
				buttons[i].setForeground(Color.RED);
			}

			// 토요일 파란색
			if (i % 7 == 6) {
				buttons[i].setForeground(Color.BLUE);
			}
		}

		cf.setButtons(buttons);
		cf.calSet();
	}

	// 이벤트 시작 메서드
	private void start() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		nextBtn.addActionListener(this);
		prevBtn.addActionListener(this);
	}

	// 체크인 날짜가 체크아웃 날짜보다 뒤면 안됨
	public int dayMinusCheck() {
		int result = 0;

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date chkInDate = format.parse(mainFrame.chkInDateTextField.getText());
			Date chkOutDate = format.parse(mainFrame.chkOutDateTextField.getText());

			if (chkOutDate.before(chkInDate)) {
				result = 1;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 체크인 날짜 선택을 오늘보다 전으로 선택 하면 안됨
	public int errorCheck() {
		Calendar currentDate = Calendar.getInstance();
//	      Date date = new Date();
		int result = 0;

		String selectDateStr = mainFrame.chkInDateTextField.getText();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		try {
			Date selectDate = format.parse(selectDateStr);

			if (selectDate != null) {
				currentDate.set(Calendar.HOUR_OF_DAY, 0);
				currentDate.set(Calendar.MINUTE, 0);
				currentDate.set(Calendar.SECOND, 0);
				currentDate.set(Calendar.MILLISECOND, 0);

//	            System.out.println("현재 날짜 : " + date);
//	            System.out.println("선택 날짜 : " + selectDate);
//	            System.out.println("-------------------------------");
//	            System.out.println("현재 날짜 : " + currentDate.getTime());
//	            System.out.println("선택 날짜 : " + selectDate);
				if (selectDate.before(currentDate.getTime()) && !selectDateStr.equals(cf.getToday())) {
					result = 1;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		for (int i = 7; i < buttons.length; i++) {

			if (e.getSource() == buttons[i] && !buttons[i].getText().isEmpty()) {
				// 날짜 yyyy-mm-dd로 문자열
				String selectDateStr = cf.year + "-" + cf.month + "-" + buttons[i].getText();
				Date selectDate = null;
				clickCount++;

				try {
					selectDate = dateFormat.parse(selectDateStr);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				if (selectDate != null) {
					if (clickCount % 2 == 1) {
						// 홀수 클릭 - 출발 날짜
						// 시작 날짜로 설정하고 텍스트필드에 업데이트
						mainFrame.chkInDateTextField.setText(dateFormat.format(selectDate));
						// 도착 날짜 초기화
						mainFrame.chkOutDateTextField.setText("");
						if (errorCheck() == 1) {
							JOptionPane.showMessageDialog(this, "당일보다 이전날짜는 선택 불가능합니다");
							mainFrame.chkInDateTextField.setText("");
							mainFrame.chkOutDateTextField.setText("");
							clickCount = 0;
						} else {
							showAvailableRooms(selectDateStr, null);
						}
					} else {
						mainFrame.chkOutDateTextField.setText(dateFormat.format(selectDate));
						if (dayMinusCheck() == 1) {
							JOptionPane.showMessageDialog(this, "불가능한 선택입니다");
							mainFrame.chkOutDateTextField.setText("");
							clickCount = 0;
						} else {
							showAvailableRooms(mainFrame.chkInDateTextField.getText(), selectDateStr);
						}
						dispose();
					}
				}

			}
		}

		int gap = 0;
		if (e.getSource() == nextBtn) { // 다음 버튼 클릭
			gap = 1;
		} else if (e.getSource() == prevBtn) { // 이전 버튼 클릭
			gap = -1;
		}

		cf.init(gap); // CalendarFunction의 init메서드를 호출해서 달력 업데이트
		label.setText(cf.getCalText()); // 년월 표시 라벨 갱신

	}

	private void showAvailableRooms(String checkInDate, String checkOutDate) {
		List<Room> availableRooms = adminDao.getAvailableCheckInRoom(checkInDate, checkOutDate);
	}

}
