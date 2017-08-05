package checkers.inference.solver.backend;

import java.util.EnumMap;
import java.util.Map;

import org.checkerframework.javacutil.ErrorReporter;

public class BackEndType {
    
    public enum BackEndTypeEnum {
        MaxSAT, Lingeling, LogiQL;
    }

    private BackEndTypeEnum backEndType = BackEndTypeEnum.MaxSAT;

    public BackEndType(String simpleBackEndName) {

        if (simpleBackEndName != null) {
            try {
                this.backEndType = BackEndTypeEnum.valueOf(simpleBackEndName);
            } catch (IllegalArgumentException x) {
                ErrorReporter.errorAbort(
                        "Back end \"" + simpleBackEndName + "\" has not been implemented yet.");
            }
        }
    }

    public final static Map<BackEndTypeEnum, String> BackEndManifest = new EnumMap<BackEndTypeEnum, String>(BackEndTypeEnum.class);

    // If a new back end is added to solver framework, add the simple name and
    // it's fully qualified name to manifest.
    static {
        BackEndManifest.put(BackEndTypeEnum.MaxSAT,
                "checkers.inference.solver.backend.maxsatbackend.MaxSat");
        BackEndManifest.put(BackEndTypeEnum.Lingeling,
                "checkers.inference.solver.backend.maxsatbackend.Lingeling");
        BackEndManifest.put(BackEndTypeEnum.LogiQL,
                "checkers.inference.solver.backend.logiqlbackend.LogiQL");
    }

    public String getFullyQualifiedName() {
        return BackEndManifest.get(backEndType);
    }

    public String getFullyQualifiedName(BackEndTypeEnum backEndType) {
        return BackEndManifest.get(backEndType);
    }

    public String getSimpleName() {
        return backEndType.name();
    }

    public BackEndTypeEnum getBackEndType() {
        return backEndType;
    }

    @Override
    public String toString() {
        return getFullyQualifiedName();
    }
}
