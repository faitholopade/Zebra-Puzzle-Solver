package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class ConstraintSolver {

    private Domain dom;
    private List<Variable> variableSet;
    private List<Constraint> constraintSet;
    private boolean isSolved;
    private boolean isSRE;

    public ConstraintSolver() {
        this.variableSet = new ArrayList<Variable>();
        this.constraintSet = new ArrayList<Constraint>();
        this.isSolved = false;
        this.isSRE = false;
    }

    public String toString() {
        // print variable
        for (int i = 0; i < variableSet.size(); i++)
            System.out.println(variableSet.get(i));
        System.out.println("");
        // print constraints
        for (int i = 0; i < constraintSet.size(); i++)
            System.out.println(constraintSet.get(i));
        System.out.println("");
        return "";
        // TODO
        // needs to print constraints as well
    }

    private void parse(String fileName) {
        try {
            File inputFile = new File(fileName);
            Scanner scanner = new Scanner(inputFile);

            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();

                if (currentLine.startsWith("Domain-")) {
                    // this is our domain - i.e. a datastructure that contains values and can be
                    // updated, played with etc.
                    String s = currentLine.replace("Domain-", "");
                    String[] array = s.split(",");
                    int[] vals = new int[array.length];
                    for (int i = 0; i < array.length; i++) {
                        vals[i] = Integer.parseInt(array[i]);
                    }
                    dom = new Domain(vals);
                } else if (currentLine.startsWith("Var-")) {
                    // this is the code for every variable (a name and a domain)
                    String s = currentLine.replace("Var-", "");
                    Variable var = new Variable(s, dom);
                    variableSet.add(var);
                } else if (currentLine.startsWith("Cons-")) {
                    // ConstraintEqualityVarPlusCons:
                    if (currentLine.startsWith("Cons-eqVPC")) {
                        String regexPattern = "\\(|\\)|\\ ";
                        String s = currentLine.replace("Cons-eqVPC(", "").replaceAll(regexPattern, "");
                        String[] values = s.split("=");
                        String[] values2 = values[1].split("\\+");
                        String val1Name = values[0];
                        String val2Name = values2[0];
                        Variable v1 = null;
                        Variable v2 = null;
                        for (Variable element : variableSet) {
                            if (element.hasThisName(val1Name)) {
                                v1 = element;
                            } else if (element.hasThisName(val2Name)) {
                                v2 = element;
                            }
                        }
                        ConstraintEqualityVarPlusCons eq = new ConstraintEqualityVarPlusCons(v1, v2,
                                Integer.parseInt(values2[1]), false);
                        constraintSet.add(eq);
                    } else if (currentLine.startsWith("Cons-eqVV")) {
                        String regexPattern = "\\(|\\)|\\s*(?=\\=)|(?<=\\=)\\s*"; // Regex that will only remove spaces
                                                                                  // between variable names
                        String s = currentLine.replace("Cons-eqVV(", "").replaceAll(regexPattern, "");
                        String[] values = s.split("=");
                        String val1Name = values[0];
                        String val2Name = values[1];
                        Variable v1 = null;
                        Variable v2 = null;
                        for (Variable var : variableSet) {
                            if (var.hasThisName(val1Name)) {
                                v1 = var;
                            } else if (var.hasThisName(val2Name)) {
                                v2 = var;
                            }
                        }
                        ConstraintEqualityVarVar eq = new ConstraintEqualityVarVar(v1, v2);
                        constraintSet.add(eq);
                    }
                    // ConstraintEqualityVarCons:
                    else if (currentLine.startsWith("Cons-eqVC")) {
                        String regexPattern = "\\(|\\)|\\ ";
                        String s = currentLine.replace("Cons-eqVC(", "").replaceAll(regexPattern, "");
                        String[] values = s.split("=");
                        String val1Name = values[0];
                        int val2 = Integer.parseInt(values[1]);
                        Variable v1 = null;
                        for (Variable var : variableSet) {
                            if (var.hasThisName(val1Name)) {
                                v1 = var;
                            }
                        }
                        ConstraintEqualityVarCons eq = new ConstraintEqualityVarCons(v1, val2);
                        constraintSet.add(eq);
                    } // ConstraintDifferenceVarVar:
                    else if (currentLine.startsWith("Cons-diff")) {
                        String regexPattern = "[\\(\\)]|(?<=,|^)\\s+|\\s+(?=,|$)"; // Regex that will only remove spaces
                                                                                   // between variable names
                        String s = currentLine.replace("Cons-diff(", "").replaceAll(regexPattern, "");
                        String[] values = s.split(",");
                        String val1Name = values[0];
                        String val2Name = values[1];
                        Variable v1 = null;
                        Variable v2 = null;
                        for (Variable var : variableSet) {
                            if (var.hasThisName(val1Name)) {
                                v1 = var;
                            } else if (var.hasThisName(val2Name)) {
                                v2 = var;
                            }
                        }
                        ConstraintDifferenceVarVar eq = new ConstraintDifferenceVarVar(v1, v2);
                        constraintSet.add(eq);
                    } else if (currentLine.startsWith("Cons-abs")) {
                        String s = currentLine.replace("Cons-abs(", "");
                        s = s.replace(")", "");
                        // s = s.replaceAll(" ", "");
                        String[] values = s.split(" - ");
                        String[] values2 = values[1].split(" = ");
                        String val1Name = values[0];
                        String val2Name = values2[0];
                        Variable v1 = null;
                        Variable v2 = null;
                        for (Variable element : variableSet) {
                            if (element.hasThisName(val1Name)) {
                                v1 = element;
                            } else if (element.hasThisName(val2Name)) {
                                v2 = element;
                            }
                        }
                        ConstraintEqualityVarPlusCons eq = new ConstraintEqualityVarPlusCons(v1, v2, 0, true);
                        constraintSet.add(eq);

                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
    }

    public List<Domain> cloneDomains() {
        List<Domain> clonedDomains = new ArrayList<Domain>();
        for (int j = 0; j < variableSet.size(); j++) {
            Domain d = new Domain(variableSet.get(j).d);
            clonedDomains.add(d);
        }
        return clonedDomains;
    }

    public boolean isReduced() {
        int i = 0;
        while (i < variableSet.size()) {
            if (!variableSet.get(i).isReducedToOnlyOneValue()) {
                return false;
            }
            i++;
        }
        return true;
    }

    public boolean isEmpty() {
        int i = 0;
        while (i < variableSet.size()) {
            if (variableSet.get(i).d.isEmpty()) {
                return true;
            }
            i++;
        }
        return false;
    }

    public boolean Satisfied() {
        int i = 0;
        while (i < constraintSet.size()) {
            if (!constraintSet.get(i).isSatisfied()) {
                return false;
            }
            i++;
        }
        return true;
    }

    public void reduce() {
        if (!isSolved) {
            applyConstraints();
            if (!isEmpty()) {
                if (isReduced() && Satisfied()) {
                    isSolved = true;
                } else {
                    exploreDomains();
                }
            }
        }
    }

    private void applyConstraints() {
        for (int i = 0; i < constraintSet.size() && !isSRE; i++) {
            if (!constraintSet.get(i).reduce()) {
                isSRE = true;
            }
        }
    }

    private void exploreDomains() {
        for (int i = 0; i < variableSet.size(); i++) {
            if (!variableSet.get(i).d.isReducedToOnlyOneValue()) {
                List<Domain> domains = cloneDomains();
                Domain[] splitDomain = variableSet.get(i).d.split();
                variableSet.get(i).d = splitDomain[0];
                reduce();

                if (!isSolved) {
                    restoreDomains(domains);
                    if (!isSolved) {
                        domains = cloneDomains();
                        variableSet.get(i).d = splitDomain[1];
                        reduce();

                        if (!isSolved) {
                            restoreDomains(domains);
                        }
                    }
                }
            }
        }
    }

    private void restoreDomains(List<Domain> domains) {
        isSRE = false;
        for (int k = 0; k < domains.size(); k++) {
            variableSet.get(k).d = domains.get(k);
        }
    }

    public ArrayList<String> printAnswer(String string) {
        parse("data.txt");
        reduce();
        ArrayList<String> answer = new ArrayList<String>();
        for (Variable v : variableSet) {
            answer.add("Sol-" + v.name + "-" + v.d.vals[0]);
        }
        return answer;
    }

    public static void main(String[] args) {
        //long startTime = System.nanoTime();
        
        ConstraintSolver problem = new ConstraintSolver();
        ArrayList<String> answer = problem.printAnswer("data.txt");
        String output = buildOutput(answer);
        
        //long endTime = System.nanoTime();
        //long elapsedTime = endTime - startTime;
        //double elapsedTimeInSeconds = elapsedTime / 1_000_000_000.0;
        
        System.out.print(output);
        //System.out.printf("\nExecution time: %.6f seconds%n", elapsedTimeInSeconds);
    }

    private static String buildOutput(ArrayList<String> answer) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < answer.size(); i++) {
            output.append(answer.get(i));
            if (i != answer.size() - 1) {
                output.append("\n");
            }
        }
        return output.toString();
    }

}