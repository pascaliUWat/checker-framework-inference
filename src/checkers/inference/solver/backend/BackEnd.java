package checkers.inference.solver.backend;

import org.checkerframework.framework.type.QualifierHierarchy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;

import checkers.inference.model.ConstantSlot;
import checkers.inference.model.Constraint;
import checkers.inference.model.Serializer;
import checkers.inference.model.Slot;
import checkers.inference.model.VariableSlot;
import checkers.inference.solver.frontend.Lattice;

/**
 * BackEnd class is the super class for all concrete back ends.
 * 
 * @author jianchu
 *
 * @param <S> Encoding type for slot. 
 * @param <T> Encdoing type for constraint.
 */
public abstract class BackEnd<S, T> {

    protected final Map<String, String> configuration;
    protected final Collection<Slot> slots;
    protected Collection<Constraint> constraints;
    protected final QualifierHierarchy qualHierarchy;
    protected final ProcessingEnvironment processingEnvironment;
    protected final Serializer<S, T> realSerializer;
    protected final Set<Integer> varSlotIds;
    protected final Lattice lattice;

    public BackEnd(Map<String, String> configuration, Collection<Slot> slots,
            Collection<Constraint> constraints, QualifierHierarchy qualHierarchy,
            ProcessingEnvironment processingEnvironment, Serializer<S, T> realSerializer, Lattice lattice) {
        this.configuration = configuration;
        this.slots = slots;
        this.constraints = constraints;
        this.qualHierarchy = qualHierarchy;
        this.processingEnvironment = processingEnvironment;
        this.realSerializer = realSerializer;
        this.varSlotIds = new HashSet<Integer>();
        this.lattice = lattice;
    }

    public abstract Map<Integer, AnnotationMirror> solve();

    public abstract void convertAll();

    /**
     * Get slot id from variable slot.
     *
     * @param constraint
     */
    public void collectVarSlots(Constraint constraint) {
        for (Slot slot : constraint.getSlots()) {
            if (!(slot instanceof ConstantSlot)) {
                this.varSlotIds.add(((VariableSlot) slot).getId());
            }
        }
    }

    public Collection<Slot> getSlots() {
        return this.slots;
    }

    public Collection<Constraint> getConstraints() {
        return this.constraints;
    }

    public ProcessingEnvironment getEnvironment() {
        return this.processingEnvironment;
    }

    public void setConstraint(Collection<Constraint> constraints) {
        this.constraints = constraints;
    }
}
