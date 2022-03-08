# DB Connectivity Checker with Tomcat JDBC Pool

The tool can be used to configure a DB connection pool with Tomcat JDBC and periodically run database queries to check 
DB connectivity issues. Some logic used here is burrowed from [WSO2 Customer Success Troubleshoot Kit](https://github.com/wso2-cs/troubleshoot-kit/tree/master/database-response-timing).
Connectivity stat logs are printed in JSON format to make it easy to analyse.

## Configuration

Configuration parameters needs to be specified by environment parameters. There are two types of parameters supported:

**DB_CHECKER_{PARAM}**: Application Specific Parameters<br>
**TOMCAT_JDBC_{PARAM}**: Tomcat JDBC Pool Configuration

List of supported Application Specific Parameters:

|Environment Variable|Description|Default Value|
|---|---|---|
|DB_CHECKER_TOTAL_ITERATIONS|Total Number of Iterations The application run with each iteration using a getConnection()| 10|
|DB_CHECKER_ITERATIONS_PER_CONNECTION|Number of times the specified query to run in each iteration using a single connection|5|
|DB_CHECKER_SLEEP_TIME_MS|Number of milliseconds to delay between each iteration|5000|
|DB_CHECKER_QUERY|SQL query to run|SELECT 1|
|DB_CHECKER_ITERATE_RESULT_SET|Whether to iterate the results returned by the specified query|true|
|DB_CHECKER_LOG_RESULT_SET|Whether to log the results returned by the specified query|true|

The list of supported Tomcat JDBC Pool Properties are listed below. For more details on Tomcat JDBC Pool properties, 
have a look at the [documentation](https://tomcat.apache.org/tomcat-9.0-doc/jdbc-pool.html).

|Environment Variable|Corresponding TomcatJDBC Pool Property|
|---|---|
|TOMCAT_JDBC_ABANDON_WHEN_PERCENTAGE_FULL|abandonWhenPercentageFull|
|TOMCAT_JDBC_ACCESS_TO_UNDERLYING_CONNECTION_ALLOWED|accessToUnderlyingConnectionAllowed|
|TOMCAT_JDBC_ALTERNATE_USERNAME_ALLOWED|alternateUsernameAllowed|
|TOMCAT_JDBC_COMMIT_ON_RETURN|commitOnReturn|
|TOMCAT_JDBC_DEFAULT_AUTO_COMMIT|defaultAutoCommit|
|TOMCAT_JDBC_DEFAULT_CATALOG|defaultCatalog|
|TOMCAT_JDBC_DEFAULT_READ_ONLY|defaultReadOnly|
|TOMCAT_JDBC_DEFAULT_TRANSACTION_ISOLATION|defaultTransactionIsolation|
|TOMCAT_JDBC_DRIVER_CLASS_NAME|driverClassName|
|TOMCAT_JDBC_FAIR_QUEUE|fairQueue|
|TOMCAT_JDBC_IGNORE_EXCEPTION_ON_PRE_LOAD|ignoreExceptionOnPreLoad|
|TOMCAT_JDBC_INITIAL_SIZE|initialSize|
|TOMCAT_JDBC_INIT_SQL|initSQL|
|TOMCAT_JDBC_JDBC_INTERCEPTORS|jdbcInterceptors|
|TOMCAT_JDBC_JMX_ENABLED|jmxEnabled|
|TOMCAT_JDBC_LOG_ABANDONED|logAbandoned|
|TOMCAT_JDBC_LOGIN_TIMEOUT|loginTimeout|
|TOMCAT_JDBC_LOG_VALIDATION_ERRORS|logValidationErrors|
|TOMCAT_JDBC_MAX_ACTIVE|maxActive|
|TOMCAT_JDBC_MAX_AGE|maxAge|
|TOMCAT_JDBC_MAX_IDLE|maxIdle|
|TOMCAT_JDBC_MAX_WAIT|maxWait|
|TOMCAT_JDBC_MIN_EVICTABLE_IDLE_TIME_MILLIS|minEvictableIdleTimeMillis|
|TOMCAT_JDBC_MIN_IDLE|minIdle|
|TOMCAT_JDBC_NAME|name|
|TOMCAT_JDBC_NUM_TESTS_PER_EVICTION_RUN|numTestsPerEvictionRun|
|TOMCAT_JDBC_PASSWORD|password|
|TOMCAT_JDBC_PROPAGATE_INTERRUPT_STATE|propagateInterruptState|
|TOMCAT_JDBC_CEMOVE_ABANDONED|removeAbandoned|
|TOMCAT_JDBC_CEMOVE_ABANDONED_TIMEOUT|removeAbandonedTimeout|
|TOMCAT_JDBC_COLLBACK_ON_RETURN|rollbackOnReturn|
|TOMCAT_JDBC_SUSPECT_TIMEOUT|suspectTimeout|
|TOMCAT_JDBC_TEST_ON_BORROW|testOnBorrow|
|TOMCAT_JDBC_TEST_ON_CONNECT|testOnConnect|
|TOMCAT_JDBC_TEST_ON_RETURN|testOnReturn|
|TOMCAT_JDBC_TEST_WHILE_IDLE|testWhileIdle|
|TOMCAT_JDBC_TIME_BETWEEN_EVICTION_RUNS_MILLIS|timeBetweenEvictionRunsMillis|
|TOMCAT_JDBC_URL|url|
|TOMCAT_JDBC_USE_DISPOSABLE_CONNECTION_FACADE|useDisposableConnectionFacade|
|TOMCAT_JDBC_USE_EQUALS|useEquals|
|TOMCAT_JDBC_USE_LOCK|useLock|
|TOMCAT_JDBC_USERNAME|username|
|TOMCAT_JDBC_USE_STATEMENT_FACADE|useStatementFacade|
|TOMCAT_JDBC_VALIDATION_INTERVAL|validationInterval|
|TOMCAT_JDBC_VALIDATION_QUERY|validationQuery|
|TOMCAT_JDBC_VALIDATION_QUERY_TIMEOUT|validationQueryTimeout|
|TOMCAT_JDBC_VALIDATOR_CLASS_NAME|validatorClassName|

## Docker Compose

To make it easy for running in container environments, the tool is published to docker-hub under [malinthaprasan/db-checker-tomcat:1.0](https://hub.docker.com/r/malinthaprasan/db-checker-tomcat)

Sample docker-compose.yaml configurations for `mssql` and `mysql` are available under `docker-compose` folder.

### mssql
```shell
$ cd docker-compose/mssql
$ docker-compose up
```
### mysql
```shell
$ cd docker-compose/mysql
$ docker-compose up
```

## Sample Log

```
{ "time": "2022-02-28 17:13:21", "type": "INFO ", "class": "Main:133", "log":{ "event": "getConnectionStart", "conIteration" : 1}}
{ "time": "2022-02-28 17:13:21", "type": "INFO ", "class": "Main:136", "log":{ "event": "getConnectionEnd", "conIteration": 1, "getConnectionTimeMs" : 488}}
{ "time": "2022-02-28 17:13:21", "type": "INFO ", "class": "Main:138", "log":{ "event": "perConnectionIterationStart", "conIteration" : 1, "perConIterations" : 5,"query" : SELECT API_NAME FROM AM_API}}
{ "time": "2022-02-28 17:13:21", "type": "INFO ", "class": "Main:142", "log":{ "event": "createStatementStart", "conIteration" : 1, "perConIteration" : 1}}
{ "time": "2022-02-28 17:13:21", "type": "INFO ", "class": "Main:146", "log":{ "event": "executeQueryStart", "conIteration" : 1, "perConIteration" : 1}}
{ "time": "2022-02-28 17:13:22", "type": "INFO ", "class": "Main:149", "log":{ "event": "executeQueryEnd", "conIteration" : 1, "perConIteration" : 1, "executeQueryTimeMs": 17}}
{ "time": "2022-02-28 17:13:22", "type": "INFO ", "class": "Main:142", "log":{ "event": "createStatementStart", "conIteration" : 1, "perConIteration" : 2}}
{ "time": "2022-02-28 17:13:22", "type": "INFO ", "class": "Main:146", "log":{ "event": "executeQueryStart", "conIteration" : 1, "perConIteration" : 2}}
{ "time": "2022-02-28 17:13:22", "type": "INFO ", "class": "Main:149", "log":{ "event": "executeQueryEnd", "conIteration" : 1, "perConIteration" : 2, "executeQueryTimeMs": 1}}
{ "time": "2022-02-28 17:13:27", "type": "INFO ", "class": "Main:133", "log":{ "event": "getConnectionStart", "conIteration" : 2}}
{ "time": "2022-02-28 17:13:27", "type": "INFO ", "class": "Main:136", "log":{ "event": "getConnectionEnd", "conIteration": 2, "getConnectionTimeMs" : 0}}
```
