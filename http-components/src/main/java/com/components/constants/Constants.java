package com.components.constants;

public final class Constants {
	private Constants() {}
	
	public static class Request {
		public static final String HTTP = "http";
		public static final String HOST = "dummy.restapiexample.com"; 
		public static final int PORT = 80;
		public static final String PATH = "/api/v1/employees";
		private Request() {}
	}
	
	public static class Headers {
		public static final String SET_COOKIE = "Set-Cookie";
		private Headers() {}
	}

}
