package edu.kh.project;

public class Calculator {

//	public int add(int a, int b) {
//		return a + b;
//	}
//	
//	public int subtract(int a, int b) {
//		return a - b;
//	}
//	
//	public int divide(int a, int b) {
//		
//		if(b == 0) {
//			//IllegalArgumentException : 매개변수가 옳지 않음
//			throw new IllegalArgumentException("ERROR : 0 으로 나눌 수 없습니다.");
//		}
//		
//		return a / b;
//	}
	
	
	public int add(int a, int b) {

		return a + b;

		}

	public int subtract(int a, int b) {

		return a - b;

		}

	public int multiply(int a, int b) {

		return a * b;

		}

	public int divide(int a, int b) {

		if (b == 0) {

		throw new IllegalArgumentException("0으로 나눌 수 없음");

		}

		return a / b;

		}
	
	
}
