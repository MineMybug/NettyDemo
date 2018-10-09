package nioAndIo.nettydemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

/**
 * 总结：
 * 	（1）创建一个引导类，然后给他指定线程模型，IO模型，连接读写处理逻辑，绑定端口之后，服务端就启动起来了。
 * 	（2） bind 方法是异步的，我们可以通过这个异步机制来实现端口递增绑定
 *  （3）Netty 服务端启动额外的参数  设置底层 TCP 参数
 * 
 * @author: ruanhang
 * @data: 2018年10月8日
 */
public class NettyServer {

	private static final int BEGIN_PORT = 8000;
	
	public static void main(String[] args) {
		
		//监听端口，传统io accept 新连接的线程组
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		
		//处理每一条连接的数据读写的线程组
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		
		//启动服务，配置相关启动参数
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		
		serverBootstrap
			.group(bossGroup, workerGroup)//设置线程模型
			.channel(NioServerSocketChannel.class)//指定服务的IO模型
			.childHandler(new ChannelInitializer<NioSocketChannel>() {//定义后续每条连接的数据读写，业务处理逻辑

				@Override
				protected void initChannel(NioSocketChannel ch) throws Exception {
					// 指定连接数据读写逻辑   读写逻辑处理器
					ch.pipeline().addLast(new FirstServerHandler());
					
				}
			}).attr(AttributeKey.newInstance("serverName"), "nettyServer")//为服务端指定一个名称  可以通过channel.atrr() 取出
			
			.childAttr(AttributeKey.newInstance("clientKey"), "clientValue")//可以给每一条连接指定自定义属性
			
			//可以给每条连接设置一些TCP底层相关的属性 
			//ChannelOption.SO_KEEPALIVE表示是否开启TCP底层心跳机制，true为开启
			.childOption(ChannelOption.SO_KEEPALIVE, true)
	        
			//表示是否开始Nagle算法，true表示关闭，false表示开启，通俗地说，如果要求高实时性，
			//有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启。
			.childOption(ChannelOption.TCP_NODELAY, true)
		
		    .option(ChannelOption.SO_BACKLOG, 1024);
		
			//表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
			bind(serverBootstrap,BEGIN_PORT);
	}

	/**
	 * 使用递归方式自动绑定端口
	 * @param serverBootstrap
	 * @param port
	 */
	private static void bind(ServerBootstrap serverBootstrap, int port) {
		// TODO Auto-generated method stub
		serverBootstrap.bind(port).addListener(future->{
			if(future.isSuccess()){
				System.out.println("端口[" + port + "]绑定成功！");
			}else {
				System.err.println("端口[" + port + "]绑定失败！");
				bind(serverBootstrap, port+1);
			}
		});
	}
}
