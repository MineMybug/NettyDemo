package nioAndIo.oldIo;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
* @author rh
* @version 创建时间：2018年10月6日 下午1:32:49
* 
* 单机io弊端分析：
* 一.在传统的io模型当中，每个连接创建成功后都需要一个线程来维护，每个线程中都有一个while死循环，如果有1w个连接
* 对应1w个线程，继而1w个死循环就会带来如下问题：
*   (1).线程资源受限：线程是操作系统中非常宝贵的资源，同一时刻有大量线程处于阻塞状态是非常严重的资源浪费
*   (2).线程切换效率低下
*   (3).数据读写是以字节流为单位，效率不高
*  
*/
public class IOServer {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(8000);
		
		//接受新连接线程
		new Thread(()->{
			while (true) {
				//(1)阻塞方式获取新的连接
				try {
					Socket socket = serverSocket.accept();
					
					//(2)每一个新的连接都创建一个线程，用来读取数据
					new Thread(()->{
						int len;
						byte[] data = new byte[1024];
						try {
							InputStream inputStream = socket.getInputStream();
							//(3)按字节流的方式读取数据
							while ((len = inputStream.read(data))!=-1) {
								System.out.println(new String(data,0,len));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}).start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
