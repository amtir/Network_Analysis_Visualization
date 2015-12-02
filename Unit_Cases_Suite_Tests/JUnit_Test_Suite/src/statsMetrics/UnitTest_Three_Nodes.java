
package statsMetrics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
* Unit test case : Three nodes Network (linked)
* Statistics and Metrics Package
* @author axm1064  ID: 1402590
* @since 08/09/2014
* 
**/
public class UnitTest_Three_Nodes {

	private  StringBuilder strInputData = new StringBuilder("");
	private StringBuilder strExpectedData = new StringBuilder("");

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		strInputData.append("{\"elements\":{\"algorithm\":\"metrics\",\"nodes\":[{\"data\":{\"id\":\"a\",\"name\":\"a\"}},{\"data\":{\"id\":\"b\",\"name\":\"b\"}},{\"data\":{\"id\":\"d\",\"name\":\"d\"}}],\"edges\":[{\"data\":{\"source\":\"a\",\"target\":\"b\"}},{\"data\":{\"source\":\"a\",\"target\":\"d\"}}]}}");
		
		strExpectedData.append("{\"metrics\": {\"nodes\":[ {\"data\":{\"id\":\"a\",\"name\":\"a\",\"deg\":\"2\"}},{\"data\":{\"id\":\"b\",\"name\":\"b\",\"deg\":\"1\"}},{\"data\":{\"id\":\"d\",\"name\":\"d\",\"deg\":\"1\"}}],\"edges\":[{\"data\":{\"source\":\"a\",\"target\":\"b\"}},{\"data\":{\"source\":\"a\",\"target\":\"d\"}}],\"stats\":[ {\"data\":{\"id\":\"Number Of Nodes\",\"val\":\"3\"}},{\"data\":{\"id\":\"Number Of Edges\",\"val\":\"2\"}},{\"data\":{\"id\":\"Density\",\"val\":\"0.6666666666666666\"}},{\"data\":{\"id\":\"Characteristic Path Length\",\"val\":\"1.3333333333333333\"}},{\"data\":{\"id\":\"Diameter\",\"val\":\"2.0\"}},{\"data\":{\"id\":\"Degree Centralization\",\"val\":\"1.0\"}},{\"data\":{\"id\":\"Closeness Centralization\",\"val\":\"1.0000000000000004\"}},{\"data\":{\"id\":\"Edge Betweenness\",\"val\":\"0.25\"}},{\"data\":{\"id\":\"Betweenness Centralization\",\"val\":\"1.0\"}}]}} ");
	}

	@Test
	public void testThreeNodes() {
		
		  metrics stat = new metrics(strInputData.toString());
		  String strResult = stat.metricsToJson();
		
		assertTrue(strResult.equals(strExpectedData.toString()));
	}

}
