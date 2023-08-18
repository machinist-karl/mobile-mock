package cn.com.wind.test.backup;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import com.github.mobile.mock.domain.HttpCaseInfo;
import com.github.mobile.mock.domain.HttpResult;
import com.github.mobile.mock.utils.ToolBox;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

public class HttpCaseHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private static final String MOCK_URL = "/wind/service/mock";
	private static Logger log = LogManager.getLogger();
	private LinkedBlockingQueue<HttpCaseInfo> queue;
	
	public HttpCaseHandler(LinkedBlockingQueue<HttpCaseInfo> queue){
		super();
		this.queue = queue;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {		
		// 如果客户端请求异常，则直接返回异常信息给客户端
		String result = checkRequst(msg);
		if (result != null) {
			send(ctx, result, HttpResponseStatus.BAD_REQUEST);
			log.debug(result);
			return;
		}
		
		Map<String,String> params = new HashMap<>();
		
		// 从Http请求中获取请求参数
		parse(msg,params);
		if(params.isEmpty() || params.size() < 4){
			String error = "获取请求参数失败,参数个数不正确";
			log.debug(error);
			String resStr = getResponseString("E101",error);
			send(ctx, resStr, HttpResponseStatus.OK);
			return;
		}
		
		// 从Map中获取请求参数并将参数存入HttpCaseInfo对象中
		HttpCaseInfo info = getCase(params);
		
		//校验HttpCaseInfo对象，如果对象的属性中有值为空，则返回错误的信息给客户端
		String chekInfo = checkHttpCase(info);
		if(!chekInfo.isEmpty()){
			log.debug(chekInfo);
			String resStr = getResponseString("E102",chekInfo);
			send(ctx, resStr, HttpResponseStatus.OK);
			return;
		}
		
		//校验method参数和replacement是否合法
		String checkCase = verifyCaseObject(info);
		if(checkCase != null){
			log.debug(checkCase);
			String resStr = getResponseString("E103",checkCase);
			send(ctx, resStr, HttpResponseStatus.OK);
			return;
		}
		
		String resStr = getResponseString("E000","已成功读取请求中的用例");
		send(ctx, resStr, HttpResponseStatus.OK);
		
		//将Case信息加入到队列中
		queue.offer(info);
		log.info("Case加入队列成功，case信息为 ： " + info.toString() + "||队列是否为空：" + queue.isEmpty());

	}
	
	/**
	 * 校验用例对象中的
	 * @param info
	 * @return
	 */
	public String verifyCaseObject(HttpCaseInfo info){
		//校验method参数是否合法
		String[] arr = new String[]{"update","delete","add"};
		List<String> list = Arrays.asList(arr);
		
		String method = info.getMethod().toLowerCase();
		if(!list.contains(method)){
			String respMessage = "请求参数Method的值：" + method + " 不在取值范围[update,delete,add]中！！！";
			log.debug(respMessage);
			return respMessage;
		}
				
		//校验replacement字段是否为合法的json串
		String replacement = info.getRespmsg();
		String m = info.getMethod();
		if(!isJSONValid(replacement) && m.equalsIgnoreCase("update")){
			String respMessage = "请求参数replacement的值：" + replacement + " 不是合法的json串";
			log.debug(respMessage);
			return "请求参数replacement的值：" + replacement + " 不是合法的json串";
		}
		
		return null;
	}
	
    public final static boolean isJSONValid(String jsonInString) {
    	if(!jsonInString.contains(":")){
    		log.debug("replacement字段值未包含冒号，不是合法的json报文");
    		return false;
    	}
    	
    	if(!jsonInString.contains("{")){
    		log.debug("replacement字段值未包含:{，不是合法的json报文");
    		return false;
    	}
    	
    	if(!jsonInString.contains("}")){
    		log.debug("replacement字段值未包含:}，不是合法的json报文");
    		return false;
    	}
    	
        try {
        	Gson gson = new Gson();
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch(JsonSyntaxException ex) {
        	log.debug("replacement字段值不是合法的json报文");
            return false;
        }
    }
	
	
	
	/**
	 * 接口标准返回字符串
	 * @param code
	 * @param message
	 * @return
	 */
	public String getResponseString(String code,String message){
		Gson gson = new Gson();
		HttpResult res = new HttpResult();
		res.setResultCode(code);
		res.setResultmessage(message);
		res.setReturnTime(ToolBox.getCurrentStringDateTime());
		
		return  gson.toJson(res);
	}
	
	/**
	 * 从http请求中封装case
	 * @return
	 */
	public HttpCaseInfo getCase(Map<String,String> params){
		//String basic = params.get("basic").toLowerCase();
		String basic = ToolBox.keysToLowCase(params.get("basic"));
		Gson gson = new Gson();
		HttpCaseInfo single = gson.fromJson(basic, HttpCaseInfo.class);
	
		single.setMethod(params.get("method"));
		single.setOperation(params.get("operation"));
		String data = params.get("replacement");
		single.setRespmsg(data);
		single.setRespMD5(DigestUtils.md5Hex(data));

		return single;
	}
	
	
	/**
	 * 获取请求参数列表
	 */
	public void parse(FullHttpRequest msg,Map<String,String> params){
		HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(msg);
		decoder.offer(msg);
		
		List<InterfaceHttpData> list = decoder.getBodyHttpDatas();
		
		for(InterfaceHttpData t : list){
			Attribute data = (Attribute) t;
			try {
				params.put(data.getName().toLowerCase().trim(), data.getValue());	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}

	/**
	 * 校验用例对象的值是否为空
	 * 
	 * @param httpcase
	 * @return
	 */
	public String checkHttpCase(Object httpcase) {
		StringBuilder sb = new StringBuilder();
		for (Field f : httpcase.getClass().getDeclaredFields()) {
			try {
				f.setAccessible(true);
				String t = (String) f.get(httpcase);
				if (t == null || t.isEmpty()) {
					String name = f.getName();
					sb.append("请求参数").append(name).append("的值为空，请检查请求参数名称或者值是否正确！");
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public String checkRequst(FullHttpRequest msg) {
		String result = null;

		// 校验是否为Http请求
		if (!(msg instanceof FullHttpRequest)) {
			result = "Unknown Request(未知请求)";
			return result;
		}

		// 校验是否为Http Post请求
		if (msg.method() != HttpMethod.POST) {
			result = "Not a Post Request(不是Post请求)";
			return result;
		}

		// 校验URL是否正确
		if (!msg.uri().equals(MOCK_URL)) {
			result = "Error Request Path(错误的请求路径)";
		}

		return result;
	}

	// 发送结果给客户端
	private void send(ChannelHandlerContext ctx, String result, HttpResponseStatus status) {

		ByteBuf content = Unpooled.copiedBuffer(result, CharsetUtil.UTF_8);
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
		ctx.writeAndFlush(response);
		// ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
	
	
	public void setQueue(LinkedBlockingQueue<HttpCaseInfo> queue) {
		this.queue = queue;
	}

}