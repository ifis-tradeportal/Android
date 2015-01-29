package com.example.ifis.utilities;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class RandomSessionString {
	
		  private SecureRandom random = new SecureRandom();

		  public String getSessionId() {
		    return new BigInteger(130, random).toString(32);
		  }

}
