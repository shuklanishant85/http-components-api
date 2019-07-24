package com.components.constants;

public final class Constants {
	private Constants() {}
	
	public static class Request {
		public static final String HTTP = "http";
		public static final String HOST = "dummy.restapiexample.com"; 
		public static final int PORT = 80;
		public static final String PATH = "/api/v1/employees";
		public static final String PROTOCOL = "HTTP/1.1";
		public static final int SOCKET_TIMEOUT = 10000;
		public static final int CONNECT_TIMEOUT = 20000;
		private Request() {}
	}
	
	public static class Headers {
		public static final String SET_COOKIE = "Set-Cookie";
		private Headers() {}
	}

}
