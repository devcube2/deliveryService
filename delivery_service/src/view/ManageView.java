package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import controller.EntryController;
import controller.ManageController;
import model.dao.ManageDao;
import model.dto.EntryDto;
import network.DSTask;

public class ManageView extends DSTask{
	public ManageView(Socket clientSocket , BufferedReader reader, PrintWriter writer) {
		this.clientSocket = clientSocket;
		this.reader = reader;
		this.writer = writer;	
	}
	
//	1. 입점목록 ( type = 0 (미승인) , type = 1 ( 승인 )
	public void entryList( int type ) throws IOException {
		ArrayList<EntryDto> result = EntryController.getInstance().enrtyList();
		
//      승인 상태 구분
		String eType = null;
		if( type == 0 ) { eType = "미승인"; }
		else if( type == 1 ) { eType = "승인";  }
		
//		유효성 검사
		if( result.size() == 0 ) { 
			println("승인 대기중인 지점이 없습니다.");
		} // if end
		
		
		println("지점번호     상호명     지점명     입점상태" );
		for( int i = 0 ; i < result.size() ; i++ ) {
			EntryDto entryDto = result.get(i);
			if( entryDto.getEtype() == type ) {
				print(entryDto.getEno() + "     "); 
				print(entryDto.getEname() + "     ");
				print(entryDto.getEspot() + "     ");
				println(eType + "     ");
			}
		} // for end
	} // f end
//	
////	2. 승인 입점목록
//	public void entryList() throws IOException {
//		ArrayList<EntryDto> result = EntryController.getInstance().enrtyList();
//		
//		println("지점번호     상호명     지점명     입점상태" );
//		for( int i = 0 ; i < result.size() ; i++ ) {
//			EntryDto entryDto = result.get(i);
//			if( entryDto.getEtype() == 1 ) {
//				String eType = "승인";
//				print(entryDto.getEno() + "     "); 
//				print(entryDto.getEname() + "     ");
//				print(entryDto.getEspot() + "     ");
//				println(eType + "     ");
//			}
//		} // for end
//		
//	} // f end
	
//	3. 입점신청 목록 페이지
	public void entry( int type ) throws IOException {
		while(true) {
			if( type == 0 ) {
				println("\n==================     입점신청 목록     ==================");
			}else if( type == 1) {
				println("\n==================     입점 목록     ==================");
			}
			entryList( type );
			
			print("\r\n1.번호선택 2.뒤로가기 ");
			int choose = nextInt(1,2);
			
			switch( choose ) {
				case 1:
					if( type == 0 ) { entryChoice( 0 ); }
					if( type == 1 ) { entryChoice( 1 ); }
					break;
				default:
					return;
			} // s end
		} // w end
		
	}// f end
	
////	4. 입점목록 페이지
//	public void entry() throws IOException {
//		while(true) {
//			
//			entryList( 1 );
//			
//			print("\r\n1.번호선택 2.뒤로가기 ");
//			int choose = nextInt(1,2);
//			
//			switch( choose ) {
//				case 1:
//					entryChoice();
//					break;
//				default:
//					return;
//			} // s end
//		} // w end
//		
//	}// f end
	
//	5. 입접신청목록 번호선택 페이지
	public void entryChoice( int type ) throws IOException {
		
		while( true ) {
			print("\n번호선택 : ");
			int eIndex = nextInt();
			
//			유효성검사
			if( check(eIndex, type) ) {	continue; }
			
			if( type == 0 ) {
				print("\n1.입점 승인 2.입점 거절 3.뒤로가기");
			}else if( type == 1 ) {
				print("\n1.입점수정 2.입점삭제 3.뒤로가기");
			}
			int choose = nextInt(1,3);
			
			switch( choose ) {
			case 1:
				if( type == 0 ) { entryApproval(eIndex); }
				else if( type == 1 ) { update(eIndex);; }
				break;
			case 2:
				if( type == 0 ) { entryRefusal(eIndex); }
				else if( type == 1 ) { delete(eIndex); }
				break;	
			} // s end
			return;
		} // w end
		
	} // f end
	
////	6. 입접목록 번호선택 페이지
//	public void entryChoice() throws IOException {
//		
//		while( true ) {
//			print("\n번호선택 : ");
//			int eIndex = nextInt();
//			print("\n1.입점수정 2.입점삭제 3.뒤로가기");
//			int choose2 = nextInt(1,3);
//			
//			switch( choose2 ) {
//			case 1:
//				update( eIndex );
//				break;
//			case 2:
//				delete( eIndex );
//				break;	
//			} // s end
//			return;
//		} // w end
//		
//	} // f end
	
//	7. 입점승인
	public void entryApproval( int eIndex ) throws IOException {
		println("\n입점 승인하시겠습니까?");
		print("1.예 2.아니오 ");
		int choose = nextInt(1,2);
		switch(choose) {
			case 1:
				boolean result = ManageController.getInstance().entryApproval(eIndex);
				if( result ) { println("입점 승인되었습니다"); }
				else { println("입점 승인 실패"); }
				break;
		} // s end
	} // f end
	
//	8. 입점거절
	public void entryRefusal( int eIndex ) throws IOException {
		println("\n입점 거절하시겠습니까?");
		print("1.예 2.아니오 ");
		nextInt(1,2);
		
		return;
	} // f end
	
//	9. 입점 정보 수정
	public void update( int eIndex ) throws IOException {
		println("\n==================     입점 정보 수정     ==================");
		print("상호명 : ");			String ename = next();
		print("지점명 : ");			String espot = next();
		println("상태 변경을 하시겠습니까?");	
		print("1.예 2.아니오");		int etype = nextInt();
		if( etype == 1 ) { etype = 0; }
		
		println("\n입점 정보를 수정하시겠습니까?");
		print("1.예 2.아니오 ");
		int choose = nextInt(1,2);
		if( choose == 2 ) { return; }
		
		EntryDto entryDto = new EntryDto();
		entryDto.setEname(ename);
		entryDto.setEspot(espot);
		entryDto.setEtype(etype);
		entryDto.setEno(eIndex);
		
		boolean result = ManageController.getInstance().update(entryDto);
		if(result) { println("입점 정보 수정이 완료되었습니다."); }
		else { println("입점 정보 수정 실패"); }
	} // f end
	
//	10. 입정 정보 삭제
	public void delete( int eIndex ) throws IOException {
		println("\n==================     입점 정보 삭제     ==================");
		println("\n입점 정보를 삭제하시겠습니까?");
		print("1.예 2.아니오 ");
		int choose = nextInt(1,2);
		switch(choose) {
			case 1:
				boolean result = ManageController.getInstance().delete(eIndex);
				if( result ) { println("입점 정보가 삭제되었습니다"); }
				else { println("입점 정보 삭제 실패"); }
				break;
		} // s end
		return;
	} // f end
	
//	11. 유효성검사
	public boolean check( int eIndex , int type ) throws IOException {
		if(	!ManageDao.getInstance().check( eIndex , type ) ) {
			print("존재하지 않는 번호입니다.\r\n");
			return true;
		}
		return false;
	} // f end
	
	
}// class end
