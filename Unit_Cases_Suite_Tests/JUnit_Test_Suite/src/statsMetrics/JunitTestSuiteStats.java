package statsMetrics;

/**
 * Test suite 
 * Group of Unit test cases 
 * related with the Statistics and Metrics Package
 * @author axm1064  ID: 1402590
 * @since 0809//2014
 *
 */

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	UnitTest_One_Node.class, 
	UnitTest_Two_Nodes.class, 
	UnitTest_Two_Indep_Nodes.class, 
	UnitTest_Three_Nodes.class, 
	UnitTest_Three_Indep_Nodes.class	
})

public class JunitTestSuiteStats {
	
	 public static void main(String[] args) {
		 
		 System.out.println("Running a Junit Test Suite for the Statistics/Metrics Package ... ");
		 
	      Result result = JUnitCore.runClasses(JunitTestSuiteStats.class);
	      for (Failure failure : result.getFailures()) {
	         System.out.println("Failure: " + failure.toString());
	      }
	      System.out.println("JunitTestSuite_StatsPack  -->  Result wasSuccessful = " + result.wasSuccessful());
	   }
	   
	   

}
