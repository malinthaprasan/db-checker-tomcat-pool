package com.example.dbchecker.tomcatpool;

import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;


public class Main {
    public static final String ENV_PREFIX_POOL = "TOMCAT_JDBC_";
    public static final String ENV_PREFIX_APP = "DB_CHECKER_";

    public static final String ENV_TOTAL_ITERATIONS = ENV_PREFIX_APP + "TOTAL_ITERATIONS";
    public static final String ENV_ITERATIONS_PER_CON = ENV_PREFIX_APP + "ITERATIONS_PER_CONNECTION";
    public static final String ENV_SLEEP_TIME_MS = ENV_PREFIX_APP + "SLEEP_TIME_MS";
    public static final String ENV_QUERY = ENV_PREFIX_APP + "QUERY";
    public static final String ENV_LOG_RESULT_SET = ENV_PREFIX_APP + "LOG_RESULT_SET";
    public static final String ENV_ITERATE_RESULT_SET = ENV_PREFIX_APP + "ITERATE_RESULT_SET";

    public static int totalIterations = 10;
    public static int iterationsPerConnection = 5;
    public static int sleepTimeMs = 5000;
    public static String query = "SELECT 1";
    public static boolean logResultSet = true;
    public static boolean iterateResultSet = true;

    final static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) throws NamingException, SQLException, InterruptedException {
        DataSource ds = new DataSource();

        getEnvAndApplyInt("ABANDON_WHEN_PERCENTAGE_FULL", ds::setAbandonWhenPercentageFull);
        getEnvAndApplyBool("ACCESS_TO_UNDERLYING_CONNECTION_ALLOWED", ds::setAccessToUnderlyingConnectionAllowed);
        getEnvAndApplyBool("ALTERNATE_USERNAME_ALLOWED", ds::setAlternateUsernameAllowed);
        getEnvAndApplyBool("COMMIT_ON_RETURN", ds::setCommitOnReturn);
        getEnvAndApplyBool("DEFAULT_AUTO_COMMIT", ds::setDefaultAutoCommit);
        getEnvAndApply("DEFAULT_CATALOG", ds::setDefaultCatalog);
        getEnvAndApplyBool("DEFAULT_READ_ONLY", ds::setDefaultReadOnly);
        getEnvAndApplyInt("DEFAULT_TRANSACTION_ISOLATION", ds::setDefaultTransactionIsolation);
        getEnvAndApply("DRIVER_CLASS_NAME", ds::setDriverClassName);
        getEnvAndApplyBool("FAIR_QUEUE", ds::setFairQueue);
        getEnvAndApplyBool("IGNORE_EXCEPTION_ON_PRE_LOAD", ds::setIgnoreExceptionOnPreLoad);
        getEnvAndApplyInt("INITIAL_SIZE", ds::setInitialSize);
        getEnvAndApply("INIT_SQL", ds::setInitSQL);
        getEnvAndApply("JDBC_INTERCEPTORS", ds::setJdbcInterceptors);
        getEnvAndApplyBool("JMX_ENABLED", ds::setJmxEnabled);
        getEnvAndApplyBool("LOG_ABANDONED", ds::setLogAbandoned);
        getEnvAndApplyInt("LOGIN_TIMEOUT", ds::setLoginTimeout);
        getEnvAndApplyBool("LOG_VALIDATION_ERRORS", ds::setLogValidationErrors);
        getEnvAndApplyInt("MAX_ACTIVE", ds::setMaxActive);
        getEnvAndApplyInt("MAX_AGE", ds::setMaxAge);
        getEnvAndApplyInt("MAX_IDLE", ds::setMaxIdle);
        getEnvAndApplyInt("MAX_WAIT", ds::setMaxWait);
        getEnvAndApplyInt("MIN_EVICTABLE_IDLE_TIME_MILLIS", ds::setMinEvictableIdleTimeMillis);
        getEnvAndApplyInt("MIN_IDLE", ds::setMinIdle);
        getEnvAndApply("NAME", ds::setName);
        getEnvAndApplyInt("NUM_TESTS_PER_EVICTION_RUN", ds::setNumTestsPerEvictionRun);
        getEnvAndApply("PASSWORD", ds::setPassword);
        getEnvAndApplyBool("PROPAGATE_INTERRUPT_STATE", ds::setPropagateInterruptState);
        getEnvAndApplyBool("REMOVE_ABANDONED", ds::setRemoveAbandoned);
        getEnvAndApplyInt("REMOVE_ABANDONED_TIMEOUT", ds::setRemoveAbandonedTimeout);
        getEnvAndApplyBool("ROLLBACK_ON_RETURN", ds::setRollbackOnReturn);
        getEnvAndApplyInt("SUSPECT_TIMEOUT", ds::setSuspectTimeout);
        getEnvAndApplyBool("TEST_ON_BORROW", ds::setTestOnBorrow);
        getEnvAndApplyBool("TEST_ON_CONNECT", ds::setTestOnConnect);
        getEnvAndApplyBool("TEST_ON_RETURN", ds::setTestOnReturn);
        getEnvAndApplyBool("TEST_WHILE_IDLE", ds::setTestWhileIdle);
        getEnvAndApplyInt("TIME_BETWEEN_EVICTION_RUNS_MILLIS", ds::setTimeBetweenEvictionRunsMillis);
        getEnvAndApply("URL", ds::setUrl);
        getEnvAndApplyBool("USE_DISPOSABLE_CONNECTION_FACADE", ds::setUseDisposableConnectionFacade);
        getEnvAndApplyBool("USE_EQUALS", ds::setUseEquals);
        getEnvAndApplyBool("USE_LOCK", ds::setUseLock);
        getEnvAndApply("USERNAME", ds::setUsername);
        getEnvAndApplyBool("USE_STATEMENT_FACADE", ds::setUseStatementFacade);
        getEnvAndApplyInt("VALIDATION_INTERVAL", ds::setValidationInterval);
        getEnvAndApply("VALIDATION_QUERY", ds::setValidationQuery);
        getEnvAndApplyInt("VALIDATION_QUERY_TIMEOUT", ds::setValidationQueryTimeout);
        getEnvAndApply("VALIDATOR_CLASS_NAME", ds::setValidatorClassName);

        if (System.getenv(ENV_TOTAL_ITERATIONS) != null &&
                !System.getenv(ENV_TOTAL_ITERATIONS).isEmpty()) {
            totalIterations = Integer.parseInt(System.getenv(ENV_TOTAL_ITERATIONS));
        }

        if (System.getenv(ENV_ITERATIONS_PER_CON) != null &&
                !System.getenv(ENV_ITERATIONS_PER_CON).isEmpty()) {
            iterationsPerConnection = Integer.parseInt(System.getenv(ENV_ITERATIONS_PER_CON));
        }

        if (System.getenv(ENV_SLEEP_TIME_MS) != null &&
                !System.getenv(ENV_SLEEP_TIME_MS).isEmpty()) {
            sleepTimeMs = Integer.parseInt(System.getenv(ENV_SLEEP_TIME_MS));
        }

        if (System.getenv(ENV_QUERY) != null &&
                !System.getenv(ENV_QUERY).isEmpty()) {
            query = System.getenv(ENV_QUERY);
        }

        if (System.getenv(ENV_LOG_RESULT_SET) != null &&
                !System.getenv(ENV_LOG_RESULT_SET).isEmpty()) {
            logResultSet = Boolean.parseBoolean(System.getenv(ENV_LOG_RESULT_SET));
        }

        if (System.getenv(ENV_ITERATE_RESULT_SET) != null &&
                !System.getenv(ENV_ITERATE_RESULT_SET).isEmpty()) {
            iterateResultSet = Boolean.parseBoolean(System.getenv(ENV_ITERATE_RESULT_SET));
        }

        log.info("{ \"event\": \"appliedParam\", \"param\": \"" + ENV_TOTAL_ITERATIONS + "\", \"value\": \"" + totalIterations + "\"}");
        log.info("{ \"event\": \"appliedParam\", \"param\": \"" + ENV_ITERATIONS_PER_CON + "\", \"value\": \"" + iterationsPerConnection + "\"}");
        log.info("{ \"event\": \"appliedParam\", \"param\": \"" + ENV_SLEEP_TIME_MS + "\", \"value\": \"" + sleepTimeMs + "\"}");
        log.info("{ \"event\": \"appliedParam\", \"param\": \"" + ENV_QUERY + "\", \"value\": \"" + query + "\"}");
        log.info("{ \"event\": \"appliedParam\", \"param\": \"" + ENV_LOG_RESULT_SET + "\", \"value\": \"" + logResultSet + "\"}");
        log.info("{ \"event\": \"appliedParam\", \"param\": \"" + ENV_ITERATE_RESULT_SET + "\", \"value\": \"" + iterateResultSet + "\"}");

        // execute queries
        afterPropertiesSet(ds);
    }

    public static void afterPropertiesSet(DataSource dataSource) throws InterruptedException {
        int j = 1;
        if (totalIterations < 0) {
            log.info("Running till interrupting.....!");
        }
        while (totalIterations < 0 || j <= totalIterations) {
            log.info("{ \"event\": \"getConnectionStart\", \"conIteration\" : " + j + "}");
            long beforeConnection = System.currentTimeMillis();
            try (Connection connection = dataSource.getConnection()) {
                log.info("{ \"event\": \"getConnectionEnd\", \"conIteration\": " + j + ", \"getConnectionTimeMs\" : "
                        + (System.currentTimeMillis() - beforeConnection) + "}");
                log.info("{ \"event\": \"perConnectionIterationStart\", \"conIteration\" : " + j +
                        ", \"perConIterations\" : " + iterationsPerConnection +
                        ",\"query\" : " + query + "}");
                for (int i = 1; i <= iterationsPerConnection; i++) {
                    log.info("{ \"event\": \"createStatementStart\", \"conIteration\" : " + j + ", " +
                            "\"perConIteration\" : " + i + "}");
                    try (Statement st = connection.createStatement()) {
                        long beforeQuery = System.currentTimeMillis();
                        log.info("{ \"event\": \"executeQueryStart\", \"conIteration\" : " + j + ", " +
                                "\"perConIteration\" : " + i + "}");
                        try (ResultSet rs = st.executeQuery(query)) {
                            log.info("{ \"event\": \"executeQueryEnd\", \"conIteration\" : " + j + ", " +
                                    "\"perConIteration\" : " + i + ", " +
                                    "\"executeQueryTimeMs\": " + (System.currentTimeMillis() - beforeQuery) + "}");
                            long beforeRs = System.currentTimeMillis();
                            if (iterateResultSet) {
                                log.info("{ \"event\": \"iterateResultSetStart\", \"conIteration\" : " + j + ", " +
                                        "\"perConIteration\" : " + i + "}");
                                int count = 0;
                                ResultSetMetaData rsmd = rs.getMetaData();
                                int columnsNumber = rsmd.getColumnCount();
                                while (rs.next()) {
                                    count++;
                                    if (logResultSet) {
                                        StringBuilder resultString = new StringBuilder();
                                        resultString.append("{ ");
                                        for (int n = 1; n <= columnsNumber; n++) {
                                            try {
                                                String columnValue = rs.getString(n);
                                                resultString.append("\"")
                                                        .append(rsmd.getColumnName(n))
                                                        .append("\": \"")
                                                        .append(columnValue)
                                                        .append("\"");
                                                if (n != columnsNumber) {
                                                    resultString.append(",");
                                                }
                                            } catch (ClassCastException e) {
                                                log.error("Error while casting java.sql.Type: " +
                                                        rsmd.getColumnType(n) + " to string", e);
                                            }
                                        }
                                        resultString.append(" }");
                                        log.info("{ \"event\": \"logResultSetItem\", \"conIteration\" : " + j + ", " +
                                                "\"perConIteration\" : " + i + ", \"resultItem\": " + count + " " +
                                                ", \"result\": " + resultString + "}");
                                    }
                                } // end while (resultset)
                                log.info("{ \"event\": \"iterateResultSetEnd\", \"conIteration\" : " + j + ", "
                                        + "\"perConIteration\" : " + i + ", \"resultSetCount\": " + count
                                        + ", \"iterateResultSetTimeMs\" : " + (System.currentTimeMillis() - beforeRs)
                                        + "}");
                            } // end if (iterateResultSet)
                        } // end try (execute)
                        catch (SQLException e) {
                            log.info("{ \"event\": \"executeQueryError\", \"conIteration\" : " + j
                                    +", \"perConIteration\" : " + i + ", \"error\": \"" + e.getMessage() + "\"}");
                            log.error("Error while executing query", e);
                        }
                    } // end try (statement)
                    catch (SQLException e) {
                        log.info("{ \"event\": \"createStatementError\", \"conIteration\" : " + j
                                +", \"perConIteration\" : " + i + ", \"error\": \"" + e.getMessage() + "\"}");
                        log.error("Error while creating statement", e);
                    }
                } // end for (iterationsPerConnection)
                Thread.sleep(sleepTimeMs);
            } // end try (connection)
            catch (SQLException e) {
                log.info("{ \"event\": \"getConnectionError\", \"conIteration\" : " + j
                        + ", \"error\": \"" + e.getMessage() + "\"}");
                log.error("Error while getting connection", e);
            }
            j++;
        } // end while (totalIterations)
    }

    public static void getEnvAndApply(String param, Consumer<String> applier) {
        String env = System.getenv(ENV_PREFIX_POOL + param);
        if (env != null && !env.equals("")) {
            applier.accept(env);
            if (!param.contains("PASSWORD")) {
                log.info("{ \"event\": \"appliedParam\", \"param\": \"" + ENV_PREFIX_POOL + param + "\", \"value\": \"" + env + "\"}");
            } else {
                log.info("{ \"event\": \"appliedParam\", \"param\": \"" + ENV_PREFIX_POOL + param + "\", \"value\": \"********\"}");
            }
        } else {
            log.info("{ \"event\": \"notAppliedParam\", \"param\": \"" + ENV_PREFIX_POOL + param + "\"}");
        }
    }

    public static void getEnvAndApplyInt(String param, Consumer<Integer> applier) {
        String env = System.getenv(ENV_PREFIX_POOL + param);
        if (env != null && !env.equals("")) {
            applier.accept(new Integer(env));
            log.info("{ \"event\": \"appliedParam\", \"param\": \"" + ENV_PREFIX_POOL + param + "\", \"value\": \"" + env + "\"}");
        } else {
            log.info("{ \"event\": \"notAppliedParam\", \"param\": \"" + ENV_PREFIX_POOL + param + "\"}");
        }
    }

    public static void getEnvAndApplyBool(String param, Consumer<Boolean> applier) {
        String env = System.getenv(ENV_PREFIX_POOL + param);
        if (env != null && !env.equals("")) {
            applier.accept(Boolean.valueOf(env));
            log.info("{ \"event\": \"appliedParam\", \"param\": \"" + ENV_PREFIX_POOL + param + "\", \"value\": \"" + env + "\"}");
        } else {
            log.info("{ \"event\": \"notAppliedParam\", \"param\": \"" + ENV_PREFIX_POOL + param + "\"}");
        }
    }
}
