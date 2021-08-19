/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage;

import static org.junit.Assert.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.ComparisonControllers;
import org.xmlunit.diff.Diff;

/**
 * The Class NewFutureTraderApplicationTests.
 */
@SpringBootTest
class NewFutureTraderApplicationTests {
  
  /**
   * Compare xml.
   */
  @Test
  void compareXml() {
    final String control = "<oromxml><meta><perf rq_id=\"a4c5f655-e3ee-4fe0-8e32-eb6e0fcb4ba3\" ts_rq_rcvd=\"2021-05-31T09:17:49:881+0.00\" "
	    		+ "ts_rq_svc_call=\"2021-05-31T09:17:49:881+0.00\" ts_rq_db_rcvd=\"2021-05-31&apos;T&apos;14:48:51.096+00:00\" ts_rq_db_done=\"2021-05-31&apos;T&apos;14:48:51.120+00:00\"/><session token=\" \"/> </meta></oromxml>";
    final String test = "<oromxml><meta><perf rq_id=\"a4c5f655-e3ee-4fe0-8e32-eb6e0fcb4ba3\" ts_rq_rcvd=\"2021-05-31T09:17:49:881+0.00\" "
	    		+ "ts_rq_svc_call=\"2021-05-31T09:17:49:881+0.00\" ts_rq_db_rcvd=\"2021-05-31&apos;T&apos;14:48:51.096+00:00\" ts_rq_db_done=\"2021-05-31&apos;T&apos;14:48:51.120+00:00\"/><session token=\" \"/> </meta></oromxml>";
    Diff myDiff = DiffBuilder
                  .compare(control)
                  .withTest(test)
                  .withComparisonController(ComparisonControllers.StopWhenDifferent)
                  .build();
    assertFalse(myDiff.toString(), myDiff.hasDifferences());
  }
}
