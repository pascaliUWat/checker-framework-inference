package checkers.inference.solver.util;

/**
 * String constants.
 * 
 * @author jianchu
 *
 */
public class Constants {

    public static final String BACK_END_TYPE = "backEndType";
    public static final String USE_GRAPH = "useGraph";
    public static final String SOLVE_IN_PARALLEL = "solveInParallel";
    public static final String COLLECT_STATISTIC = "collectStatistic";
    
    // Back end types
    public static final String MAX_SAT = "checkers.inference.solver.backend.maxsatbackend.MaxSat";
    public static final String LINGELING = "checkers.inference.solver.backend.maxsatbackend.Lingeling";
    public static final String LOGIQL = "checkers.inference.solver.backend.logiqlbackend.LogiQL";

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String CONSTANT_SLOT = "ConstantSlot";
    public static final String VARIABLE_SLOT = "VariableSlot";
}
