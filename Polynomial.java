/* Zachary Evans 
 * CSE 274 Section A
 * Project 1
 * Dr. Mohammad
 * This project is a polynomial calculator that
 * performs various functions.
 */

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;

public class Polynomial {

	private ArrayList<Node> terms;

	//============================= Node Inner Class (represents a single term)
	private class Node implements Comparable<Node> {
		double coefficient;
		int exponent;

		public Node(double c, int e) {
			coefficient = c;
			exponent = e;
		} 

		// Changed the compareTo to account for ordering terms with 0 for exponent
		@Override
		public int compareTo( Node node ) {
			if(node.exponent == 0) {
				if(exponent != 0)	return -1000;
				else				return (int)(node.coefficient-coefficient);
			}
			return node.exponent - exponent;	
		}

		// 2 helper methods I added for toString and comparing terms
		@Override
		public String toString() {
			if(Math.abs(coefficient) < 0.0000000001) return "";

			String ret = ((coefficient < 0 ? " - " : " + ") + Math.abs(coefficient));
			if(exponent == 0) 	return ret;
			return ret + "x" + (exponent != 1 ? "^" + exponent : "");  // accounts for -exponents
		}

		public boolean equals(Node n) {
			return compareTo(n) == 0;
		}
	}


	//==================================================================== Constructors
	public Polynomial() { 														// default constructor		
		terms = new ArrayList<Node>();
	}

	public Polynomial(Polynomial poly) { // copy constructor
		this();
		ArrayList<Node> polyNodes = poly.terms;
		for (Node node : polyNodes) {
			addTerms(node);
		}
	}

	public Polynomial(ArrayList<Double> coef, ArrayList<Integer> expon) {		// workhorse
		if (coef.size() != expon.size()) {
			throw new InvalidParameterException();
		}
		terms = new ArrayList<Node>();
		for (int i = 0; i < coef.size(); i++) {
			addTerms(new Node(coef.get(i), expon.get(i)));
			Collections.sort(terms);
		}
	}
	
	/**
	 * This is a private method to help with addition
	 * @param node takes in a node to be added
	 */
	private void addTerms(Node node) {
		for (Node term: terms) {
			if (term.exponent == node.exponent) {
				term.coefficient += node.coefficient;
				return;
			}
		}
		terms.add(new Node(node.coefficient, node.exponent));
	}
	
	/**
	 * This is a private method to help with subtraction
	 * @param node takes in a node to be subtracted
	 */
	private void subTerms(Node node) {
		for (Node terms: terms) {
			if (terms.exponent == node.exponent) {
				terms.coefficient -= node.coefficient;
				return;
			}
		}
		terms.add(new Node(node.coefficient*-1, node.exponent));
		
	}
	

	//==================================================================== Methods
	// check all terms for equality
	/**
	 * Checks for equality
	 * @param poly the polynomial to be checked
	 * @return true if equal false otherwise
	 */
	public boolean equals(Polynomial poly) {
		boolean check = true;
		if (terms.size() == poly.terms.size()) {
			for (int i = 0; i < terms.size(); i++) {
				if (!(terms.get(i).equals(poly.terms.get(i)))) {
					check = false;
				}
			}
			if (check == false) { 
				return false;
			}
			else {
				return true;
			}
		} else {
		return false;
		}
	}

	//====================================================== Add
	/**
	 * adds two polynomials together
	 * @param poly the second polynomial to be added
	 * @return the resulting polynomial
	 */
	public Polynomial add(Polynomial poly) {
		Polynomial ret = new Polynomial(this);
		for (Node node : poly.terms) {
			ret.addTerms(node);
		}
		Collections.sort(ret.terms);
		return ret;		
	}

	//====================================================== Subtract
	/**
	 * Subtracts two polynomials
	 * @param poly the polynomial to be subtracted
	 * @return the resulting polynomial
	 */
	public Polynomial subtract(Polynomial poly) {
		Polynomial ret = new Polynomial(this);
		for (Node node : poly.terms) {
			ret.subTerms(node);
		}
		Collections.sort(ret.terms);
		return ret;		

	}

	//====================================================== Multiply
	/**
	 * Multiplies two polynomials together
	 * @param poly The polynomial to be multiplied by
	 * @return the resulting polynomial sorted
	 */
	public Polynomial multiply(Polynomial poly) {
		Polynomial ret = new Polynomial();
		for (Node node : poly.terms) {
			for (Node terms: terms) {
				ret.terms.add(new Node(terms.coefficient* node.coefficient, terms.exponent + node.exponent));
			}
		}
		Collections.sort(ret.terms);
	
		
		for (int i = 0; i < ret.terms.size(); i++) {
			for (int j = i+1; j < ret.terms.size(); j++) {
				if (ret.terms.get(j).exponent == ret.terms.get(i).exponent) {
					ret.terms.get(i).coefficient += ret.terms.get(j).coefficient;
					ret.terms.get(j).coefficient = 0;
					ret.terms.get(j).exponent = 0;
				}
			}
		}
		Collections.sort(ret.terms);
		return ret;	
	}

	//====================================================== Evaluate
	// Evaluates the polynomial with the parameter value for the variable
	/**
	 * Evaluates the polynomial for some value
	 * @param value the value to be plugged in
	 * @return the result
	 */
	public double evaluate(double value) { 
		double ret = 0.0;
		for (Node terms: terms) {
			ret += Math.pow(value, terms.exponent) * terms.coefficient;
		}
		return ret;
	}

	// finds the derivative of host polynomial: 
	//		Formula: (expo * coef) ^ (expo-1)
	/**
	 * Takes the derivative of the polynomial
	 * @return the resulting derivative sorted
	 */
	public Polynomial derivative() { 
		Polynomial ret = new Polynomial();
		int tempE = 0;
		double tempC = 0;
		for (Node terms : terms) {
			tempC = terms.exponent * terms.coefficient;
			tempE = terms.exponent -1;
			ret.terms.add(new Node(tempC, tempE));
		}
		Collections.sort(ret.terms);
		return ret;
	}

	//====================================================== toString
	/**
	 * Returns a string version of the polynomial that is sorted
	 */
	public String toString() {
		Collections.sort(terms);
		StringBuilder ret = new StringBuilder();
		for(Node n : terms)			
			ret.append( n.toString() );
		if(ret.length() == 0) return "0.0";
		ret.deleteCharAt(2);
		if(ret.charAt(1) == '+') 	ret.deleteCharAt(1);
		return ret.toString().trim();	
	}
}
