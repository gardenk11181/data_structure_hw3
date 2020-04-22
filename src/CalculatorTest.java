import java.io.*;
import java.util.Stack;

public class CalculatorTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input)
	{
		try {
			Long result = calculate(input);
			System.out.println(result);
		} catch(Exception e) {
			System.out.println("ERROR");
		}
	}

	public static boolean isOperand(char ch) {
		// 숫자이면 true 반
		if(ch>='0' && ch <='9') return true;
		else return false;
	}

	public static int charToInt(char c) {
		int num=0;
		switch(c){
			case '^':
				num = 1;
			case '*':
			case '/':
			case '%':
				num = 2;
			case '+':
			case '-':
				num = 3;
		}
		return num;
	}

	public static boolean isPrefer(char c1, char c2) {
		// c1의 우선순위가 더 높거나 같으면 true 반환
		int i1,i2=0;
		if(c2=='(') return true;
		if(c1==')') return false;
		if(c1=='(') return true;
		return charToInt(c1)<=charToInt(c2);

	}

	public static Long operate(Long num1, Long num2, char operator) {
		switch (operator) {
			case '^':
				return (long)Math.pow(num1,num2);
			case '*':
				return num1*num2;
			case '/':
				return num1/num2;
			case '%':
				return num1%num2;
			case '+':
				return num1+num2;
			case '-':
				return num1-num2;
			default:
				return null;
		}
	}

	public static Long calculate(String input) {
//		if(isInfix(input) == false) throw Exception();

		Stack<Long> operandStack = new Stack<>();
		Stack<Character> operatorStack = new Stack<>();

		int count=0;
		for(int i=0; i<input.length(); i++) {
			char ch = input.charAt(i);
			if(isOperand(ch)) {
				count++;
			} else {
				if(ch == '-' && count==0) { // -가 unary 일 때
					count++;
					continue;
				}
				if(ch == ' '|| ch == '\t') continue; // 공백일 때는 아무것도 안함.
				operandStack.push(Long.parseLong(input.substring(i-count,i))); // 숫자 push
				count=0; // 초기

				if(isPrefer(ch,operatorStack.peek())) { // 새로들어온 연산자의 우선순위가 높거나 같으 그대로 진행
					operatorStack.push(input.charAt(i));
				} else { // 새로 들어온 연산자의 우선순위가 낮으면 높아질 때까지 연산 시작
					while(isPrefer(ch,operatorStack.peek())) {
						// 괊호 없애주는 것 필요함
						Long num2 = operandStack.pop();
						Long num1 = operandStack.pop();
						char operator = operatorStack.pop();
						try {
							operandStack.push(operate(num1,num2,operator));
						} catch (NullPointerException ne) {
							System.err.println("incorrect operator");
						}

					}
				}
			}
		}

		return operandStack.pop();

	}

	public static boolean isInfix(String input) {
		return true;
	}
}
