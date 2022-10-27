package main.request;

public class Euler {
    // is n prime?
    public static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i*i <= n; i++)
            if (n % i == 0) return false;
        return true;
    }

    //Find the sum of all the multiples of 3 or 5 below 1000.
    public static int euler1() {
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            if (i % 3 == 0 || i % 5 == 0) {
                sum += i;
            }
        }
        return sum;
    }

    // Which prime, below one-million, can be written as the sum of the most consecutive primes?
public static int euler50() {
        int max = 1000000;
        int[] primes = new int[max];
        int count = 0;
        for (int i = 2; i < max; i++) {
            if (isPrime(i)) {
                primes[count] = i;
                count++;
            }
        }
        int maxCount = 0;
        int maxPrime = 0;
        for (int i = 0; i < count; i++) {
            int sum = 0;
            int j = i;
            while (sum < max) {
                sum += primes[j];
                if (isPrime(sum) && j - i > maxCount) {
                    maxCount = j - i;
                    maxPrime = sum;
                }
                j++;
            }
        }
        return maxPrime;
    }




}
