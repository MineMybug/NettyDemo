package nioAndIo.nettydemo;

import java.nio.charset.Charset;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 数据业务处理类
 * @author: ruanhang
 * @data: 2018年10月9日
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter  {

	@Override
	public void channelActive(ChannelHandlerContext ctx){
		
		System.out.println(new Date() + ": 客户端写出数据！");
		
		//1.获取数据
		ByteBuf buf = getByteBuf(ctx);
		
		//2.写数据
		ctx.channel().writeAndFlush(buf); //数据写到服务端
	}

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        
    	ByteBuf buf = (ByteBuf)msg;
    	
    	System.out.println(new Date() + ": 客户端读到的数据=====>>" + buf.toString(Charset.forName("utf-8")));
    	
    }
	
	
	private ByteBuf getByteBuf(ChannelHandlerContext ctx) {

		//1.获取二进制抽象 buff
		ByteBuf buf = ctx.alloc().buffer();  //ctx.alloc() 获取到一个 ByteBuf 的内存管理器
		
		//2.准备数据，指定字符串的编码格式 utf-8
		byte[] bytes = "你好，mybug!".getBytes(Charset.forName("utf-8"));
		
		//3.填充数据到ByteBuf
		buf.writeBytes(bytes);
		
		return buf;
	}

}
