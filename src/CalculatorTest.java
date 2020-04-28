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
				System.out.print("ERROR\n");
			}
		}
	}

	private static void command(String input)
	{

		Vector<String> split = splitAll(input);
		String[] results = calculate(split);
		System.out.print(results[1]+"\n");
		System.out.print(results[0]+"\n");

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
			case '~':
				num = 2;
				break;
			case '*':
			case '/':
			case '%':
				num = 3;
				break;
			case '+':
			case '-':
				num =4;
				break;
		}
		return num;
	}
	public static boolean isPrefer(String c1, String c2) {
		// c1의 우선순위가 더 높거나 같으면 true 반환
		int i1,i2=0;
		if(c2.charAt(0)=='(') return true; // peek가 (이면
		if(c1.charAt(0)==')') return false; // )이면 무조건 operation 진행
		if(c1.charAt(0)=='(') return true; // 들어오는 문자가 (이면 무조건 push
		if(c1.charAt(0)=='^'&&c2.charAt(0)=='^') return true;
		return charToInt(c1.charAt(0))<charToInt(c2.charAt(0));

	}

	public static long parseUnary(String num) {
		int count=0;
		for(int i=0; i<num.length(); i++) {
			if(num.charAt(i)!='~') break;
			count++;
		}
		String abs = num.substring(count);
		if(count%2==0) {
			return Long.parseLong(abs);
		}else {
			return Long.parseLong("-"+abs);
		}
	}

	public static String operate(String num1, String num2, char operator) {
//		System.out.println(num1+", "+num2+", "+operator);
		long parseNum1 = parseUnary(num1);
		long parseNum2 = parseUnary(num2);
		Long result;
		switch (operator) {
			case '^':
				if(parseNum1==0 && parseNum2<0) result = (long)1/0;
				else result = (long)Math.pow(parseNum1,parseNum2);
				break;
			case '*':
				result =  parseNum1*parseNum2;
				break;
			case '/':
				result = parseNum1/parseNum2;
				break;
			case '%':
				if(parseNum2==0) result = (long)1/0;
				else result =  parseNum1%parseNum2;
				break;
			case '+':
				result =  parseNum1+parseNum2;
				break;
			case '-':
				result =  parseNum1-parseNum2;
				break;
			default:
				result = (long)0/0;
		}
//		System.out.println("result : "+result);
		return Long.toString(result);
	}

	public static boolean isOperand(String num) {
		if(num.charAt(0)>='0' && num.charAt(0)<='9') return true;
		else return false;
	}

	public static boolean isOperator(String op) {
		if(op.matches("\\+|-|\\*|%|/|^")) return true;
		return false;
	}

	public static Vector<String> splitAll(String input) { // split
		Vector<String> result = new Vector<>();
		int count=0;
		String unary = "";
		boolean hasOperator = false;
		boolean isUnary = false;
		boolean hasOperand = false;
		for(int i=0; i<input.length(); i++) {
			char ch = input.charAt(i);
			if(isNumber(ch)) {
				count++;
				hasOperand = true;
			} else {
				if (ch == ' ' || ch == '\t') {
					if(count!=0) {
						if(isUnary) {
							isUnary = false;
							result.add(unary);
							result.add(input.substring(i-count,i));
							unary = "";
						} else {
							result.add(input.substring(i-count,i));
						}
						hasOperator = false;
						count=0;
					}
					continue;
				}
				if (ch == '-' && count == 0 && (hasOperator || !hasOperand)) {
					isUnary = true;
					unary += "~";
					continue;
				}
				if(count!=0) {
					if(isUnary) {
						isUnary = false;
						result.add(unary);
						result.add(input.substring(i-count,i));
						unary = "";
					} else {
						result.add(input.substring(i-count,i));
					}
					hasOperator = false;
					count=0;
				}
				if(ch==')' && result.elementAt(result.size()-2).equals("("))  {
					String tmp = result.lastElement();
					result.removeElementAt(result.size()-1);
					result.removeElementAt(result.size()-1);
					result.add(tmp);
				} else {
					if((isOperator(Character.toString(ch))|| ch==')') && isOperator(result.lastElement())) {
						int k = 1/0;
					}
					if(isUnary) {
						result.add(unary);
						isUnary = false;
						unary = "";
					}
					result.add(Character.toString(ch));
					if(ch!=')' && ch!=')') hasOperator = true;
				}


			}
		}
		if(isUnary) {
			result.add(unary);
			result.add(input.substring(input.length()-count));
		} else result.add(input.substring(input.length()-count,input.length()));

		if(result.lastElement().equals("")) result.remove(result.size()-1);

//		//debug
//		for(int i=0; i<result.size(); i++) {
//			System.out.println(result.elementAt(i)+", i: "+i);
//		}
//		//


		return result;
	}


	public static String rightAsso(String str) {
		int count=0;
		String result=" ";
		for(int i=0; i<str.length(); i++) {
			if(!(str.charAt(i)=='~')) break;
			result += "~ ";
			count++;
		}

		if(count==0) return str+" ";
		else return str.substring(count)+" "+result.trim()+" ";
	}

	public static String unaryCal(String num, String unary) {
		boolean isPositive = true;
		for(int i=0; i<unary.length(); i++) {
			isPositive = !isPositive;
		}
		if(isPositive) return num;
		else return "-"+num;
	}

	public static String splitUnary(String unary) {
		String result = "";
		for(int i=0; i<unary.length(); i++) {
			result += "~ ";
		}
		return result;
	}

	public static String[] calculate(Vector<String> input) {
//		if(isInfix(input) == false) throw Exception();

		Stack<String> operandStack = new Stack<>();
		Stack<String> operatorStack = new Stack<>();
		String result = "";

		for(int i=0; i<input.size(); i++) {
			String str = input.elementAt(i);
			if(isOperand(str)) { // 피연산자일 때
				operandStack.add(str);
				result += rightAsso(str);
			} else { // 연산자일 때
				if(operatorStack.isEmpty() || isPrefer(str,operatorStack.peek())) {
					operatorStack.push(str);
				} else { // 우선순위가 낮을 때

					do {
						String operator = operatorStack.pop();
						if(operator.contains("~")) {
							String num = operandStack.pop();
							operandStack.push(unaryCal(num,operator));
							result += splitUnary(operator);
						} else {
							String num2 = operandStack.pop();
							String num1 = operandStack.pop();
							operandStack.push(operate(num1,num2,operator.charAt(0)));
							result += operator+" ";
						}

					} while(!operatorStack.isEmpty() && !isPrefer(str,operatorStack.peek()) && !(operatorStack.peek().charAt(0)=='('));
					if(str.charAt(0)!=')') operatorStack.push(str);
					if(operatorStack.peek().charAt(0)=='(') operatorStack.pop();
				}
			}
		}
		while(!operatorStack.isEmpty()) {
			String operator = operatorStack.pop();
			if(operator.contains("~")) {
				String num = operandStack.pop();
				operandStack.push(unaryCal(num,operator));
				result += splitUnary(operator);
			} else {
				String num2 = operandStack.pop();
				String num1 = operandStack.pop();
				operandStack.push(operate(num1,num2,operator.charAt(0)));
				result += operator+" ";
			}
		}

		if(operandStack.size()!=1 || !operatorStack.isEmpty()) {
			int i= 1/0;
		}
		String[] results = {operandStack.pop(),result.trim()};
		return results;

	}

	public static boolean isInfix(String input) {
		return true;
	}
}
