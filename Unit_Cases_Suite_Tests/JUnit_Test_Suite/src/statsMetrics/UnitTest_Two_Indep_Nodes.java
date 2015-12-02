
package statsMetrics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
* Unit test case : Two independent nodes Network
* Statistics and Metrics Package
* @author axm1064  ID: 1402590
* @since 08/09/2014
* 
**/
public class UnitTest_Two_Indep_Nodes {

	private  StringBuilder strInputData = new StringBuilder("");
	private StringBuilder strExpectedData = new StringBuilder("");

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		strInputData.append("{\"elements\":{\"algorithm\":\"metrics\",\"nodes\":[{\"data\":{\"id\":\"a\",\"name\":\"a\"}},{\"data\":{\"id\":\"b\",\"name\":\"b\"}}]}}");
		strExpectedData.append("{\"metrics\": {\"nodes\":[ {\"data\":{\"id\":\"a\",\"name\":\"a\",\"deg\":\"0\"}},{\"data\":{\"id\":\"b\",\"name\":\"b\",\"deg\":\"0\"}}],\"stats\":[ {\"data\":{\"id\":\"Number Of Nodes\",\"val\":\"2\"}},{\"data\":{\"id\":\"Number Of Edges\",\"val\":\"0\"}},{\"data\":{\"id\":\"Density\",\"val\":\"0.0\"}},{\"data\":{\"id\":\"Characteristic Path Length\",\"val\":\"0.0\"}},{\"data\":{\"id\":\"Diameter\",\"val\":\"0.0\"}},{\"data\":{\"id\":\"Degree Centralization\",\"val\":\"NaN\"}},{\"data\":{\"id\":\"Closeness Centralization\",\"val\":\"NaN\"}},{\"data\":{\"id\":\"Edge Betweenness\",\"val\":\"0.0\"}},{\"data\":{\"id\":\"Betweenness Centralization\",\"val\":\"NaN\"}}]}} ");
		
	}

	@Test
	public void testTwoIndepNodes() {
		
		  metrics stat = new metrics(strInputData.toString());
		  String strResult = stat.metricsToJson();
		
		assertTrue(strResult.equals(strExpectedData.toString()));
	}

}
