package view;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import controller.CustomerController;
import controller.MemberCommunicateController;
import network.DSTask;
import model.dto.MemberDto;
import model.dto.ShopDto;
import model.dto.ShopMenuDto;

public class CustomerView extends DSTask {
	private MemberDto loginMember;

	public CustomerView(Socket clientSocket, BufferedReader reader, PrintWriter writer, MemberDto loginMember) {
		this.clientSocket = clientSocket;
		this.reader = reader;
		this.writer = writer;
		this.loginMember = loginMember;
	}

	public MemberDto getLoginMember() {
		return loginMember;
	}

	public String getLoginId() {
		return loginMember.getId();
	}

	public int getLoginMno() {
		return loginMember.getMno();
	}

	public void index() throws IOException {
		while (true) {
			print("\r\n┌──────────────────────── 일반회원 페이지 ──────────────────────┐\r\n");
			print("\r\n1.배달음식검색 2.로그아웃 : ");			
			int choose = nextInt(1, 3);

			if (choose == 1) {
				search();
			} else if (choose == 2) {
				logout(getLoginId());
				break;
			}
		}
	}

	private void search() throws IOException {
		printf("\r\n음식명 입력 : ");

		// 1. 메뉴(음식명)를 가진 가맹점 리스트 출력 및 선택
		ShopDto shopDto = choiceShop(next());
		if (shopDto == null) {
			return;
		}

		// 2. 가맹점 메뉴 출력 및 선택
		ShopMenuDto shopMenuDto = choiceMenu(shopDto);
		if (shopMenuDto == null) {
			return;
		}

		// 3. 선택 메뉴 주문
		if (orderMenu(shopMenuDto)) {
			String franId = CustomerController.getInstance().selectMid(shopMenuDto.getEno());			
			if (franId != null) {
				
				MemberCommunicateController.getInstance().printf(franId,
						"\r\n┌──────────────────────────────────────────────────┐\r\n" +
						"│ 주문 들어왔습니다!!                              │\r\n" +
						"│ 주문자: %s" + "│\r\n" +
						"│ 주문점: %s" + "│\r\n" +
						"│ 주문메뉴: %s" + "│\r\n" +
						"│ 주문가격: %s" + "│\r\n" +
						"└──────────────────────────────────────────────────┘\r\n"
						, a.convert(getLoginId(), 41) , a.convert(shopMenuDto.getEname(), 41) , 
						a.convert(shopMenuDto.getMename(), 39), a.convert(shopMenuDto.getMeprice()+"", 39));
				
			}
		}
	}

	private ShopDto choiceShop(String menu) throws IOException {
		// 메뉴 판매하는 가맹점 조회
		ArrayList<ShopDto> shopList = CustomerController.getInstance().searchShop(menu, getLoginMno());

		if (shopList.size() == 0) {
			printf("'%s' 메뉴를 판매중인 가맹점이 없습니다.\r\n", menu);
			return null;
		}		

		// 메뉴 판매하는 가맹점 출력
		print("\r\n┌─────┬────────────────────┬────────────────────┐\r\n");
		print("│" + a.convert("번호" , 5) + "│" + a.convert("지점명" , 20) +
				"│" + a.convert("지점위치" , 20) + "│\r\n" );
		int i;	int count = 0;
		for (i = 0; i < shopList.size(); i++) {
			count++;
			print("├─────┼────────────────────┼────────────────────┤\r\n");
			print("│" + a.convert( (i+1)+"" , 5 ) ); 
			print("│" + a.convert( shopList.get(i).getEname() , 20 ) );
			print("│" + a.convert( shopList.get(i).getEspot() , 20 ) +  "│\r\n");
		}
		
		if( shopList.size() == count ) {
			print("└─────┴────────────────────┴────────────────────┘\r\n");
		} // if end
		
		printf("%d. 뒤로가기\r\n", ++i);

		print("\r\n번호 선택 : ");
		int choose = nextInt(1, i);
		if (choose == i) { // 뒤로가기 선택했으면
			return null;
		}

		return shopList.get(choose - 1); // ArrayList 의 인덱스는 0부터 시작이기 때문에 index 에 -1 해준다.
	}

	private ShopMenuDto choiceMenu(ShopDto shopDto) throws IOException {
		ArrayList<ShopMenuDto> shopMenuList = CustomerController.getInstance().searchShopMenu(shopDto.getEno());

		if (shopMenuList.size() == 0) {
			// 사실 이 로직은 탈 수 없다. 위에서 가맹점 검색시 menu 테이블과 join 하여 확인하였기 때문이다.
			// 다만 혹시 모르기 때문에 넣은 코드이다.
			print("메뉴가 없습니다.\r\n");
			return null;
		}		

		// 해당 가맹점 메뉴 출력
		print("\r\n┌─────┬────────────────────┬────────────────────┐\r\n");
		print("│" + a.convert("번호" , 5) + "│" + a.convert("메뉴명" , 20) +
				"│" + a.convert("메뉴가격" , 20) + "│\r\n" );
		
		int i;	int count = 0;
		for (i = 0; i < shopMenuList.size(); i++) {
			count++;
			print("├─────┼────────────────────┼────────────────────┤\r\n");
			print("│" + a.convert( (i+1)+"" , 5 ) ); 
			print("│" + a.convert( shopMenuList.get(i).getMename() , 20 ) );
			print("│" + a.convert( shopMenuList.get(i).getMeprice()+"" , 20 ) +  "│\r\n");
		}
		
		if( shopMenuList.size() == count ) {
			print("└─────┴────────────────────┴────────────────────┘\r\n");
		} // if end

		printf("%d. 첫화면가기\r\n", ++i);
		
		print("\r\n번호 선택 : ");
		int choose = nextInt(1, i);
		if (choose == i) { // 첫화면가기 선택했으면
			return null;
		}

		// 선택한 메뉴 객체 리턴
		return shopMenuList.get(choose - 1);
	}

	private boolean orderMenu(ShopMenuDto shopMenuDto) throws IOException {
		if (!CustomerController.getInstance().orderMenu(shopMenuDto, getLoginMno())) {
			print("** 주문 실패 **\r\n");
			return false;
		}
		print("\r\n주문 완료되었습니다.\r\n");
		return true;
	}
}
