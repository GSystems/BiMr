var engine; //Variable to store instance of ScriptEngine Instance

function project_function() // script function for  Load Tester project BiMrStress
{
	var loadtest = engine.createLoadTest();
	loadtest.setRunDurationInTime(1/*minutes*/);
	loadtest.setRampUpTime(0);
	loadtest.setTestDescription('');
	loadtest.setBurstStrategy(10/*virtual users*/);
	loadtest.setRunProjectOnMultipleMachines(false);
	loadtest.setSaveSuccessfulResponseDetails(false);
	loadtest.setStopOnFailedTaskCount(ILoadTest.HITS_PER_USER, 10);
	loadtest.collectTopTasks(10);
	loadtest.collectFailedTasks(10);
	loadtest.setUseRecordedCookiesWhileReplaying(false);
	loadtest.setUseSameTimeOut(false);
	loadtest.setTimeout(120);
	loadtest.setSaveAllTaskDetails(false);
	loadtest.setTaskDetailsBufferSize(8192);
	loadtest.setTreatTimeOutAsFailed(true);
	loadtest.setRecordThinkTime(true);
	loadtest.setDefaultThinkTime(0);
	loadtest.setIgnoreThinkTimeWhileReplaying(true);
	loadtest.setResolveSubTask(true);
	loadtest.setResponseTimeInMilliSeconds(true);
	loadtest.setThroughputType(ILoadTest.THROUGHPUT_IN_KB);
	loadtest.setCompleteActionGroupAfterTestStop(ILoadTest.NO_COMPLETE_ANY_ACTION_GROUP);
	loadtest.setCloseConnectionAfterEachIteration(false);
	loadtest.setShowCombinedChart(false);
	loadtest.setShowSystemMonitorChart(false);
	loadtest.setShowGroupSummaryChart(false);
	loadtest.setShowDBChart(false);
	loadtest.setShowUserVsThroughputChart(false);
	loadtest.setShowScatterPlot(true);
	loadtest.setShowTaskResponseChart(true);
	loadtest.setShowAverage90Percent(false);
	loadtest.setShowSubTasks(false);
	loadtest.setIgnoreOrUpdateTaskBasedOnResponseTime(false, 0, 10, 0);
	loadtest.setSystemMonitor(false);
	loadtest.setSimulateBrowser(true);
	loadtest.setClearCacheAfterEachIteration(true);
	loadtest.setThrottleNetworkBandwith(false);
	loadtest.setNetworkBandwithFetchCriteria(ILoadTest.FETCH_SAME_NB);
	loadtest.setLoadBalancing(false);
	loadtest.setIPFetchCriteria(ILoadTest.FETCH_SAME_IP);
	loadtest.setResponseTimeDistribution(100);
	loadtest.setReplayLinkedProjects(false);
	loadtest.addLinkProjectGroup('BiMrStress', 'ActionGroup1', false, 1, 100.0);
	loadtest.setLoadTestDatabase(false, '');
	loadtest.setConnectionPoolSettings(true, -1, 50, 10, 50);
	loadtest.setCloseDBConnectionAfter(ILoadTest.EVERY_TASK);
	loadtest.setGroupRunStrategy(ILoadTest.SEQUENTIAL_GROUP_SELECTION, ILoadTest.START_ACTION_END_REPEAT);
	var group = loadtest.addStartingGroup();
	group.setGroupPauseOption(false, 0, IGroup.PAUSE_START_OF_TEST, 0);
	group.setGroupStopOption(false, IGroup.STOP_AFTER_N_ITERATIONS, 0);
	group.setStartingURL('');
	group.setContainsLoginTask(false);
	group.setUseUrlRewritingSessionManagement(false);
	group.setAuthenticationMechanism(IHttpGroup.NO_AUTHENTICATION);
	group.setAuthenticationDomainName('');
	group.setUseClientSSLAuthentication(false);
	var group = loadtest.addActionGroup(IGroup.GROUP_HTTP, 'ActionGroup1');
	group.setIgnored(false);
	group.setRepeatCount(1);
	group.setRunVirtualUsers(100.0);
	group.setGroupPauseOption(false, 0, IGroup.PAUSE_START_OF_TEST, 0);
	group.setGroupStopOption(false, IGroup.STOP_AFTER_N_ITERATIONS, 0);
	group.setStartingURL('');
	group.setContainsLoginTask(false);
	group.setUseUrlRewritingSessionManagement(false);
	group.setAuthenticationMechanism(IHttpGroup.NO_AUTHENTICATION);
	group.setAuthenticationDomainName('');
	group.setUseClientSSLAuthentication(false);
	var group = loadtest.addEndingGroup();
	group.setGroupPauseOption(false, 0, IGroup.PAUSE_START_OF_TEST, 0);
	group.setGroupStopOption(false, IGroup.STOP_AFTER_N_ITERATIONS, 0);
	group.setStartingURL('');
	group.setContainsLoginTask(false);
	group.setUseUrlRewritingSessionManagement(false);
	group.setAuthenticationMechanism(IHttpGroup.NO_AUTHENTICATION);
	group.setAuthenticationDomainName('');
	group.setUseClientSSLAuthentication(false);
}

function global_validation(request)
{
}

function group_1() // script function for StartingGroup
{
}

function group_2() // script function for ActionGroup1
{
}

function group_3() // script function for EndingGroup
{
}
