package checkers.inference.solver.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Recorder for statistics.
 * 
 * @author jianchu
 *
 */
public class StatisticRecorder {

    public enum StatisticKey {
        
        /* Basic Info */
        SLOTS_SIZE, 
        CONSTRAINT_SIZE, 
        GRAPH_SIZE,
        CNF_VARIABLE_SIZE, 
        CNF_CLAUSE_SIZE, 
        LOGIQL_PREDICATE_SIZE, 
        LOGIQL_DATA_SIZE, 
        ANNOTATOIN_SIZE,
        
        /* Timing Info*/
        GRAPH_GENERATION_TIME, 
        OVERALL_PARALLEL_SOLVING_TIME,
        OVERALL_SEQUENTIAL_SOLVING_TIME,
        OVERALL_NOGRAPH_SOLVING_TIME,
        SAT_SERIALIZATION_TIME,
        SAT_SOLVING_TIME,
        LOGIQL_SERIALIZATION_TIME, 
        LOGIQL_SOLVING_TIME,
    }

    // Use atomic integer when back ends run in parallel.
    public final static AtomicInteger satSerializationTime = new AtomicInteger(0);
    public final static AtomicInteger satSolvingTime = new AtomicInteger(0);
    private final static Map<StatisticKey, Long> statistic = new HashMap<StatisticKey, Long>();

    static {
        /* Initialize basic Info */
        statistic.put(StatisticKey.SLOTS_SIZE, (long) 0);
        statistic.put(StatisticKey.CONSTRAINT_SIZE, (long) 0);
        statistic.put(StatisticKey.GRAPH_SIZE, (long) 0);
        statistic.put(StatisticKey.CNF_VARIABLE_SIZE, (long) 0);
        statistic.put(StatisticKey.CNF_CLAUSE_SIZE, (long) 0);
        statistic.put(StatisticKey.LOGIQL_PREDICATE_SIZE, (long) 0);
        statistic.put(StatisticKey.LOGIQL_DATA_SIZE, (long) 0);
        statistic.put(StatisticKey.ANNOTATOIN_SIZE, (long) 0);

        /* Initialize timing Info */
        statistic.put(StatisticKey.GRAPH_GENERATION_TIME, (long) 0);
        statistic.put(StatisticKey.OVERALL_PARALLEL_SOLVING_TIME, (long) 0);
        statistic.put(StatisticKey.OVERALL_SEQUENTIAL_SOLVING_TIME, (long) 0);
        statistic.put(StatisticKey.OVERALL_NOGRAPH_SOLVING_TIME, (long) 0);
        statistic.put(StatisticKey.SAT_SERIALIZATION_TIME, (long) 0);
        statistic.put(StatisticKey.SAT_SOLVING_TIME, (long) 0);
        statistic.put(StatisticKey.LOGIQL_SERIALIZATION_TIME, (long) 0);
    }

    public static synchronized void recordSingleSerializationTime(long value) {
        satSerializationTime.addAndGet((int) value);
    }

    public static synchronized void recordSingleSolvingTime(long value) {
        satSolvingTime.addAndGet((int) value);
    }

    public static void record(StatisticKey key, Long value) {
        synchronized (statistic) {
            if (key.equals(StatisticKey.LOGIQL_PREDICATE_SIZE)) {
                // LogiQL predicate size are fixed for same underlining type
                // system.
                statistic.put(key, value);
            } else {
                long oldValue = statistic.get(key);
                statistic.put(key, value + oldValue);
            }
        }
    }

    public static Map<StatisticKey, Long> getStatistic() {
        return statistic;
    }
}
