import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ABCalculator {
  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to this simple calculator! Author: Adam Bobocky @ LEAF Academy");
    System.out.println("It can handle these characters: 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, ^, *, /, +, -, p, g");
    System.out.println("'p' stands for the constant 𝛑");
    System.out.println("'g' stands for earths gravitational force of 9.81");
    System.out.println("");
    System.out.println("");
    System.out.println("");
    System.out.println("Please drop a mathematical expression:");
    System.out.println("");

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); // Prepare to read user input

    String expr = reader.readLine(); // Read user input into 'expr'

    expr = expr.replace(",", "."); // Replace all "," with "." so that we account for slovak style too where , is used for fractions instead of .

    List<String> elements = segmented(expr); // Slice the user input up into "operations", "constants" and "numbers"
    List<String> equation = elements; // Just save the initial equation for later showing

    // Refactoring loop that applies Refactor function on each iteration
    // If no chances are made by the Refactor function anymore, we proceed to print out the result
    String newFormat = "";
    String prevFormat = "empty"; // Set prevFormat to something not equal to the newFormat so we get at least a single iteration of Refactor

    System.out.println("");
    System.out.println("-------------------");
    System.out.println("Refactor: " + String.join(" ", elements));
    while(!((String)prevFormat).equals((String)newFormat)){ // Loop while previous result of Refactor is not same as the current one
      prevFormat = elements.toString();

      elements = Refactor(elements); // Apply Refactor function to 'elements'

      newFormat = elements.toString();

      System.out.println("Refactor: " + String.join(" ", elements)); // Debug priting just for visualization purposes
    }
    System.out.println("-------------------");

    System.out.println("");
    System.out.println(String.join(" ", equation) + " = " + String.join(" ", elements)); // Print out the final result
    System.out.println("");
    System.out.println("(Any illegal characters were stripped from the expression, for example spaces)"); // Warning note on why resulting equation might differ from inputted one
  }
  // Function to get type of character
  public static String getType(char inp){
    String sw = inp + "";

    switch(sw){
      case "+":
        return "operation";
      case "-":
        return "operation";
      case "*":
        return "operation";
      case "/":
        return "operation";
      case "^":
        return "operation";
      case "p":
        return "constant";
      case "g":
        return "constant";
      case "(":
        return "separator";
      case ")":
        return "separator";
      case "1":
        return "number";
      case "2":
        return "number";
      case "3":
        return "number";
      case "4":
        return "number";
      case "5":
        return "number";
      case "6":
        return "number";
      case "7":
        return "number";
      case "8":
        return "number";
      case "9":
        return "number";
      case "0":
        return "number";
      case ".":
        return "number";
      default:
        return "null";
    }
  }
  // Function to clear and segment the whole string
  // Ex. '5+ 5.25 ^p   ' -> ['5', '+', '5.25', '^', 'p']
  // This way fractions are kept intact, no need for any space rule for separation and illegal characters are stripped
  public static List<String> segmented(String expr) {
    List<String> elements = new ArrayList<String>();

    int i = 0;
    String push = "";
    String type = "";

    while(i < expr.length()){ // Iterate over all 'char's in the string
      char c = expr.charAt(i);

      String cType = getType(c); // Get type of current character

      if(cType != "null"){ // If type not equal to 'null', than we can proceed to push it on
        if(cType != type && push.length() > 0){ // Current characters type is different from previous, we push the previous one as a complete string into the array and start new string
          elements.add(push);

          push = "";
        }

        type = cType;
        push = push + c;
      }

      i++;
    }

    if(push.length() > 0){ // If there is any leftover string, we push it
      elements.add(push);
    }

    return elements;
  }
  // Function that does single refactorization operation, in the correct order of -> assign constants numbers, ^, *, /, +, -
  public static List<String> Refactor(List<String> elements){
    // Find all constants, those are prerequisite in orders of operation
    for (int i = 0; i < elements.size(); i++) {
      if(elements.get(i).equals("p")){
        elements.set(i, String.valueOf(Math.PI));

        return elements;
      }
    }
    for (int i = 0; i < elements.size(); i++) {
      if(elements.get(i).equals("g")){
        elements.set(i, "9.81");

        return elements;
      }
    }
    // Find all powers, those are first in orders of operation
    for (int i = elements.size() - 1; i > 0; i--) {
      if(elements.get(i).equals("^")){
        if(getType(elements.get(i + 1).charAt(0)) == "number" && getType(elements.get(i - 1).charAt(0)) == "number"){
          float firstNum = Float.parseFloat(elements.get(i - 1));
          float secondNum = Float.parseFloat(elements.get(i + 1));

          List<String> out = Stream.concat(elements.subList(0, i - 1).stream(), Arrays.asList(new String[]{ String.valueOf(Math.pow(firstNum, secondNum)) }).stream()).collect(Collectors.toList());

          return Stream.concat(out.stream(), elements.subList(i + 2, elements.size()).stream()).collect(Collectors.toList());
        }
      }
    }
    // Find all multiplications, those are second in orders of operation
    for (int i = elements.size() - 1; i > 0; i--) {
      if(elements.get(i).equals("*")){
        if(getType(elements.get(i + 1).charAt(0)) == "number" && getType(elements.get(i - 1).charAt(0)) == "number"){
          float firstNum = Float.parseFloat(elements.get(i - 1));
          float secondNum = Float.parseFloat(elements.get(i + 1));

          List<String> out = Stream.concat(elements.subList(0, i - 1).stream(), Arrays.asList(new String[]{ String.valueOf(firstNum * secondNum) }).stream()).collect(Collectors.toList());

          return Stream.concat(out.stream(), elements.subList(i + 2, elements.size()).stream()).collect(Collectors.toList());
        }
      }
    }
    // Find all divisions, those are third in orders of operation
    for (int i = elements.size() - 1; i > 0; i--) {
      if(elements.get(i).equals("/")){
        if(getType(elements.get(i + 1).charAt(0)) == "number" && getType(elements.get(i - 1).charAt(0)) == "number"){
          float firstNum = Float.parseFloat(elements.get(i - 1));
          float secondNum = Float.parseFloat(elements.get(i + 1));

          List<String> out = Stream.concat(elements.subList(0, i - 1).stream(), Arrays.asList(new String[]{ String.valueOf(firstNum / secondNum) }).stream()).collect(Collectors.toList());

          return Stream.concat(out.stream(), elements.subList(i + 2, elements.size()).stream()).collect(Collectors.toList());
        }
      }
    }
    // Find all additions, those are fourth in orders of operation
    for (int i = elements.size() - 1; i > 0; i--) {
      if(elements.get(i).equals("+")){
        if(getType(elements.get(i + 1).charAt(0)) == "number" && getType(elements.get(i - 1).charAt(0)) == "number"){
          float firstNum = Float.parseFloat(elements.get(i - 1));
          float secondNum = Float.parseFloat(elements.get(i + 1));

          List<String> out = Stream.concat(elements.subList(0, i - 1).stream(), Arrays.asList(new String[]{ String.valueOf(firstNum + secondNum) }).stream()).collect(Collectors.toList());

          return Stream.concat(out.stream(), elements.subList(i + 2, elements.size()).stream()).collect(Collectors.toList());
        }
      }
    }
    // Find all substractions, those are fifth in orders of operation
    for (int i = elements.size() - 1; i > 0; i--) {
      if(elements.get(i).equals("-")){
        if(getType(elements.get(i + 1).charAt(0)) == "number" && getType(elements.get(i - 1).charAt(0)) == "number"){
          float firstNum = Float.parseFloat(elements.get(i - 1));
          float secondNum = Float.parseFloat(elements.get(i + 1));

          List<String> out = Stream.concat(elements.subList(0, i - 1).stream(), Arrays.asList(new String[]{ String.valueOf(firstNum - secondNum) }).stream()).collect(Collectors.toList());

          return Stream.concat(out.stream(), elements.subList(i + 2, elements.size()).stream()).collect(Collectors.toList());
        }
      }
    }

    return elements;
  }
}
