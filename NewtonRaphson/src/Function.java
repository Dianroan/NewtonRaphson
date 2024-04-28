import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Function {
    private String stringFuntion;
    private double startingPoint;
    private double tolerance;
    private List<AlgebraicTerm> normalTerms = new ArrayList<>();
    private List<AlgebraicTerm> derivedTerms = new ArrayList<>();
    public Scanner scanner = new Scanner(System.in);
    public Function() {
        this.stringFuntion = "";
        this.startingPoint = 0;
        this.tolerance = 0.0000001;
    }

    public String getStringFuntion() {
        return stringFuntion;
    }

    public void setStringFuntion(String stringFuntion) {
        this.stringFuntion = stringFuntion;
    }

    public double getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(double startingPoint) {
        this.startingPoint = startingPoint;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public void insertStringFuntion(){
        do{
            System.out.println("Por favor ingresa la ecuacion:");
            System.out.print("F(x) = ");
            this.setStringFuntion(scanner.nextLine());
        } while (!validateEquation(this.getStringFuntion()));
    }

    public void insertEvaluationConfiguration(){
        System.out.println("Por favor ingresa el punto de partida:");
        this.setStartingPoint(scanner.nextDouble());
        System.out.println("Por favor ingresa la tolerancia:");
        this.setTolerance(scanner.nextDouble());
    }

    public String insertSpaces(String equation) {
        String equationWithSpaces = equation.replaceAll("([+-])", " $1");
        return equationWithSpaces;
    }

    public String[] separatingEquationIntoStringTerms(String equation){
        equation = equation.replaceAll(" ", "");
        equation = insertSpaces(equation);
        String[] termStrings = equation.split(" ");
        return  termStrings;
    }

    public AlgebraicTerm parseAlgebraicTerm(String termString){
        AlgebraicTerm term = new AlgebraicTerm();
        term.sign = !termString.startsWith("-");
        termString = termString.replaceAll("([+-])", "");
        String[] parts = termString.split("\\*");
        if (termString.contains("*")){
            term.coefficient = Integer.parseInt(parts[0]);
        }
        String variablePart = parts[parts.length - 1];
        if (variablePart.contains("^")) {
            String[] exponentParts = variablePart.split("\\^");
            term.exponent = Integer.parseInt(exponentParts[1]);
        }
        if(!termString.contains("*") && !termString.contains("^")){
            term.coefficient = Integer.parseInt(termString);
            term.variable=' ';
        }
        return  term;
    }

    public List<AlgebraicTerm> parseEquation(String equation) {
        List<AlgebraicTerm> terms = new ArrayList<>();
        String[] termStrings = separatingEquationIntoStringTerms(equation);
        for (String termString: termStrings){
            terms.add(parseAlgebraicTerm(termString));
        }
        return terms;
    }

    public List<AlgebraicTerm> deriveEachTerm(){
        List<AlgebraicTerm> terms = new ArrayList<>();
        for (AlgebraicTerm term: this.normalTerms){
            terms.add(term.deriveTerm());
        }
        return terms;
    }

    public double evaluateFuntion(List<AlgebraicTerm> funtion, double value) {
        double result = 0;
        for (AlgebraicTerm term: funtion){
            result += term.evaluateTerm(value);
        }
        return result;
    }

    public boolean validateEquation(String equation) {
        if (equation.isEmpty()) {
            System.out.println("Error: La ecuación no puede estar vacía.");
            return false;
        }
        if (!equation.matches("[\\d\\s\\+\\-\\*xX^()]*")) {
            System.out.println("Error: La ecuación contiene caracteres no válidos.");
            return false;
        }
        if (equation.startsWith("+") || equation.startsWith("-") || equation.endsWith("+") || equation.endsWith("-")) {
            System.out.println("Error: La ecuación no puede empezar o terminar con un operador.");
            return false;
        }
        int openingParentheses = 0;
        int closingParentheses = 0;
        for (char c : equation.toCharArray()) {
            if (c == '(') {
                openingParentheses++;
            } else if (c == ')') {
                closingParentheses++;
            }
        }
        if (openingParentheses != closingParentheses) {
            System.out.println("Error: La ecuación tiene un número incorrecto de paréntesis.");
            return false;
        }
        return true;
    }

    public void NewtonRaphson(){
        this.insertStringFuntion();
        double x0 = startingPoint;
        this.normalTerms = parseEquation(this.stringFuntion);
        this.derivedTerms = deriveEachTerm();
        while (true) {
            double fx0 = evaluateFuntion(this.normalTerms, x0);
            double dfx0 = evaluateFuntion(this.derivedTerms, x0);
            if (Math.abs(dfx0) <= tolerance) {
                dfx0 = 0.0000001;
            }
            x0 = x0 - fx0 / dfx0;
            if (Math.abs(fx0) <= tolerance) {
                break;
            }
        }
        System.out.println("El valor de la raíz es: " + x0);
    }
}