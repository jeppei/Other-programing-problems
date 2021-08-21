package Jobs.PoorMansParallelism;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class ComplexNumber {
    
    private double re;    // The real part
    private double im;    // The imaginary part
    
    ComplexNumber(double realPartIn, double complexPartIn) {
        re = realPartIn; 
        im = complexPartIn;
    }
    
    public double getRe() { return re; }
    public double getIm() { return im; }
    
    public ComplexNumber multiply(ComplexNumber cIn) {
        // (a+bi)(c+di) = (ac-bd) + (ad+bc)i
        double a = re;
        double b = im;
        double c = cIn.getRe();
        double d = cIn.getIm();
        return new ComplexNumber(a*c-b*d, a*d+b*c);
    }
    
    public void add(ComplexNumber z) {
        // (a+bi) + (c+di) = (a+c) + (b+d)i
        re = re + z.getRe();
        im = im + z.getIm();
    }
    
    public void add(double c, double d) {
        // (a+bi) + (c+di) = (a+c) + (b+d)i
        re = re + c;
        im = im + d;
    }
    
    public void setRe(double a) {
        re = a;
    }
    public void setIm(double a) {
        im = a;
    }
    
    public void squareMe() {
        // (a+bi)(a+bi) = (a^2-b^2) + 2abi
        double newRe = re*re-im*im;
        double newIm = 2*re*im;
        re = newRe;
        im = newIm;
    }
    
    public void squareMeAndAdd(ComplexNumber z) {
        // (a+bi)^2 + (c+di) = (a^2-b^2) + 2abi + (c+di) 
        //                   = (a^2-b^2+c) + (2ab+d)i
        double newRe = (re*re-im*im) + (z.getRe());
        double newIm = (2*re*im)     + (z.getIm());
        re = newRe;
        im = newIm;
    }
    
    public double abs() {
        //|(a+bi)| = sqrt( a^2 + b^2 )
        return Math.sqrt(re*re+im*im);
    } 
    
    @Override
    public String toString() {
        return String.format("%.2f", re) + " + " + String.format("%.2f", im) + "i";
    }
}
