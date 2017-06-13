package com.qingcity.test;

import com.qingcity.constants.CmdConstant;
import com.qingcity.entity.MsgEntity;
import com.qingcity.proto.PlayerInfo.C2S_CreateRole;
import com.qingcity.proto.PlayerInfo.C2S_Login;
import com.qingcity.proto.PlayerInfo.C2S_Register;
import com.qingcity.proto.ShopProto.C2S_Shop;
import com.qingcity.test.client.BaseTestAction;

public class LoginTest extends BaseTestAction {

	private static final LoginTest instance = new LoginTest();

	public static LoginTest getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		LoginTest login = new LoginTest();
		login.connectServer(10);
		//for(int i = 0;i<10000;i++){
			//System.out.println("第"+i+"登录");
		login.testLogin();
	//	}
		//login.register();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//login.createRole();
		//login.beginPlay();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//login.play();
		//login.changeCloth();
		//login.getWeekRank();
		//login.buy();
	}
	
	
	public void buy(){
		C2S_Shop.Builder shop = C2S_Shop.newBuilder();
		shop.setType(1);
		shop.setId(2);
		byte[] bytes = shop.build().toByteArray();
		MsgEntity test = new MsgEntity();
		test.setUserId(100008);
		test.setMsgLength((short) bytes.length);
		test.setCmdCode(CmdConstant.C2S_SHOP_BUY);
		test.setData(bytes);
		channelList.get(1).writeAndFlush(test);
		channelList.get(1).closeFuture();
	}
	
	public void register() {
		// for (int i = 0; i < 2; i++) {
		C2S_Register.Builder register = C2S_Register.newBuilder();
		register.setPhone("13552154555");
		register.setPassword("13552154555");
		register.setPassword2("13552154555");
		register.setCode("04265-5P5AH-9893K-0CT3L-QQQXA");
		byte[] bytes = register.build().toByteArray();
		MsgEntity test = new MsgEntity();
		test.setMsgLength((short) bytes.length);
		test.setCmdCode(CmdConstant.C2S_USER_REGISTER);
		test.setData(bytes);
		channelList.get(1).writeAndFlush(test);
		channelList.get(1).closeFuture();
		// }
	}

	public void createRole() {
		C2S_CreateRole.Builder createRole = C2S_CreateRole.newBuilder();
		createRole.setNickname("测试经验");
		byte[] bytes = createRole.build().toByteArray();
		MsgEntity test = new MsgEntity();
		test.setUserId(100009);
		test.setMsgLength((short) bytes.length);
		test.setCmdCode(CmdConstant.C2S_USER_CREATE_ROLE);
		test.setData(bytes);
		channelList.get(1).writeAndFlush(test);
		channelList.get(1).closeFuture();
	}

	public void testLogin() {
		try {
			// for (int i = 0; i < 10; i++) {
			C2S_Login.Builder login = C2S_Login.newBuilder();
			login.setPhone("18677755508");
			login.setPassword("18677755508");
			byte[] bytes = login.build().toByteArray();
			MsgEntity test = new MsgEntity();
			test.setMsgLength((short) bytes.length);
			test.setCmdCode(CmdConstant.C2S_USER_LOGIN);
			test.setData(bytes);
			channelList.get(1).writeAndFlush(test);
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// }
		// System.out.println(System.currentTimeMillis() / 1000);
		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// // 发送ping消息
		// for (int i = 0; i < 3; i++) {
		// for (int j = 0; j < 10; j++) {
		// KeepAliveMsg.Builder keepAlive = KeepAliveMsg.newBuilder();
		// keepAlive.setContent("ping" + i + " " + j);
		// byte[] pb = keepAlive.build().toByteArray();
		// MsgEntity pmsg = new MsgEntity();
		// pmsg.setCmdCode(CmdConstant.PING);
		// pmsg.setData(pb);
		// pmsg.setMsgLength(pb.length); 
		// }
		// }
		// for (int i = 1; i <= 2; i++) {
		// System.out.println("------------");
		// ChatMessage.Builder chat = ChatMessage.newBuilder();
		// chat.setUId(60 + i);
		// chat.setUsername("李慧婷" + i);
		// chat.setTarget(i);
		// chat.setContent("公会里的各位你们好,我是李慧婷 " + i);
		// byte[] chatByte = chat.build().toByteArray();
		// MsgEntity test = new MsgEntity();
		// test.setMsgLength(chatByte.length);
		// test.setCmdCode((short) ChatConstant.SOCIETY_MSG);
		// test.setData(chatByte);
		// channelList.get(i).writeAndFlush(test);
		// }
		// 发送ping消息
		// for (int i = 0; i < 3; i++) {
		// for (int j = 0; j < 10; j++) {
		// KeepAliveMsg.Builder keepAlive = KeepAliveMsg.newBuilder();
		// keepAlive.setContent("ping" + i + " " + j);
		// byte[] pb = keepAlive.build().toByteArray();
		// MsgEntity pmsg = new MsgEntity();
		// pmsg.setCmdCode(CmdConstant.PING);
		// pmsg.setData(pb);
		// pmsg.setMsgLength(pb.length);
		// channelList.get(i).writeAndFlush(pmsg);
		// }
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// }
		// for (int i = 1; i <= 2; i++) {
		// System.out.println("------------");
		// ChatMessage.Builder chat = ChatMessage.newBuilder();
		// chat.setUId(60 + i);
		// chat.setUsername("李慧婷" + i);
		// chat.setTarget(ChatConstant.WORLD_MSG);
		// chat.setContent("世界里的各位你们好,我是李慧婷 " + i);
		// byte[] chatByte = chat.build().toByteArray();
		// MsgEntity test = new MsgEntity();
		// test.setMsgLength(chatByte.length);
		// test.setCmdCode((short) ChatConstant.WORLD_MSG);
		// test.setData(chatByte);
		// channelList.get(i).writeAndFlush(test);
		// }
	}

}
