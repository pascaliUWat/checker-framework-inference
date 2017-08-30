package checkers.inference.solver.backend;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;

import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.javacutil.ErrorReporter;

import checkers.inference.model.Constraint;
import checkers.inference.model.Serializer;
import checkers.inference.model.Slot;
import checkers.inference.solver.backend.logiqlbackend.LogiQLBackEnd;
import checkers.inference.solver.backend.logiqlbackend.LogiQLTranslator;
import checkers.inference.solver.backend.maxsatbackend.LingelingBackEnd;
import checkers.inference.solver.backend.maxsatbackend.MaxSatBackEnd;
import checkers.inference.solver.backend.maxsatbackend.MaxSatTranslator;
import checkers.inference.solver.frontend.Lattice;

public enum BackEndType {

    MAXSAT("MaxSAT", MaxSatBackEnd.class, MaxSatTranslator.class),
    LINGELING("Lingeling", LingelingBackEnd.class, MaxSatTranslator.class),
    LOGIQL("LogiQL", LogiQLBackEnd.class, LogiQLTranslator.class);

    public final String simpleName;
    public final Class<? extends BackEnd<?, ?, ?>> backEndClass;
    public final Class<? extends Translator<?, ?, ?>> translatorClass;

    private BackEndType(String simpleName, Class<? extends BackEnd<?, ?, ?>> backEndClass,
            Class<? extends Translator<?, ?, ?>> translatorClass) {
        this.simpleName = simpleName;
        this.backEndClass = backEndClass;
        this.translatorClass = translatorClass;
    }

    public Translator<?, ?, ?> createDefaultTranslator(Lattice lattice) {
        Constructor<?> cons;
        try {
            cons = translatorClass.getConstructor(Lattice.class);
            return (Translator<?, ?, ?>) cons.newInstance(lattice);
        } catch (Exception e) {
            ErrorReporter.errorAbort(
                    "Exception happends when creating default serializer for " + simpleName + " backend.", e);
            // Dead code.
            return null;
        }
    }

    public BackEnd<?, ?, ?> createBackEnd(Map<String, String> configuration,
            Collection<Slot> slots, Collection<Constraint> constraints,
            QualifierHierarchy qualHierarchy, ProcessingEnvironment processingEnvironment,
            Lattice lattice, Translator<?, ?, ?> defaultTranslator) {
        try {
            Constructor<?> cons = backEndClass.getConstructor(Map.class, Collection.class,
                    Collection.class, QualifierHierarchy.class, ProcessingEnvironment.class,
                    Serializer.class, Lattice.class);

            return (BackEnd<?, ?, ?>) cons.newInstance(configuration, slots, constraints, qualHierarchy,
                    processingEnvironment, defaultTranslator, lattice);
        } catch (Exception e) {
            ErrorReporter.errorAbort(
                    "Exception happends when creating " + simpleName + " backend.", e);
            // Dead code.
            return null;
        }
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