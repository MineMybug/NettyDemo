package nioAndIo.oldIo;

import java.net.Socket;
import java.util.Date;

/**
* @author rh
* @version 创建时间：2018年10月6日 下午1:45:59
*/
public class IOClient {
	
	public static void main(String[] args) {
		new Thread(()->{
			try {
				Socket socket = new Socket("127.0.0.1", 8000);
				while (true) {
					socket.getOutputStream().write((new Date() + ": hello world").getBytes());
					Thread.sleep(2000);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}

}
