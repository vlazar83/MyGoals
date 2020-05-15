package com.vlazar83.mygoals;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void randomization() {
        for(int i = 0; i<100;i++){

            MyAssertions.assertDoesNotThrow(() ->
                    System.out.println(Settings.getInstance().getRandomGoldenSentence()));

        }
    }

}

class MyAssertions{
    public static void assertDoesNotThrow(FailingRunnable action){
        try{
            action.run();
        }
        catch(Exception ex){
            throw new Error("expected action not to throw, but it did!", ex);
        }
    }
}

@FunctionalInterface interface FailingRunnable { void run() throws Exception; }