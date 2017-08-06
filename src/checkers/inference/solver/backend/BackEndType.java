package checkers.inference.solver.backend;

public enum BackEndType {

    MAXSAT("MaxSAT", "checkers.inference.solver.backend.maxsatbackend.MaxSat"), 
    LINGELING("Lingeling", "checkers.inference.solver.backend.maxsatbackend.Lingeling"), 
    LOGIQL("LogiQL", "checkers.inference.solver.backend.logiqlbackend.LogiQL");

    public final String simpleName;
    public final String fullyQualifiedName;

    private BackEndType(String simpleName, String fullyQualifiedName) {
        this.simpleName = simpleName;
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public static BackEndType getBackEndType(String simpleName) {
        for (BackEndType backEndType : BackEndType.values()) {
            if (backEndType.simpleName.equals(simpleName)) {
                return backEndType;
            }
        }
        return null;
    }
}
