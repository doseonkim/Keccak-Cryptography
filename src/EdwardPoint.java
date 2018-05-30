import java.math.BigInteger;

public class EdwardPoint {

	public BigInteger x;
	
	public BigInteger y; // = new BigInteger("5791495046070831845648263565746991846022619976177402329865390891430932500895492240974901064055209720354272289114195063034690642284077346568207160764098338120");
	
	public EdwardPoint() {	
		this(BigInteger.ZERO, BigInteger.ONE);
	}

	public EdwardPoint(BigInteger x) {
		this(x, solve_y(x));
	}
	
	public EdwardPoint(BigInteger x, BigInteger y) {	
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(EdwardPoint other) {
		return this.x.equals(other.x) && this.y.equals(other.y);
	}
	
	public EdwardPoint negate() {
		BigInteger n_x = x.multiply(new BigInteger("-1"));
		return new EdwardPoint(n_x, this.y);
	}
	
	public void add(EdwardPoint o) {
		BigInteger x1 = this.x;
		BigInteger y1 = this.y;
		BigInteger x2 = o.x;
		BigInteger y2 = o.y;
		BigInteger x_top = (x1.multiply(y2)).add((y1.multiply(x2)));
		BigInteger x_bot = (BigInteger.ONE.add((EdwardCurve.d.multiply(x1).multiply(x2).multiply(y1).multiply(y2)))).modInverse(EdwardCurve.p);
		
		BigInteger y_top = (y1.multiply(y2)).subtract((x1.multiply(x2)));
		BigInteger y_bot = (BigInteger.ONE.subtract((EdwardCurve.d.multiply(x1).multiply(x2).multiply(y1).multiply(y2)))).modInverse(EdwardCurve.p);
		
		BigInteger x_new = (x_top.multiply(x_bot)).mod(EdwardCurve.p);
		BigInteger y_new = (y_top.multiply(y_bot)).mod(EdwardCurve.p);
		
		this.x = x_new;
		this.y = y_new;
	}
	
	public EdwardPoint exponentiation(BigInteger s, EdwardPoint G) {
		EdwardPoint Y = new EdwardPoint(G.x, G.y);
		for (int i = s.bitCount()-1; i >= 0; i--) {
			Y.add(Y);
			if (s.testBit(i)) 
				Y.add(G);
		}
		return Y;
	}
	
	private static BigInteger solve_y(BigInteger x) {
		BigInteger num = BigInteger.ONE.subtract(x.pow(2));
		BigInteger den = BigInteger.ONE.subtract((EdwardCurve.d.multiply(x.pow(2))));
		BigInteger v = num.multiply(den.modInverse(EdwardCurve.p));
		BigInteger y = sqrt(v, EdwardCurve.p, false);
		return y;
	}
	
	public void print() {
		System.out.println("x: " + x + "\ny: " + y);
	}
	
	/**
	* Compute a square root of v mod p with a specified
	* least significant bit, if such a root exists.
	*
	* @param v the radicand.
	* @param p the modulus (must satisfy p mod 4 = 3).
	* @param lsb desired least significant bit (true: 1, false: 0).
	* @return a square root r of v mod p with r mod 2 = 1 iff lsb = true
	* if such a root exists, otherwise null.
	*/
	public static BigInteger sqrt(BigInteger v, BigInteger p, boolean lsb) {
		assert (p.testBit(0) && p.testBit(1)); // p = 3 (mod 4)
		if (v.signum() == 0) {
			return BigInteger.ZERO;
		}
		BigInteger r = v.modPow(p.shiftRight(2).add(BigInteger.ONE), p);
		if (r.testBit(0) != lsb) {
			r = p.subtract(r); // correct the lsb
		}
			return (r.multiply(r).subtract(v).mod(p).signum() == 0) ? r : null;
	}
	
}
