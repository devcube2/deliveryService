package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import network.DSTask;

public class FranchiseView extends DSTask {
	public FranchiseView(Socket clientSocket, BufferedReader reader, PrintWriter writer) {
		this.clientSocket = clientSocket;
		this.reader = reader;
		this.writer = writer;
	}

	public void index() throws IOException {
		// TODO: 가맹여부 포함한 환영메시지 구현
		println("===== 가맹회원 페이지입니다. =====");
		println("1.가맹신청 2.등록메뉴 3.주문콜대기 4.콜리스트 5.로그아웃");
		int choose = nextInt(1, 5);
	}
}
