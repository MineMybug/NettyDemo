package nioAndIo.nettydemo;

import java.nio.charset.Charset;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * server端 读写逻辑器
 * 
 * @author: ruanhang
 * @data: 2018年10月9日
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

	//这个方法在接收到客户端发来的数据之后被回调
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ByteBuf buf = (ByteBuf)msg;
		
//		ctx.fireChannelRead(buf);
		
		System.out.println(new Date() + ": 服务端读到的数据====>> " +  buf.toString(Charset.forName("utf-8")));
		
		System.out.println(new Date() + ": 服务端写出数据！");
		
		Thread.sleep(2000);
		
		//获取数据
		ByteBuf out = getByteBuf(ctx);
		
		ctx.channel().writeAndFlush(out);
		
	}

	private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
		
		byte[] bytes = "你好，客户端，我是爱你的服务端！".getBytes(Charset.forName("utf-8"));
		
		ByteBuf buf = ctx.alloc().buffer();
		
		buf.writeBytes(bytes);
		
		return buf;
	}

}
