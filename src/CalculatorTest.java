import java.io.*;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

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
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.getMessage());
			}
		}
	}

	private static void command(String input)
	{

		Vector<String> split = splitAll(input);
		Long result = calculate(split);
		System.out.println(result);

	}

	public static boolean isNumber(char ch) {
		// 숫자이면 true 반
		if(ch>='0' && ch <='9') return true;
		else return false;
	}

	public static int charToInt(char c) {
		int num=0;
		switch(c){
			case '^':
				num = 1;
				break;
			case '*':
			case '/':
			case '%':
				num = 2;
				break;
			case '+':
			case '-':
				num = 3;
				break;
		}
		return num;
	}
	public static boolean isPrefer(char c1, char c2) {
		// c1의 우선순위가 더 높거나 같으면 true 반환
		int i1,i2=0;
		if(c2=='(') return true; // peek가 (이면
		if(c1==')') return false; // )이면 무조건 operation 진행
		if(c1=='(') return true; // 들어오는 문자가 (이면 무조건 push
		return charToInt(c1)<=charToInt(c2);

	}

	public static Long operate(Long num1, Long num2, char operator) {
		System.out.println(num1+", "+num2+", "+operator);
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

	public static boolean isOperand(String num) {
		if(num.charAt(0)>='0' && num.charAt(0)<='9') return true;
		if(num.charAt(0)=='-' && num.length()>=2) return true;
		else return false;
	}

	public static Vector<String> splitAll(String input) { // split
		Vector<String> result = new Vector<>();
		int count=0;
		boolean isUnaric = false;
		for(int i=0; i<input.length(); i++) {
			char ch = input.charAt(i);
			if(isNumber(ch)) {
				count++;
			} else {
				if (ch == ' ' || ch == '\t') {
					if(count!=0) {
						if(isUnaric) {
							isUnaric = false;
							result.add("-"+input.substring(i-count,i));
							count=0;
							continue;
						}
						result.add(input.substring(i-count,i));
						count=0;
					}
					continue;
				}
				if (ch == '-' && count == 0) {
					isUnaric = true;
					continue;
				}
				if(count!=0) {
					if(isUnaric) {
						isUnaric = false;
						result.add("-"+input.substring(i-count,i));
					} else {
						result.add(input.substring(i-count,i));
					}
					count=0;
				}

				result.add(Character.toString(ch));
			}
		}
		if(isUnaric) result.add("-"+input.substring(input.length()-count));
		else result.add(input.substring(input.length()-count,input.length()));

		if(result.lastElement().equals("")) result.remove(result.size()-1);

		//debug
		for(int i=0; i<result.size(); i++) {
			System.out.println(result.elementAt(i)+", i: "+i);
		}
		//


		return result;
	}

	public static Long calculate(Vector<String> input) {
//		if(isInfix(input) == false) throw Exception();

		Stack<Long> operandStack = new Stack<>();
		Stack<Character> operatorStack = new Stack<>();

		for(int i=0; i<input.size(); i++) {
			String str = input.elementAt(i);
			if(isOperand(str)) { // 피연산자일 때
				operandStack.add(Long.parseLong(str));
			} else { // 연산자일 때
				char op = str.charAt(0); // char type 연산자
				if(operatorStack.isEmpty() || isPrefer(op,operatorStack.peek())) {
					operatorStack.push(op);
				} else { // 우선순위가 낮을 때
					do {
						Long num2 = operandStack.pop();
						Long num1 = operandStack.pop();
						char operator = operatorStack.pop();
						operandStack.push(operate(num1,num2,operator));
					} while(!operatorStack.isEmpty() && !isPrefer(op,operatorStack.peek()) && !(operatorStack.peek()=='('));
					if(op!=')') operatorStack.push(op);
					if(operatorStack.peek()=='(') operatorStack.pop();
				}
			}
		}
		while(!operatorStack.isEmpty()) {
			Long num2 = operandStack.pop();
			Long num1 = operandStack.pop();
			char operator = operatorStack.pop();
			operandStack.push(operate(num1,num2,operator));
		}

		return operandStack.pop();

	}

	public static boolean isInfix(String input) {
		return true;
	}
}
