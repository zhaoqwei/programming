package com.dong.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dong.dao.Dao;
import com.dong.user.User;

public class Server extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		
		/*	ServletOutputStream out = response.getOutputStream();
		//PrintWriter writer = response.getWriter(); 
		  Map<String, String[]> params = request.getParameterMap(); 
		  String queryString = "null";
		  int k = 0;
		  int j = 0;
		  for (String key : params.keySet()) { 
		  String[] values = params.get(key); 
		   for (int i = 0; i < values.length; i++) { 
		    String value = values[i];
		    queryString += "|" + value;
		  }
		}
	  // 去掉最后一个空格 
	  queryString = queryString.substring(0, queryString.length() - 1);
	  out.write(queryString.getBytes("UTF-8"));*/

		Map<String, String[]> params = request.getParameterMap(); 
		String[] queryString = new String[3];
		int j = 0;
		for (String key : params.keySet()) {
			String[] values = params.get(key);
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				queryString[j] = value;
				j++;
			}
		}

		User user = new User(queryString[0],queryString[1],queryString[2]);
		
		/* 加入数据库 */
		Dao dao = new Dao();
		dao.addUser(user);
		
		/* 从数据库中获取排名 */
		String result = dao.getRank(user);
		
		ServletOutputStream out = response.getOutputStream();
		out.write(result.getBytes("UTF-8"));
	}

}