/**
 * 
 */
package statsMetrics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test case : One node Network
 * Statistics and Metrics Package
 * @author axm1064  ID: 1402590
 * @since 08/09/2014
 *
 */
public class UnitTest_One_Node {

	
	private  StringBuilder strInputData = new StringBuilder("");
	private StringBuilder strExpectedData = new StringBuilder("");

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		strInputData.append("{\"elements\":{\"algorithm\":\"metrics\",\"nodes\":[{\"data\":{\"id\":\"a\",\"name\":\"a\"}}]}}");
		
		strExpectedData.append("{\"metrics\": {\"nodes\":[ {\"data\":{\"id\":\"a\",\"name\":\"a\",\"deg\":\"0\"}}],\"stats\":[ {\"data\":{\"id\":\"Number Of Nodes\",\"val\":\"1\"}},{\"data\":{\"id\":\"Number Of Edges\",\"val\":\"0\"}},{\"data\":{\"id\":\"Density\",\"val\":\"NaN\"}},{\"data\":{\"id\":\"Characteristic Path Length\",\"val\":\"NaN\"}},{\"data\":{\"id\":\"Diameter\",\"val\":\"0.0\"}},{\"data\":{\"id\":\"Degree Centralization\",\"val\":\"NaN\"}},{\"data\":{\"id\":\"Closeness Centralization\",\"val\":\"NaN\"}},{\"data\":{\"id\":\"Edge Betweenness\",\"val\":\"NaN\"}},{\"data\":{\"id\":\"Betweenness Centralization\",\"val\":\"NaN\"}}]}} ");
		
	}

	@Test
	public void testOneNode() {
		
		  metrics stat = new metrics(strInputData.toString());
		  String strResult = stat.metricsToJson();
		
		assertTrue(strResult.equals(strExpectedData.toString()));
	}

}
