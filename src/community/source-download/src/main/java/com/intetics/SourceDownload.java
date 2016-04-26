package com.intetics;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SourceDownload {
	
	public SourceDownload() {
		
	}
	
	public void downloadSource(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getOutputStream().write("111111222222333333".getBytes());
	}

}
