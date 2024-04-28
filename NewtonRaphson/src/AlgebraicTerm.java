public class AlgebraicTerm {
    public boolean sign;
    public int coefficient;
    public char variable;
    public int exponent;

    public AlgebraicTerm() {
        this.sign = true;
        this.coefficient = 1;
        this.variable = 'x';
        this.exponent = 1;
    }
    @Override
    public String toString() {
        return (sign ? "+" : "-") + coefficient + "*" + variable + "^" + exponent;
    }

    public double evaluateTerm(double value) {
        if(this.variable == ' '){
            if (!this.sign) {
                return this.coefficient *= -1;
            }
            return this.coefficient;
        }
        double result = this.coefficient * Math.pow(value, this.exponent);
        if (!this.sign) {
            result *= -1;
        }
        return result;
    }

    public AlgebraicTerm deriveTerm() {
        AlgebraicTerm derivative = new AlgebraicTerm();
        derivative.sign = this.sign;
        if(this.variable == ' '){
            derivative.coefficient = 0;
            derivative.variable = ' ';
        } else {
            derivative.coefficient = this.coefficient * this.exponent;
            derivative.variable = this.variable;
            derivative.exponent = this.exponent - 1;
        }
        if (derivative.exponent == 0) {
            derivative.variable = ' ';
        }
        return derivative;
    }
}
