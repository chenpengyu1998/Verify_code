package com.atguigu.servlet;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atguigu.utils.VerifyCodeConfig;

import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class CodeSenderServlet
 */
@WebServlet("/CodeSenderServlet")
public class CodeSenderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CodeSenderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取手机号
		String phone_no = request.getParameter("phone_no");
		//获取验证码
		String code = getCode(6);
		//拼接key
		String codeKey = "Verify_code:"+phone_no+":code";
		//往redis中进行存储
		Jedis jedis = new Jedis("192.168.238.128",6379);
		jedis.setex(codeKey, 120, code);
		jedis.close();
		response.getWriter().print(true);
		
	}
	
	
	private String getCode(int length) {
		String code = "";
		Random random = new Random();
		for(int i = 0; i < length; i++) {
			int rand = random.nextInt(10);
			code += rand;
		}
		return code;
	}

}
