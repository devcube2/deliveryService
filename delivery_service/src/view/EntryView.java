package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import controller.EntryController;
import controller.RoadAddressController;
import model.dao.Dao;
import model.dto.EntryDto;
import model.dto.MemberDto;
import model.dto.RoadAddressDto;
import network.DSTask;

public class EntryView extends DSTask{
	private MemberDto loginMember;
	
	// 접속된 클라이언트 네트워크 소켓을 받아서 그대로 사용하기 위한 생성자
	public EntryView(Socket clientSocket, BufferedReader reader, PrintWriter writer, MemberDto loginMember) {
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
	
//	메소드	
//	1. 입점 신청
	public void entryJoin() throws IOException {
		println("\n==================     입점신청     ==================");
		print("상호명 : ");			String ename = next();
		print("지점명 : ");			String espot = next();
		
		print("도로명주소 검색: ");
		RoadAddressDto roadAddress = choiceRoadAddress(next());
		print("상세주소: ");
		roadAddress.setDetailAddress(next());
		
		EntryDto entryDto = new EntryDto();
		entryDto.setEname(ename);
		entryDto.setEspot(espot);
		entryDto.setLogInMno(getLoginId());
		
		boolean result = EntryController.getInstance().entryJoin( entryDto , roadAddress);
		if( result ) { println( "입점신청이 완료되었습니다." ); }
		else { println( "입점신청 실패" ); }
		
	} // f end
	
//	2. 입점목록 리스트
	public void entryList() throws IOException {
		ArrayList<EntryDto> result = EntryController.getInstance().enrtyList();
		
		println("\n==================     지점선택     ==================");
		printf("%-5s%-15s%-20s%-10s\r\n " , "지점번호" , "상호명" , "지점명" , "입점상태" );
		for( int i = 0 ; i < result.size() ; i++ ) {
			EntryDto entryDto = result.get(i);
			if( entryDto.getMno() == Dao.getInstance().selectMno(getLoginId()) ) {
				String eType = null;
				if( entryDto.getEtype() == 0 ) { eType = "미승인"; }
				else if( entryDto.getEtype() == 1 ){ eType = "승인"; }
				printf("%-5s%-15s%-20s%-10s\r\n" , 
						entryDto.getEno() , 
						entryDto.getEname() ,
						entryDto.getEspot() , 
						eType
						);
			}
		} // for end
		
	} // f end
	
//	3. 메뉴 등록 페이지
	public void menuIndex() throws IOException {
			entryList();
			print("\n지점번호 선택 : "); 
			int eno = nextInt();
			while( true ) {
				println("\n==================     메뉴 페이지     ==================");
				print("\n1.메뉴등록 2.메뉴수정 3.메뉴삭제 4.뒤로가기 ");
				int choose = nextInt(1,4);
				switch( choose ) {
				case 1: 
					write(eno);
					break;
				case 2: 
					update(eno);
					break;
				case 3:
					delete(eno);
					break;
				case 4:
					return;
				} // switch end
				
			} // w end
		
	} // f end
	
//	4. 메뉴 리스트
	public ArrayList<Integer> menuList( int eno ) throws IOException {
		ArrayList<EntryDto> result = EntryController.getInstance().menuList();
		ArrayList<Integer> arr = new ArrayList<>();
		
		printf("%-5s%-15s%-20s%-10s\r\n", "번호", "카테고리", "메뉴명", "메뉴가격");
		int count = 1;
		for( int i = 0 ; i < result.size() ; i++ ) {
			EntryDto entryDto = result.get(i);
			if( entryDto.getEno() == eno ) {
				arr.add(entryDto.getMeno());
				printf("%-5d%-15s%-20s%-10d\r\n", 
						count , 
						entryDto.getCname() ,
						entryDto.getMename() , 
						entryDto.getMeprice()
						);
				count++;
			} // if end
		} // for end
		return arr;
		
	} // f end
	
//	5. 메뉴번호 호출 메소드
	public int meno( int eno ) throws IOException{
		ArrayList<Integer> arr = menuList(eno);
		print("메뉴번호 선택 : "); 
		int mIndex = nextInt();
		int result = arr.get(mIndex-1);
		return result;
	} // f end
	
//	6. 카테고리 리스트
	public void cList() throws IOException{
		println("\n==================     카테고리     ==================");
		printf("%-5s%-15s\r\n", "번호", "카테고리명");
		
		ArrayList<EntryDto> result = EntryController.getInstance().cList();
		for( int i = 0 ; i < result.size() ; i++ ) {
			EntryDto entryDto = result.get(i);
				printf("%-5d%-15s\r\n", 
						entryDto.getCno() ,
						entryDto.getCname()
						);
		} // for end
		
	} // f end
	
//	7. 메뉴등록
	public void write( int eno ) throws IOException {
		println("\n==================     메뉴등록     ==================");
		cList();
		print("카테고리 번호 : ");		int cno = nextInt();
		print("메뉴명 : ");			String mename = next();
		print("메뉴 가격 : ");		int meprice = nextInt();
		EntryDto entryDto = new EntryDto( mename , meprice , cno , eno );
		
		boolean result = EntryController.getInstance().write( entryDto );		
		if( result ) { println("메뉴등록이 완료되었습니다."); } 
		else { println("메뉴등록 실패"); } 
	} // f end
	
//	8. 메뉴수정
	public void update( int eno ) throws IOException {
		println("\n==================     메뉴수정     ==================");
		int meno = meno( eno );
		cList();
		print("카테고리 번호 : ");		int cno = nextInt();
		print("메뉴명 : ");			String mename = next();
		print("메뉴 가격 : ");		int meprice = nextInt();
		EntryDto entryDto = new EntryDto( mename , meprice , cno , eno );
		
		println("\n정말 수정하시겠습니까?");
		print("1. 예 2. 아니요 ");
		int choose = nextInt();
		if( choose == 2 ) { return; }
		boolean result = EntryController.getInstance().update(meno , entryDto);
		if( result ) { println("메뉴수정이 완료되었습니다."); } 
		else { println("메뉴수정 실패"); }
		
	} // f end
	
//	9. 메뉴삭제
	public void delete( int eno ) throws IOException {
		println("\n==================     메뉴삭제     ==================");
		int meno = meno( eno );
		println("\n정말 삭제하시겠습니까?");
		print("1. 예 2. 아니요 ");
		int choose = nextInt();
		if( choose == 2 ) { return; }
		boolean result = EntryController.getInstance().delete(meno);
		if( result ) { println("메뉴삭제가 완료되었습니다."); } 
		else { println("메뉴삭제 실패"); }
	} // f end
	
	
//	주소 불러오기 api
	private RoadAddressDto choiceRoadAddress(String keyword) throws IOException {
		RoadAddressDto roadAddress;
		
		roadAddress = choiceRoadAddressInter(keyword);
		if (roadAddress != null) return roadAddress;
		
		// 올바른 검색 주소 입력할때까지 무한 루프
		while ( (roadAddress = choiceRoadAddressInter(next())) == null) {			
		}
		
		
		return roadAddress;
	}

	
	
	private RoadAddressDto choiceRoadAddressInter(String keyword) throws IOException {
		ArrayList<RoadAddressDto> roadAddressList = RoadAddressController.getInstance().getRoadAddress(keyword);
		
		if (roadAddressList.size() == 0) {
			print("도로명 주소가 없습니다. 검색어를 다시 입력해주세요 : ");
			return null;
		}

		println("----- 검색된 주소 확인후 맞는 주소 번호 선택해주세요.");
		for (int i = 0; i < roadAddressList.size(); i++) {
			println(String.format("(%d) 주소", i + 1));
			println(String.format("우편번호: %s", roadAddressList.get(i).getZipCode()));
			println(String.format("도로명 주소: %s", roadAddressList.get(i).getRoadAddress()));
			println(String.format("지번 주소: %s", roadAddressList.get(i).getJibunAddress()));
		}		

		print(": ");
		int choose = nextInt(1, roadAddressList.size());

		// 선택한 도로명 주소 DTO 리턴
		return roadAddressList.get(choose - 1); // ArrayList 인덱스는 0부터 시작하므로 choose 값에 -1 해준다.
	}
	
}
