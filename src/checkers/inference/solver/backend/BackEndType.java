package checkers.inference.solver.backend;

import checkers.inference.model.Serializer;
import checkers.inference.solver.backend.logiqlbackend.LogiQLBackEnd;
import checkers.inference.solver.backend.logiqlbackend.LogiQLSerializer;
import checkers.inference.solver.backend.maxsatbackend.LingelingBackEnd;
import checkers.inference.solver.backend.maxsatbackend.MaxSatBackEnd;
import checkers.inference.solver.backend.maxsatbackend.MaxSatSerializer;

public enum BackEndType {

    MAXSAT("MaxSAT", MaxSatBackEnd.class, MaxSatSerializer.class), 
    LINGELING("Lingeling", LingelingBackEnd.class, MaxSatSerializer.class), 
    LOGIQL("LogiQL", LogiQLBackEnd.class, LogiQLSerializer.class);

    public final String simpleName;
    public final Class<? extends BackEnd<?, ?>> backEndClass;
    public final Class<? extends Serializer<?, ?>> serializerClass;

    private BackEndType(String simpleName, Class<? extends BackEnd<?, ?>> backEndClass,
            Class<? extends Serializer<?, ?>> serializerClass) {
        this.simpleName = simpleName;
        this.backEndClass = backEndClass;
        this.serializerClass = serializerClass;
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
