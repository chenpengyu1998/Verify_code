package com.atguigu.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atguigu.utils.VerifyCodeConfig;

import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class CodeVerifyServlet
 */
@WebServlet("/CodeVerifyServlet")
public class CodeVerifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CodeVerifyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		//获取验证码和手机号
		String phone_no = request.getParameter("phone_no");
		String Verify_code = request.getParameter("verify_code");
		//从redis获取手机号对应的验证码
		//拼接key
		String codeKey = "Verify_code:"+phone_no+":code";
		String countKey = "Verify_code:"+phone_no+":count";
		
		
		Jedis jedis = new Jedis("192.168.238.128",6379);
		
		String count = jedis.get(countKey);
		if(count == null) {
			jedis.setex(countKey, 24*60*60, "1");
		}else if(Integer.parseInt(count)<=2) {
			jedis.incrBy(countKey, 1);
		}else {
			response.getWriter().print("limit");
			jedis.close();
			return;
		}
		String code = jedis.get(codeKey);
		System.out.println("redis:"+code);
		System.out.println("input:"+Verify_code);
		if(code.equals(Verify_code)) {
			response.getWriter().print(true);
		}else {
			response.getWriter().print(false);
		}
		jedis.close();
		
	}

}
